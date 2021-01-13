package com.hiveelpay.boot;

import com.braintreegateway.BraintreeGateway;
import com.google.common.collect.Maps;
import com.hiveelpay.boot.service.channel.hiveel.BraintreeConfig;
import com.hiveelpay.common.util.MyLog;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 *
 */
@SpringBootApplication
@EnableAsync(proxyTargetClass = true)
@EnableScheduling
@MapperScan(value = "com.hiveelpay.dal.dao.mapper")
public class PayBootAppliaction {
    private static final MyLog _log = MyLog.getLog(PayBootAppliaction.class);
//    private static String DEFAULT_CONFIG_FILENAME = "config.properties";

    @Autowired
    private BraintreeConfig braintreeConfig;

    public static void main(String[] args) {
        SpringApplication.run(PayBootAppliaction.class, args);
    }

    @PostConstruct
    public void init() {
        for (String tz : TimeZone.getAvailableIDs()) {
            System.out.println("Server Avalliable timezone:" + tz);
        }
        TimeZone.setDefault(TimeZone.getTimeZone("America/Los_Angeles"));   // ??? UTC

        System.out.println("Spring boot application running in UTC timezone :" + new Date());
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
    public Validator getValidator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public TaskScheduler taskScheduler(@Qualifier(value = "scheduledThreadPoolExecutor") ScheduledThreadPoolExecutor scheduledThreadPoolExecutor) {
        return new ConcurrentTaskScheduler(scheduledThreadPoolExecutor);
    }

    @Bean
    public ScheduledThreadPoolExecutor scheduledThreadPoolExecutor() {
        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(100);
        poolExecutor.setRejectedExecutionHandler((r, executor) -> _log.warn("Too many tasks."));
        return poolExecutor;
    }

    @Bean
    public BraintreeGateway gateway() {
        try {
//            Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource(DEFAULT_CONFIG_FILENAME));
            /**
             * BT_ENVIRONMENT=sandbox
             * BT_MERCHANT_ID=hdkfsz2tnsx7tq9s
             * BT_PUBLIC_KEY=4mw23fj89g9n3z3h
             * BT_PRIVATE_KEY=25d93abdec90d31701b6d3edd00d5b0b
             */
            Map<String, String> map = Maps.newHashMap();
//            map.put("BT_ENVIRONMENT", properties.getProperty("BT_ENVIRONMENT"));
//            map.put("BT_MERCHANT_ID", properties.getProperty("BT_MERCHANT_ID"));
//            map.put("BT_PUBLIC_KEY", properties.getProperty("BT_PUBLIC_KEY"));
//            map.put("BT_PRIVATE_KEY", properties.getProperty("BT_PRIVATE_KEY"));
            map.put("BT_ENVIRONMENT", braintreeConfig.getBtEnvironment());
            map.put("BT_MERCHANT_ID", braintreeConfig.getBtMerchantId());
            map.put("BT_PUBLIC_KEY", braintreeConfig.getBtPublicKey());
            map.put("BT_PRIVATE_KEY", braintreeConfig.getBtPrivateKey());

            return BraintreeGatewayFactory.fromConfigMapping(map);
        } catch (Exception e) {
            _log.error("Could not load Braintree configuration from config file or system environment.", e);
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }
}
