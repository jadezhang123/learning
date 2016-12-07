/**
 * Created by Zhang Junwei on 2016/12/5 0005.
 */
var pagingComponent = null;
avalon.ready(function () {
    //添加公共模块的依赖
    var layerModule = COMMON.layer;

    //执行分页查询，为私有方法
    var executePaging = function () {
        layerModule.openLoading();
        pagingComponent.queryingParam.pageNo = pagingComponent.pageNo;
        $.ajaxFun({
            url: pagingComponent.pagingURL,
            type: "post",
            dataType: "json",
            data: pagingComponent.queryingParam,
            onSuccess: function (result) {
                pagingComponent.successCallback(result);
                pagingComponent.pageNo = result.pageNo;
                pagingComponent.total = result.totalPages;
                pagingComponent.records = result.recordCount;
                if (result.rows.length === 0) {
                    $("#paging_noDataTip").show();
                } else {
                    $("#paging_noDataTip").hide();
                }
                layerModule.closeLoading();
            }
        });
    };
    pagingComponent = avalon.define({
        $id: 'pagingComponent',
        pagingURL: '',
        queryingParam: {},
        pageNo: 1,                //页码
        total: 1,                //总页数
        records: 1,              //总计记录数
        noDataTip: '无数据！',
        successCallback: function (page) {
            console.log(page);
        },

        /**
         * 配置分页相关参数，在加载此组件后调用
         * @param data
         *     {
         *         pagingURL:'url',   //分页请求的url, *初始化时必须配置*
         *         queryingParam: {}, //分页请求的查询参数
         *         successCallback:function, //分页请求成功后的回调方法,传入参数为后台返回的分页对象（BizData4Page），*初始化时必须配置*
         *         noDataTip: '',     //无数据时的提示信息
         *     }
         *     可缺省配置
         */
        configure: function (data) {
            if ((!pagingComponent.pagingURL && !data.pagingURL) || (!pagingComponent.successCallback && !data.successCallback)) {
                throw new Error('the pagingURL and successCallback must be configured');
            }
            for (var key in data) {
                if (data.hasOwnProperty(key) && data[key]) {
                    pagingComponent[key] = data[key];
                }
            }
        },
        /**
         * 更新查询参数，两种调用方式
         *
         * 1：updateQueryingParam(param)
         *    param: 查询参数对象
         *
         * 2: updateQueryingParam(key, value)
         *    key: 查询参数对象的属性名
         *    value: 查询参数对象的属性名对应的值
         */
        updateQueryingParam: function () {
            if (arguments.length == 0) {
                pagingComponent.queryingParam = arguments[0];
            } else if (arguments.length == 2) {
                pagingComponent.queryingParam[arguments[0]] = arguments[1];
            }
        },

        queryPage: function (pageNo) {
            if (pageNo > pagingComponent.total || pageNo < 1) {
                return;
            }
            pagingComponent.pageNo = pageNo;
            executePaging();
        }

    });
    avalon.scan(document.getElementById('pagingComponent'), pagingComponent);
});