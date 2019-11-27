package com.zing.passbook.controller;

import com.zing.passbook.log.LogConstants;
import com.zing.passbook.log.LogGenerator;
import com.zing.passbook.service.IFeedbackService;
import com.zing.passbook.service.IGainPassTemplateService;
import com.zing.passbook.service.IInventoryService;
import com.zing.passbook.service.IUserPassService;
import com.zing.passbook.vo.Feedback;
import com.zing.passbook.vo.GainPassTemplateRequest;
import com.zing.passbook.vo.Pass;
import com.zing.passbook.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Zing
 * @date 2019-11-27
 */
@Slf4j
@RestController
@RequestMapping("/passbook")
public class PassbookController {

    @Autowired
    private IUserPassService userPassService;

    @Autowired
    private IInventoryService inventoryService;

    @Autowired
    private IGainPassTemplateService gainPassTemplateService;

    @Autowired
    private IFeedbackService feedbackService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * <h2>获取用户个人的优惠券信息</h2>
     *
     * @param userId 用户 id
     * @return {@link Response}
     */
    @GetMapping("/user/passInfo")
    public Response passInfo(Long userId) throws Exception {
        LogGenerator.log(httpServletRequest, userId, LogConstants.ActionName.USER_PASS_INFO, null);
        return userPassService.getUserPassInfo(userId);
    }

    /**
     * <h2>获取用户使用了的优惠券信息</h2>
     *
     * @param userId 用户 id
     * @return {@link Response}
     */
    @GetMapping("/user/usedPassInfo")
    public Response usedPassInfo(Long userId) throws Exception {
        LogGenerator.log(httpServletRequest, userId, LogConstants.ActionName.USER_USED_PASS_INFO, null);
        return userPassService.getUserUsedPassInfo(userId);
    }

    /**
     * <h2>用户使用优惠券</h2>
     *
     * @param pass {@link Pass}
     * @return {@link Response}
     */
    @PostMapping("/user/usePass")
    public Response usePass(@RequestBody Pass pass) throws Exception {
        LogGenerator.log(httpServletRequest, pass.getUserId(), LogConstants.ActionName.USER_USE_PASS, pass);
        return userPassService.userUsePass(pass);
    }

    /**
     * <h2>获取库存信息</h2>
     *
     * @param userId 用户 id
     * @return {@link Response}
     */
    @GetMapping("/inventoryInfo")
    public Response inventoryInfo(Long userId) throws Exception {
        LogGenerator.log(httpServletRequest, userId, LogConstants.ActionName.INVENTORY_INFO, null);
        return inventoryService.getInventoryInfo(userId);
    }

    /**
     * <h2>用户领取优惠券</h2>
     *
     * @param request {@link GainPassTemplateRequest}
     * @return {@link Response}
     */
    @PostMapping("/gainPassTemplate")
    public Response gainPassTemplate(@RequestBody GainPassTemplateRequest request) throws Exception {
        LogGenerator.log(httpServletRequest, request.getUserId(), LogConstants.ActionName.GAIN_PASS_TEMPLATE, request);
        return gainPassTemplateService.gainPassTemplate(request);
    }

    /**
     * <h2>用户创建评论</h2>
     *
     * @param feedback {@link Feedback}
     * @return {@link Response}
     */
    @PostMapping("/createFeedback")
    public Response createFeedback(@RequestBody Feedback feedback) {
        LogGenerator.log(httpServletRequest, feedback.getUserId(), LogConstants.ActionName.CREATE_FEEDBACK, feedback);
        return feedbackService.createFeedback(feedback);
    }

    /**
     * <h2>用户获取评论信息</h2>
     *
     * @param userId 用户 id
     * @return {@link Response}
     */
    @GetMapping("/getFeedback")
    public Response getFeedback(Long userId) {
        LogGenerator.log(httpServletRequest, userId, LogConstants.ActionName.GET_FEEDBACK, null);
        return feedbackService.getFeedback(userId);
    }

    /**
     * <h2>异常演示接口</h2>
     *
     * @return {@link Response}
     */
    @GetMapping("/exception")
    Response exception() throws Exception {
        throw new Exception("Welcome To XXXXX");
    }
}
