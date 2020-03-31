let supplierID;

//新增供应商
function addNewSupplier() {
    $("input[name='supplier']").each(function () {
        $(this).attr("value", "");
    });
    $("#saveButton").attr("onclick", "addSupplier()");
    $("#newSupplier").modal("show");
}

//提交数据
function addSupplier() {
    if(judgeVal()) {
        const data = {
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
            url : "/supplier/toSupplier",
            data : JSON.stringify(data),
            contentType : "application/json",
            success : function () {
                alert("添加成功");
                window.location.href = "/supplier/toSupplier";
            },
            error : function () {
                alert("添加失败");
            }
        });
    }
}

//判断是否为空
function judgeVal() {
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
    return true;
}

//导出Excel
function exportExcel() {
    if ($("#allSupplier").prop("checked")) {
        const companyName = "";
        window.location.href = "/supplier/toSupplier/export?companyName="+companyName;
    } else {
        let companyName = new Array();
        $("input[name='supplier']").each(function () {
            if ($(this).prop("checked")) {
                const self = $(this);
                const td = self.parent("td");
                const tr = td.parent("tr");
                const supplier = tr.find("td:eq(2)").text();
                companyName.push(supplier);
            }
        });
        console.log(companyName);
        if (companyName.length == 0) {
            alert("请选择需要下载的内容");
            return;
        }
        window.location.href = "/supplier/toSupplier/export?companyName="+companyName;
    }

}

//查询供应商
function searchSupplier() {
    let companyName = $("#search").val();
    if(companyName == null) {
        companyName = "";
    }
    $.ajax({
        type : "GET",
        url : "/supplier/toSupplier/" + companyName,
        data : {"companyName" : companyName},
        dataType: "json",
        success : function (data) {
            buildTable(data);
        },
        error : function () {

        }
    });
}

//编辑供应商
function edit(dom) {
    const self = $(dom);
    const td = self.parent("td");
    const tr = td.parent("tr");
    supplierID = tr.find("td:eq(1)").text();
    $.ajax({
        type : "GET",
        url : "/supplier/toSupplier/id/" + supplierID,
        data : {"supplierID" : supplierID},
        dataType : "json",
        success : function (res) {
            $("input[name='supplier']").each(function () {
                const id = $(this).attr("id");
                $(this).attr("value", res[id]);
            });
            $("#saveButton").attr("onclick", "saveChange()");
            $("#newSupplier").modal("show");
        },
        error : function () {

        }
    });
}

//提交更改
function saveChange() {
    if(judgeVal()) {
        const data = {
            "supplierID" : supplierID,
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
            url : "/supplier/toSupplier",
            data : JSON.stringify(data),
            contentType : "application/json",
            success : function () {
                alert("修改成功!");
                window.location.href = "/supplier/toSupplier";
            },
            error : function () {
                alert("修改失败");
            }
        })
    }
}

function deleteSupplier(dom) {
    const self = $(dom);
    const td = self.parent("td");
    const tr = td.parent("tr");
    supplierID = tr.find("td:eq(0)").text();
    $.ajax({
        type : "DELETE",
        url : "/supplier/toSupplier",
        data : JSON.stringify(supplierID),
        contentType : "application/json",
        success : function () {
            alert("删除成功");
            window.location.href = "/supplier/toSupplier";
        }
    })
}

//复选框选中
function selectAll(dom) {
    if ($(dom).prop("checked")) {
        $("input[name='supplier']").each(function () {
            $(this).prop("checked", true);
        });
    } else {
        $("input[name='supplier']").each(function () {
            $(this).prop("checked", false);
        });
    }
}

//重建表
function buildTable(data) {
    $("#supplierItem tbody").empty();
    for (let i = 0; i < data.length; i++) {
        const res = data[i];
        const tr = $("<tr></tr>");
        const checkbox = $("<td></td>").append($("<input name='supplier' type='checkbox'/>"));
        const supplierID = $("<td></td>").append(res["supplierID"]);
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
        tr.append(checkbox).append(supplierID).append(companyName).append(contactName).append(contactTitle)
            .append(address).append(city).append(phone).append(fax).append(postalCode)
            .append(operation);
        $("#supplierItem tbody").append(tr);
    }
    $("#count").html(data.length);
    $("#page").remove();
}