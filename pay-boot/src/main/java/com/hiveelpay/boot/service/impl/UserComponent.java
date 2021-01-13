package com.hiveelpay.boot.service.impl;

import com.google.common.base.Strings;
import com.hiveelpay.boot.service.channel.hiveel.AuthConfig;
import com.hiveelpay.common.model.auth.AuthUserResponse;
import com.hiveelpay.common.model.auth.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Component
public class UserComponent {
    private static final Logger log = LoggerFactory.getLogger(UserComponent.class);
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AuthConfig authConfig;


    public boolean isUserExists(String id) {
        try {
            ResponseEntity<AuthUserResponse> res = restTemplate.getForEntity(authConfig.getApiUserInfo() + id, AuthUserResponse.class);
            if (!res.getStatusCode().is2xxSuccessful()) {
                return false;
            }
            if (res.getBody() == null) {
                return false;
            }
            String code = res.getBody().getCode();
            if (Strings.isNullOrEmpty(code) || !code.equalsIgnoreCase("SUCCESS")) {
                return false;
            }
            List<User> user = res.getBody().getData();

            if (user == null || user.isEmpty()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("", e);
        }
        return false;
    }

    public List<User> getUsers(String... ids) {
        if (ids == null || ids.length <= 0) {
            return Collections.emptyList();
        }

        try {
            String reqUrl = authConfig.getApiUserInfo() + String.join(",", ids);
            ResponseEntity<AuthUserResponse> res = restTemplate.getForEntity(reqUrl, AuthUserResponse.class);
            if (!res.getStatusCode().is2xxSuccessful()) {
                log.error("访问" + reqUrl + "失败");
                return Collections.emptyList();
            }
            List<User> users = res.getBody().getData();
            if (users == null) {
                return Collections.emptyList();
            }
            return users;
        } catch (Exception e) {
            log.error("可忽略", e);
        }
        return Collections.emptyList();
    }

    public User getUser(String memberId) {
        if (Strings.isNullOrEmpty(memberId)) {
            return null;
        }
        try {
            List<User> users = getUsers(memberId);
            if (users != null && !users.isEmpty()) {
                return users.get(0);
            }
        } catch (Exception e) {
            log.error("调用接口获取用户失败,memberId={}", memberId, e);
        }
        return null;
    }

}
