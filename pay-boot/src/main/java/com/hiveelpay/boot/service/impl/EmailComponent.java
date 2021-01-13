package com.hiveelpay.boot.service.impl;

import com.google.common.base.Strings;
import com.hiveelpay.boot.service.channel.hiveel.EmailConfig;
import com.hiveelpay.common.util.MyLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class EmailComponent {
    private static final MyLog log = MyLog.getLog(EmailComponent.class);
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private EmailConfig emailConfig;

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("apiKey", emailConfig.getApiKey());
        return headers;
    }


    public void sendEmail(MultiValueMap<String, String> map) {
        if (map == null || map.isEmpty()) {
            return;
        }
        if (!emailConfig.isSwitchOn()) {
            return;
        }
        map.forEach((k, v) -> log.info("send email parameters:k:{},v:{}", k, v));
        try {
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, getHeaders());
            ResponseEntity<HiveelEmailResponse> response = restTemplate.postForEntity(emailConfig.getApi(), request, HiveelEmailResponse.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("邮件通知,结果：", response.getBody());
                return;
            }
            log.info("邮件通知失败:{}", response);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public void postExceptionEmail(String subject, Throwable throwable, String reqStr) {
        try {
            postExceptionEmail(subject, Strings.nullToEmpty(reqStr) + exceptionToString(throwable));
        } catch (Exception e) {
            log.error("We can ignore this exception.", e);
        }
    }

    public void postExceptionEmail(String subject, String content) {
        try {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("subject", subject);
            map.add("content", content);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, getHeaders());
            ResponseEntity<HiveelEmailResponse> response = restTemplate.postForEntity(emailConfig.getErrorApi(), request, HiveelEmailResponse.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("异常通知邮件：{}", response.getBody());
                return;
            }
            log.info("异常通知邮件，失败:{}", response);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private String exceptionToString(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(Strings.nullToEmpty(throwable.getMessage()));
        sb.append("堆栈信息:<br/>");
        StackTraceElement[] traceElements = throwable.getStackTrace();
        if (traceElements != null) {
            for (StackTraceElement traceElement : traceElements) {
                sb.append(traceElement.getClassName());
                sb.append(":");
                sb.append(traceElement.getFileName());
                sb.append(":");
                sb.append(traceElement.getLineNumber());
                sb.append(":");
                sb.append(traceElement.getMethodName());
                sb.append("()<br/>");
            }
        }
        return sb.toString();
    }
}
