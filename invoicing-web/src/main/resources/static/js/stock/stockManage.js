$(function () {
   $("#orderStatus").change(function () {
       const flag = $("#orderStatus").val();
       if (flag == "待入库") {
           $.ajax({
               type : "GET",
               url : "/stock/stockManage/inbound",
               data : JSON.stringify(flag),
               contentType : "application/json",
               dataType : "json",
               success : function (data) {
                   $("#stockItem tbody").empty();
                   for (let i = 0; i < data.length; i++) {
                       const res = data[i];
                       const tr = $("<tr></tr>");
                       const expand = $("<td></td>").append($("<a href='' onclick='expandOrder(this);return false;'></a>")
                           .append($("<span class='fa fa-angle-right' style='color: black;'></span>")));
                       const orderID = $("<td></td>").append(res["orderID"]);
                       const orderDate = $("<td></td>").append(res["orderDate"]);
                       const inbound = $("<td></td>")
                           .append($("<a onclick='inbound(this)' class='btn btn-info'></a>")
                               .append($("<i class='glyphicon glyphicon-copy'></i>")).append("入库"))
                       tr.append(expand).append(orderID).append(orderDate).append(inbound);
                       $("#stockItem tbody").append(tr);
                   }
               },
               error : function () {

               }
           });
       } else {
           $.ajax({
               type : "GET",
               url : "/stock/stockManage/outbound",
               data : JSON.stringify(flag),
               contentType : "application/json",
               dataType : "json",
               success : function (data) {
                   $("#stockItem tbody").empty();
                   for (let i = 0; i < data.length; i++) {
                       const res = data[i];
                       const tr = $("<tr></tr>");
                       const expand = $("<td></td>").append($("<a href='' onclick='expandOrder(this);return false;'></a>")
                           .append($("<span class='fa fa-angle-right' style='color: black;'></span>")));
                       const orderID = $("<td></td>").append(res["orderID"]);
                       const orderDate = $("<td></td>").append(res["orderDate"]);
                       const inbound = $("<td></td>")
                           .append($("<a onclick='outbound(this)' class='btn btn-success'></a>")
                               .append($("<i class='glyphicon glyphicon-copy'></i>")).append("出库"))
                       tr.append(expand).append(orderID).append(orderDate).append(inbound);
                       $("#stockItem tbody").append(tr);
                   }
               },
               error : function () {

               }
           });
       }
   });

});

function inbound(data) {
    const self = $(data);
    const td = self.parent("td");
    const tr = td.parent("tr");
    const orderID = {
        "orderID" : tr.find("td:eq(1)").text()
    };

    console.log(orderID);
    const msg = "确定要入库吗?";
    if (confirm(msg) == true) {
        $.ajax({
            type : "PUT",
            url : "/stock/stockManage/inbound",
            data : JSON.stringify(orderID),
            contentType : "application/json",
            success : function () {
                tr.remove();
            },
            error : function () {

            }
        });
    }
}

function outbound(data) {
    const self = $(data);
    const td = self.parent("td");
    const tr = td.parent("tr");
    const orderID = {
        "orderID" : tr.find("td:eq(1)").text()
    };

    console.log(orderID);
    const msg = "确定要出库吗?";
    if (confirm(msg) == true) {
        $.ajax({
            type : "PUT",
            url : "/stock/stockManage/outbound",
            data : JSON.stringify(orderID),
            contentType : "application/json",
            dataType : "text",
            success : function (data) {
                // tr.remove();
            },
            error : function () {

            }
        });
    }
}

function expandOrder(data) {
    const self = $(data);
    const td = self.parent("td");
    const tr = td.parent("tr");
    const orderID = tr.find("td:eq(1)").text();
    const flag = $("#orderStatus").val();
    const span = self.children("span");
    if (flag == "待入库") {
        if (span.hasClass("fa-angle-right")) {
            span.removeClass("fa-angle-right");
            span.addClass("fa-angle-down");
            $.ajax({
                type : "GET",
                url : "/stock/stockManage/inbound/" + orderID,
                data : JSON.stringify(orderID),
                contentType : "application/json",
                dataType : "json",
                success : function (data) {
                    let res = data[0];
                    const newTr = $("<tr></tr>");
                    const newTd = $("<td colspan='4'></td>");
                    for (let i = 0; i < data.length; i++) {
                        res = data[i];
                        const div = $("<div class='row form-inline' align='left'></div>")
                            .append($("<div class='col-sm-1'></div>").append($("<label style='width: 70px;'>产品名称：</label>")).append(res["productName"]))
                            .append($("<div class='col-sm-1'></div>").append($("<label style='width: 70px;'>待入库量：</label>")).append(res["quantity"]))
                            .append($("<div class='col-sm-1'></div>").append($("<label style='width: 60px;'>库存量：</label>")).append(res["unitsInStock"]));
                        newTd.append(div);
                        newTr.append(newTd);
                        tr.after(newTr);
                    }
                },
                error : function () {

                }
            });
        }else {
            span.removeClass("fa-angle-down");
            span.addClass("fa-angle-right");
            tr.next().remove();
        }
    } else {
        if (span.hasClass("fa-angle-right")) {
            span.removeClass("fa-angle-right");
            span.addClass("fa-angle-down");
            $.ajax({
                type : "GET",
                url : "/stock/stockManage/outbound/" + orderID,
                data : JSON.stringify(orderID),
                contentType : "application/json",
                dataType : "json",
                success : function (data) {
                    let res = data[0];
                    const newTr = $("<tr></tr>");
                    const newTd = $("<td colspan='4'></td>");
                    for (let i = 0; i < data.length; i++) {
                        res = data[i];
                        const div = $("<div class='row form-inline' align='left'></div>")
                            .append($("<div class='col-sm-1'></div>").append($("<label style='width: 70px;'>产品名称：</label>")).append(res["productName"]))
                            .append($("<div class='col-sm-1'></div>").append($("<label style='width: 70px;'>待出库量：</label>")).append(res["quantity"]))
                            .append($("<div class='col-sm-1'></div>").append($("<label style='width: 60px;'>库存量：</label>")).append(res["unitsInStock"]));
                        newTd.append(div);
                        newTr.append(newTd);
                        tr.after(newTr);
                    }
                },
                error : function () {

                }
            });
        }else {
            span.removeClass("fa-angle-down");
            span.addClass("fa-angle-right");
            tr.next().remove();
        }
    }
}