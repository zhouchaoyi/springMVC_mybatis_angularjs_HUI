/**
 * Created by Administrator on 2016/4/12.
 */
var app = angular.module("userApp",[]).config(function($httpProvider) {

    $httpProvider.defaults.headers
        .common['Accept'] = 'application/json; charset=utf-8';
    $httpProvider.defaults.headers.post['Content-Type'] = 'application/json; charset=UTF-8';

});

app.controller('userCtrl', ['$scope', '$rootScope', 'BusinessService', function ($scope, $rootScope, BusinessService) {
    $scope.userType={};
    $scope.userType.id=getQueryString("type_id");
    $scope.userType.remark="";
    $scope.userType.status=false;

    $scope.userTypeList={};
    $scope.checkAllVal=false;

    $scope.checkAll=function() {
        for(var i=0;i<$scope.userTypeList.length;i++) {
            $scope.userTypeList[i].checked=$scope.checkAllVal;
        }
    };

    $scope.checkItem=function(checked) {
        if(!checked) {
            $scope.checkAllVal=false;
        }
    };

    $scope.queryUserTypeById=function() {
        var param={};
        param.typeId=$scope.userType.id;
        var jsonStr=JSON.stringify(param);
        BusinessService.post(myRootUrl+"/userMgmt/queryUserTypeById" ,jsonStr).success(function (data) {
            //console.log(data.data);
            $scope.userType.code = data.data.typeCode;
            $scope.userType.name = data.data.typeName;
            $scope.userType.remark = data.data.remark;
            $scope.userType.status = data.data.status==1?true:false;
        });
    }

    //角色编辑页面获取角色信息
    if(null!=$scope.userType.id&&$scope.userType.id.length>0) {
        $scope.queryUserTypeById();
    }

    $scope.listUserType=function() {
        BusinessService.post(myRootUrl+"/userMgmt/listUserType" ,"").success(function (data) {
            //console.log(data.data);
            $scope.userTypeList = data.data;
            for(var i=0;i<$scope.userTypeList.length;i++) {
                $scope.userTypeList[i].checked=false;
            }
        });
    };


    $scope.submitUserTypeForm=function(isValid) {
        if(isValid) {
            var jsonStr=JSON.stringify($scope.userType);
            //alert(jsonStr);
            if(null!=$scope.userType.id&&$scope.userType.id.length>0) {
                BusinessService.post(myRootUrl + "/userMgmt/updateUserType", jsonStr).success(function (data) {
                    if (data.data == 1) {
                        parent.window.location.href = parent.window.location.href;
                    }
                });
            }else {
                BusinessService.post(myRootUrl + "/userMgmt/addUserType", jsonStr).success(function (data) {
                    if (data.data == 1) {
                        parent.window.location.href = parent.window.location.href;
                    }
                });
            }
        }else {
            alert("请完整正确的填写");
        }
    };

    $scope.roleEdit=function(title,url,id,w,h) {
        //alert(id);
        layer_show(title,url+"?type_id="+id,w,h);
    };


    $scope.delUserType=function(id) {
        if(confirm("角色删除须谨慎，确认要删除吗？")) {
            var ids = "";
            if (null != id && id != undefined) {
                ids = id;
            } else {
                for(var i=0;i<$scope.userTypeList.length;i++) {
                    if($scope.userTypeList[i].checked==true) {
                        if(ids=="") {
                            ids=ids+$scope.userTypeList[i].typeId;
                        }else {
                            ids = ids + "," +$scope.userTypeList[i].typeId;
                        }
                    }
                }
                if(ids=="") {
                    alert("请选择要删除的项");
                    return;
                }
            }
            var param={"ids":ids};
            var jsonStr=JSON.stringify(param);
            //console.log("jsonStr="+jsonStr);
            BusinessService.post(myRootUrl + "/userMgmt/deleteUserType", jsonStr).success(function (data) {
                if (data.data > 0) {
                    alert("删除成功");
                    $scope.listUserType();
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

