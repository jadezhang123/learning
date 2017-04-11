/**
 * Created by Zhang Junwei on 2017/4/10 0010.
 */
define(function (require) {
    //require('common/classSelectorPanel');
    var $ = require('zepto');
    var avalon = require('avalon');
    var common = require('common');
    var eChartsUtility = require('common/echartsUtility');
    //组装Series
    var packingOneSeriesData = function (data, xAxisData) {
        var oneSeriesData = [], maxIndex = data.length - 1, statValue = '';
        for (var index = 0, length = data.length, item; index < length; index++) {
            var item = data[index];
            if (index == maxIndex) {
                statValue = ">=" + item.statValueBegin;
            } else {
                statValue = item.statValueBegin + '~' + item.statValueEnd;
            }
            if (xAxisData[index]) {
                xAxisData[index] = xAxisData[index] + " / " + statValue;
            } else {
                xAxisData[index] = statValue;
            }
            oneSeriesData.push(item.manCount);
        }
        return oneSeriesData;
    };
    var VM = avalon.define({
        $id: 'sportStat',
        sex: 1,
        schoolCode: '',
        examCode: '',
        examName: '',
        gradeCode: '',
        classCode: '',
        reportCode: '',
        classes: [],
        classReport: {},
        detailsOfReport: [],
        init: function () {
            VM.examCode = '62bbd327a97248fea9b7b562583e71f9';
            VM.classCode = '14538893517315347';

            VM.queryReportOfClass();

        },
        //获取班级的报告
        queryReportOfClass: function () {
            common.ajaxFun({
                url: '/grow/sport/classStatistics/examReportOfClass',
                type: 'post',
                data: {
                    examCode: VM.examCode,
                    classCode: VM.classCode
                },
                onSuccess: function (data) {
                    if (data.code) {
                        VM.examName = data.examName;
                        VM.reportCode = data.code;
                        VM.classReport = data;
                        VM.noStatData = false;
                        $("#noDataTip_sport_stat").hide();
                        VM.queryDetailReportBySex();
                        VM.queryHeightAndWeightBySex();
                        VM.drawBMIChart();
                    } else {
                        VM.noStatData = true;
                        $("#noDataTip_sport_stat").show();
                    }
                }
            });
        },

        //渲染BMI图表
        drawBMIChart: function () {
            eChartsUtility.pie('bmiChart', VM.packingOptionDataForBMI());
        },

        //为BMI组装Option
        packingOptionDataForBMI: function () {
            var masterReport = VM.classReport;
            var data = [];
            if (VM.sex === 1) {
                data.push({name: '正常', value: masterReport.bmiOfNormal4M});
                data.push({name: '低体重', value: masterReport.bmiOfLow4M});
                data.push({name: '超重', value: masterReport.bmiOfOver4M});
                data.push({name: '肥胖', value: masterReport.bmiOfFat4M});
            } else {
                data.push({name: '正常', value: masterReport.bmiOfNormal4F});
                data.push({name: '低体重', value: masterReport.bmiOfLow4F});
                data.push({name: '超重', value: masterReport.bmiOfOver4F});
                data.push({name: '肥胖', value: masterReport.bmiOfFat4F});
            }
            return {
                name: '学生BMI情况',
                legendData: ['正常', '低体重', '超重', '肥胖'],
                seriesData: data
            };
        },

        //根据性别查询报告的详情：各项目优秀率分布
        queryDetailReportBySex: function () {
            common.ajaxFun({
                url: '/grow/sport/classStatistics/detailReportOfSex',
                type: 'post',
                data: {
                    reportCode: VM.reportCode,
                    sex: VM.sex
                },
                onSuccess: function (data) {
                    VM.detailsOfReport = data;
                    //eChartsUtility.bar('rateChart', VM.packingOptionDataForRate());
                    eChartsUtility.setOptionForChart('rateChart', VM.packingOptionForRate());
                }
            });
        },

        //各项目优秀率分布
        packingOptionDataForRate: function () {
            var legendData = ['优秀', '良好', '及格', '不及格'];
            var propKeys = ['bestCount', 'betterCount', 'passCount', 'noPassCount'],
                colors = ['#027fb6', '#42bef4', '#fc9139', '#C0C0C0'];
            var yAxisData = [], seriesData = [], oneSeriesData;

            for (var index = 0, length = propKeys.length; index < length; index++) {
                oneSeriesData = [];
                VM.detailsOfReport.forEach(function (item) {
                    if (index == 0) {
                        yAxisData.push(VM.subItemName(item.itemName));
                    }
                    oneSeriesData.push(item[propKeys[index]]);
                });
                seriesData.push({name: legendData[index], data: oneSeriesData, color: colors[index]});
            }

            return {
                legendData: legendData,
                showLegend: true,
                yAxisName: '人数',
                xAxisName: '项目',
                axisData: yAxisData,
                seriesData: seriesData,
                stack: '总量',
                exchangeXY: false
            };
        },

        packingOptionForRate: function () {
            return {
                "title": {"top": "top", "left": "center"},
                "legend": {"top": "top", "left": "right", "show": true, "data": ["优秀", "良好", "及格", "不及格"]},
                "tooltip": {"trigger": "axis", confine: true, "axisPointer": {"type": "line"}},
                "yAxis": [{
                    show: false,
                    "type": "category",
                    "name": "项目",
                    "data": ["肺活量", "50米跑", "坐位体前屈", "立定跳远", "引体向上（男）/1分钟仰卧起坐（女）", "1000米跑（男）/800米跑（女）"]
                }],
                "xAxis": [{"type": "value", "name": "人数", "minInterval": 1}],
                "series": [{
                    "name": "优秀",
                    "type": "bar",
                    "data": [0, 3, 0, 0, 3, 0],
                    "stack": "总量",
                    "itemStyle": {"normal": {"color": "#027fb6"}}
                }, {
                    "name": "良好",
                    "type": "bar",
                    "data": [0, 0, 0, 2, 3, 0],
                    "stack": "总量",
                    "itemStyle": {"normal": {"color": "#42bef4"}}
                }, {
                    "name": "及格",
                    "type": "bar",
                    "data": [1, 0, 3, 0, 0, 3],
                    "stack": "总量",
                    "itemStyle": {"normal": {"color": "#fc9139"}}
                }, {
                    "name": "不及格",
                    "type": "bar",
                    "data": [3, 1, 0, 3, 0, 2],
                    "stack": "总量",
                    "itemStyle": {"normal": {"color": "#C0C0C0"}},
                    label: {
                        normal: {
                            show: true,
                            formatter: '{b}',
                            position: [-10, 0],
                            textStyle: {color: '#000'},
                        }
                    }
                }],
                animation: false
            }
        },

        subItemName: function (name) {
            if (name.length > 9) {
                return name.substring(0, 9) + "\n" + name.substring(9, name.length);
            }
            return name;
        },

        //根据性别查询身高体重分布情况
        queryHeightAndWeightBySex: function () {
            common.ajaxFun({
                url: '/grow/sport/classStatistics/heightAndWeightOfSex',
                type: 'post',
                data: {
                    reportCode: VM.reportCode,
                    sex: VM.sex
                },
                onSuccess: function (data) {
                    VM.drawHWeightChart(data['height'], data['weight']);
                }
            });
        },

        //渲染身高体重图表
        drawHWeightChart: function (hdata, wData) {
            //eChartsUtility.line('h_wChart', VM.packingOptionDataForHWeight(hdata, wData));
            eChartsUtility.setOptionForChart('h_wChart', VM.packingOptionForHWeight());
        },
        //为身高体重图表组装Option
        packingOptionDataForHWeight: function (manCountsAsHeight, manCountsAsWeight) {
            var legendData = ['学生身高', '学生体重'], seriesData = [], xAxisData = [], oneSeriesData = [];

            oneSeriesData = packingOneSeriesData(manCountsAsHeight, xAxisData);
            seriesData.push({name: legendData[0], data: oneSeriesData, color: '#98cdec'});

            oneSeriesData = packingOneSeriesData(manCountsAsWeight, xAxisData);
            seriesData.push({name: legendData[1], data: oneSeriesData, color: '#027fb6'});

            return {
                legendData: legendData,
                showLegend: true,
                yAxisName: '人数',
                xAxisName: 'M/KG',
                axisData: xAxisData,
                seriesData: seriesData,
                showArea: true
            };
        },

        packingOptionForHWeight: function () {
            return {
                "title": {"top": "top", "left": "center"},
                "legend": {"top": "top", "left": "right", "show": true, "data": ["学生身高", "学生体重"]},
                "tooltip": {"trigger": "axis", "axisPointer": {"type": "line"}},
                "xAxis": [{
                    "type": "category",
                    "name": "M/KG",
                    axisLabel:{interval:0},
                    "data": ["0~1.1/0~30", "1.1~1.3/30~40", "1.3~1.5/40~50", ">=1.5/>=50"]
                }],
                "yAxis": [{"type": "value", "name": "人数", "minInterval": 1}],
                "series": [{
                    "name": "学生身高",
                    "type": "line",
                    "data": [1, 2, 3, 3],
                    "itemStyle": {"normal": {"color": "#98cdec"}},
                    "areaStyle": {"normal": {}}
                }, {
                    "name": "学生体重",
                    "type": "line",
                    "data": [3, 2, 5, 3],
                    "itemStyle": {"normal": {"color": "#027fb6"}},
                    "areaStyle": {"normal": {}}
                }]
            };
        },

        afterSelectClass: function () {

        },

        goBack: function () {

        }
    });
    avalon.ready(function () {
        eChartsUtility.initChart('h_wChart');
        eChartsUtility.initChart('bmiChart');
        eChartsUtility.initChart('rateChart');
        VM.init();
    });
});