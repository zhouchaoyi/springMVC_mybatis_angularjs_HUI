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

    $scope.model={};
    $scope.model.loginName="";
    $scope.model.password="";
    $scope.loginUrl="/userMgmt/login.do";

    $scope.submitForm=function() {
        if(trimStr($scope.model.loginName)=="") {
            alert("账号不能为空");
            return;
        }
        if($scope.model.password=="") {
            alert("密码不能为空");
            return;
        }
        //console.log(jsonStr);
        BusinessService.post(myRootUrl + $scope.loginUrl, $scope.model).success(function (data) {
            //console.log(data);
            if(data.status.errorCode!="000000") {
                alert(data.status.errorMsg);
                return;
            }else {
                localStorage.setItem("token",data.data.token);
                window.location.href = "./";
            }
        }).error(function(data, status, headers, config) {
            console.log("error<<<<");
        });

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

