package com.zing.springboot.configure;

import com.zing.springboot.service.ISplitService;
import com.zing.springboot.service.impl.SplitServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(value = {ISplitService.class, SplitServiceImpl.class})
public class SplitAutoConfigure {

    @Bean
    @ConditionalOnMissingBean
    public ISplitService starterService() {
        return new SplitServiceImpl();
    }

}
