/**
 * Created by Administrator on 2016/6/8.
 */

app.controller('myCtrl', ['$scope', '$rootScope', 'BusinessService', function ($scope, $rootScope, BusinessService) {
    $scope.user={};

    $scope.initData=function() {
        var param={};
        var token=getCookie("token");
        param.id=getParamFromToken(token,"userId");
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

}]);


