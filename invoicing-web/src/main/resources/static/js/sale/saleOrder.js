$(function () {
    $(".expandOrder").click(function () {
        const self = $(this);
        const td = self.parent("td");
        const tr = td.parent("tr");
        const orderID = tr.find("td:eq(1)").text();
        const span = self.children("span");
        if (span.hasClass("fa-angle-right")) {
            span.removeClass("fa-angle-right");
            span.addClass("fa-angle-down");
            $.ajax({
                type : "GET",
                url : "/sale/saleOrder/detail/" + orderID,
                data : JSON.stringify(orderID),
                contentType : "application/json",
                dataType : "json",
                success : function (data) {
                    let sum = 0;
                    let res = data[0];
                    const newTr = $("<tr></tr>");
                    const newTd = $("<td colspan='7'></td>");
                    const shipCity = res["shipCity"];
                    const shipAddress = res["shipAddress"];
                    const shipPostalCode = res["shipPostalCode"];
                    const shipDiv = $("<div class='row form-inline'></div>")
                        .append($("<div class='col-sm-1'></div>").append($("<label style='width: 70px;'>收货城市：</label>")).append(shipCity))
                        .append($("<div class='col-sm-2'></div>").append($("<label>详细地址：</label>")).append(shipAddress))
                        .append($("<div class='col-sm-1'></div>").append($("<label>邮政编码：</label>")).append(shipPostalCode));
                        newTd.append(shipDiv);
                        newTr.append(newTd);
                        tr.after(newTr);
                    for (let i = 0; i < data.length; i++) {
                        res = data[i];
                        const div = $("<div class='row form-inline' align='left'></div>")
                            .append($("<div class='col-sm-1'></div>").append($("<label style='width: 70px;'>产品名称：</label>")).append(res["productName"]))
                            .append($("<div class='col-sm-1'></div>").append($("<label style='width: 50px;'>售价：</label>")).append(res["unitPrice"] + "元"))
                            .append($("<div class='col-sm-1'></div>").append($("<label style='width: 50px;'>数量：</label>")).append(res["quantity"]))
                            .append($("<div class='col-sm-1'></div>").append($("<label style='width: 70px;'>合计金额：</label>")).append(res["totalPrice"] + "元"));
                        newTd.append(div);
                        newTr.append(newTd);
                        tr.after(newTr);
                        sum = sum + res["totalPrice"];
                    }
                    const sumDiv = $("<div class='row form-inline' align='left'></div>")
                        .append($("<label style='padding-left: 15px;'>总金额：</label>")).append(sum + "元");
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
    });
});