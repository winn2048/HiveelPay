package com.hiveelpay.shop.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.hiveelpay.shop.config.HiveelConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import com.hiveelpay.common.model.response.ProductItemResponse;
import com.hiveelpay.common.model.response.StringResponse;
import com.hiveelpay.common.model.vo.PayRequestVo;
import com.hiveelpay.common.util.MyLog;

@Controller
@RequestMapping("/shopping")
public class ShoppingController {
    private final static MyLog _log = MyLog.getLog(IndexController.class);
    @Autowired
    private HiveelConfig hiveelConfig;
    @Autowired
    private RestTemplate restTemplate;


    @PostMapping("/pay")
    public ModelAndView pay(String productId) {
        ModelAndView mav = new ModelAndView("/pay_error");
        if (Strings.isNullOrEmpty(productId)) {
            mav.addObject("msg", "productid cant't null!");
        }
        ResponseEntity<ProductItemResponse> responseEntity = restTemplate.getForEntity(hiveelConfig.getApiProductShow().replaceAll("_productId_", productId),
                ProductItemResponse.class);
        _log.info("ResponseEntity:", responseEntity);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            mav = new ModelAndView("/pre_order_view");
            mav.addObject("product", responseEntity.getBody().getData());
        }

        mav.addObject("token", "null");
        ResponseEntity<StringResponse> tokenEntity = restTemplate.getForEntity(hiveelConfig.getApiPayToken(), StringResponse.class);
        if (tokenEntity.getStatusCode().is2xxSuccessful()) {
            mav.addObject("token", tokenEntity.getBody().getData());
        }

        return mav;
    }

    @PostMapping("/doPay")
    public ModelAndView doPay(PayRequestVo payRequestVo) {
        ModelAndView modelAndView = new ModelAndView("/pay_success");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.put("userId", Lists.newArrayList(payRequestVo.getUserId()));

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("userId", payRequestVo.getUserId());
        map.add("productId", payRequestVo.getProductId());
        map.add("accountType", payRequestVo.getAccountType().name());
        map.add("payType", payRequestVo.getPayType().name());
        map.add("billedMethod", payRequestVo.getBilledMethod().name());
        map.add("payMethodId", payRequestVo.getPayMethodId());
        map.add("firstName", payRequestVo.getFirstName());
        map.add("lastName", payRequestVo.getLastName());
        map.add("addr", payRequestVo.getAddr());
        map.add("addrSuite", payRequestVo.getAddrSuite());
        map.add("city", payRequestVo.getCity());
        map.add("state", payRequestVo.getState());
        map.add("zipcode", payRequestVo.getZipcode());
        map.add("docZipcode", payRequestVo.getDocZipcode());
        map.add("addressType", payRequestVo.getAddressType().name());
        map.add("clientToken", payRequestVo.getClientToken());
        map.add("nonce", payRequestVo.getNonce());
        map.add("mchId", payRequestVo.getMchId());
//        map.add("carIds", payRequestVo.getCarIds());
        map.add("channelId", payRequestVo.getChannelId());
        map.add("totalAmount", payRequestVo.getTotalAmount());
        map.add("serviceTimes", payRequestVo.getServiceTimes());//服务时间，逗号分隔，可以选多个，会员产品，只能选一个
        map.add("docId", payRequestVo.getDocId());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            ResponseEntity<StringResponse> responseEntity = restTemplate.postForEntity(hiveelConfig.getApiPayCheckout(), request, StringResponse.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                StringResponse response = responseEntity.getBody();
                modelAndView.addObject("response", response);
                return modelAndView;
            }
        } catch (Exception e) {

        }
        modelAndView = new ModelAndView("/pay_error");
        return modelAndView;
    }

}
