let productID;

//新增产品
function addNewProduct() {
    $("input[name='product']").each(function () {
        $(this).attr("value", "");
    });
    $("input[name='productImg']").prop("value", "");
    $("#saveButton").attr("onclick", "addProduct()");
    $("#productID").attr("readOnly", false);
    $("#newProduct").modal("show");
}

//提交插入数据
function addProduct() {
    if(judgeVal()) {
        const data = {
            "productID" : productID,
            "productName" : $("#productName").val(),
            "unitPrice" : $("#unitPrice").val(),
            "quantityPerUnit" : $("#quantityPerUnit").val(),
            "supplierID" : $("#supplierID").val(),
            "categoryID" : $("#categoryID").val()
        };

        $.ajax({
            type : "POST",
            url : "/product/toProductMessage",
            data : JSON.stringify(data),
            contentType : "application/json",
            success : function () {

            },
            error : function () {
                alert("添加失败");
                return;
            }
        });
        productImg();
    }
}

//判断是否为空，flag判断是update还是insert
function judgeVal() {
    const productName = $("#productName").val();
    const unitPrice = $("#unitPrice").val();
    const quantityPerUnit = $("#quantityPerUnit").val();
    const file = $("#productImg")[0].files[0];
    if (productName == "" || productName == null) {
        alert("产品名称不能为空");
        return false;
    }
    if (quantityPerUnit == "" || quantityPerUnit == null) {
        alert("规格不能为空");
        return false;
    }
    if (unitPrice == "" || unitPrice == null) {
        alert("单价不能为空");
        return false;
    }
    if (file == null) {
        alert("请选择产品图片");
        return false;
    }
    return true;
}

//导出Excel
function exportExcel() {
    const companyName = $("#search").val();
    window.location.href = "/customer/toCustomer/export?companyName="+companyName;
}

//查询客户
function searchProduct() {
    let productName = $("#search").val();
    if(productName == null) {
        productName = "";
    }
    $.ajax({
        type : "GET",
        url : "/product/toProductMessage/name/" + productName,
        data : {"productName" : productName},
        dataType: "json",
        success : function (data) {
            $("#productItem tbody").empty();
            for (let i = 0; i < data.length; i++) {
                const res = data[i];
                const tr = $("<tr></tr>");
                const expand = $("<td></td>")
                    .append($("<a href='' onclick='expandProduct(this);return false;'></a>")
                        .append($("<span class='fa fa-angle-right' style='color: black'></span>")))
                const idTD = $("<td></td>").append(res["productID"]);
                const productName = $("<td></td>").append(res["productName"]);
                const unitPrice = $("<td></td>").append(res["unitPrice"]);
                const companyName = $("<td></td>").append(res["companyName"]);
                const operation = $("<td></td>")
                    .append($("<a onclick='edit(this)' class='btn btn-primary'></a>")
                        .append($("<i class='glyphicon glyphicon-pencil'></i>")).append("编辑"))
                    .append($("<a onclick='deleteProduct(this)' class='btn btn-danger'></a>")
                        .append($("<i class='glyphicon glyphicon-trash'></i>")).append("删除"));
                tr.append(expand).append(idTD).append(productName).append(unitPrice).append(companyName)
                    .append(operation);
                $("#productItem tbody").append(tr);
            }
            $("#count").html(data.length);
            $("#page").remove();
        },
        error : function () {

        }
    });
}

//编辑产品信息
function edit(dom) {
    const self = $(dom);
    const td = self.parent("td");
    const tr = td.parent("tr");
    productID = tr.find("td:eq(1)").text();
    $.ajax({
        type : "GET",
        url : "/product/toProductMessage/" + productID,
        data : JSON.stringify(productID),
        dataType : "json",
        success : function (res) {
            const data = res[0];
            $("input[name='product']").each(function () {
                const id = $(this).attr("id");
                $(this).attr("value", data[id]);
            });
            const category = $("#categoryID");
            category.prop("text", data["categoryName"]);
            category.prop("value", data["categoryID"]);
            const supplier = $("#supplierID");
            supplier.prop("text", data["companyName"]);
            supplier.prop("value", data["supplierID"]);
            $("#saveButton").attr("onclick", "saveChange()");
            $("#newProduct").modal("show");
        },
        error : function () {

        }
    });
}

//提交更改
function saveChange() {
    if(judgeVal()) {
        const data = {
            "productID" : productID,
            "productName" : $("#productName").val(),
            "unitPrice" : $("#unitPrice").val(),
            "quantityPerUnit" : $("#quantityPerUnit").val(),
            "supplierID" : $("#supplierID").val(),
            "categoryID" : $("#categoryID").val()
        };
        $.ajax({
            type : "PUT",
            url : "/product/toProductMessage",
            data : JSON.stringify(data),
            contentType : "application/json",
            success : function () {

            },
            error : function () {
                alert("修改失败");
                return;
            }
        })
        productImg();
    }
}

//删除产品信息
function deleteProduct(dom) {
    const self = $(dom);
    const td = self.parent("td");
    const tr = td.parent("tr");
    productID = {
        "productID" : tr.find("td:eq(1)").text()
    };
    console.log(JSON.stringify(productID));
    $.ajax({
        type : "DELETE",
        url : "/product/toProductMessage",
        data : JSON.stringify(productID),
        contentType : "application/json",
        success : function () {
            alert("删除成功");
            window.location.href = "/product/toProductMessage";
        }
    })
}


//展开当前行
function expandProduct(dom) {
    const self = $(dom);
    const td = self.parent("td");
    const tr = td.parent("tr");
    const productID = tr.find("td:eq(1)").html();
    const span = self.children("span");
    if (span.hasClass("fa-angle-right")) {
        span.removeClass("fa-angle-right");
        span.addClass("fa-angle-down");
        $.ajax({
            type : "GET",
            url : "/product/toProductMessage/" + productID,
            data : JSON.stringify(productID),
            contentType : "application/json",
            dataType : "json",
            success : function(data) {
                const res = data[0];
                const newTr = $("<tr></tr>");
                const newTd = $("<td></td>").attr("colspan", "6").attr("style", "text-align:left");
                const categoryName = $("<div></div>").append($("<label></label>").append("产品类别").addClass("expand-label")).append(
                    $("<div></div>").append($("<span></span>").append(res["categoryName"])).addClass("expand-div")
                );
                const quantityPerUnit = $("<div></div>").append($("<label></label>").append("单位重量").addClass("expand-label")).append(
                    $("<div></div>").append($("<span></span>").append(res["quantityPerUnit"])).addClass("expand-div")
                );
                const unitsOnOrder = $("<div></div>").append($("<label></label>").append("销售量").addClass("expand-label")).append(
                    $("<div></div>").append($("<span></span>").append(res["unitsOnOrder"])).addClass("expand-div")
                );
                const unitsInStock = $("<div></div>").append($("<label></label>").append("库存量").addClass("expand-label")).append(
                    $("<div></div>").append($("<span></span>").append(res["unitsInStock"])).addClass("expand-div")
                );
                newTd.append(categoryName).append(quantityPerUnit).append(unitsOnOrder).append(unitsInStock);
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

function productImg() {
    const file = $("#productImg")[0].files[0];

    if (file != null) {
        const formData = new FormData();
        formData.append("name", file.name);
        formData.append("file", file);
        formData.append("productName", $("#productName").val());
        $.ajax({
            type : "POST",
            url : "/product/toProductMessage/addImg",
            data : formData,
            processData : false,
            contentType : false,
            success : function () {
                alert("修改成功!");
                window.location.href = "/product/toProductMessage";
            },
            error : function () {
                alert("修改失败");
            }
        });
    }
}

function getImg(dom) {
    const self = $(dom);
    const td = self.parent("td");
    const tr = td.parent("tr");
    const productID = tr.find("td:eq(1)").text();
    const imgUrl = "/static/img/";
    $.ajax({
        type : "GET",
        url : "/product/toProductMessage/img?productID="+productID,
        dataType : "text",
        success : function (res) {
            console.log(res);
            $("#img").attr("src", imgUrl+res);
            $("#newImage").modal("show");
        },
        error : function () {
            alert("找不到图片");
        }
    });


}