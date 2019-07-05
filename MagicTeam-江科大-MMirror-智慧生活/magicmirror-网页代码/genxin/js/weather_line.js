
// 基于准备好的dom，初始化echarts实例
var myChart = echarts.init(document.getElementById('weather-line'));

var time = [];
var temperature = [];
var humidity = [];
var smog = [];


option = {
    title: {
        text: '家居温湿度、烟雾变化情况'
    },
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data: ['温度', '湿度', '烟雾']
    },
    grid: {
        right: 85,
        left: 45
    },
    toolbox: {          //右上角工具栏
        show: true,
        feature: {
            dataZoom: {
                yAxisIndex: 'none'
            },
            dataView: { readOnly: true },
            magicType: { type: ['line', 'bar'] },
            saveAsImage: {}
        }
    },
    xAxis: {
        type: 'category',
        boundaryGap: false,
        data: []
    },
    yAxis: [
        {
            name: '温度',
            type: 'value',
            position: 'right',
            min: function (value) {
                return value.min - 3;
            },
            max: function (value) {
                return value.max + 3;
            },
            axisLabel: {
                formatter: '{value} °C'
            }
        },
        {
            name: '湿度',
            type: 'value',
            position: 'left',
            offset: 5,
            axisLabel: {
                formatter: '{value} %'
            },
        },
        {
            name: '烟雾',
            type: 'category',
            offset: 37,
            data: ['fine', 'dangerous']
        }
    ]
    ,

    series: [
        {
            name: '温度',
            type: 'line',
            color: ['#0066FF'],
            data: [],
            markPoint: {
                data: [
                    { type: 'max', name: '最大值' },
                    { type: 'min', name: '最小值' }
                ]
            }
        },
        {
            name: '湿度',
            type: 'line',
            color: ['#009900'],
            data: [],
            yAxisIndex: 1,
        },
        {
            name: '烟雾',
            type: 'line',
            step: 'end',
            color: ['#C23531'],
            data: [],
            yAxisIndex: 2


        }
    ]
};
myChart.setOption(option);
myChart.showLoading();  //图表加载动画

updateChart();         //更新图表

for (var i = 0; i < smog.length; i++) {
    if (smog[i] != 'fine') {
        alert("请检查您家中空气质量情况！！");
        alert("请确认！您应检查家中空气质量情况！！");
        break;
    }
}

//更新图表的方法
function updateChart() {
    $.ajax({
        type: "get",
        async: true,            //异步请求（同步请求将会锁住浏览器，用户其他操作必须等待请求完成才可以执行）
        url: "http://101.132.169.177/magicmirror/genxin/sensor_10.php",    //请求发送
        data: {},
        dataType: "json",        //返回数据形式为json
        success: function (result) {
            //请求成功时执行该函数内容，result即为服务器返回的json对象
            if (result) {
                for (var i = 0; i < result.length; i++) {
                    temperature.push(result[i].temperature);    //挨个取出并填入数组  
                }
                temperature.reverse();
                for (var i = 0; i < result.length; i++) {
                    humidity.push(result[i].humidity);    //挨个取出并填入数组
                }
                humidity.reverse();
                for (var i = 0; i < result.length; i++) {
                    smog.push(result[i].smoke);    //挨个取出并填入数组
                }
                smog.reverse();
                for (var i = 0; i < result.length; i++) {
                    var temp=((String)(result[i].reg_date)).split(" ");
                    time.push(temp[1]);    //挨个取出并填入数组
                }
                time.reverse();
                myChart.hideLoading();    //隐藏加载动画
                myChart.setOption({        //加载数据图表
                    xAxis: {
                        data: time

                    },
                    series: [
                        {
                            // 根据名字对应到相应的系列
                            name: '温度',
                            data: temperature
                        },
                        {
                            name: '湿度',
                            data: humidity
                        },
                        {
                            name: '烟雾',
                            data: smog
                        }
                    ]
                });

            }

        },
        error: function (errorMsg) {
            //请求失败时执行该函数
            alert("您的浏览器不支持AJAX，图表请求数据失败!");
            myChart.hideLoading();
        }
    })
}