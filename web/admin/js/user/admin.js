/**
 * Created by Administrator on 2016/4/12.
 */
var app = angular.module("myApp",[]).config(function($httpProvider) {

    $httpProvider.defaults.headers
        .common['Accept'] = 'application/json; charset=utf-8';
    $httpProvider.defaults.headers.post['Content-Type'] = 'application/json; charset=UTF-8';
    $httpProvider.defaults.transformRequest = [function(data) {
        if(null!=localStorage.getItem("token")) {
            data.token=localStorage.getItem("token");
        }
        var jsonStr=JSON.stringify(data);
        return jsonStr;
    }];
    $httpProvider.defaults.transformResponse = [function(data) {
        var json = JSON.parse(data);
        if(json.status.errorCode=="000001") {
            alert(json.status.errorMsg);
            return null;
        }else if(json.status.errorCode=="000002") {
            alert(json.status.errorMsg);
            top.location.href=loginUrl;
        }
        return json;
    }];

});

app.controller('myCtrl', ['$scope', '$rootScope', 'BusinessService', function ($scope, $rootScope, BusinessService) {

    //表格相关属性和方法
    $scope.items={};
    $scope.checkAllVal=false;
    $scope.checkAll=function() {
        for(var i=0;i<$scope.items.length;i++) {
            $scope.items[i].checked=$scope.checkAllVal;
        }
    };
    $scope.checkItem=function(checked) {
        if(!checked) {
            $scope.checkAllVal=false;
        }
    };
    $scope.currentPage=1;
    $scope.pageSize=10;
    $scope.pages=1;
    $scope.gridPrompt=true;
    $scope.gridPromptTxt="数据加载中......";
    $scope.listItems=function() {
        $scope.gridPrompt=true;
        $scope.gridPromptTxt="数据加载中......";
        $scope.param.currentPage=$scope.currentPage;
        $scope.param.pageSize=$scope.pageSize;
        $scope.param.orderBy=$scope.orderBy;
        $scope.param.searchStr=$scope.searchStr;
        BusinessService.post(myRootUrl+$scope.listUrl ,$scope.param).success(function (data) {
            //console.log(data);
            if(null==data) {
                return;
            }
            if(data.data.total>0) {
                $scope.gridPrompt = false;
            }else {
                $scope.gridPrompt = true;
                $scope.gridPromptTxt="没有符合条件的记录";
            }
            $scope.items = data.data.items;
            for (var i = 0; i < $scope.items.length; i++) {
                $scope.items[i].checked = false;
            }
            $scope.pages = data.data.pages;
            if ($scope.pages > 1) {
                laypage({
                    cont: 'pagingDiv',
                    pages: $scope.pages, //可以叫服务端把总页数放在某一个隐藏域，再获取。假设我们获取到的是18
                    curr: function () { //通过url获取当前页，也可以同上（pages）方式获取
                        return $scope.currentPage;
                    }(),
                    jump: function (e, first) { //触发分页后的回调
                        if (!first) { //一定要加此判断，否则初始时会无限刷新
                            $scope.currentPage = e.curr;
                            $scope.listItems();
                        }
                    }
                });
            } else {
                $("#pagingDiv").html("");
            }

        });
    };
    $scope.searchKeyup = function(e){
        var keycode = window.event?e.keyCode:e.which;
        if(keycode==13){
            $scope.listItems();
        }
    };

    //-----表格相关属性和方法 结束


    //自已定义的属性和方法
    //列表页属性
    $scope.param={};
    $scope.param.userType="admin";
    $scope.orderBy="userId,-1";
    $scope.listUrl="/userMgmt/listUserByType.do";

    //表单页属性
    $scope.insertUrl="/userMgmt/addUser.do";
    $scope.updateUrl="/userMgmt/updateUser.do";
    $scope.queryUrl="/userMgmt/queryUserById.do";
    $scope.deleteUrl="/userMgmt/deleteUser.do";
    $scope.mainKey="userId"; //删除方法需要用到,表示根据哪个字段来删除
    $scope.model={};
    $scope.model.id=getQueryString("id");
    $scope.model.idCardType=0;
    $scope.model.sex=0;
    $scope.model.publicAccount=false;
    $scope.model.status=false;
    $scope.model.userType="admin";
    $scope.options={};
    $scope.options.idCardType=[{
        value:0,
        label:"身份证"
    },{
        value:1,
        label:"军官证"
    },{
        value:2,
        label:"港澳台居民证"
    },{
        value:3,
        label:"护照"
    },{
        value:4,
        label:"其他"
    }];



    $scope.queryModelById=function() {
        var param={};
        param.id=$scope.model.id;
        BusinessService.post(myRootUrl+$scope.queryUrl ,param).success(function (data) {
            //console.log(data.data);
            $scope.model.loginName = data.data.loginName;
            $scope.model.loginPassword = data.data.loginPassword;
            $scope.model.userName = data.data.userName;
            $scope.model.idCardType = data.data.idCardType;
            $scope.model.idCard = data.data.idCard;
            $scope.model.sex = data.data.sex;
            $scope.model.sex = data.data.sex;
            $scope.model.publicAccount = data.data.publicAccount==1?true:false;
            $scope.model.status = data.data.status==1?true:false;
        });
    }

    //编辑页面获取属性
    if(null!=$scope.model.id&&$scope.model.id.length>0) {
        $scope.queryModelById();
    }

    $scope.submitForm=function() {
        //console.log(jsonStr);
        if(null!=$scope.model.id&&$scope.model.id.length>0) {
            BusinessService.post(myRootUrl + $scope.updateUrl, $scope.model).success(function (data) {
                if(null==data) {
                    return;
                }
                if (data.data == 1) {
                    parent.window.location.href = parent.window.location.href;
                }
            }).error(function(data, status, headers, config) {
                console.log("error<<<<");
            });
        }else {
            BusinessService.post(myRootUrl + $scope.insertUrl, $scope.model).success(function (data) {
                if(null==data) {
                    return;
                }
                if (data.data == 1) {
                    parent.window.location.href = parent.window.location.href;
                }
            }).error(function(data, status, headers, config) {
                console.log("error<<<<");
            });
        }

    };

    $scope.addItem=function(title,url,w,h){
        layer_show(title,url,w,h);
    }

    $scope.editItem=function(title,url,id,w,h) {
        //alert(id);
        layer_show(title,url+"?id="+id,w,h);
    };


    $scope.deleteItem=function(id) {
        var ids = "";
        if (null != id && id != undefined) {
            ids = id;
        } else {
            for(var i=0;i<$scope.items.length;i++) {
                if($scope.items[i].checked==true) {
                    if(ids=="") {
                        ids=ids+$scope.items[i][$scope.mainKey];
                    }else {
                        ids = ids + "," +$scope.items[i][$scope.mainKey];
                    }
                }
            }
            if(ids=="") {
                alert("请选择要删除的项");
                return;
            }
        }
        if(confirm("删除须谨慎，确认要删除吗？")) {
            var param={"ids":ids};
            //console.log("jsonStr="+jsonStr);
            BusinessService.post(myRootUrl + $scope.deleteUrl, param).success(function (data) {
                if (data.data > 0) {
                    alert("删除成功");
                    $scope.listItems();
                }
            });
        }
    };



}]);


//流水业务类
app.factory('BusinessService', ['$http', function ($http) {
    var get = function (url,params) {
        return $http({
            params: params,
            url: url,
            method: 'GET'
        });
    };

    var post = function (url, business) {
        return $http.post(url, business);
    };

    return {
        get: function (url,params) {
            return get(url,params);
        },
        post: function (url, business) {
            return post(url, business);
        }
    };
}]);

