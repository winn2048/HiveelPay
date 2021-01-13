package com.hiveelpay.mgr.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.hiveelpay.common.enumm.PayProductStatusEnum;
import com.hiveelpay.common.enumm.PayProductTypeEnum;
import com.hiveelpay.common.enumm.ServiceLengthUnitEnum;
import com.hiveelpay.common.util.AmountUtil;
import com.hiveelpay.common.util.DateUtil;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.dal.dao.model.PayProduct;
import com.hiveelpay.dal.dao.plugin.PageModel;
import com.hiveelpay.mgr.service.PayProductsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 支付产品  controller
 */
@Controller
@RequestMapping("/products")
public class ProductsController {
    private final static MyLog _log = MyLog.getLog(ProductsController.class);

    @Autowired
    private PayProductsService payProductsService;

    @RequestMapping("/list.html")
    public String listInput(ModelMap model) {
        return "products/list";
    }

    @RequestMapping("/edit.html")
    public String editInput(String payProductId, ModelMap model) {
        PayProduct item = null;
        if (StringUtils.isNotBlank(payProductId)) {
            item = payProductsService.selectPayProductInfo(payProductId);
            if (item.getAmount() != null && item.getAmount() > 0) {
                item.setAmount(item.getAmount() / 100);
            }
        }
        if (item == null) {
            item = new PayProduct();
//            item.setProductId(HiveelID.getInstance().getRandomId("P"));
            item.setAmount(0L);
            item.setSupportAutoPay(true);
            item.setQuantity(0);
            item.setServiceLength(0);
            item.setServiceLengthUnit(ServiceLengthUnitEnum.DAY);
            item.setProductType(PayProductTypeEnum.ADVANCING);
            item.setProductStatus(PayProductStatusEnum.SELLING);
        }
        model.put("item", item);
        return "products/edit";
    }

    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute PayProduct payProduct, Integer pageIndex, Integer pageSize) {
        PageModel pageModel = new PageModel();
        int count = payProductsService.count(payProduct);
        if (count <= 0) return JSON.toJSONString(pageModel);
        List<PayProduct> payProductList = payProductsService.list((pageIndex - 1) * pageSize, pageSize, payProduct);
        if (!CollectionUtils.isEmpty(payProductList)) {
            JSONArray array = new JSONArray();
            for (PayProduct mi : payProductList) {
                JSONObject object = (JSONObject) JSONObject.toJSON(mi);
                object.put("createTime", DateUtil.date2Str(mi.getCreateAt()));
                object.put("amount", AmountUtil.convertCent2Dollar(mi.getAmount() + ""));
                object.put("productType", mi.getProductType().getName());
                object.put("productStatus", mi.getProductStatus().getName());
                object.put("serviceLengthUnit", mi.getServiceLengthUnit().getName());
                object.put("supportAutoPay", mi.isSupportAutoPay() ? "支持" : "不支持");
                object.put("btPlanId", Strings.nullToEmpty(mi.getBtPlanId()));
                array.add(object);
            }
            pageModel.setList(array);
        }
        pageModel.setCount(count);
        pageModel.setMsg("ok");
        pageModel.setRel(true);
        return JSON.toJSONString(pageModel);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(@RequestParam String params) {
        JSONObject po = JSONObject.parseObject(params);
        PayProduct payProduct = new PayProduct();

        Long id = po.getLong("id");
        String productId = po.getString("productId");

        payProduct.setId(id);
        payProduct.setProductId(productId);
        payProduct.setProductName(po.getString("productName"));
        payProduct.setQuantity(po.getInteger("quantity"));
        payProduct.setAmount(Long.valueOf(AmountUtil.convertDollar2Cent(po.getString("amount"))));
        payProduct.setProductDescription(po.getString("productDescription"));
        payProduct.setProductType(PayProductTypeEnum.byValue(po.getInteger("productType")));
        payProduct.setProductStatus(PayProductStatusEnum.byValue(po.getInteger("productStatus")));
        payProduct.setServiceLength(po.getInteger("serviceLength"));
        payProduct.setServiceLengthUnit(ServiceLengthUnitEnum.byVal(po.getInteger("serviceLengthUnit")));
        payProduct.setBtPlanId(po.getString("btPlanId"));
        if (po.getString("supportAutoPay") == null) {
            po.put("supportAutoPay", "off");
        }
        payProduct.setSupportAutoPay(po.getString("supportAutoPay").equalsIgnoreCase("on"));

        int result;
        if (StringUtils.isBlank(productId)) {
            // 添加
            result = payProductsService.addPayProduct(payProduct);
        } else {
            // 修改
            result = payProductsService.updatePayProduct(payProduct);
        }
        _log.info("保存支付产品,返回:{}", result);
        return result + "";
    }

    @RequestMapping(value = "/view.html", method = RequestMethod.GET)
    public String viewInput(String payProductId, ModelMap model) {
        PayProduct item = null;
        if (StringUtils.isNotBlank(payProductId)) {
            item = payProductsService.selectPayProductInfo(payProductId);
            if (item != null) {
                item.setAmount(item.getAmount() / 100);
            }
        }
        if (item == null) item = new PayProduct();
        model.put("item", item);
        return "products/view";
    }

    @RequestMapping(value = "/{payProductId}/del", method = RequestMethod.GET)
    @ResponseBody
    public String delItem(@PathVariable("payProductId") String payProductId) {
        if (StringUtils.isNotBlank(payProductId)) {
            payProductsService.deleteProduct(payProductId);
            _log.info("成功删除:" + payProductId);
            return "success";
        }
        return "failed";
    }

}