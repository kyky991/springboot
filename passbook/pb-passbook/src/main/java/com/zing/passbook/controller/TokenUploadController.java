package com.zing.passbook.controller;

import com.zing.passbook.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Zing
 * @date 2019-11-27
 */
@Slf4j
@Controller
public class TokenUploadController {

    @Autowired
    private StringRedisTemplate redisTemplate;


    @GetMapping("/upload")
    public String upload() {
        return "upload";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }

    @PostMapping("/token")
    public String tokenFileUpload(String merchantsId, String passTemplateId, MultipartFile file, RedirectAttributes redirectAttributes) {
        if (passTemplateId == null || file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "passTemplateId is null or file is empty");
            return "redirect:/uploadStatus";
        }

        try {
            File f = new File(Constants.TOKEN_DIR + merchantsId);
            if (!f.exists()) {
                log.info("Create File: {}", f.mkdir());
            }

            Path path = Paths.get(Constants.TOKEN_DIR, merchantsId, passTemplateId);
            Files.write(path, file.getBytes());

            if (!writeTokenToRedis(path, passTemplateId)) {
                redirectAttributes.addFlashAttribute("message", "write token error");
            } else {
                redirectAttributes.addFlashAttribute("message", "You successfully uploaded '" + file.getOriginalFilename() + "'");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/uploadStatus";
    }

    /**
     * <h2>将 token 写入 redis</h2>
     *
     * @param path {@link Path}
     * @param key  redis key
     * @return true/false
     */
    private boolean writeTokenToRedis(Path path, String key) {
        Set<String> tokens;

        try (Stream<String> stream = Files.lines(path)) {
            tokens = stream.collect(Collectors.toSet());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }

        if (!CollectionUtils.isEmpty(tokens)) {
            redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                tokens.forEach(token -> connection.sAdd(key.getBytes(), token.getBytes()));
                return null;
            });

            return true;
        }

        return false;
    }

}
