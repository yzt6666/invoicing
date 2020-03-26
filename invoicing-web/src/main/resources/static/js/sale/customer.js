let customerID;

//新增客户
function addNewCustomer() {
    $("input[name='customer']").each(function () {
        $(this).attr("value", "");
    });
    $("#saveButton").attr("onclick", "addCustomer()");
    $("#customerID").attr("readOnly", false);
    $("#newCustomer").modal("show");
}

//提交数据
function addCustomer() {
    const flag = judgeVal(true);
    if(!flag) {
        return;
    }
    const data = {
        "customerID" : $("#customerID").val(),
        "companyName" : $("#companyName").val(),
        "contactName" : $("#contactName").val(),
        "contactTitle" : $("#contactTitle").val(),
        "address" : $("#address").val(),
        "city" : $("#city").val(),
        "phone" : $("#phone").val(),
        "fax" : $("#fax").val(),
        "postalCode" : $("#postalCode").val()
    };
    $.ajax({
        type : "POST",
        url : "/customer/toCustomer",
        data : JSON.stringify(data),
        contentType : "application/json",
        success : function () {
            alert("添加成功");
            window.location.href = "/customer/toCustomer";
        },
        error : function () {
            alert("添加失败");
        }
    });
}

//判断是否为空，flag判断是update还是insert
function judgeVal(flag) {
    customerID = $("#customerID").val();
    const companyName = $("#companyName").val();
    const linkMan = $("#contactName").val();
    const address = $("#address").val();
    const city = $("#city").val();
    const phone = $("#phone").val();

    if (companyName == "" || companyName == null) {
        alert("公司名称不能为空");
        return false;
    }
    if(linkMan == "" || linkMan == null) {
        alert("联系人不能为空");
        return false;
    }
    if(address == "" || address == null) {
        alert("地址不能为空");
        return false;
    }
    if(city == "" || city == null) {
        alert("城市不能为空");
        return false;
    }
    if(phone == "" || phone == null) {
        alert("电话不能为空");
        return false;
    }
    let result = true;
    if(flag) {
        $.ajax({
            type : "GET",
            url : "/customer/toCustomer/" + customerID,
            data : {"customerID" : customerID},
            dataType : "json",
            async : false,
            success : function (res) {
                if (!$.isEmptyObject(res)) {
                    alert("客户编号已存在");
                    result = false;
                }
            }
        });
    }
    return result;
}

//判断ID是否重复
function judgeID() {

    return true;
}

//导出Excel
function exportExcel() {
    const companyName = $("#search").val();
    window.location.href = "/customer/toCustomer/export?companyName="+companyName;
}

//查询客户
function searchCustomer() {
    let companyName = $("#search").val();
    if(companyName == null) {
        companyName = "";
    }
    $.ajax({
        type : "GET",
        url : "/customer/toCustomer/name/" + companyName,
        data : {"companyName" : companyName},
        dataType: "json",
        success : function (data) {
            $("#customerItem tbody").empty();
            for (let i = 0; i < data.length; i++) {
                const res = data[i];
                const tr = $("<tr></tr>");
                const customerID = $("<td></td>").append(res["customerID"]);
                const companyName = $("<td></td>").append(res["companyName"]);
                const contactName = $("<td></td>").append(res["contactName"]);
                const contactTitle = $("<td></td>").append(res["contactTitle"]);
                const address = $("<td></td>").append(res["address"]);
                const city = $("<td></td>").append(res["city"]);
                const phone = $("<td></td>").append(res["phone"]);
                const fax = $("<td></td>").append(res["fax"]);
                const postalCode = $("<td></td>").append(res["postalCode"]);
                const operation = $("<td></td>")
                    .append($("<a onclick='edit(this)' class='btn btn-primary'></a>")
                        .append($("<i class='glyphicon glyphicon-pencil'></i>")).append("编辑"))
                    .append($("<a onclick='deleteSupplier(this)' class='btn btn-danger'></a>")
                        .append($("<i class='glyphicon glyphicon-trash'></i>")).append("删除"));
                tr.append(customerID).append(companyName).append(contactName).append(contactTitle)
                    .append(address).append(city).append(phone).append(fax).append(postalCode)
                    .append(operation);
                $("#customerItem tbody").append(tr);
            }
            $("#count").html(data.length);
            $("#page").remove();
        },
        error : function () {

        }
    });
}

//编辑供应商
function edit(dom) {
    $("#customerID").attr("readOnly", true);
    const self = $(dom);
    const td = self.parent("td");
    const tr = td.parent("tr");
    customerID = tr.find("td:eq(0)").text();
    $.ajax({
        type : "GET",
        url : "/customer/toCustomer/" + customerID,
        data : {"customerID" : customerID},
        dataType : "json",
        success : function (res) {
            $("input[name='customer']").each(function () {
                const id = $(this).attr("id");
                $(this).attr("value", res[id]);
            });
            $("#saveButton").attr("onclick", "saveChange()");
            $("#newCustomer").modal("show");
        },
        error : function () {

        }
    });
}

//提交更改
function saveChange() {
    if(judgeVal()) {
        const data = {
            "customerID" : customerID,
            "companyName" : $("#companyName").val(),
            "contactName" : $("#contactName").val(),
            "contactTitle" : $("#contactTitle").val(),
            "address" : $("#address").val(),
            "city" : $("#city").val(),
            "phone" : $("#phone").val(),
            "fax" : $("#fax").val(),
            "postalCode" : $("#postalCode").val()
        };

        $.ajax({
            type : "PUT",
            url : "/customer/toCustomer",
            data : JSON.stringify(data),
            contentType : "application/json",
            success : function () {
                alert("修改成功!");
                window.location.href = "/customer/toCustomer";
            },
            error : function () {
                alert("修改失败");
            }
        })
    }
}

function deleteCustomer(dom) {
    const self = $(dom);
    const td = self.parent("td");
    const tr = td.parent("tr");
    customerID = {
        "customerID" : tr.find("td:eq(0)").text()
    };
    console.log(JSON.stringify(customerID));
    $.ajax({
        type : "DELETE",
        url : "/customer/toCustomer",
        data : JSON.stringify(customerID),
        contentType : "application/json",
        success : function () {
            alert("删除成功");
            window.location.href = "/customer/toCustomer";
        }
    })
}

