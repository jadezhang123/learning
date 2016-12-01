/**
 * Created by zjw on 2016/11/10.
 */
/**
 * 图表渲染工具
 * 所需data数据格式
 *  1 => [{name：xxx,value:xxx}, ...]  用于饼图、单一的柱形图
 *  2 => {
 *        groupKey1 :[{name:XXX, value:XXX},...],
 *        groupKey2 :[{name:XXX, value:XXX},...],
 *        ...
 *       } 
 */
var OWN_ECHARTS = (function () {

    var chartDataFormatter = {
        /**
         * data格式 => [{name:XXX,value:XXX},{name:XXX,value:XXX},......]
         * 多用于饼图、单一的柱形图的数据源
         * @param data
         * @returns {{category: Array, data: Array}}
         */
        formatNoGroupData: function (data) {
            var categories = [];
            var datas = [];

            for (var i = 0; i < data.length; i++) {
                categories.push(data[i].name || "");
                datas.push({name: data[i].name, value: data[i].value || 0});
            }

            return {category: categories, data: datas};
        },

        /**
         *   data格式 => {
         *        groupKey1 :[{name:XXX, value:XXX},...],
         *        groupKey2 :[{name:XXX, value:XXX},...],
         *        ...
         *   } 
         * @param data
         * @param type
         * @param is_stack
         * @returns {{category: Array, xAxis: Array, series: Array}}
         */
        formatGroupData: function (data, type, isStack) {
            var chart_type = 'line';
            if (type) {
                chart_type = type || 'line';
            }
            var xAxis = [], group = [], series = [];

            var maxLengthKey = '', tempLength = 0;

            for (var key in data) {
                if (data.hasOwnProperty(key)) {
                    group.push(key);
                    if (tempLength < data[key].length) {
                        tempLength = data[key].length;
                        maxLengthKey = key;
                    }
                }
            }

            for (var i = 0, length = data[maxLengthKey].length, obj; i < length; i++) {
                obj = data[maxLengthKey][i];
                xAxis.push(obj.name);
            }

            var xAxisName = '', seriesOption = {};

            for (var i = 0, gLength = group.length; i < gLength; i++) {
                var tempData = [];
                seriesOption = getSeriesOptionByType(type, isStack);
                seriesOption.name = group[i];
                for (var j = 0, xALength = xAxis.length, obj; j < xALength; j++) {
                    xAxisName = xAxis[j];
                    yAxisValue = getValueByNmae(data[group[i]], xAxisName);
                    tempData.push(yAxisValue);
                }
                seriesOption.data = tempData;
                series.push(seriesOption);
            }
            return {category: group, xAxis: xAxis, series: series};
        }
    };

    var getValueByNmae = function(values,name){
        var value = null;
        if ( !values || !name ) {
            return null;
        }
        for (var i = 0, length = values.length, obj; i < length; i++) {
            obj = values[i];
            if (obj.name == name) {
                value = obj.value;
                break;
            }
        }
        return value;
    };

    var getSeriesOptionByType = function(type, isStack){
        var seriesTempOption = {
            type:type,
        };
        if (isStack) {
           seriesTempOption.stack = 'stack'; 
        }
        if (type === 'line') {
            seriesTempOption.connectNulls = true;
        }
        return seriesTempOption;
    };

    var commonOption = {
        //通用的图表基本配置
        common:{
            tooltip: {
                trigger: 'axis'
            },
            toolbox: {
                show: true, //是否显示工具栏
                feature: {
                    mark: true,
                    dataView: {readOnly: false}, //数据预览
                    saveAsImage: true //是否保存图片
                }
            }
        },
        //通用的折线图表的基本配置
        commonLine:{
            tooltip: {
                trigger: 'axis'
            },
            toolbox: {
                show: true,
                feature: {
                    dataView: {readOnly: false},
                    saveAsImage: true,
                }
            }
        }
    };

    /**
     * @param data  数据格式 => [{name：xxx,value:xxx}, ...]
     * @param name
     * @param isCircle  是否显示为环状
     * @param roseType  显示为南丁格尔，'radius' 或 'area'
     */
    var pie = function (data, name, isCircle, roseType) {
        var pieData = chartDataFormatter.formatNoGroupData(data);
        var option = {
            tooltip: {
                trigger: 'item',
                formatter: '{b} : {c} ({d}/%)',
                show: true
            },
            legend: {
                x: 'center',
                y: 'top',
                data: pieData.category
            },
            series: [
                {
                    name: name || "",
                    type: 'pie',
                    radius: isCircle && !roseType ? ['50%', '70%'] : '65%',
                    roseType: roseType,
                    center: ['50%', '50%'],
                    data: pieData.data
                }
            ]
        };
        return $.extend({}, commonOption.common, option);
    };

    var pieWithSubChart = function(){
        
    }

    /**
     * @param data
     * @param xName
     * @param yName
     * @param isStack
     */
    var line = function (data, xName, yName, isStack) {
        var lineData = chartDataFormatter.formatGroupData(data, 'line', isStack);
        var option = {
            legend: {
                x: 'center',
                y: 'top',
                data: lineData.category
            },
            xAxis: [{
                name: xName || '',
                type: 'category', //X轴均为category，Y轴均为value
                data: lineData.xAxis,
            }],
            yAxis: [{
                name: yName || '',
                type: 'value',
            }],
            series: lineData.series
        };
        return $.extend({}, commonOption.commonLine, option);
    };

    return {
        chartOptionTemplates: {
            pie: pie,
            line: line
        },
        renderChart: function (chart, option, merge) {
            chart.setOption(option);
        }
    };
})();
