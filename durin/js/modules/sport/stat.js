/**
 * Created by Zhang Junwei on 2017/4/10 0010.
 */
define(function (require) {
    //require('common/classSelectorPanel');
    var common = require('common');
    var avalon = require('avalon');
    var eChartsUtility = require('common/echartsUtility');
    //组装Series
    var packingOneSeriesData = function (data, xAxisData) {
        var oneSeriesData = [], maxIndex = data.length - 1, statValue = '';
        for (var index = 0, length = data.length, item; index < length; index++) {
            var item = data[index];
            if (index == maxIndex) {
                statValue = ">="+item.statValueBegin;
            } else {
                statValue = item.statValueBegin + '~' + item.statValueEnd;
            }
            if (xAxisData[index]){
                xAxisData[index] = xAxisData[index] + " / " + statValue;
            }else {
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
        },
        //获取班级的报告
        queryReportOfClass: function () {
            common.ajaxFun({
                url: '/grow/sport/classStatistics/examReportOfClass',
                type: 'post',
                dataType: 'json',
                data: {
                    examCode: VM.examCode,
                    classCode: VM.classCode
                },
                onSuccess: function (data) {
                    if (data.code) {
                        VM.classReport = data;
                        VM.noStatData = false;
                        $("#noDataTip_sport_stat").hide();
                        VM.examName = data.examName;
                        VM.reportCode = data.code;
                        VM.queryDetailReportBySex();
                        VM.queryHeightAndWeightBySex();
                        VM.drawBMIChart(data, VM.sex);
                    } else {
                        VM.noStatData = true;
                        $("#noDataTip_sport_stat").show();
                    }
                }
            });
        },

        //渲染BMI图表
        drawBMIChart: function (masterReport, sex) {
            eChartsUtility.pie('bmiChart', VM.packingOptionDataForBMI(masterReport, sex));
        },

        //为BMI组装Option
        packingOptionDataForBMI: function (masterReport, sex) {
            var data = [];
            if (sex === 1) {
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
            openLoading();
            common.ajaxFun({
                url: '/grow/sport/classStatistics/detailReportOfSex',
                type: 'post',
                dataType: 'json',
                data: {
                    reportCode: VM.reportCode,
                    sex: VM.sex
                },
                onSuccess: function (data) {
                    VM.detailsOfReport = data;
                    eChartsUtility.bar('rateChart', VM.packingOptionDataForRate());
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
                yAxisName: '项目',
                xAxisName: '人数',
                axisData: yAxisData,
                seriesData: seriesData,
                stack: '总量',
                exchangeXY: true
            };
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
                dataType: 'json',
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
            eChartsUtility.line('h_wChart', VM.packingOptionDataForHWeight(hdata, wData));
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

        afterSelectClass: function () {

        }
    });
    avalon.ready(function () {
        eChartsUtility.initChart('h_wChart');
        eChartsUtility.initChart('bmiChart');
        eChartsUtility.initChart('rateChart');
        VM.init();
    });
});