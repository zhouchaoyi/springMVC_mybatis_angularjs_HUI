/**
 * Created by Administrator on 2016/4/12.
 */

app.controller('myCtrl', ['$scope', '$rootScope', 'BusinessService', function ($scope, $rootScope, BusinessService) {

    //表格（1）相关属性和方法
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
    $scope.pageSize=10;
    $scope.pages=1;
    $scope.gridPrompt=true;
    $scope.gridPromptTxt="数据加载中......";
    $scope.listItems=function() {
        $scope.gridPrompt=true;
        $scope.gridPromptTxt="数据加载中......";
        $scope.param.currentPage=$scope.currentPage;
        $scope.param.pageSize=$scope.pageSize;
        $scope.param.orderBy=$scope.orderBy;
        if($scope.searchStr) {
            $scope.param.searchStr = $scope.searchStr;
        }else {
            $scope.param.searchStr="";
        }
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

    //=====================表格（1）相关属性和方法 结束



    //表格（2）相关属性和方法
    $scope.items2={};
    $scope.checkAllVal2=false;
    $scope.checkAll2=function() {
        for(var i=0;i<$scope.items2.length;i++) {
            $scope.items2[i].checked=$scope.checkAllVal2;
        }
    };
    $scope.checkItem2=function(checked) {
        if(!checked) {
            $scope.checkAllVal2=false;
        }
    };
    $scope.currentPage2=1;
    $scope.pageSize2=10;
    $scope.pages2=1;
    $scope.gridPrompt2=true;
    $scope.gridPromptTxt2="数据加载中......";
    $scope.listItems2=function() {
        $scope.gridPrompt2=true;
        $scope.gridPromptTxt2="数据加载中......";
        $scope.param2.currentPage=$scope.currentPage2;
        $scope.param2.pageSize=$scope.pageSize2;
        $scope.param2.orderBy=$scope.orderBy2;
        if($scope.searchStr2) {
            $scope.param2.searchStr = $scope.searchStr2;
        }else {
            $scope.param2.searchStr="";
        }
        BusinessService.post(myRootUrl+$scope.listUrl2 ,$scope.param2).success(function (data) {
            //console.log(data);
            if(null==data) {
                return;
            }
            if(data.data.total>0) {
                $scope.gridPrompt2 = false;
            }else {
                $scope.gridPrompt2 = true;
                $scope.gridPromptTxt2="没有符合条件的记录";
            }
            $scope.items2 = data.data.items;
            for (var i = 0; i < $scope.items2.length; i++) {
                $scope.items2[i].checked = false;
            }
            if($scope.changeGridData2) {
                $scope.changeGridData2();
            }
            $scope.pages2 = data.data.pages;
            if ($scope.pages2 > 1) {
                laypage({
                    cont: 'pagingDiv2',
                    pages: $scope.pages2, //可以叫服务端把总页数放在某一个隐藏域，再获取。假设我们获取到的是18
                    curr: function () { //通过url获取当前页，也可以同上（pages）方式获取
                        return $scope.currentPage2;
                    }(),
                    jump: function (e, first) { //触发分页后的回调
                        if (!first) { //一定要加此判断，否则初始时会无限刷新
                            $scope.currentPage2 = e.curr;
                            $scope.listItems2();
                        }
                    }
                });
            } else {
                $("#pagingDiv2").html("");
            }

        });
    };
    $scope.searchKeyup2 = function(e){
        var keycode = window.event?e.keyCode:e.which;
        if(keycode==13){
            $scope.listItems2();
        }
    };

    //=====================表格（2）相关属性和方法 结束


    //自已定义的属性和方法
    //公共属性
    $scope.query={};
    $scope.query.userId=getQueryString("userId");
    $scope.query.loginName=getQueryString("loginName");

    //列表（1）属性
    $scope.param={};
    $scope.param.userId=$scope.query.userId;
    $scope.orderBy="joinDate,1";
    $scope.mainKey="groupId"; //方法需要用到,表示根据哪个字段来操作
    $scope.listUrl="/userMgmt/listUserJoinGroup.do";
    $scope.changeGridData=function() {

    };
    //列表（2）属性
    $scope.param2={};
    $scope.param2.userId=$scope.query.userId;
    $scope.orderBy2="groupId,-1";
    $scope.mainKey2="groupId"; //方法需要用到,表示根据哪个字段来操作
    $scope.listUrl2="/userMgmt/listUserGroupForUser.do";
    $scope.changeGridData2=function() {

    };


    //初始化数据
    $scope.initData=function() {
        $scope.listItems();
        $scope.listItems2();
    };

    $scope.addMember=function(id) {
        var ids = "";
        if (null != id && id != undefined) {
            ids = id;
        } else {
            for(var i=0;i<$scope.items2.length;i++) {
                if($scope.items2[i].checked==true) {
                    if(ids=="") {
                        ids=ids+$scope.items2[i][$scope.mainKey2];
                    }else {
                        ids = ids + "," +$scope.items2[i][$scope.mainKey2];
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
                $scope.listItems2();
                $scope.listItems();
            }
        });

    };

    $scope.deleteMember=function(id) {
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
                    $scope.listItems2();
                    $scope.listItems();
                }
            });
        }

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

}]);


