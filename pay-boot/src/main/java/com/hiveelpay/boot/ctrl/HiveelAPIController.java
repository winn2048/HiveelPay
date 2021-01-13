package com.hiveelpay.boot.ctrl;

import com.hiveelpay.boot.service.HiveelAPI4CarService;
import com.hiveelpay.common.domain.RestAPIResult;
import com.hiveelpay.common.domain.ResultStatus;
import com.hiveelpay.common.model.response.CarServicesCarOfDayProperty;
import com.hiveelpay.common.model.response.CarServicesProperty;
import com.hiveelpay.common.model.response.CarServicesResponse;
import com.hiveelpay.common.util.MyLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/car")
public class HiveelAPIController {
    private static final MyLog _log = MyLog.getLog(HiveelAPIController.class);
    @Autowired
    private HiveelAPI4CarService hiveelAPI4CarServiceImpl;

    /**
     * 获取车的有效服务
     *
     * @param carId
     * @return
     */
    @GetMapping("/services/{carId}")
    public RestAPIResult<CarServicesResponse> queryInvalidCarServices(@PathVariable("carId") String carId) {
//        carId="123";
        CarServicesResponse response = hiveelAPI4CarServiceImpl.queryCarServices(carId);
        if (response.getAdvancing() == null) {
            response.setAdvancing(new CarServicesProperty());
        }
        if (response.getCarOfDay() == null) {
            response.setCarOfDay(new CarServicesCarOfDayProperty());
        }
        if (response.getSearchResult() == null) {
            response.setSearchResult(new CarServicesProperty());
        }
        if (response.getHighlighting() == null) {
            response.setHighlighting(new CarServicesProperty());
        }
        _log.info("Car:{},valid service:{}", carId, response);
        return new RestAPIResult<>(ResultStatus.SUCCESS, response);
    }
}
