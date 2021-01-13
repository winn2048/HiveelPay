package com.hiveelpay.mgr.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hiveelpay.common.enumm.BizOrderStatus;
import com.hiveelpay.common.util.AmountUtil;
import com.hiveelpay.common.util.DateUtil;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.dal.dao.model.BizOrder;
import com.hiveelpay.dal.dao.plugin.PageModel;
import com.hiveelpay.mgr.service.BizOrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/biz_order")
public class BizOrderController {

    private final static MyLog _log = MyLog.getLog(BizOrderController.class);

    @Autowired
    private BizOrderService bizOrderService;

    @RequestMapping("/list.html")
    public String listInput(ModelMap model) {
        return "biz_order/list";
    }

    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute BizOrder bizOrder, Integer pageIndex, Integer pageSize) {
        if (bizOrder.getOrderStatus() != null && bizOrder.getOrderStatus().equals(BizOrderStatus.ILLEGAL)) {
            bizOrder.setOrderStatus(null);// all
        }

        PageModel pageModel = new PageModel();
        int count = bizOrderService.count(bizOrder);
        if (count <= 0) return JSON.toJSONString(pageModel);
        List<BizOrder> bizOrderList = bizOrderService.getPayOrderList((pageIndex - 1) * pageSize, pageSize, bizOrder);
        if (!CollectionUtils.isEmpty(bizOrderList)) {
            JSONArray array = new JSONArray();
            for (BizOrder po : bizOrderList) {
                JSONObject object = (JSONObject) JSONObject.toJSON(po);
                if (po.getCreateAt() != null) object.put("createTime", DateUtil.date2Str(po.getCreateAt()));
                if (po.getPaySuccessTime() != null)
                    object.put("paySuccessTime", DateUtil.date2Str(po.getPaySuccessTime()));
                if (po.getAmount() != null) object.put("amount", AmountUtil.convertCent2Dollar(po.getAmount() + ""));
                if (po.getOrderStatus() != null) object.put("orderStatus", po.getOrderStatus().getVal());
                array.add(object);
            }
            pageModel.setList(array);
        }
        pageModel.setCount(count);
        pageModel.setMsg("ok");
        pageModel.setRel(true);
        return JSON.toJSONString(pageModel);
    }

    @RequestMapping("/view.html")
    public ModelAndView viewInput(String bizOrderNo) {
        BizOrder item = null;
        if (StringUtils.isNotBlank(bizOrderNo)) {
            item = bizOrderService.selectPayOrder(bizOrderNo);
        }
        ModelAndView mav = new ModelAndView("biz_order/view");
        if (item == null) {
            item = new BizOrder();
            mav.addObject("item", item);
            return mav;
        }
        JSONObject object = (JSONObject) JSON.toJSON(item);
        if (item.getPaySuccessTime() != null)
            object.put("paySuccessTime", DateUtil.date2Str(item.getPaySuccessTime()));
        if (item.getCreateAt() != null)
            object.put("createAt", DateUtil.date2Str(item.getCreateAt()));
        if (item.getLastUpdateAt() != null)
            object.put("lastUpdateAt", DateUtil.date2Str(item.getLastUpdateAt()));
        if (item.getServiceTimes() != null && !item.getServiceTimes().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            item.getServiceTimes().forEach(i -> {
                sb.append(DateUtil.date2Str(i.getServiceStartTime()));
                sb.append("-");
                sb.append(DateUtil.date2Str(i.getServiceEndTime()));
                sb.append(";");

            });
            object.put("serviceTimes", sb.toString());
        }
        if (item.getAmount() != null) object.put("amount", AmountUtil.convertCent2Dollar(item.getAmount() + ""));
        if (item.getPayAmount() != null)
            object.put("payAmount", AmountUtil.convertCent2Dollar(item.getPayAmount() + ""));
        if (item.getOrderStatus() != null) object.put("orderStatus", item.getOrderStatus().getVal());

        mav.addObject("item", object);
        return mav;
    }

}