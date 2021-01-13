package com.hiveelpay.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import com.hiveelpay.common.model.response.ProductListResponse;
import com.hiveelpay.common.model.vo.ProductVo;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.shop.config.HiveelConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Controller
public class IndexController {
    private final static MyLog _log = MyLog.getLog(IndexController.class);
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HiveelConfig hiveelConfig;

    @RequestMapping("/")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("/index");
        try {
            ResponseEntity<ProductListResponse> responseEntity = restTemplate.getForEntity(hiveelConfig.getApiLoadAllProducts(), ProductListResponse.class);
            _log.info("ResponseEntity:", responseEntity);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {

                Map<String, List<ProductVo>> result = responseEntity.getBody().getData();
                _log.info("Get products form interface:{}", responseEntity.getBody());
                mav.addObject("mapList", result);
            }
        } catch (Exception e) {
            _log.error("Get products from pay_center error!", e);
        }
        return mav;
    }
}
