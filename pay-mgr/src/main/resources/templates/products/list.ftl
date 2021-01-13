<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>支付产品列表</title>
    <link rel="stylesheet" href="../plugins/layui/css/layui.css" media="all"/>
    <link rel="stylesheet" href="../css/global.css" media="all">
    <link rel="stylesheet" href="../plugins/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="../css/table.css"/>
</head>

<body>
<div class="admin-main">

    <blockquote class="layui-elem-quote">
        <button type="button" class="layui-btn layui-btn-small" id="add"><i class="fa fa-plus" aria-hidden="true"></i>
            添加
        </button>
        <div class="layui-form" style="float:right;">
            <div class="layui-form-item" style="margin:0;">
                <label class="layui-form-label">产品ID</label>
                <div class="layui-input-inline">
                    <input type="text" name="productId" id="productId" autocomplete="off" class="layui-input">
                </div>
                <label class="layui-form-label">产品状态</label>
                <div class="layui-input-inline">
                    <select name="status" id="status" lay-search="">
                        <option value="-99">所有状态</option>
                        <option value="1">已保存</option>
                        <option value="2">可购买</option>
                        <option value="3">已锁定</option>
                        <option value="4">不再服务了</option>
                        <option value="5">卖完了</option>
                    </select>
                </div>
                <div class="layui-form-mid layui-word-aux" style="padding:0;">
                    <button id="search" lay-filter="search" class="layui-btn" lay-submit><i class="fa fa-search"
                                                                                            aria-hidden="true"></i> 查询
                    </button>
                </div>
            </div>
        </div>
    </blockquote>

    <fieldset class="layui-elem-field">
        <legend>产品列表</legend>
        <div class="layui-field-box layui-form">
            <table class="layui-table admin-table">
                <thead>
                <tr>
                    <th style="width: 30px;"><input type="checkbox" lay-filter="allselector" lay-skin="primary"></th>
                    <th>产品ID</th>
                    <th>产品名称</th>
                    <th>数量</th>
                    <th>产品类型</th>
                    <th>金额</th>
                    <th>状态</th>
                    <th>服务时长</th>
                    <th>AutoPay(订阅ID)</th>
                    <th>创建时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody id="content">
                </tbody>
            </table>
        </div>
    </fieldset>
    <div class="admin-table-page">
        <div id="paged" class="page">
        </div>
    </div>
</div>
<!--模板-->
<script type="text/html" id="tpl">
    {{# layui.each(d.list, function(index, item){ }}
    <tr>
        <td><input type="checkbox" lay-skin="primary"></td>
        <td>{{ item.productId }}</td>
        <td>{{ item.productName }}</td>
        <td>{{ item.quantity }}</td>
        <td>{{ item.productType }}</td>
        <td>{{ item.amount }}</td>
        <td>{{ item.productStatus }}</td>
        <td>{{ item.serviceLength }} {{ item.serviceLengthUnit }}</td>
        <td>{{ item.supportAutoPay }} <span style="color: red;">{{ item.btPlanId }}</span></td>
        <td>{{ item.createTime }}</td>
        <td>
            <a href="javascript:;" data-id="{{ item.productId }}" data-opt="view"
               class="layui-btn layui-btn-normal layui-btn-mini">详情</a>
            <a href="javascript:;" data-id="{{ item.productId }}" data-opt="edit"
               class="layui-btn layui-btn-mini">编辑</a>
            <a href="javascript:;" data-id="{{ item.productId }}" data-opt="del"
               class="layui-btn layui-btn-danger layui-btn-mini">删除</a>
        </td>
    </tr>
    {{# }); }}
</script>
<script type="text/javascript" src="../plugins/layui/layui.js"></script>
<script>
    layui.config({
        base: '../js/'
    });

    layui.use(['paging', 'form'], function () {
        var $ = layui.jquery,
                paging = layui.paging(),
                layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
                layer = layui.layer, //获取当前窗口的layer对象
                form = layui.form();

        paging.init({
            openWait: true,
            url: '/products/list?v=' + new Date().getTime(), //地址
            elem: '#content', //内容容器
            params: { //发送到服务端的参数
            },
            type: 'GET',
            tempElem: '#tpl', //模块容器
            pageConfig: { //分页参数配置
                elem: '#paged', //分页容器
                pageSize: 10 //分页大小
            },
            success: function () { //渲染成功的回调
                //alert('渲染成功');
            },
            fail: function (msg) { //获取数据失败的回调
                //alert('获取数据失败')
            },
            complate: function () { //完成的回调
                //alert('处理完成');
                //重新渲染复选框
                form.render('checkbox');
                form.on('checkbox(allselector)', function (data) {
                    var elem = data.elem;

                    $('#content').children('tr').each(function () {
                        var $that = $(this);
                        //全选或反选
                        $that.children('td').eq(0).children('input[type=checkbox]')[0].checked = elem.checked;
                        form.render('checkbox');
                    });
                });

                //绑定所有预览按钮事件
                $('#content').children('tr').each(function () {
                    var $that = $(this);
                    $that.children('td:last-child').children('a[data-opt=view]').on('click', function () {
                        viewForm($(this).data('id'));
                    });
                });

                //绑定所有编辑按钮事件
                $('#content').children('tr').each(function () {
                    var $that = $(this);
                    $that.children('td:last-child').children('a[data-opt=edit]').on('click', function () {
                        editForm($(this).data('id'));
                    });
                });

                //绑定所有删除按钮事件
                $('#content').children('tr').each(function () {
                    var $that = $(this);
                    $that.children('td:last-child').children('a[data-opt=del]').on('click', function () {
                        delItem($(this).data('id'));
                    });
                });

            },
        });
        //获取所有选择的列
        $('#getSelected').on('click', function () {
            var names = '';
            $('#content').children('tr').each(function () {
                var $that = $(this);
                var $cbx = $that.children('td').eq(0).children('input[type=checkbox]')[0].checked;
                if ($cbx) {
                    var n = $that.children('td:last-child').children('a[data-opt=edit]').data('name');
                    names += n + ',';
                }
            });
            layer.msg('你选择的名称有：' + names);
        });

        $('#search').on('click', function () {
            var productId = $("#productId").val();
            var status = $("#status").val();
            paging.get({
                "productId": productId,
                "status": status,
                "v": new Date().getTime()
            });
        });

        var addBoxIndex = -1;
        $('#add').on('click', function () {
            if (addBoxIndex !== -1)
                return;
            editForm('');
        });
        $('#import').on('click', function () {
            var that = this;
            var index = layer.tips('只想提示地精准些', that, {tips: [1, 'white']});
            $('#layui-layer' + index).children('div.layui-layer-content').css('color', '#000000');
        });

        function delItem(id) {
            $.get('/products/' + id + '/del', null, function (form) {
                if (form == 'success') {
                    layer.msg('删除成功!', {icon: 1});
                } else if (form == 'failed') {
                    layer.msg('删除失败!', {icon: 2});
                }
                location.reload(); //刷新
            });
        }

        function viewForm(id) {
            //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
            $.get('/products/view.html?payProductId=' + id, null, function (form) {
                addBoxIndex = layer.open({
                    type: 1,
                    title: '详情',
                    content: form,

                    shade: false,
                    offset: ['100px', '30%'],
                    area: ['600px', '550px'],
                    zIndex: 19950924,
                    maxmin: false,

                    full: function (elem) {
                        var win = window.top === window.self ? window : parent.window;
                        $(win).on('resize', function () {
                            var $this = $(this);
                            elem.width($this.width()).height($this.height()).css({
                                top: 0,
                                left: 0
                            });
                            elem.children('div.layui-layer-content').height($this.height() - 95);
                        });
                    },
                    end: function () {
                        addBoxIndex = -1;
                    }
                });
                layer.full(addBoxIndex);
            });
        }

        function editForm(productId) {
            //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
            $.get('/products/edit.html?payProductId=' + productId, null, function (form) {
                addBoxIndex = layer.open({
                    type: 1,
                    title: '保存产品',
                    content: form,
                    btn: ['保存', '取消'],
                    shade: false,
                    offset: ['100px', '30%'],
                    area: ['600px', '450px'],
                    zIndex: 19950924,
                    maxmin: false,
                    yes: function (index) {
                        //触发表单的提交事件
                        $('form.layui-form').find('button[lay-filter=edit]').click();
                    },
                    full: function (elem) {
                        var win = window.top === window.self ? window : parent.window;
                        $(win).on('resize', function () {
                            var $this = $(this);
                            elem.width($this.width()).height($this.height()).css({
                                top: 0,
                                left: 0
                            });
                            elem.children('div.layui-layer-content').height($this.height() - 95);
                        });
                    },
                    success: function (layero, index) {
                        //弹出窗口成功后渲染表单
                        var form = layui.form();
                        form.render();
                        form.on('submit(edit)', function (data) {
                            //这里可以写ajax方法提交表单
                            $.ajax({
                                type: "POST",
                                url: "/products/save",
                                data: "params=" + JSON.stringify(data.field),
                                success: function (msg) {
                                    if (msg == 1) {
                                        layerTips.msg('保存成功');
                                        layerTips.close(index);
                                        location.reload(); //刷新
                                    } else {
                                        layerTips.msg('保存失败');
                                        layerTips.close(index);
                                        location.reload(); //刷新
                                    }
                                }
                            });
                            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                        });
                    },
                    end: function () {
                        addBoxIndex = -1;
                    }
                });
                layer.full(addBoxIndex);
            });
        }

    });
</script>
</body>

</html>