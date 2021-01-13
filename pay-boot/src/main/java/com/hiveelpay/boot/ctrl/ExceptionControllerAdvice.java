package com.hiveelpay.boot.ctrl;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.hiveelpay.boot.service.SystemHasExceptionEvent;
import com.hiveelpay.common.domain.RestAPIResult;
import com.hiveelpay.common.domain.ResultStatus;
import com.hiveelpay.common.util.MyLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {
    private static final MyLog _log = MyLog.getLog(ExceptionControllerAdvice.class);
    @Autowired
    private ApplicationEventPublisher publisher;

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String exception(final Throwable throwable, HttpServletRequest request) {
        String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");

        if (throwable instanceof IllegalArgumentException) {
            errorMessage = "非法参数";
        }

        RestAPIResult<String> rs = new RestAPIResult<>(ResultStatus.FAILED, null, errorMessage);
        publisher.publishEvent(new SystemHasExceptionEvent("#HIVEELPAY", throwable, requestToString(request)));
        return new Gson().toJson(rs);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String exception(final IllegalArgumentException throwable, HttpServletRequest request) {
        String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");

        RestAPIResult<String> rs = new RestAPIResult<>(ResultStatus.FAILED, "ILLEGALARGUMENT", errorMessage);
        publisher.publishEvent(new SystemHasExceptionEvent("#HIVEELPAY", throwable, requestToString(request)));
        return new Gson().toJson(rs);
    }

    private String requestToString(HttpServletRequest request) {
        StringBuilder content = new StringBuilder();
        if (request != null) {
            String uri = request.getRequestURI();
            content.append("<br/>访问URI:").append(uri).append("<br/>参数: <br/>");
            Map<String, String[]> parameters = request.getParameterMap();
            if (parameters != null && !parameters.isEmpty()) {
                for (Map.Entry<String, String[]> m : parameters.entrySet()) {
                    content.append(Strings.nullToEmpty(m.getKey())).append(":").append(m.getValue() == null ? "" : (String.join(",", m.getValue()))).append("<br/>");
                }
            }
        }

        return content.toString();
    }
}
