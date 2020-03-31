$(function () {

});
let employeeID;
//显示模态框，并显示当前选择对象的权限
function changePermission(dom) {
    resetItem();
    const self = $(dom);
    const td = self.parent("td");
    const tr = td.parent("tr");
    employeeID = tr.find("td:eq(0)").text();
    $.ajax({
        type : "GET",
        url : "/system/toUserManage/" + employeeID,
        data : "1",
        contentType : "text/html",
        dataType : "json",
        success : function (res) {
            for (let i = 0; i < res.length; i++) {
                selectItem(res[i]);
            }
            $("#permission").modal("show");
        },
        error : function () {
            console.log("error");
        }
    })
}

//提交更改
function changeSubmit() {
    let array = new Array();
    array.push(employeeID);
    $("input[name='perms']").each(function () {
        if (this.checked) {
            array.push(this.value);
        }
    });
    $.ajax({
        type : "PUT",
        url : "/system/toUserManage",
        data : JSON.stringify(array),
        contentType : "application/json",
        success : function () {

        },
        error : function () {

        }
    })
}

//遍历使具有权限的checkbox选中
function selectItem(perms) {
    $("input[name='perms']").each(function () {
       if($(this).val() == perms) {
           $(this).prop("checked", true);
       }
    });
}

//重置复选框，避免重复
function resetItem() {
    $("input[name='perms']").each(function () {
       $(this).prop("checked", false);
    });
}

//删除用户
function deleteUser(dom) {
    const self = $(dom);
    const td = self.parent("td");
    const tr = td.parent("tr");
    employeeID = tr.find("td:eq(0)").text();
    $.ajax({
        type : "DELETE",
        url : "/system/toUserManage",
        data : JSON.stringify(employeeID),
        contentType : "application/json",
        success : function () {
            alert("删除成功");
            window.location.href = "/system/toUserManage";
        },
        error : function () {

        }
    })
}

//数据备份
function backup() {
    window.location.href = "/system/backup";
}

//打开文件选择对话框
function openFile() {
    $("#recoverFile").click();
}

//数据恢复
function recover() {
    const file = $("#recoverFile")[0].files[0];
    const formData = new FormData();
    formData.append("name", file.name);
    formData.append("file", file);
    const reader = new FileReader();
    reader.readAsDataURL(file);
    console.log(reader);
    $.ajax({
        type : "POST",
        url : "/system/recover",
        data : formData,
        processData : false,
        contentType : false,
        success : function () {
            alert("数据恢复成功");
        },
        error : function () {
            alert("数据恢复失败");
        }
    });

}