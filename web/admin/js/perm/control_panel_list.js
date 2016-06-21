/**
 * Created by Administrator on 2016/4/12.
 */

app.controller('myCtrl', ['$scope', '$rootScope', 'BusinessService', function ($scope, $rootScope, BusinessService) {

    //表格自定义的属性和方法
    $scope.dg={};
    $scope.dg.param={};
    $scope.dg.param.parentClassId="";
    $scope.dg.param.status=false;
    $scope.dg.param.pageSize=30;
    $scope.dg.param.orderBy="classId,1";
    $scope.dg.listUrl="/permMgmt/listControlPanel.do";
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
            for(var j=1;j<data.classId.length/10;j++){
                data.itemName="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+data.itemName;
            }
        });
    };
    //表格自定义的属性和方法=================结束


    $scope.deleteUrl="/permMgmt/deleteControlPanel.do";
    $scope.moveUrl="/permMgmt/moveControlPanel.do";
    $scope.dg.mainKey="itemId"; //删除和移动方法需要用到,表示根据主键来操作
    //其他属性
    $scope.options={};
    $scope.options.items=[];


    //初始化数据
    $scope.initData=function() {
        //获取父节点信息
        $scope.getParentItem();
    };

    //获取父节点信息
    $scope.getParentItem = function() {
        //获取父部门信息的参数
        var param={};
        param.currentPage=-1;
        param.pageSize=-1;
        param.orderBy="classId,1";
        param.classLevel=1;
        param.status=$scope.dg.param.status;
        var optionValue="classId";
        var optionLabel="itemName";

        //获取父部门信息
        $scope.loadSelectOptionData($scope.options,"items",$scope.dg.listUrl,param,optionValue,optionLabel,true,"");
    };

    $scope.changeParam = function() {
        $scope.dg.listItems();
        $scope.getParentItem();
    };

    $scope.addItem=function(title,url,w,h){
        var count=$scope.chooseCount();
        if(count>1) {
            alert("只能选择一条记录");
            return;
        }
        var parentId="";
        var classId="";
        if(count==1) {
            parentId=$scope.getItem("itemId")+"";
            classId=$scope.getItem("classId");
            if(classId.length>20) {
                alert("不能再增加子节点！请不选择节点或者选择第一、第二层的节点添加子节点！");
                return;
            }
        }
        if(!w) {
            w=$(window).width();
        }
        if(!h) {
            h = $(window).height();
        }
        if(parentId.length>0) {
            layer_show(title, url+"?parentId="+parentId+"&classId="+classId, w, h);
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
                    if($scope.afterDeleteItem) {
                        $scope.afterDeleteItem();
                    }
                }
            });
        }
    };

    $scope.afterDeleteItem = function() {
        //获取父节点信息
        $scope.getParentItem();
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
        var id=$scope.getItem($scope.dg.mainKey);
        var param={};
        param.move=position;
        param.id=id;
        BusinessService.post(myRootUrl + $scope.moveUrl, param).success(function (data) {
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

    $scope.loadSelectOptionData = function(obj,arrayName,url,param,optionValue,optionLabel,hasClassId,defaultValue) {
        BusinessService.post(myRootUrl + url, param).success(function (data) {
            if(null==data) {
                return;
            }
            if(hasClassId) {
                for (var i = 0; i < data.data.items.length; i++) {
                    for (var j = 0; j < data.data.items[i].classId.length / 10; j++) {
                        if (data.data.items[i][optionLabel].indexOf("└") == -1) {
                            data.data.items[i][optionLabel] = "└" + data.data.items[i][optionLabel];
                        }
                        if (j > 0) {
                            data.data.items[i][optionLabel] = "　" + data.data.items[i][optionLabel];
                        }
                    }
                }
            }
            obj[arrayName]=data.data.items;
            var data={};
            data[optionValue]=defaultValue;
            data[optionLabel]="-请选择-"
            obj[arrayName].unshift(data);

        });
    };

}]);


