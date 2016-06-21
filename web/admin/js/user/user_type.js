/**
 * Created by Administrator on 2016/4/12.
 */

app.controller('myCtrl', ['$scope', '$rootScope','$compile', 'BusinessService', function ($scope, $rootScope,$compile, BusinessService) {

    //表格的通用方法
    $scope.loadGridData = function() {
        BusinessService.post(myRootUrl+$scope.listUrl ,$scope.param).success(function (data) {
           $scope.setGridData(data);
        });
    };
    //表格的通用方法=================结束

    //表格自定义的属性和方法
    $scope.param={};
    $scope.param.pageSize=10;
    $scope.param.orderBy="";
    $scope.listUrl="/userMgmt/listUserType.do";
    $scope.changeGridData=function() {
        for (var i = 0; i < $scope.items.length; i++) {
            if($scope.items[i].status==1) {
                $scope.items[i].status="<font color=green>启用</font>";
            }else if($scope.items[i].status==0) {
                $scope.items[i].status="<font color=red>禁用</font>";
            }
        }

    };
    //表格自定义的属性和方法=================结束

    $scope.mainKey="typeId"; //删除方法需要用到,表示根据哪个字段来删除
    $scope.deleteUrl="/userMgmt/deleteUserType.do";

    $scope.userType={};
    $scope.userType.id=getQueryString("type_id");
    $scope.userType.remark="";
    $scope.userType.status=false;


    $scope.initData = function() {
        //console.log("come in initData<<<<<<");
        //console.log($scope);
        //console.log($scope.listItems);
        //$scope.listItems();
    };

    $scope.queryUserTypeById=function() {
        var param={};
        param.typeId=$scope.userType.id;
        BusinessService.post(myRootUrl+"/userMgmt/queryUserTypeById.do" ,param).success(function (data) {
            //console.log(data.data);
            $scope.userType.code = data.data.typeCode;
            $scope.userType.name = data.data.typeName;
            $scope.userType.remark = data.data.remark;
            $scope.userType.status = data.data.status==1?true:false;
        });
    };

    //角色编辑页面获取角色信息
    if(null!=$scope.userType.id&&$scope.userType.id.length>0) {
        $scope.queryUserTypeById();
    }

    $scope.submitUserTypeForm=function(isValid) {
        if(isValid) {
            //alert(jsonStr);
            if(null!=$scope.userType.id&&$scope.userType.id.length>0) {
                BusinessService.post(myRootUrl + "/userMgmt/updateUserType.do", $scope.userType).success(function (data) {
                    if (data.data == 1) {
                        parent.window.location.href = parent.window.location.href;
                    }
                });
            }else {
                BusinessService.post(myRootUrl + "/userMgmt/addUserType.do", $scope.userType).success(function (data) {
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
                for(var i=0;i<$scope.items.length;i++) {
                    if($scope.items[i].checked==true) {
                        if(ids=="") {
                            ids=ids+$scope.items[i].typeId;
                        }else {
                            ids = ids + "," +$scope.items[i].typeId;
                        }
                    }
                }
                if(ids=="") {
                    alert("请选择要删除的项");
                    return;
                }
            }
            var param={"ids":ids};
            //console.log("jsonStr="+jsonStr);
            BusinessService.post(myRootUrl + "/userMgmt/deleteUserType.do", param).success(function (data) {
                if (data.data > 0) {
                    alert("删除成功");
                    $scope.listItems();
                }
            });
        }
    };

}]);


