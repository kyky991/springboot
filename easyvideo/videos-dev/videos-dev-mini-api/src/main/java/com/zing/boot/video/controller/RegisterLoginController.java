package com.zing.boot.video.controller;

import com.zing.boot.video.WXSessionModel;
import com.zing.boot.video.config.ResourceConfig;
import com.zing.boot.video.pojo.User;
import com.zing.boot.video.pojo.vo.UserVO;
import com.zing.boot.video.service.IUserService;
import com.zing.boot.video.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@Api(value="用户注册登录的接口", tags= {"注册和登录的controller"})
public class RegisterLoginController extends BasicController {

    @Autowired
    private IUserService userService;

    @Autowired
    private ResourceConfig resourceConfig;

    @Autowired
    private RedisOperator redis;

    @ApiOperation(value = "用户注册", notes = "注册和登录的controller")
    @PostMapping("/register")
    public JSONResult register(@RequestBody User user) throws Exception {
        // 1. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
            return JSONResult.errorMsg("用户名或密码不能为空");
        }

        // 2. 判断用户名是否存在
        boolean exist = userService.queryUsernameIsExist(user.getUsername());

        // 3. 保存用户，注册信息
        if (!exist) {
            user.setNickname(user.getUsername());
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            user.setFansCounts(0);
            user.setReceiveLikeCounts(0);
            user.setFollowCounts(0);
            userService.saveUser(user);
        } else {
            return JSONResult.errorMsg("用户名已经存在，请换一个再试");
        }

        user.setPassword("");

        String uniqueToken = UUID.randomUUID().toString();
		redis.set(USER_REDIS_SESSION + ":" + user.getId(), uniqueToken, 1000 * 60 * 30);

        UserVO userVO = setUserRedisSessionToken(user);

        return JSONResult.ok(userVO);
    }

    public UserVO setUserRedisSessionToken(User userModel) {
        String uniqueToken = UUID.randomUUID().toString();
        redis.set(USER_REDIS_SESSION + ":" + userModel.getId(), uniqueToken, 1000 * 60 * 30);

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        userVO.setUserToken(uniqueToken);
        return userVO;
    }

    @ApiOperation(value="用户登录", notes="用户登录的接口")
    @PostMapping("/login")
    public JSONResult login(@RequestBody User user) throws Exception {
        String username = user.getUsername();
        String password = user.getPassword();

        // 1. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return JSONResult.ok("用户名或密码不能为空...");
        }

        // 2. 判断用户是否存在
        User userResult = userService.queryUserForLogin(username,
                MD5Utils.getMD5Str(user.getPassword()));

        // 3. 返回
        if (userResult != null) {
            userResult.setPassword("");
            UserVO userVO = setUserRedisSessionToken(userResult);
            return JSONResult.ok(userVO);
        } else {
            return JSONResult.errorMsg("用户名或密码不正确, 请重试...");
        }
    }

    @ApiOperation(value="微信登录", notes="微信登录的接口")
    @PostMapping("/wxLogin")
    public JSONResult wxLogin(String code) {
        if (StringUtils.isBlank(code)) {
            return JSONResult.errorMsg("授权失败...");
        }

        Map<String, String> params = new HashMap<>();
        params.put("appid", resourceConfig.getAppId());
        params.put("secret", resourceConfig.getSecret());
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");

        String result = HttpClientUtils.doGet(resourceConfig.getCode2SessionUrl(), params);
        WXSessionModel model = JsonUtils.jsonToPojo(result, WXSessionModel.class);

        redis.set(USER_REDIS_SESSION + ":" + model.getOpenid(), model.getSession_key(), 1000 * 60 * 30);

        return JSONResult.ok();
    }

    @ApiOperation(value="用户注销", notes="用户注销的接口")
    @ApiImplicitParam(name="userId", value="用户id", required=true, dataType="String", paramType="query")
    @PostMapping("/logout")
    public JSONResult logout(String userId) {
        redis.del(USER_REDIS_SESSION + ":" + userId);
        return JSONResult.ok();
    }
}
