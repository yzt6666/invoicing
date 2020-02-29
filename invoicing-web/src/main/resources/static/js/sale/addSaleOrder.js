let saleArray = new Array();
$(function () {
    //产品种类改变时,修改产品名称里面的值
    $("#categoryID").change(function () {
        const categoryID = $("#categoryID").val();
        const select = $("#productID");
        $.ajax({
            type : "GET",
            url : "/purchase/purchaseOrder/" + categoryID,
            data : JSON.stringify(categoryID),
            contentType : "application/json",
            dataType : "json",
            success : function(res) {
                $("#productID").empty();
                $("#productID").append("<option value='' selected hidden></option>")
                let products;
                let productName;
                let productID;
                for (let i = 0; i < res.length; i++) {
                    let option = $("<option></option>");
                    products = res[i];
                    productName = products["productName"];
                    productID = products["productID"];
                    option.append(productName).attr("value", productID);
                    select.append(option);
                }
            }

        });
    });
    //选择产品后将信息添加到表格中
    $("#productID").change(function () {
        const productID = $(this).val();
        if (judgeID(productID)) {
            return;
        } else {
            if (saleArray.length == 0) {
                $("#purchaseItem tbody").empty();
            }
            const tr = $("<tr></tr>");
            $.ajax({
                type : "GET",
                url : "/product/" + productID,
                data : JSON.stringify(productID),
                contentType : "application/json",
                dataType : "json",
                success : function (data) {
                    const res = data[0];
                    const idTd = $("<td></td>").append(res["productID"]);
                    const nameTd = $("<td></td>").append(res["productName"]);
                    const quantityPerUnit = $("<td></td>").append(res["quantityPerUnit"]);
                    const unitPrice = $("<td></td>").append(res["unitPrice"] + "元");
                    const quantity = $("<td width='200px'></td>")
                        .append($("<input type='text' id='quantity' name='quantity' class='form-control' max='8'/>")
                            .attr("onkeyup", "value=value.replace(/^(0+)|[^\\d]+/g,'')")
                        );
                    const operate = $("<td></td>").append($("<a onclick='deleteID(this)' class='btn btn-danger'></a>")
                        .append("<i class='glyphicon glyphicon-trash'>删除</i>")
                    );
                    tr.append(idTd).append(nameTd).append(quantityPerUnit).append(unitPrice).append(quantity).append(operate);
                    $("#purchaseItem tbody").append(tr);
                }
            });
            saleArray.push(productID);
        }
    });

    $("#saveInfo").click(function () {
        let purchaseArray = new Array();
        if (saleArray.length != 0) {
            const customerID = $("#customerID").val();
            const shipProvince = $("#shipProvince").val();
            const shipCity = $("#shipCity").val();
            const shipAddress = $("#shipAddress").val();
            const shipPostalCode = $("#shipPostalCode").val();
            const shipName = $("#shipName").val();
            const trList = $("#purchaseItem tbody").children("tr");
            for (let i = 0; i < trList.length; i++) {
                const td = trList.eq(i).find("td");
                const purchaseOrder = {
                    "customerID" : customerID,
                    "shipProvince" : shipProvince,
                    "shipCity" : shipCity,
                    "shipAddress" : shipAddress,
                    "shipPostalCode" : shipPostalCode,
                    "shipName" : shipName,
                    "productID" : td.eq(0).text(),
                    "productName" : td.eq(1).text(),
                    "unitPrice" : td.eq(3).text(),
                    "quantity" : td.eq(4).find("input").val()
                };
                purchaseArray.push(purchaseOrder);
            }
            $.ajax({
                type : "POST",
                url : "/sale/saleOrder/create",
                data : JSON.stringify(purchaseArray),
                contentType : "application/json",
                dataType : "text",
                success : function (res) {
                    // window.location.href = "/purchase/purchaseOrder";
                },
                error : function () {
                    console.log("error");
                }
            });
        }
    });
});

function judgeID(productID) {
    for (let i = 0; i < saleArray.length; i++) {
        if (saleArray[i] == productID) {
            return true;
        }
    }
    return false;
}
//删除相应的行和产品编号
function deleteID(data) {
    const a = $(data);
    console.log();
    const tr = a.parent("td").parent("tr");
    const productID = tr.find("td").eq(0).text();
    tr.remove();

    for (let i = 0; i < saleArray.length; i++) {
        if (saleArray[i] == productID) {
            saleArray.splice(i, 1);
            if (saleArray.length == 0) {
                //数组为空时，添加暂无数据行
                $("#purchaseItem tbody").append($("<tr></tr>").append("<td colspan='6'>暂无数据</td>"));
            }
            return;
        }
    }
}

function deleteAll() {
    saleArray = new Array();
    $("#purchaseItem tbody").empty();
    $("#purchaseItem tbody").append($("<tr></tr>").append("<td colspan='6'>暂无数据</td>"));
}
