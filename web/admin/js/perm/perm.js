/**
 * Created by Administrator on 2016/4/12.
 */

app.controller('myCtrl', ['$scope', '$rootScope', 'BusinessService', function ($scope, $rootScope, BusinessService) {
    //tree属性设置
    var zTreeObj;

    var setting = {
        view: {
            fontCss : {"font-weight":"bold"},
            selectedMulti: false
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback: {
            onClick: onClick
        },
        check: {
            enable: true,
            chkStyle: "checkbox",
            chkboxType: { "Y": "s","N": "s" }
        }
    };

    var zNodes = [{
        id: -1,
        name: "模块/权限设置",
        open: true,
        icon: "../../images/module/manage.gif",
        nocheck : true
    }];
    //tree属性设置 结束=====================


    $scope.listUrl="/permMgmt/listModuleForPerm.do";
    $scope.query={};
    $scope.query.id=getQueryString("id");
    $scope.query.type=getQueryString("type");

    $scope.initData=function() {
        $scope.loadModule();
    };

    //加载tree的数据
    $scope.loadModule=function() {
        var param={};
        param.currentPage=-1;
        param.pageSize=-1;
        param.orderBy="classId,1";
        param.searchStr="";
        param.permUserId=$scope.query.id;
        param.permType=$scope.query.type;
        BusinessService.post(myRootUrl + $scope.listUrl, param).success(function (data) {
            if(null==data) {
                return;
            }
            //console.log(data);
            for(var i=0;i<data.data.items.length;i++) {
                var item=data.data.items[i];
                var obj={};
                obj.id=item.moduleId;
                obj.pId=item.parentId;
                obj.name=item.moduleName;
                obj.moduleCode=item.moduleCode;
                obj.userType=item.userType;
                obj.moduleUrl=item.url;
                obj.relationUrl=item.relationUrl;
                obj.remark=item.remark;
                obj.bExt=item.bExt==1?'分类目录':'权限/模块';
                obj.status=item.status==1?'启用':'禁用';
                if(item.hasPerm=="1") {
                    obj.checked=true;
                }else {
                    obj.checked=false;
                }
                obj=$scope.setIcon(item,obj);
                //console.log(obj);
                zNodes.push(obj);
            }
            zTreeObj = $.fn.zTree.init($("#moduleTree"), setting, zNodes);
            zTreeObj.expandAll(true);
        });
    };

    $scope.save = function() {
        var treeObj = $.fn.zTree.getZTreeObj("moduleTree");
        var nodes = treeObj.getCheckedNodes(true);
        var moduleIds="";
        for(var i=0;i<nodes.length;i++) {
            if(moduleIds=="") {
                moduleIds+=nodes[i].id;
            }else {
                moduleIds+=","+nodes[i].id;
            }
        }
        var url="/permMgmt/addPerm.do";
        var param={};
        param.id=$scope.query.id;
        param.type=$scope.query.type;
        param.moduleIds=moduleIds;
        BusinessService.post(myRootUrl + url, param).success(function (data) {
            if(null==data) {
                return;
            }
            alert("保存成功");
            var appElement = parent.document.querySelector('[ng-controller=myCtrl]');
            var $scope = parent.angular.element(appElement).scope();
            $scope.listItems();
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
        });
    };


    //设置图标
    $scope.setIcon = function(item,obj) {
        if(item.bExt=="1") { //是分类目录
            if(item.url!="") { //路径不为空
                if (item.status == "1") { //被启用
                    if(item.isParent=="1") {
                        obj.iconOpen = "../../images/module/module_fld2.gif";
                        obj.iconClose = "../../images/module/module_fld1.gif";
                    }else {
                        obj.icon = "../../images/module/module_fld2.gif";
                    }
                }else {
                    if(item.isParent=="1") {
                        obj.iconOpen = "../../images/module/module_fld2_forbidden.gif";
                        obj.iconClose = "../../images/module/module_fld1_dis.gif";
                    }else {
                        obj.icon = "../../images/module/module_fld2_forbidden.gif";
                    }
                }
            }else {
                if (item.status == "1") { //被启用
                    if(item.isParent=="1") {
                        obj.iconOpen = "../../images/module/fld2.gif";
                        obj.iconClose = "../../images/module/fld1.gif";
                    }else {
                        obj.icon = "../../images/module/fld2.gif";
                    }
                }else {
                    if(item.isParent=="1") {
                        obj.iconOpen = "../../images/module/fld2_forbidden.gif";
                        obj.iconClose = "../../images/module/fld1_forbidden.gif";
                    }else {
                        obj.icon = "../../images/module/fld2_forbidden.gif";
                    }
                }
            }
        }else {
            if (item.isPermOnly == "1") { //是权限
                if (item.status == "1") {
                    obj.icon = "../../images/module/perm_icon.gif";
                } else {
                    obj.icon = "../../images/module/perm_icon_forbidden.gif";
                }
            } else { //是模块
                if(item.url!="") { //路径不为空
                    if (item.status == "1") { //被启用
                        obj.icon="../../images/module/module.gif";
                    }else {
                        obj.icon="../../images/module/module_forbidden.gif";
                    }
                }else {
                    if (item.status == "1") { //被启用
                        obj.icon="../../images/module/module2.gif";
                    }else {
                        obj.icon="../../images/module/module2_forbidden.gif";
                    }
                }
            }
        }
        return obj;
    };


}]);



