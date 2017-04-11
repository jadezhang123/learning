/**
 * Created by Zhang Junwei on 2017/4/10 0010.
 */
define(function (require) {
    var echarts = require('echarts');

    var charts = {};

    //将所有的echarts实体挂靠在charts上
    var initChart = function (id) {

        if (!charts[id]) {
            charts[id] = echarts.init(document.getElementById(id));
            charts[id].showLoading({
                text: '正在努力的读取数据中...',
            });
        }
    };

    var pie = function (chartId, optionData, merge) {
        var option = {};
        if (merge) {
            option = {
                title: {text: optionData.name},
                legend: {data: optionData.legendData},
                series: [{name: optionData.name, data: optionData.seriesData}]
            };
        } else {
            option = {
                title: {top: 'top', left: 'center', text: optionData.title,},
                tooltip: {trigger: 'item', formatter: optionData.tooltipFormatter || "{a} <br/>{b} : {c} ({d}%)"},
                legend: {orient: 'vertical', left: 'left', data: optionData.legendData},
                series: [{
                    name: optionData.name, type: 'pie', radius: '40%', center: ['50%', '50%'],
                    data: optionData.seriesData,
                    label: {normal: {show: true, formatter: optionData.labelFormatter || '{b}:{c}'}}
                }]
            };
        }
        setOptionForChart(chartId, option);
    };

    var singleBar = function (chartId, optionData, merge) {
        var option = {};
        if (merge) {
            option = {
                title: {text: optionData.name},
                legend: {data: optionData.legendData},
                series: [{name: optionData.name, data: optionData.seriesData}]
            };
        } else {
            option = {
                title: {text: optionData.name,},
                tooltip: {trigger: 'item'},
                xAxis: [{type: 'category', name: optionData.xAxisName, data: optionData.xAxisData}],
                yAxis: [{type: 'value', name: optionData.yAxisName}],
                series: [{name: optionData.name, type: 'bar', data: optionData.seriesData}]
            };
        }
        setOptionForChart(chartId, option);
    };

    //为类目表创建option，主要为多折线或柱状图
    var getCategoryOption = function (optionData) {
        var option = {
            title: {top: 'top', left: 'center', text: optionData.name},
            legend: {top: 'top', left: 'right', show: optionData.showLegend || false, data: optionData.legendData},
            tooltip: {trigger: 'axis', axisPointer: {type: 'line'}},
            xAxis: [{type: 'category', name: optionData.xAxisName, data: optionData.axisData}],
            yAxis: [{type: 'value', name: optionData.yAxisName, minInterval: 1}],
            series: []
        };
        if (optionData.exchangeXY) {
            option.xAxis = [{type: 'value', name: optionData.xAxisName, minInterval: 1}];
            option.yAxis = [{type: 'category', name: optionData.yAxisName, data: optionData.axisData}];
        }
        return option;
    };

    var packingDataForSeries = function (option, optionData, type) {
        for (var index = 0, length = optionData.seriesData.length, obj; index < length; index++) {
            var obj = optionData.seriesData[index];
            option.series.push({
                name: obj.name, type: type, data: obj.data, stack: optionData.stack,
                itemStyle: {normal: {color: obj.color}},
                areaStyle: (optionData.showArea ? {normal: {}} : null)
            });
        }
    };

    var updateChartByType = function (chartId, optionData, merge, type) {
        var option = {};
        if (merge) {
            option = {
                title: {text: optionData.name},
                legend: {data: optionData.legendData},
                series: []
            };
        } else {
            option = getCategoryOption(optionData);
        }
        packingDataForSeries(option, optionData, type);
        setOptionForChart(chartId, option);
    }

    var setOptionForChart = function (chartId, option) {
        console.log(JSON.stringify(option));
        if (charts[chartId]) {
            charts[chartId].hideLoading();
            charts[chartId].setOption(option);
        } else {
            initChart(chartId);
            setOptionForChart(chartId, option);
        }
    };

    var bar = function (chartId, optionData, merge) {
        updateChartByType(chartId, optionData, merge, 'bar');
    };

    var line = function (chartId, optionData, merge) {
        updateChartByType(chartId, optionData, merge, 'line');
    };

    //绑定监听到series的click事件
    var onSeries = function (chartId, callback) {
        var chart = charts[chartId];
        if (chart) {
            chart.on('click', function (params) {
                // 点击到了 series 上
                if (params.componentType === 'series') {
                    callback && callback(params, chart);
                }
            });
        }
    };

    return {
        initChart: initChart,
        setOptionForChart: setOptionForChart,
        onSeries: onSeries,
        dispatchActionForChart: function (chartId, param) {
            if (charts[chartId]) {
                charts[chartId].dispatchAction(param);
            }
        },
        pie: pie,
        singleBar: singleBar,
        line: line,
        bar: bar
    };

});