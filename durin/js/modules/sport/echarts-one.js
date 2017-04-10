// JavaScript Document
(function (){
var dom = document.getElementById("all-sgtz");
  var myChart = echarts.init(dom);
  var app = {};
  option = null;
  option = {
  title: {
      text: ''
  },
  tooltip : {
      trigger: 'axis'
  },
  legend: {
      x:'90%',
      data:['学生体重','学生身高']
  },
  toolbox: {
      feature: {
          saveAsImage: {}
      }
  },
  grid: {
      left: '1%',
      right: '1%',
      bottom: '3%',
      containLabel: true
  },
  xAxis : [
      {
          type : 'category',
          boundaryGap : false,
          data : ['0','0~30/0~1.1','30~40/1.1~1.3','40~50/1.3~1.5','≥50/≥1.5','(kg/M)']
      }
  ],
  yAxis : [
      {
          type : 'value'
      }
  ],
  series : [
      {
          name:'学生体重',
          type:'line',
		  itemStyle:{normal: {color:'#596c96'}},
          stack: '总量',
          areaStyle: {normal: {}},
          data:[0,2, 8, 20, 10,  0]
      },
      {
          name:'学生身高',
          type:'line',
          stack: '总量',
		  itemStyle:{normal: {color:'#68a0f4'}},
          label: {
              normal: {
                  show: true,
                  position: 'top'
              }
          },
          areaStyle: {normal: {}},
          data:[0,11, 9, 14,6, 0]
      }
  ]
  };
  ;
  if (option && typeof option === "object") {
  myChart.setOption(option, true);
  }
})();

  //第二部分
(function (){
var dom = document.getElementById("all_ech");
var myChart = echarts.init(dom);
var app = {};
option = null;
option = {
    title : {
        text: '',
        subtext: '',
        x:'left'
    },
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
    },
    legend: {
        orient: 'vertical',
        x:'left',
    y:'10px',
        data: ['正常','低体重','超重','肥胖']
    },
    series : [
        {
            name: '人数/比例',
            type: 'pie',
            radius : '52%',
            center: ['50%', '60%'],
            data:[
                {value:5, name:'低体重',itemStyle:{normal: {color:'#68a0f4'}},},
                {value:5, name:'超重',itemStyle:{normal: {color:'#ff8a00'}},},
                {value:5, name:'肥胖',itemStyle:{normal: {color:'#ddd'}},},
                {value:25, name:'正常',itemStyle:{normal: {color:'#28ce57'}},},
            ],
            itemStyle: {
                emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            }
        }
    ]
};;
if (option && typeof option === "object") {
    myChart.setOption(option, true);
}
})();
  
// 第三部分
(function (){
  var dom = document.getElementById("all_tc");
var myChart = echarts.init(dom);
var app = {};
option = null;
app.title = '堆叠条形图';

option = {
    tooltip : {
        trigger: 'axis',
        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        }
    },
    legend: {
    x:'right',
        data: ['优秀', '良好','及格','不及格']
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis:  {
        type: 'value'
    },
    yAxis: {
        type: 'category',
        data: ['肺活量','50米跑步','仰卧起坐','1分钟跳绳','50米*8往返跑','坐位体前屈']
    },
  series: [
	  {
		  name: '优秀',
		  type: 'bar',
		  stack: '总量',
		  itemStyle:{normal: {color:'#28ce57'}},
		  label: {
			  normal: {
				  show: true,
				  position: 'insideRight'
			  }
		  },
		  data: [11, 10, 8, 10,12,15]
	  },
	  {
		  name: '良好',
		  type: 'bar',
		  stack: '总量',
		  itemStyle:{normal: {color:'#68a0f4'}},
		  label: {
			  normal: {
				  show: true,
				  position: 'insideRight'
			  }
		  },
		  data: [11, 10, 14,10,15,11]
	  },
	  {
		  name: '及格',
		  type: 'bar',
		  stack: '总量',
		  itemStyle:{normal: {color:'#ff8a00'}},
		  label: {
			  normal: {
				  show: true,
				  position: 'insideRight'
			  }
		  },
		  data: [7, 11, 4,14,8,7]
	  },
	  {
		  name: '不及格',
		  type: 'bar',
		  stack: '总量',
		  itemStyle:{normal: {color:'#e0e0e0'}},
		  label: {
			  normal: {
				  show: true,
				  position: 'insideRight'
			  }
		  },
		  data: [11, 9, 14,6,5,7]
	  },
       
    ]
};;
if (option && typeof option === "object") {
    myChart.setOption(option, true);
}
})();
