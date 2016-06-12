/**
 * Created by Administrator on 2016/6/8.
 */

app.controller('myCtrl', ['$scope', '$rootScope', 'BusinessService', function ($scope, $rootScope, BusinessService) {
    $scope.user={};
    $scope.menu="";

    $scope.initData=function() {
        var token=getCookie("token");
        if(null==token) {
            alert("请先登录");
            window.location.href=loginUrl;
        }
        var userId=getParamFromToken(token,"userId");
        //获取用户信息
        $scope.getUserInfo(userId);
        //获取菜单信息
        $scope.getMenuInfo(userId);
    };

    $scope.getUserInfo = function(userId) {
        var param={};
        param.id=userId;
        param.showName=1;
        BusinessService.post(myRootUrl+"/userMgmt/queryUserById.do" ,param).success(function (data) {
            if(null==data) {
                return;
            }
            $scope.user.userName=data.data.userName;
            $scope.user.userType=data.data.userType;
            $scope.user.sex=data.data.sex=="0"?"男":"女";
        });
    };

    $scope.getMenuInfo = function(userId) {
        var param={};
        BusinessService.post(myRootUrl+"/permMgmt/listMenu.do" ,param).success(function (data) {
            if(null==data) {
                return;
            }
            //console.log(data);
            var htmlStr="";
            for(var i=0;i<data.data.menu.length;i++) {
                var item = data.data.menu[i];
                htmlStr += "<dl>";
                htmlStr += "<dt><i class='Hui-iconfont'>&#xe6c6;</i> "+item.itemName+"<i class='Hui-iconfont menu_dropdown-arrow'>&#xe6d5;</i></dt>";
                htmlStr += "<dd>";
                htmlStr += "<ul>";
                for(var j=0;j<item.child.length;j++) {
                    var childItem = item.child[j];
                    if(childItem.level=="2") {
                        htmlStr += "<li style='padding-left:26px;color:#1e90ff;font-size:12px'>--"+childItem.itemName+"--</li>";
                    }else if(childItem.level=="3") {
                        htmlStr += "<li><a _href='"+childItem.url+"' data-title='"+childItem.itemName+"' href='javascript:void(0)'>»&nbsp;"+childItem.itemName+"</a></li>";
                    }
                }
                htmlStr += "</ul>";
                htmlStr += "</dd>";
                htmlStr += "</dl>";
            }
            $(".menu_dropdown").html(htmlStr);
            $.Huifold(".menu_dropdown dl dt",".menu_dropdown dl dd","fast",1,"click");
        });
    };

    $scope.logOut = function() {
        delCookie("token");
        window.location.href=loginUrl;
    };

}]);


