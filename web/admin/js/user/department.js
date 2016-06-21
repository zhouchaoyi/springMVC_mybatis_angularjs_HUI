/**
 * Created by Administrator on 2016/4/12.
 */

app.controller('myCtrl', ['$scope', '$rootScope', 'BusinessService', function ($scope, $rootScope, BusinessService) {

    //表格自定义的属性和方法
    $scope.dg={};
    $scope.dg.param={};
    $scope.dg.param.pageSize=15;
    $scope.dg.param.orderBy="classId,1";
    $scope.dg.listUrl="/userMgmt/listDepartment.do";
    $scope.dg.loadGridData = function() {
        BusinessService.post(myRootUrl+$scope.dg.listUrl ,$scope.dg.param).success(function (data) {
            $scope.dg.setGridData(data);
        });
    };
    $scope.dg.changeGridData=function() {
        angular.forEach($scope.dg.items, function(data){
            for(var j=1;j<data.classId.length/10;j++){
                data.departmentName="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+data.departmentName;
            }
        });
    };
    //表格自定义的属性和方法=================结束


    $scope.dg.mainKey="departmentId"; //删除方法需要用到,表示根据哪个字段来删除
    $scope.deleteUrl="/userMgmt/deleteDepartment.do";
    //表单页属性
    $scope.insertUrl="/userMgmt/addDepartment.do";
    $scope.updateUrl="/userMgmt/updateDepartment.do";
    $scope.queryUrl="/userMgmt/queryDepartmentById.do";

    $scope.model={};
    $scope.model.id=getQueryString("id");
    $scope.query={};
    $scope.query.parentId=getQueryString("parentId");
    //console.log($scope.query.parentId);
    $scope.model.parentId = -1;
    if($scope.query.parentId!=null) {
        $scope.model.parentId = parseInt($scope.query.parentId);
    }

    $scope.model.isTypeOnly=false;
    $scope.options={};
    $scope.options.dept=[];

    //初始化数据
    $scope.initData=function() {
        //console.log("init");
        //编辑页面获取属性
        if(null!=$scope.model.id&&$scope.model.id.length>0) {
            $scope.queryModelById();
        }

        //获取父部门信息
        var param={};
        param.currentPage=-1;
        param.pageSize=-1;
        param.orderBy="classId,1";
        BusinessService.post(myRootUrl + $scope.dg.listUrl, param).success(function (data) {
            if(null==data) {
                return;
            }
            for (var i = 0; i < data.data.items.length; i++) {
                for(var j=0;j<data.data.items[i].classId.length/10;j++){
                    if(data.data.items[i].departmentName.indexOf("└")==-1) {
                        data.data.items[i].departmentName="└"+data.data.items[i].departmentName;
                    }
                    if(j>0) {
                        data.data.items[i].departmentName = "　" + data.data.items[i].departmentName;
                    }
                }
            }
            $scope.options.dept=data.data.items;
            var data={};
            data.departmentId=-1;
            data.departmentName="-请选择-"
            $scope.options.dept.unshift(data);

        });
    };

    $scope.queryModelById=function() {
        var param={};
        param.id=$scope.model.id;
        BusinessService.post(myRootUrl+$scope.queryUrl ,param).success(function (data) {
            //console.log(data.data);
            $scope.model.departmentName = data.data.departmentName;
            $scope.model.parentId = data.data.parentId;
            $scope.model.classId = data.data.classId;
            $scope.model.departmentKey = data.data.departmentKey;
            $scope.model.remark = data.data.remark;
            $scope.model.isTypeOnly = data.data.isTypeOnly==1?true:false;
        });
    }

    $scope.beforeSubmitForm=function() {
        return true;
    };

    $scope.submitForm=function() {
        //console.log($scope.model);
        if(!$scope.beforeSubmitForm()) {
            return;
        }
        if(null!=$scope.model.id&&$scope.model.id.length>0) {
            BusinessService.post(myRootUrl + $scope.updateUrl, $scope.model).success(function (data) {
                //console.log(data);
                if(null==data) {
                    return;
                }
                parent.window.location.href = parent.window.location.href;

            }).error(function(data, status, headers, config) {
                console.log("error<<<<");
            });
        }else {
            BusinessService.post(myRootUrl + $scope.insertUrl, $scope.model).success(function (data) {
                if(null==data) {
                    return;
                }
                parent.window.location.href = parent.window.location.href;

            }).error(function(data, status, headers, config) {
                console.log("error<<<<");
            });
        }

    };

    $scope.addItem=function(title,url,w,h){
        if(!w) {
            w=$(window).width();
        }
        if(!h) {
            h = $(window).height();
        }
        layer_show(title,url,w,h);
    }

    $scope.addChildItem=function(title,url,w,h){
        var count=$scope.chooseCount();
        if(count>1) {
            alert("只能选择一条记录");
            return;
        }
        var parentId="";
        if(count==1) {
            parentId=$scope.getItem("departmentId")+"";
        }
        if(!w) {
            w=$(window).width();
        }
        if(!h) {
            h = $(window).height();
        }
        if(parentId.length>0) {
            layer_show(title, url+"?parentId="+parentId, w, h);
        }else {
            layer_show(title, url, w, h);
        }
    }

    $scope.dg.editItem=function(title,url,id,w,h) {
        if(!w) {
            w=$(window).width();
        }
        if(!h) {
            h = $(window).height();
        }
        layer_show(title,url+"?id="+id,w,h);
    };


    $scope.dg.deleteItem=function(id) {
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
                    $scope.dg.listItems();
                }
            });
        }
    };

    $scope.doMove=function(position) {
        var count=$scope.chooseCount();
        if(count==0) {
            alert("请选择记录");
            return;
        }
        if(count>1) {
            alert("只能选择一条记录");
            return;
        }
        var departmentId=$scope.getItem("departmentId")
        var url="/userMgmt/doMoveDepartment.do";
        var param={};
        param.move=position;
        param.departmentId=departmentId;
        BusinessService.post(myRootUrl + url, param).success(function (data) {
            if(null==data) {
                return;
            }
            $scope.dg.listItems();
        });
    };

    $scope.chooseCount=function() {
        var count=0;
        for(var i=0;i<$scope.dg.items.length;i++) {
            if($scope.dg.items[i].checked==true) {
                count++;
            }
        }
        return count;
    };

    $scope.getItem=function(key) {
        for(var i=0;i<$scope.dg.items.length;i++) {
            if($scope.dg.items[i].checked==true) {
                return $scope.dg.items[i][key];
            }
        }
    };

}]);


