/**
 * Created by Administrator on 2016/4/12.
 */

app.controller('myCtrl', ['$scope', '$rootScope', 'BusinessService', function ($scope, $rootScope, BusinessService) {

    //表格自定义的属性和方法
    $scope.dg={};
    $scope.dg.param={};
    $scope.dg.param.userType="admin";
    $scope.dg.param.pageSize=10;
    $scope.dg.param.orderBy="userId,-1";
    $scope.dg.listUrl="/userMgmt/listUserByType.do";
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

            var tempStr="";
            for(var j=0;j<data.userGroup.length;j++){
                tempStr+="<i class='Hui-iconfont'>&#xe62b;</i>"+data.userGroup[j].groupName+"&nbsp;&nbsp;";
            }
            data.userGroup=tempStr;

            if(data.sex==1) {
                data.sex="女";
            }else if(data.sex==0) {
                data.sex="男";
            }
        });
    };
    //表格自定义的属性和方法=================结束


    //表单页属性
    $scope.insertUrl="/userMgmt/addUser.do";
    $scope.updateUrl="/userMgmt/updateUser.do";
    $scope.queryUrl="/userMgmt/queryUserById.do";
    $scope.deleteUrl="/userMgmt/deleteUser.do";
    $scope.dg.mainKey="userId"; //删除方法需要用到,表示根据哪个字段来删除
    $scope.model={};
    $scope.model.id=getQueryString("id");
    $scope.model.idCardType=0;
    $scope.model.sex=0;
    $scope.model.publicAccount=false;
    $scope.model.status=false;
    $scope.model.userType="admin";
    $scope.options={};
    $scope.options.idCardType=[{
        value:0,
        label:"身份证"
    },{
        value:1,
        label:"军官证"
    },{
        value:2,
        label:"港澳台居民证"
    },{
        value:3,
        label:"护照"
    },{
        value:4,
        label:"其他"
    }];



    $scope.queryModelById=function() {
        var param={};
        param.id=$scope.model.id;
        BusinessService.post(myRootUrl+$scope.queryUrl ,param).success(function (data) {
            //console.log(data.data);
            $scope.model.loginName = data.data.loginName;
            $scope.model.loginPassword = data.data.loginPassword;
            $scope.model.userName = data.data.userName;
            $scope.model.idCardType = data.data.idCardType;
            $scope.model.idCard = data.data.idCard;
            $scope.model.sex = data.data.sex;
            $scope.model.sex = data.data.sex;
            $scope.model.publicAccount = data.data.publicAccount==1?true:false;
            $scope.model.status = data.data.status==1?true:false;
        });
    }

    //编辑页面获取属性
    if(null!=$scope.model.id&&$scope.model.id.length>0) {
        $scope.queryModelById();
    }

    $scope.submitForm=function() {
        //console.log(jsonStr);
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

    $scope.joinGroup = function() {
        var count=$scope.chooseCount();
        if(count==0) {
            alert("请选择记录");
            return;
        }
        if(count>1) {
            alert("只能选择一条记录");
            return;
        }
        var userId=$scope.getItem($scope.dg.mainKey);
        var loginName=$scope.getItem("loginName");
        var url = "user_join_group.html?userId="+userId+"&loginName="+encodeURIComponent(loginName);
        var w=$(window).width();
        var h=$(window).height();
        $scope.layer_show("用户组",url,w,h);
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
        var name="";
        if(count==1) {
            val=$scope.getItem("userId")+"";
            name=$scope.getItem("loginName")+"";
        }
        if(!w) {
            w=$(window).width();
        }
        if(!h) {
            h = $(window).height();
        }
        layer_show(title+"-"+name, url+"?id="+val+"&type=0", w, h);
    };

    $scope.addItem=function(title,url,w,h){
        layer_show(title,url,w,h);
    };

    $scope.dg.editItem=function(title,url,id,w,h) {
        //alert(id);
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


