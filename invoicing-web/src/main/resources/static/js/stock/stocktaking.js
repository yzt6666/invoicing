let stockID;
$(function () {
    $("#move").click(function () {


    });

    $("#save").click(function () {
        const stockArea = $("#stockArea").val();
        console.log(stockID);
        const stock = {
            "stockID" : stockID,
            "stockArea" : stockArea
        }
        $.ajax({
            type : "PUT",
            url : "/stock/toStocktaking",
            data : JSON.stringify(stock),
            contentType : "application/json",
            dataType : "text",
            success : function (res) {
                alert("success");
                window.location.href = "/stock/toStocktaking"
            }
        });
    });

    $("#search").click(function () {
        const productName = $("#productName").val();
        $.ajax({
            type: "GET",
            url : "/stock/toStocktaking/search/" + productName,
            data : JSON.stringify(productName),
            contentType: "application/json",
            dataType: "json",
            success : function (data) {
                $("#stockItem tbody").empty();
                for (let i = 0; i < data.length; i++) {
                    const res = data[i];
                    const tr = $("<tr></tr>");
                    const stockID = $("<td></td>").append(res["stockID"]);
                    const productName = $("<td></td>").append(res["productName"]);
                    const quantityPerUnit = $("<td></td>").append(res["quantityPerUnit"]);
                    const unitsInStock = $("<td></td>").append(res["unitsInStock"]);
                    const stockArea = $("<td></td>").append(res["stockArea"]);
                    const move = $("<td></td>")
                        .append($("<a onclick='move(this)' class='btn btn-info'></a>")
                            .append($("<i class='glyphicon glyphicon-pencil'></i>")).append("移库"));
                    tr.append(stockID).append(productName).append(quantityPerUnit).append(unitsInStock)
                        .append(stockArea).append(move);
                    $("#stockItem tbody").append(tr);
                }
                $("#count").html(data.length);
                $("#page").remove();
            },
            error : function () {

            }
        });
    });

});

function move(res) {
    $("#moveStock").modal("show");
    const self = $(res);
    const td = self.parent("td");
    const tr = td.parent("tr");
    stockID = tr.find("td:eq(0)").text();
}