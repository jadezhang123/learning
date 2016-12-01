/**
 *此工具类用于生产常用图表的Option，特殊Option需要自我定制
 */
 var EchartsOptionFactory = (function () {
	//饼图的通用设置
	var pieCommOption = {
		title : {
			x:'center'
		},
		tooltip : {
			trigger: 'item',
			formatter: "{a} <br/>{b} : {c} ({d}%)"
		},
		legend: {
			x : 'center',
			y : 'top',
		},
		toolbox: {
			show : true,
			feature : {
				mark : {show: true},
				dataView : {show: true, readOnly: true},
				magicType : {
					show: true,
					type: ['pie', 'funnel']
				},
				saveAsImage : {show: true}
			}
		},
	};

	var commOption = {
		pie: pieCommOption,
	};

	getCommOption = function (individuationOption) {

		var chartType = individuationOption.type;

		var option = commOption[chartType] || {};
		option.title.text = individuationOption.name || '';
		
		option.legend.data = individuationOption.legendData || [];

		if ('pie' === chartType) {
			return getPieOption(option, individuationOption);
		}
		return option;
	};

	//私有方法
	getPieOption = function(option, individuationOption){
		
		var seriesOptionGenerator = getSeriesOptionGenerator(individuationOption.type);

		var subChart = individuationOption.subChart;

		var radius = individuationOption.radius;
		
		option.series = [];
		var hasSubChart = subChart && true;
		var seriesOption = {};
		if (!hasSubChart) {
			seriesOption = seriesOptionGenerator({
				radius: radius,
				roseType: individuationOption.roseType
			});
			seriesOption.name = individuationOption.name || '';
			option.series.push(seriesOption);
		}else{

			var chartNames = individuationOption.subChart.chartNames || [];
			var columnCount = parseInt(subChart.columnCount), rowCount = parseInt(subChart.rowCount);
			columnCount = isNaN(columnCount) ? 2: columnCount;
			rowCount = isNaN(rowCount) ? 2: rowCount;

			if (rowCount * columnCount < chartNames.length) {
				rowCount = Math.ceil(chartNames.length / columnCount);
			}

			var colGap = 100/(columnCount*2), rowGap = 100/(rowCount*2);
			var colNo=0,rowNo=0;

			var nameIndex = 0;
			var subChartRadius;
			if (!radius) {
			 	subChartRadius = (100/(rowCount*columnCount)+ 10) + '%';
			}else{
				subChartRadius = radius;
			}
			for (rowNo = 0; rowNo < rowCount; rowNo++) {
				for (colNo = 0; colNo < columnCount; colNo++) {
					nameIndex = rowNo*columnCount + colNo;
					if (nameIndex < chartNames.length) {
						seriesOption = seriesOptionGenerator({
							radius: subChartRadius,
							roseType: individuationOption.roseType
						}); 
						seriesOption.name = chartNames[nameIndex];
						seriesOption.center = [(rowNo*2+1)*rowGap + '%', (colNo*2+1)*colGap + '%'];
						option.series.push(seriesOption);
					}
					
				}		
			}
		}

		return option;
	};

	getSeriesOptionGenerator = function(type){
		switch(type){
			case 'pie':
				return function(option){
					return {
						type:'pie',
						radius : option.radius || '60%',
						roseType : option.roseType,
						label: {
							normal: {
								show: false
							},
							emphasis: {
								show: true
							}
						},
						lableLine: {
							normal: {
								show: false
							},
							emphasis: {
								show: true
							}
						}
					};
				};
			default:
				return function(){

				};
		}
	};

	return {
		getCommOption:getCommOption
	};
})();


