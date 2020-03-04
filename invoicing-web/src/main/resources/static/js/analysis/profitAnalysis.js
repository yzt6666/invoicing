let profitDom;
let profitChart;
$(function () {
    let year = $("#year").val();
    profitDom = $("#profitContainer").get(0);
    profitChart = echarts.init(profitDom);
    const option = initOption();
    profitChart.setOption(option, true);
    profit(year);
    $("#year").change(function () {
        year = $("#year").val();
        profit(year);
    });
});

function initOption() {
    const option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        legend: {
            data: ['利润', '支出', '收入']
        },

        xAxis: [
            {
                type: 'value',
                name : "盈亏情况(千元)"
            }
        ],
        yAxis: [
            {
                type: 'category',
                name : "月份",
                axisTick: {
                    show: false
                },
                data: []
            }
        ],
        series: [
            {
                name: '利润',
                type: 'bar',
                label: {
                    show: true,
                    position: 'inside'
                },
                data: []
            },
            {
                name: '收入',
                type: 'bar',
                stack: '总量',
                label: {
                    show: true
                },
                data: []
            },
            {
                name: '支出',
                type: 'bar',
                stack: '总量',
                label: {
                    show: true,
                    position: 'left'
                },
                data: []
            }
        ]
    };
    return option;
}

function profit(yearValue) {
    const year = yearValue;
    $.ajax({
        type : "GET",
        url : "/analysis/toProfitAnalysis/profit",
        data : "year="+year,
        contentType : "text/html",
        dataType : "json",
        success : function (data) {
            let monthArray = new Array();
            let saleArray = new Array();
            let payArray = new Array();
            let profitArray = new Array();
            for (let i = 0; i < data.length; i++) {
                const res = data[i];
                monthArray.push(res["month"]+"月");
                saleArray.push((res["sale"] / 1000).toFixed(2));
                payArray.push((-res["pay"] / 1000).toFixed(2));
                profitArray.push((res["profit"] / 1000).toFixed(2));
            }
            profitChart.setOption({
                yAxis: [
                    {
                        data : monthArray
                    },
                ],
                series : [
                    {
                        data : profitArray
                    },
                    {
                        data : saleArray
                    },
                    {
                        data : payArray
                    }
                ]
            })
        },
        error : function () {

        }
    });
}