$(function () {
    $(".expandProduct").click(function () {
        const self = $(this);
        const td = self.parent("td");
        const tr = td.parent("tr");
        const productID = tr.find("td:eq(1)").html();
        const span = self.children("span");
        console.log(tr.next());
        if (span.hasClass("fa-angle-right")) {
            span.removeClass("fa-angle-right");
            span.addClass("fa-angle-down");
            $.ajax({
                type : "get",
                url : "/product/" + productID,
                data : JSON.stringify(productID),
                contentType : "application/json",
                dataType : "json",
                success : function(res) {
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


    });
});