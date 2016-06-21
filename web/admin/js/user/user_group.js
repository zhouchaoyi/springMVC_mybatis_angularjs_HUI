/**
 * Created by Administrator on 2016/4/12.
 */

app.controller('myCtrl', ['$scope', '$rootScope', 'BusinessService', function ($scope, $rootScope, BusinessService) {

    //表格自定义的属性和方法
    $scope.dg={};
    $scope.dg.param={};
    $scope.dg.param.pageSize=10;
    $scope.dg.param.orderBy="createdDate,1";
    $scope.dg.listUrl="/userMgmt/listUserGroup.do";
    $scope.dg.loadGridData = function() {
        BusinessService.post(myRootUrl+$scope.dg.listUrl ,$scope.dg.param).success(function (data) {
            $scope.dg.setGridData(data);
        });
    };
    $scope.dg.changeGridData=function() {
        angular.forEach($scope.dg.items, function(data){
            if(data.status==1) {
                data.status="<font color=green>启用</font>";
            }else if(data.status==0) {
                data.status="<font color=red>禁用</font>";
            }
        });
    };
    //表格自定义的属性和方法=================结束


    //自已定义的属性和方法
    $scope.deleteUrl="/userMgmt/deleteUserGroup.do";
    $scope.dg.mainKey="groupId"; //删除方法需要用到,表示根据哪个字段来删除
    //表单页属性
    $scope.insertUrl="/userMgmt/addUserGroup.do";
    $scope.updateUrl="/userMgmt/updateUserGroup.do";
    $scope.queryUrl="/userMgmt/queryUserGroupById.do";

    $scope.model={};
    $scope.model.id=getQueryString("id");
    $scope.model.departmentId = -1;

    $scope.model.status=true;
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
        var url="/userMgmt/listDepartment.do";
        BusinessService.post(myRootUrl + url, param).success(function (data) {
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
            $scope.model.groupName = data.data.groupName;
            $scope.model.groupKey = data.data.groupKey;
            $scope.model.departmentId = data.data.departmentId;
            $scope.model.remark = data.data.remark;
            $scope.model.status = data.data.status==1?true:false;
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

    $scope.memberManager=function(title,url,w,h){
        var count=$scope.chooseCount();
        if(count==0) {
            alert("请选择记录");
            return;
        }
        if(count>1) {
            alert("只能选择一条记录");
            return;
        }
        var val="";
        var groupName="";
        if(count==1) {
            val=$scope.getItem("groupId")+"";
            groupName=$scope.getItem("groupName")+"";
        }
        if(!w) {
            w=$(window).width();
        }
        if(!h) {
            h = $(window).height();
        }
        if(val.length>0) {
            $scope.layer_show(title, url+"?groupId="+val+"&groupName="+encodeURIComponent(groupName), w, h);
        }else {
            $scope.layer_show(title, url, w, h);
        }
    };

    $scope.setPerm=function(title,url,w,h){
        var count=$scope.chooseCount();
        if(count==0) {
            alert("请选择记录");
            return;
        }
        if(count>1) {
            alert("只能选择一条记录");
            return;
        }
        var val="";
        var groupName="";
        if(count==1) {
            val=$scope.getItem("groupId")+"";
            groupName=$scope.getItem("groupName")+"";
        }
        if(!w) {
            w=$(window).width();
        }
        if(!h) {
            h = $(window).height();
        }
        layer_show(title+"-"+groupName, url+"?id="+val+"&type=1", w, h);
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

    $scope.layer_show=function(title,url,w,h){
        if (title == null || title == '') {
            title=false;
        };
        if (url == null || url == '') {
            url="404.html";
        };
        if (w == null || w == '') {
            w=800;
        };
        if (h == null || h == '') {
            h=($(window).height() - 50);
        };
        layer.open({
            type: 2,
            area: [w+'px', h +'px'],
            fix: false, //不固定
            maxmin: true,
            shade:0.4,
            title: title,
            content: url,
            cancel: function(index){
                $scope.dg.listItems();
                return true;
            }
        });
    };

}]);


