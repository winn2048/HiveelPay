package com.hiveelpay.boot.ctrl;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.hiveelpay.boot.ctrl.exceptions.HiveelUserNotExistsException;
import com.hiveelpay.common.util.MyLog;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

public class BaseController {
    private static final MyLog _log = MyLog.getLog(BaseController.class);

    protected String getUserId(HttpServletRequest request) {
        String userId = "";
        if (request == null) {
            throw new HiveelUserNotExistsException();
        }
        userId = Optional.ofNullable(request.getHeader("userId")).orElse("");
        _log.info("hiveelPay####BaseController#########################userId={},uri={}", userId, request.getRequestURI());
        if (Strings.isNullOrEmpty(userId)) {
            Map<String, String[]> map = request.getParameterMap();
            for (Map.Entry<String, String[]> m : map.entrySet()) {
                _log.error(m.getKey() + "->" + new Gson().toJson(m.getValue()));
            }

            throw new HiveelUserNotExistsException();
        }
        return userId;
    }

    protected String getUserIdNullAble(HttpServletRequest request) {
        String userId = "";
        if (request == null) {
            throw new HiveelUserNotExistsException();
        }
        userId = Optional.ofNullable(request.getHeader("userId")).orElse("");
        return Strings.emptyToNull(userId);
    }


}
