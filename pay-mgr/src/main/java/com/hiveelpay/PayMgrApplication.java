package com.hiveelpay;

import com.hiveelpay.common.util.MyLog;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.validation.Validator;
import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class PayMgrApplication {
    private static final MyLog _log = MyLog.getLog(PayMgrApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PayMgrApplication.class, args);
    }

    @Bean
    public Validator getValidator() {
        return new LocalValidatorFactoryBean();
    }


    @Bean
    public TaskScheduler taskScheduler(@Qualifier(value = "scheduledThreadPoolExecutor") ScheduledThreadPoolExecutor scheduledThreadPoolExecutor) {
        return new ConcurrentTaskScheduler(scheduledThreadPoolExecutor);
    }
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        RestTemplate template = builder.build();
        template.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                _log.error("", response);
                return response.getStatusCode().is2xxSuccessful();
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                _log.error("", response);
            }
        });
        return template;
    }
    @Bean
    public ScheduledThreadPoolExecutor scheduledThreadPoolExecutor() {
        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(30);
        poolExecutor.setRejectedExecutionHandler((r, executor) -> _log.warn("Too many tasks."));
        return poolExecutor;
    }
}