/**
 * Created by Administrator on 2016/4/12.
 */

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
    $scope.pageSize=30;
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
            if($scope.changeGridData) {
                $scope.changeGridData();
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

    //=====================表格相关属性和方法 结束


    //自已定义的属性和方法
    //列表页属性
    $scope.param={};
    $scope.param.parentClassId="";
    $scope.param.status=false;
    $scope.orderBy="classId,1";
    $scope.mainKey="itemId"; //删除和移动方法需要用到,表示根据主键来操作
    $scope.listUrl="/permMgmt/listControlPanel.do";
    $scope.deleteUrl="/permMgmt/deleteControlPanel.do";
    $scope.moveUrl="/permMgmt/moveControlPanel.do";
    $scope.changeGridData=function() {
        for (var i = 0; i < $scope.items.length; i++) {
            for(var j=1;j<$scope.items[i].classId.length/10;j++){
                $scope.items[i].itemName="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+$scope.items[i].itemName;
            }
        }

    };

    //其他属性
    $scope.options={};
    $scope.options.items=[];


    //初始化数据
    $scope.initData=function() {
        //显示列表
        $scope.listItems();
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
        param.status=$scope.param.status;
        var optionValue="classId";
        var optionLabel="itemName";

        //获取父部门信息
        $scope.loadSelectOptionData($scope.options,"items",$scope.listUrl,param,optionValue,optionLabel,true,"");
    };

    $scope.changeParam = function() {
        $scope.listItems();
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

    $scope.editItem=function(title,url,id,w,h) {
        if(!w) {
            w=$(window).width();
        }
        if(!h) {
            h = $(window).height();
        }
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
        var id=$scope.getItem($scope.mainKey);
        var param={};
        param.move=position;
        param.id=id;
        BusinessService.post(myRootUrl + $scope.moveUrl, param).success(function (data) {
            if(null==data) {
                return;
            }
            $scope.listItems();
        });
    };

    $scope.chooseCount=function() {
        var count=0;
        for(var i=0;i<$scope.items.length;i++) {
            if($scope.items[i].checked==true) {
                count++;
            }
        }
        return count;
    };

    $scope.getItem=function(key) {
        for(var i=0;i<$scope.items.length;i++) {
            if($scope.items[i].checked==true) {
                return $scope.items[i][key];
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


