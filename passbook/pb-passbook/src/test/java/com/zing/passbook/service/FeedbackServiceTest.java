package com.zing.passbook.service;

import com.alibaba.fastjson.JSON;
import com.zing.passbook.constant.FeedbackType;
import com.zing.passbook.vo.Feedback;
import com.zing.passbook.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author Zing
 * @date 2019-11-29
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class FeedbackServiceTest extends AbstractServiceTest {

    @Autowired
    private IFeedbackService feedbackService;

    @Test
    public void createFeedback() {
        Feedback appFeedback = new Feedback();
        appFeedback.setUserId(userId);
        appFeedback.setType(FeedbackType.APP.getCode());
        appFeedback.setTemplateId("-1");
        appFeedback.setComment("666");
        Response response = feedbackService.createFeedback(appFeedback);
        log.info(JSON.toJSONString(response));

        Feedback passFeedback = new Feedback();
        passFeedback.setUserId(userId);
        passFeedback.setType(FeedbackType.PASS.getCode());
        passFeedback.setTemplateId("364ad9f9e53774e59e40c92b1eb44ba8");
        passFeedback.setComment("666666666666");
        response = feedbackService.createFeedback(passFeedback);
        log.info(JSON.toJSONString(response));
    }

    @Test
    public void getFeedback() {
        Response response = feedbackService.getFeedback(userId);
        log.info(JSON.toJSONString(response));
    }
}