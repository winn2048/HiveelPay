package com.hiveelpay.boot.ctrl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hiveelpay.boot.service.PayProductService;
import com.hiveelpay.common.domain.RestAPIResult;
import com.hiveelpay.common.enumm.PayProductTypeEnum;
import com.hiveelpay.common.model.vo.ProductVo;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.dal.dao.model.PayProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.hiveelpay.common.domain.ResultStatus.FAILED;
import static com.hiveelpay.common.domain.ResultStatus.SUCCESS;
import static java.util.stream.Collectors.groupingBy;

@RestController
@RequestMapping("/api/product")
public class HiveelProductController extends BaseController {
    private static final MyLog _log = MyLog.getLog(HiveelProductController.class);

    @Autowired
    private ConversionService conversionService;
    @Autowired
    private PayProductService payProductServiceImpl;

    @GetMapping("/all")
    public RestAPIResult<Map<String, List<ProductVo>>> all() {
        Map<String, List<ProductVo>> rsMap = Maps.newHashMap();
        List<PayProduct> list = payProductServiceImpl.findAll();
        if (!list.isEmpty()) {
            List<ProductVo> listVo = Lists.newArrayList();
            list.forEach(i -> listVo.add(conversionService.convert(i, ProductVo.class)));
            Map<Integer, List<ProductVo>> map = listVo.stream().collect(groupingBy(ProductVo::getProductType));
            map.forEach((k, v) -> rsMap.put(PayProductTypeEnum.byValue(k).name(), v));
        }
        return new RestAPIResult<>(SUCCESS, rsMap);
    }


    /**
     * 获取可购买产品列表
     *
     * @param type 10-Membership ，20 - 搜索结果排名靠前
     * @return
     */
    @GetMapping("/{type}/list")
    public RestAPIResult<Map<String, List<ProductVo>>> list(@PathVariable(value = "type") Integer type) {
        PayProductTypeEnum payProductType = PayProductTypeEnum.byValue(type);
        if (payProductType.equals(PayProductTypeEnum.ILLEGAL)) {
            return new RestAPIResult<>(FAILED);
        }

        List<PayProduct> list = payProductServiceImpl.findProductsByType(payProductType);
        List<ProductVo> listVo = Lists.newArrayList();
        list.forEach(i -> listVo.add(conversionService.convert(i, ProductVo.class)));
        Map<String, List<ProductVo>> rsMap = Maps.newHashMap();
        rsMap.put(payProductType.name(), listVo);
        return new RestAPIResult<>(SUCCESS, rsMap);
    }

    @GetMapping("/{productId}/show")
    public RestAPIResult<ProductVo> showProduct(@PathVariable("productId") String productId) {
        PayProduct product = payProductServiceImpl.findProductByProductId(productId);
        ProductVo result = conversionService.convert(product, ProductVo.class);
        return new RestAPIResult<>(SUCCESS, result);

    }
}
