let saleDom;
let categoryDom
let saleChart;
let categoryChart;
$(function () {
    let year = $("#year").val();
    let month = $("#month").val();
    let categoryID = $("#categoryID").val();
    //初始化第一个图表
    saleDom = $("#saleContainer").get(0);
    saleChart = echarts.init(saleDom);
    let saleOption = initOption();
    saleChart.setOption(saleOption, true);
    saleChart.showLoading();
    initByTime(year, month);
    //初始化第二个图表
    categoryDom = $("#categoryContainer").get(0);
    categoryChart = echarts.init(categoryDom);
    let categoryOption = initOption();
    categoryChart.setOption(categoryOption, true);
    categoryChart.showLoading();
    initByCategory(year, month, categoryID);

    $("#year").change(function () {
        year = $("#year").val();
        month = $("#month").val();
        categoryID = $("#categoryID").val();
        initByTime(year, month);
        initByCategory(year, month, categoryID);
        productRank(year, month);
    });

    $("#month").change(function () {
        year = $("#year").val();
        month = $("#month").val();
        categoryID = $("#categoryID").val();
        initByTime(year, month);
        initByCategory(year, month, categoryID);
        productRank(year, month);
    });

    $("#categoryID").change(function () {
        year = $("#year").val();
        month = $("#month").val();
        categoryID = $("#categoryID").val();
        initByCategory(year, month, categoryID);
    });
});

function initOption() {
    let option = {
        title : {
            left : 'center',
            text : '销售情况'
        },
        tooltip: { //鼠标悬停提示内容
            trigger: 'axis',
            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                type: 'cross'    // 十字线显示
            }
        },
        legend: {
            y: '20px',
            data: ['销售额', '销售量'],
            selectedMode: false,
        },
        xAxis: [
            //X轴
            {
                type: 'category',
                data: [],
                axisLabel: {interval: 0},
            },
        ],
        yAxis: [ //两个y轴  左边y轴
            {
                type: 'value',
                name: "销售额(元)",
                axisLabel: {
                    show: true,
                    interval: 'auto',
                    formatter: '{value} '
                },
                splitLine: {
                    show: true,
                    lineStyle: {
                        type: 'dashed'
                    }
                },
            },
            //右边y轴
            {
                type: 'value',
                name: "销售量",
                axisLabel: {
                    show: true,
                    interval: 'auto',
                    formatter: '{value} '
                },
                splitLine: {
                    show: true,
                    lineStyle: {
                        type: 'dashed'
                    }
                },
                splitArea: {
                    show: false
                },
            }
        ],
        series: [ //用于指定图标显示类型
            //第一个柱状图的数据
            {
                name: '销售额',
                type: 'bar',
                yAxisIndex: '0',// 第一个柱状图的数据
                itemStyle: {
                    normal: {
                        color: function (params){
                            const colorList = [
                                '#C1232B','#B5C334','#FCCE10','#E87C25','#27727B',
                                '#FE8463','#9BCA63','#FAD860','#F3A43B','#60C0DD',
                                '#D7504B','#C6E579','#F4E001','#F0805A','#26C0C0'
                            ];
                            return colorList[params.dataIndex];
                        },
                        label: {
                            show: true
                        }
                    }
                },
                data: []
            },
            //右边Y轴的数据
            {
                name: '销售量',
                type: 'line',
                symbol: 'emptyCircle',
                showAllSymbol: true, //动画效果
                symbolSize: 3,
                smooth: true, //光滑的曲线
                yAxisIndex: '1',
                itemStyle: {
                    normal: {
                        color : '#ffb348',
                        label: {
                            show: true,
                            formatter: '{c}',
                            textStyle: {
                                color: '#000000'
                            }
                        }
                    }
                },
                data: []
            },
        ]
    };
    return option;
}

function initByTime(yearValue, monthValue) {
    console.log(yearValue);
    let year = yearValue;
    let month = monthValue;
    const date = {
        "year" : year,
        "month" : month
    };
    let categoryArray = new Array();
    let saleArray = new Array();
    let quantityArray = new Array();
    $.ajax({
        type : "POST",
        url : "/analysis/toSaleAnalysis/date",
        data : JSON.stringify(date),
        contentType : "application/json",
        dataType : "json",
        success : function (data) {
            for (let i = 0; i < data.length; i++) {
                const res = data[i];
                categoryArray.push(res["categoryName"]);
                saleArray.push(res["sale"]);
                quantityArray.push(res["quantity"]);
            }
            saleChart.hideLoading();
            saleChart.setOption({
                title : {
                    text : year + "年" + month + "月销售情况"
                },
                xAxis : {
                    data : categoryArray
                },
                series : [
                    {
                        data : saleArray
                    },
                    {
                        data : quantityArray
                    }
                ]
            });
        }
    });
}

function initByCategory(yearValue, monthValue, categoryValue) {
    let year = yearValue;
    let month = monthValue;
    let categoryID = categoryValue;
    let categoryName = $("#categoryID").find("option:selected").text();
    console.log(categoryName);
    const date = {
        "year" : year,
        "month" : month,
        "categoryID" : categoryID
    };
    let productArray = new Array();
    let saleArray = new Array();
    let quantityArray = new Array();
    $.ajax({
        type : "POST",
        url : "/analysis/toSaleAnalysis/category",
        data : JSON.stringify(date),
        contentType : "application/json",
        dataType : "json",
        success : function (data) {
            for (let i = 0; i < data.length; i++) {
                const res = data[i];
                productArray.push(res["productName"]);
                saleArray.push(res["sale"]);
                quantityArray.push(res["quantity"]);
            }
            categoryChart.hideLoading();
            categoryChart.setOption({
                title : {
                    text : year + "年" + month + "月" + categoryName + "销售情况"
                },
                xAxis : {
                    data : productArray
                },
                series : [
                    {
                        data : saleArray
                    },
                    {
                        data : quantityArray
                    }
                ]
            });
        },
        error : function () {
            console.log("error")
        }
    });
}

function productRank(yearValue, monthValue) {
    const year = yearValue;
    const month = monthValue;
    const params = {
        "year" : year,
        "month" : month
    }
    const title = year + "年" + month + "月销量排行";
    $("#rankTitle").html(title)
    $.ajax({
        type : "POST",
        url : "/analysis/toSaleAnalysis/productRank",
        data : JSON.stringify(params),
        contentType : "application/json",
        dataType : "json",
        success : function (data) {
            $("#rankList tbody").empty();
            for (let i = 0; i < data.length; i++) {
                const res = data[i];
                const tr = $("<tr></tr>");
                const rank = $("<td></td>").append(i + 1);
                const productName = $("<td></td>").append(res["productName"]);
                const categoryName = $("<td></td>").append(res["categoryName"]);
                const quantity = $("<td></td>").append(res["quantity"]);
                tr.append(rank).append(productName).append(categoryName).append(quantity);
                $("#rankList").append(tr);
            }
        },
        error : function () {

        }
    });
}