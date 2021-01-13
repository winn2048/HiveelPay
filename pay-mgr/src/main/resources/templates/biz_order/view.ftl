<div style="margin: 15px;">
    <form class="layui-form">
        <div class="layui-form-item">
            <label class="layui-form-label">交易单号</label>
            <div class="layui-input-block">
                <input type="text" disabled="disabled" class="layui-input" value="${item.bizOrderNo!"" }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">客户ID</label>
            <div class="layui-input-block">
                <input type="text" disabled="disabled" class="layui-input" value="${item.customerId!"" }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">产品ID</label>
            <div class="layui-input-block">
                <input type="text" disabled="disabled" class="layui-input" value="${item.productId!"" }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">产品名称</label>
            <div class="layui-input-block">
                <input type="text" disabled="disabled" class="layui-input" value="${item.productName!"" }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">商户ID</label>
            <div class="layui-input-block">
                <input type="text" disabled="disabled" class="layui-input" value="${item.mchId!"" }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">支付通道ID</label>
            <div class="layui-input-block">
                <input type="text" disabled="disabled" class="layui-input" value="${item.channelId!"" }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">金额($)</label>
            <div class="layui-input-block">
                <input type="text" disabled="disabled" class="layui-input" value="${item.amount!"" }">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">订单状态</label>
            <div class="layui-input-block">
				<#if item.orderStatus = 1>
                    <input type="text" style="color: black" disabled="disabled" class="layui-input" value="已保存" }">
                <#elseif item.orderStatus = 2>
                    <input type="text" style="color: green" disabled="disabled" class="layui-input" value="开始支付" }">
                <#elseif item.orderStatus = 3>
                    <input type="text" style="color: orange" disabled="disabled" class="layui-input" value="支付中" }">
                <#elseif item.orderStatus = 4>
                    <input type="text" style="color: orange" disabled="disabled" class="layui-input" value="支付失败" }">
                <#elseif item.orderStatus = 8>
                    <input type="text" style="color: orange" disabled="disabled" class="layui-input" value="支付成功" }">
                <#elseif item.orderStatus = 9>
                    <input type="text" style="color: orange" disabled="disabled" class="layui-input" value="订单有效" }">
                <#elseif item.orderStatus = 11>
                    <input type="text" style="color: orange" disabled="disabled" class="layui-input" value="订单服务终止" }">
                <#else>
                </#if>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">支付成功时间</label>
            <div class="layui-input-block">
                <input type="text" disabled="disabled" class="layui-input" value="${item.paySuccessTime!''}">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">服务开始时间</label>
            <div class="layui-input-block">
                <input type="text" disabled="disabled" class="layui-input" value="${item.serviceTimes!''}">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">服务截止时间</label>
            <div class="layui-input-block">
                <input type="text" disabled="disabled" class="layui-input" value="${item.serviceEndTime!''}">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">创建时间</label>
            <div class="layui-input-block">
                <input type="text" disabled="disabled" class="layui-input" value="${item.createAt!''}">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">更新时间</label>
            <div class="layui-input-block">
                <input type="text" disabled="disabled" class="layui-input" value="${item.lastUpdateAt!''} ">
            </div>
        </div>

        <button lay-filter="edit" lay-submit style="display: none;"></button>
    </form>
</div>