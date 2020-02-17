$(function(){
    //左侧导航栏点击展开下拉菜单
    $(".menu-main").click(function(){
        var self = $(this);
        var li = self.parent("li");
        var ul = li.children("ul");
        var span = self.find("span").eq(2);
        if (ul.hasClass("in")) {
            ul.removeClass("in");
            span.removeClass("fa-angle-down");
            span.addClass("fa-angle-left");
        } else {
            ul.addClass("in");
            span.removeClass("fa-angle-left");
            span.addClass("fa-angle-down");
        }
    });
});
