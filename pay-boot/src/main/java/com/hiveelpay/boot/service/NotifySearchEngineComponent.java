package com.hiveelpay.boot.service;

import com.hiveelpay.boot.service.channel.hiveel.SearchConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NotifySearchEngineComponent {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SearchConfig searchConfig;

    public boolean notifySearchEngine(String carId) {
        if (carId == null || carId.trim().length() <= 0) {
            return false;
        }
        String url = searchConfig.getCarUpdate();
        url = url.replace("_carId", carId);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody() != null && responseEntity.getBody().equalsIgnoreCase("success");
        }
        return false;
    }
}
