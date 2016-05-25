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
    $scope.orderBy="createdDate,1";
    $scope.mainKey="groupId"; //删除方法需要用到,表示根据哪个字段来删除
    $scope.listUrl="/userMgmt/listUserGroup.do";
    $scope.deleteUrl="/userMgmt/deleteUserGroup.do";
    $scope.changeGridData=function() {

    };

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
                $scope.listItems();
                return true;
            }
        });
    };

}]);


