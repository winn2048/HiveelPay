<div style="margin: 15px;">
    <form class="layui-form">
        <div class="layui-form-item">
            <label class="layui-form-label">产品ID</label>
            <div class="layui-input-block">
                <input type="text" disabled="disabled" class="layui-input" value="${item.productId!"" }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">名称</label>
            <div class="layui-input-block">
                <input type="text" disabled="disabled" class="layui-input" value="${item.productName!"" }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">描述</label>
            <div class="layui-input-block">
                <textarea disabled="disabled" class="layui-input" cols="100"
                          rows="3">${item.productDescription!"" }</textarea>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">订阅ID</label>
            <div class="layui-input-block">
                <input disabled="disabled" class="layui-input" cols="100" rows="3" value="${item.btPlanId!"" }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">AutoPay</label>
            <div class="layui-input-block">
                <#if item.supportAutoPay = true>
                    <input type="text" style="color: green" disabled="disabled" class="layui-input" value="启用" }">
                <#else>
                <input type="text" style="color: red" disabled="disabled" class="layui-input" value="不支持" }">
                </#if>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">金额($)</label>
            <div class="layui-input-block">
                <input type="text" disabled="disabled" class="layui-input" value="${item.amount!"" }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">数量</label>
            <div class="layui-input-block">
                <input type="text" disabled="disabled" class="layui-input" value="${item.quantity!"" }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">类别</label>
            <div class="layui-input-block">

                <input type="text" disabled="disabled" class="layui-input" value="${item.productType!"" }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">状态</label>
            <div class="layui-input-block">

                <input type="text" disabled="disabled" class="layui-input" value="${item.productStatus!"" }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">有效时长</label>
            <div class="layui-input-block">
                <input type="text" disabled="disabled" class="layui-input" value="${item.serviceLength!"" }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">有效时长单位</label>
            <div class="layui-input-block">
                <input type="text" disabled="disabled" class="layui-input" value="${item.serviceLengthUnit!"" }">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">创建时间</label>
            <div class="layui-input-block">
                <input type="text" disabled="disabled" class="layui-input"
                       value="${(item.createAt?string("yyyy-MM-dd HH:mm:ss"))!''} ">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">更新时间</label>
            <div class="layui-input-block">
                <input type="text" disabled="disabled" class="layui-input"
                       value="${(item.lastUpdateAt?string("yyyy-MM-dd HH:mm:ss"))!''} ">
            </div>
        </div>

        <button lay-filter="edit" lay-submit style="display: none;"></button>
    </form>
</div>