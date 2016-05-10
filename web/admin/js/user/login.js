/**
 * Created by Administrator on 2016/4/12.
 */

app.controller('myCtrl', ['$scope', '$rootScope', 'BusinessService', function ($scope, $rootScope, BusinessService) {

    $scope.model={};
    $scope.model.loginName="";
    $scope.model.password="";
    $scope.model.saveAccount=false;
    $scope.loginUrl="/userMgmt/login.do";

    $scope.hasLocalAccount=false;
    $scope.localAccount=[];
    var tokenStr=getCookie("token_str");
    if(null!=tokenStr) {
        var tokenArray = tokenStr.split(",");
        for(var i=0;i<tokenArray.length;i++) {
            $scope.localAccount[i]={};
            $scope.localAccount[i].loginName=getParamFromToken(tokenArray[i],"loginName");
            $scope.localAccount[i].token=tokenArray[i];
        }
        $scope.hasLocalAccount=true;
    }

    $scope.loginByLocalAccount=function(token) {
        var exp = getParamFromToken(token,"exp");
        setCookie("token",token,exp);
        var url="/userMgmt/loginByLocalAccount.do";
        var param={};
        BusinessService.post(myRootUrl + url, param).success(function (data) {
            if(null==data) {
                return;
            }
            if(data.data==1) {
                window.location.href = "./";
            }

        }).error(function(data, status, headers, config) {
            console.log("error<<<<");
        });
    };

    $scope.delLocalAccount=function() {
        delCookie("token_str");
        window.location.href = window.location.href;
    };

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
            //localStorage.setItem("token",data.data.token);
            if(null==data) {
                return;
            }
            if($scope.model.saveAccount) {
                addTokenInTokenStr(data.data.token); //保存到本地常用账号
            }
            //console.log(getCookie("token_str"))
            window.location.href = "./";

        }).error(function(data, status, headers, config) {
            console.log("error<<<<");
        });

    };

}]);


