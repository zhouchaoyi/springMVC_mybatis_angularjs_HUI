/**
 * Created by Administrator on 2016/4/12.
 */

app.controller('myCtrl', ['$scope', '$rootScope', 'BusinessService', function ($scope, $rootScope, BusinessService) {
    //公共属性
    $scope.query={};
    $scope.query.userId=getQueryString("userId");
    $scope.query.loginName=getQueryString("loginName");

    //表格（1）自定义的属性和方法
    $scope.dg={};
    $scope.dg.param={};
    $scope.dg.param.userId=$scope.query.userId;
    $scope.dg.param.pageSize=10;
    $scope.dg.param.orderBy="joinDate,1";
    $scope.dg.listUrl="/userMgmt/listUserJoinGroup.do";
    $scope.dg.loadGridData = function() {
        BusinessService.post(myRootUrl+$scope.dg.listUrl ,$scope.dg.param).success(function (data) {
            $scope.dg.setGridData(data);
        });
    };
    $scope.dg.changeGridData=function() {
        angular.forEach($scope.dg.items, function(data){

        });
    };
    //表格（1）自定义的属性和方法=================结束

    //表格（2）自定义的属性和方法
    $scope.dg2={};
    $scope.dg2.param={};
    $scope.dg2.param.userId=$scope.query.userId;
    $scope.dg2.param.pageSize=10;
    $scope.dg2.param.orderBy="groupId,-1";
    $scope.dg2.listUrl="/userMgmt/listUserGroupForUser.do";
    $scope.dg2.loadGridData = function() {
        BusinessService.post(myRootUrl+$scope.dg2.listUrl ,$scope.dg2.param).success(function (data) {
            $scope.dg2.setGridData(data);
        });
    };
    $scope.dg2.changeGridData=function() {
        angular.forEach($scope.dg2.items, function(data){

        });
    };
    //表格（2）自定义的属性和方法=================结束


    $scope.dg.mainKey="groupId"; //方法需要用到,表示根据哪个字段来操作
    $scope.dg2.mainKey="groupId"; //方法需要用到,表示根据哪个字段来操作
    //初始化数据
    $scope.initData=function() {
        //$scope.listItems();
        //$scope.listItems2();
    };

    $scope.addMember=function(id) {
        var ids = "";
        if (null != id && id != undefined) {
            ids = id;
        } else {
            for(var i=0;i<$scope.dg2.items.length;i++) {
                if($scope.dg2.items[i].checked==true) {
                    if(ids=="") {
                        ids=ids+$scope.dg2.items[i][$scope.dg2.mainKey];
                    }else {
                        ids = ids + "," +$scope.dg2.items[i][$scope.dg2.mainKey];
                    }
                }
            }
            if(ids=="") {
                alert("请选择要添加的用户组");
                return;
            }
        }

        var param={};
        param.ids=ids;
        param.userId=$scope.query.userId;
        var url="/userMgmt/joinGroup.do";
        BusinessService.post(myRootUrl + url, param).success(function (data) {
            if (data.data > 0) {
                alert("操作成功");
                $scope.dg2.listItems();
                $scope.dg.listItems();
            }
        });

    };

    $scope.deleteMember=function(id) {
        var ids = "";
        if (null != id && id != undefined) {
            ids = id;
        } else {
            for(var i=0;i<$scope.dg.items.length;i++) {
                if($scope.dg.items[i].checked==true) {
                    if(ids=="") {
                        ids=ids+$scope.dg.items[i][$scope.dg.mainKey];
                    }else {
                        ids = ids + "," +$scope.dg.items[i][$scope.dg.mainKey];
                    }
                }
            }
            if(ids=="") {
                alert("请选择要删除的用户组");
                return;
            }
        }

        if(confirm("删除须谨慎，确认要删除吗？")) {
            var param = {};
            param.ids = ids;
            param.userId = $scope.query.userId;
            var url = "/userMgmt/cancelJoinGroup.do";
            BusinessService.post(myRootUrl + url, param).success(function (data) {
                if (data.data > 0) {
                    alert("操作成功");
                    $scope.dg2.listItems();
                    $scope.dg.listItems();
                }
            });
        }

    };

    //$scope.chooseCount=function() {
    //    var count=0;
    //    for(var i=0;i<$scope.items.length;i++) {
    //        if($scope.items[i].checked==true) {
    //            count++;
    //        }
    //    }
    //    return count;
    //};
    //
    //$scope.getItem=function(key) {
    //    for(var i=0;i<$scope.items.length;i++) {
    //        if($scope.items[i].checked==true) {
    //            return $scope.items[i][key];
    //        }
    //    }
    //};

}]);


