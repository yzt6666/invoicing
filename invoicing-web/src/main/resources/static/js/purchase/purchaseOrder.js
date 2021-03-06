let productArray = new Array();
$(function () {

});
//扩展订单
function expandOrder(dom) {
    const self = $(dom);
    const td = self.parent("td");
    const tr = td.parent("tr");
    const orderID = tr.find("td:eq(1)").text();
    const span = self.children("span");
    if (span.hasClass("fa-angle-right")) {
        span.removeClass("fa-angle-right");
        span.addClass("fa-angle-down");
        $.ajax({
            type : "GET",
            url : "/purchase/purchaseOrder/detail/" + orderID,
            data : JSON.stringify(orderID),
            contentType : "application/json",
            dataType : "json",
            success : function (data) {
                let sum = 0;
                const newTr = $("<tr></tr>");
                const newTd = $("<td colspan='7'></td>");
                for (let i = 0; i < data.length; i++) {
                    const res = data[i];
                    const div = $("<div class='row form-inline' align='left'></div>")
                        .append($("<label style='padding-left: 30px;'>产品名称：</label>")).append(res["productName"])
                        .append($("<label style='padding-left: 30px;'>进价：</label>")).append(res["purchasePrice"] + "元")
                        .append($("<label style='padding-left: 30px;'>数量：</label>")).append(res["quantity"])
                        .append($("<label style='padding-left: 30px;'>合计金额：</label>")).append(res["totalPrice"] + "元");
                    newTd.append(div);
                    newTr.append(newTd);
                    tr.after(newTr);
                    sum = sum + res["totalPrice"];
                }
                const sumDiv = $("<div class='row form-inline' align='left'></div>")
                    .append($("<label style='padding-left: 30px;'>总金额：</label>")).append(sum + "元");
                newTd.append(sumDiv);
                newTr.append(newTd);
                tr.after(newTr);
            }
        });
    } else {
        span.removeClass("fa-angle-down");
        span.addClass("fa-angle-right");
        tr.next().remove();
    }
}

//根据订单状态条件设置请求的url
function orderStatus() {
    const flag = $("#flag").val();
    const startDate = $("#startDate").val();
    const endDate = $("#endDate").val();
    let url;
    if (startDate == "" && endDate == "") {
        url = "purchaseOrder/order?flag=" + flag;
    } else if (startDate == "" || endDate == "") {
        if (startDate != "") {
            url = "purchaseOrder/order?flag="+flag+"&startDate="+startDate+"&endDate="+startDate;
        } else {
            url = "purchaseOrder/order?flag="+flag+"&startDate="+endDate+"&endDate="+endDate;
        }
    } else {
        url = "purchaseOrder/order?flag="+flag+"&startDate="+startDate+"&endDate="+endDate;
    }
    sendAjax(url);
}

//根据日期状态设置url
function dateStatus() {
    const flag = $("#flag").val();
    const startDate = $("#startDate").val();
    const endDate = $("#endDate").val();
    let url;
    if (startDate == "" && endDate== "") {
        alert("请选择日期");
    }

    if(startDate != "" && endDate != "") {
        if (startDate > endDate) {
            alert("结束日期应大于开始日期");
            return;
        }
        if (flag == "") {
            url = "purchaseOrder/order?startDate="+startDate+"&endDate="+endDate;
        } else {
            url = "purchaseOrder/order?flag="+flag+"&startDate="+startDate+"&endDate="+endDate;
        }
    } else if(startDate == ""){
        if (flag == "") {
            url = "purchaseOrder/order?startDate="+endDate+"&endDate="+endDate;
        } else {
            url = "purchaseOrder/order?flag="+flag+"&startDate="+endDate+"&endDate="+endDate;
        }
    } else {
        if (flag == "") {
            url = "purchaseOrder/order?startDate="+startDate+"&endDate="+startDate;
        } else {
            url = "purchaseOrder/order?flag="+flag+"&startDate="+startDate+"&endDate="+startDate;
        }
    }
    sendAjax(url);
}

//发送ajax请求
function sendAjax(url) {
    $.ajax({
        type : "GET",
        url : url,
        dataType: "json",
        success :function (res) {
            buildTable(res);
        }
    })
}

//重建表格
function buildTable(data) {
    $("#purchaseItem tbody").empty();
    for (let i = 0; i < data.length; i++) {
        const res = data[i];
        const tr = $("<tr></tr>");
        const expand = $("<td></td>")
            .append($("<a href='' onclick='expandOrder(this);return false;'></a>")
                .append($("<span class='fa fa-angle-right' style='color: black'></span>")))
        const orderID = $("<td></td>").append(res["orderID"]);
        const companyName = $("<td></td>").append(res["companyName"]);
        const username = $("<td></td>").append(res["username"]);
        const orderDate = $("<td></td>").append(res["orderDate"]);
        const remark = $("<td></td>").append(res["remark"]);
        let operation;
        if(res["flag"] == "已完成") {
            operation = $("<td></td>")
                .append($("<a onclick='returnOrder(this)' class='btn btn-warning'></a>")
                    .append($("<i class='glyphicon glyphicon-file'></i>")).append("退单"))
                .append($("<a onclick='deleteOrder(this)' class='btn btn-danger'></a>")
                    .append($("<i class='glyphicon glyphicon-trash'></i>")).append("删除"))
                .append($("<a onclick='exportExcel(this);' class='btn btn-success'></a>")
                    .append($("<i class='glyphicon glyphicon-save'></i>")).append("下载"));
            console.log(operation);
        } else {
            operation = $("<td></td>")
                .append($("<a onclick='deleteOrder(this)' class='btn btn-danger'></a>")
                    .append($("<i class='glyphicon glyphicon-trash'></i>")).append("删除"))
                .append($("<a onclick='exportExcel(this);' class='btn btn-success'></a>")
                    .append($("<i class='glyphicon glyphicon-save'></i>")).append("下载"));
        }
        tr.append(expand).append(orderID).append(companyName).append(username).append(orderDate).append(remark)
            .append(operation);
        $("#purchaseItem tbody").append(tr);
    }
    $("#count").html(data.length);
    $("#page").remove();
}

//删除订单
function deleteOrder(dom) {
    const self = $(dom);
    const td = self.parent("td");
    const tr = td.parent("tr");
    const orderID = {
        "orderID" : tr.find("td:eq(1)").text()
    };
    $.ajax({
        type : "DELETE",
        url : "/purchase/purchaseOrder/order",
        data : JSON.stringify(orderID),
        contentType : "application/json",
        success : function () {
            alert("删除成功");
            window.location.href = "/purchase/purchaseOrder";
        },
        error : function () {
            alert("删除失败");
        }
    });
}

//退单
function returnOrder(dom) {
    const self = $(dom);
    const td = self.parent("td");
    const tr = td.parent("tr");
    const orderID = {
        "orderID" : tr.find("td:eq(1)").text()
    };
    $.ajax({
        type : "PUT",
        url : "/purchase/purchaseOrder/order",
        data : JSON.stringify(orderID),
        contentType : "application/json",
        success : function () {
            alert("退单成功");
            window.location.href = "/purchase/purchaseOrder";
        },
        error : function () {
            alert("退单失败");
        }
    });
}

//导出Excel
function exportExcel(dom) {
    const self = $(dom);
    const td = self.parent("td");
    const tr = td.parent("tr");
    const orderID = tr.find("td:eq(1)").text();
    window.location.href = "/purchase/purchaseOrder/export?orderID="+orderID;
}