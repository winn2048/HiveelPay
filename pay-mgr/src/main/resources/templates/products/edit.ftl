<div style="margin: 15px;">
    <form class="layui-form">
        <div class="layui-form-item">
            <label class="layui-form-label">ID</label>
            <div class="layui-input-block">
                <input type="text" name="id" placeholder="id" autocomplete="off"
                       class="layui-input" value="${item.id?if_exists }" readonly>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">产品ID</label>
            <div class="layui-input-block">
                <input type="text" name="productId" placeholder="产品ID" autocomplete="off"
                       class="layui-input" value="${item.productId?if_exists }" readonly>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">产品名称</label>
            <div class="layui-input-block">
                <input type="text" name="productName" lay-verify="required" placeholder="产品名称" autocomplete="off"
                       class="layui-input" value="${item.productName?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">产品描述</label>
            <div class="layui-input-block">
				<textarea type="text" name="productDescription" placeholder="产品描述"
                          autocomplete="off" class="layui-input"
                          cols="100" rows="2">${item.productDescription?if_exists }</textarea>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">是否启用AutoPay</label>
            <div class="layui-input-block">
                <input type="checkbox" name="supportAutoPay" lay-skin="switch"
                       <#if (item.supportAutoPay!false) == true >checked="checked"</#if> >
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">订阅ID</label>
            <div class="layui-input-block">
                <input type="text" name="btPlanId"   placeholder="Plan ID " autocomplete="off"
                       class="layui-input" value="${item.btPlanId?if_exists }"/>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">价格</label>
            <div class="layui-input-block">
                <input type="number" name="amount" lay-verify="required" placeholder="价格" autocomplete="off"
                       class="layui-input" value="${item.amount?if_exists }"/>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">数量</label>
            <div class="layui-input-block">
                <input type="number" name="quantity" lay-verify="required" placeholder="数量" autocomplete="off"
                       class="layui-input" value="${item.quantity?if_exists }"/>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">有效期</label>
            <div class="layui-input-block">
                <input type="number" name="serviceLength" lay-verify="required" placeholder="有效期，0-无限时间"
                       autocomplete="off"
                       class="layui-input" value="${item.serviceLength?if_exists }"/>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">单位（有效期）</label>
            <div class="layui-input-block">
                <select name="serviceLengthUnit" id="serviceLengthUnit">
                    <option value="2" <#if item.serviceLengthUnit.val==2 >selected</#if>>小时</option>
                    <option value="4"<#if item.serviceLengthUnit.val==4 >selected</#if>>天</option>
                    <option value="6"<#if item.serviceLengthUnit.val==6 >selected</#if>>月</option>
                </select>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">产品类型</label>
            <div class="layui-input-block">
                <select name="productType" id="productType">
                    <option value="10" <#if item.productType.val==10 >selected</#if> >Membership</option>
                    <option value="12"<#if item.productType.val==12 >selected</#if> >Advancing</option>
                    <option value="14"<#if item.productType.val==14 >selected</#if> >Car Of Day</option>
                    <option value="20"<#if item.productType.val==20 >selected</#if> >Search Result</option>
                    <option value="22"<#if item.productType.val==22 >selected</#if> >Highlighting</option>
                    <option value="30"<#if item.productType.val==30 >selected</#if> >OIL CHANGE</option>
                    <option value="31"<#if item.productType.val==31 >selected</#if> >PREINSPECTION</option>
                    <option value="34"<#if item.productType.val==34 >selected</#if> >TRADE-IN</option>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">产品状态</label>
            <div class="layui-input-block">
                <select name="productStatus" id="productStatus">
                    <option value="1" <#if item.productStatus.val==1 >selected</#if>>已保存</option>
                    <option value="2"<#if item.productStatus.val==2 >selected</#if>>可购买</option>
                    <option value="3"<#if item.productStatus.val==3 >selected</#if>>已锁定</option>
                    <option value="4"<#if item.productStatus.val==4 >selected</#if>>不再服务</option>
                    <option value="5"<#if item.productStatus.val==5 >selected</#if>>卖完了</option>
                </select>
            </div>
        </div>
        <button lay-filter="edit" lay-submit style="display: none;"></button>
    </form>
</div>