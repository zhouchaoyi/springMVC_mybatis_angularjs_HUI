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
        }
    };

    var zNodes = [{
        id: -1,
        name: "模块/权限设置",
        open: true,
        icon: "../../images/module/manage.gif"
    }];
    //tree属性设置 结束=====================

    $scope.listUrl="/permMgmt/listModule.do";
    $scope.deleteUrl="/permMgmt/deleteModule.do";
    //表单属性
    $scope.insertUrl="/permMgmt/addModule.do";
    $scope.updateUrl="/permMgmt/updateModule.do";
    $scope.queryUrl="/permMgmt/queryModuleById.do";
    $scope.model={};
    $scope.model.id=getQueryString("id");
    $scope.model.parentId=getQueryString("parentId");
    $("#citySel").attr("value",getQueryString("parentName"));
    $scope.model.isPermOnly = "0";
    $scope.model.status = true;

    $scope.array={};
    $scope.array.userType=[];

    $scope.initData=function() {
        $scope.loadModule();
        if(null!=$scope.model.parentId&&$scope.model.parentId.length>0) { //新增页面
            $scope.loadUserType();
        }
        if(null!=$scope.model.id&&$scope.model.id.length>0) { //编辑页面
            $scope.queryModelById();
        }

    };

    //加载tree的数据
    $scope.loadModule=function() {
        var param={};
        param.currentPage=-1;
        param.pageSize=-1;
        param.orderBy="classId,1";
        param.searchStr="";
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
                obj=$scope.setIcon(item,obj);
                //console.log(obj);
                zNodes.push(obj);
            }
            zTreeObj = $.fn.zTree.init($("#moduleTree"), setting, zNodes);

            if(null!=$scope.model.id&&$scope.model.id.length>0) { //编辑页面
                var node = zTreeObj.getNodeByParam("id", $scope.model.id, null);
                if(node.pId=="-1") {
                    $("#citySel").attr("value", "根节点");
                }else {
                    node = zTreeObj.getNodeByParam("id", node.pId, null);
                    var parentName = node.name;
                    $("#citySel").attr("value", parentName);
                }
            }
        });
    };


    //获取用户类别信息
    $scope.loadUserType=function(userTypeStr) {
        //console.log(userTypeStr);
        var param={};
        var url="/userMgmt/listUserType.do";
        BusinessService.post(myRootUrl + url, param).success(function (data) {
            if(null==data) {
                return;
            }
            for(var i=0;i<data.data.length;i++) {
                if(userTypeStr) {
                    var str = "," + userTypeStr + ",";
                    var str2 = "," + data.data[i].typeCode + ",";
                    if (str.indexOf(str2) != -1) {
                        data.data[i].checked = true;
                    } else {
                        data.data[i].checked = false;
                    }
                }else {
                    data.data[i].checked = false;
                }
            }
            $scope.array.userType=data.data;
        });
    };

    $scope.queryModelById=function() {
        var param={};
        param.id=$scope.model.id;
        BusinessService.post(myRootUrl+$scope.queryUrl ,param).success(function (data) {
            //console.log(data.data);
            $scope.model.parentId = data.data.parentId;
            $scope.model.moduleName = data.data.moduleName;
            $scope.model.moduleCode = data.data.moduleCode;
            $scope.model.isPermOnly = data.data.isPermOnly;
            $scope.model.remark = data.data.remark;
            $scope.model.url = data.data.url;
            $scope.model.relationUrl = data.data.relationUrl;
            $scope.model.bExt = data.data.bExt==1?true:false;
            $scope.model.status = data.data.status==1?true:false;

            $scope.loadUserType(data.data.userType);
        });
    };

    $scope.beforeSubmitForm=function() {
        $scope.model.moduleCode=$scope.model.moduleCode.toUpperCase();
        var str="";
        for(var i=0;i<$scope.array.userType.length;i++) {
            if($scope.array.userType[i].checked==true) {
                if(str=="") {
                    str+=$scope.array.userType[i].typeCode;
                }else {
                    str+=","+ $scope.array.userType[i].typeCode;
                }
            }
        }
        $scope.model.userType=str;
        return true;
    };

    $scope.afterSubmitForm = function(data) {
        if(null!=$scope.model.id&&$scope.model.id.length>0) {
            var pTree = parent.$.fn.zTree.getZTreeObj("moduleTree");;
            var node = pTree.getNodeByParam("id", $scope.model.id, null);
            if(node.pId==data.data.parentId) { //父节点没有变
                var selectedNode = pTree.getSelectedNodes()[0];
                selectedNode.name = data.data.moduleName;
                selectedNode=$scope.setIcon(data.data,selectedNode);
                pTree.updateNode(selectedNode);
            }else { //父节点变了
                var targetNode = pTree.getNodeByParam("id", data.data.parentId, null);
                var selectedNode = pTree.getSelectedNodes()[0];
                selectedNode.name = data.data.moduleName;
                selectedNode=$scope.setIcon(data.data,selectedNode);
                pTree.moveNode(targetNode, selectedNode, "inner");
            }
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
        }else {
            var pTree = parent.$.fn.zTree.getZTreeObj("moduleTree");
            var parentNode = pTree.getSelectedNodes()[0];
            var item = data.data;
            var node={};
            node.id=item.moduleId;
            node.pId=item.parentId;
            node.name=item.moduleName;
            node=$scope.setIcon(item,node);
            //console.log(node);
            pTree.addNodes(parentNode, node);
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
        }
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
                $scope.afterSubmitForm(data);
                //parent.window.location.href = parent.window.location.href;

            }).error(function(data, status, headers, config) {
                console.log("error<<<<");
            });
        }else {
            BusinessService.post(myRootUrl + $scope.insertUrl, $scope.model).success(function (data) {
                if(null==data) {
                    return;
                }
                $scope.afterSubmitForm(data);
                //parent.window.location.href = parent.window.location.href;

            }).error(function(data, status, headers, config) {
                console.log("error<<<<");
            });
        }

    };

    $scope.addChildNode = function() {
        var title="新增节点";
        var url="module_prop.html";
        var w=600;
        var h=550;
        var selectedNode = zTreeObj.getSelectedNodes();
        var parentId="-1";
        var parentName="根节点";
        if(selectedNode.length>0) {
            parentId=selectedNode[0].id;
            if(parentId!="-1") {
                parentName = selectedNode[0].name;
            }
        }
        //console.log(parentId);
        //console.log(parentName);
        layer_show(title,url+"?parentId="+parentId+"&parentName="+encodeURIComponent(parentName),w,h);
    };

    $scope.editNode = function() {
        var selectedNode = zTreeObj.getSelectedNodes();
        if(selectedNode.length==0) {
            alert("请选择要编辑的节点");
            return;
        }
        if(selectedNode[0].id==-1) {
            alert("不能编辑根节点");
            return;
        }
        var title="节点属性编辑";
        var url="module_prop.html";
        var w=600;
        var h=550;
        layer_show(title,url+"?id="+selectedNode[0].id,w,h);
    };

    $scope.deleteNode = function() {
        var selectedNode = zTreeObj.getSelectedNodes();
        if(selectedNode.length==0) {
            alert("请选择要删除的节点");
            return;
        }
        if(selectedNode[0].id==-1) {
            alert("不能删除根节点");
            return;
        }
        if(confirm("确定要删除 ‘"+selectedNode[0].name+"’ 节点吗")) {
            var param={};
            param.moduleId=selectedNode[0].id;
            BusinessService.post(myRootUrl + $scope.deleteUrl, param).success(function (data) {
                if(null==data) {
                    return;
                }
                zTreeObj.removeNode(selectedNode[0]);
            })

        }
    };

    $scope.doMove=function(position) {
        var selectedNode = zTreeObj.getSelectedNodes();
        if(selectedNode.length==0) {
            alert("请选择要移动的节点");
            return;
        }
        if(selectedNode[0].id==-1) {
            alert("不能移动根节点");
            return;
        }
        var url="/permMgmt/moveModule.do";
        var param={};
        param.move=position;
        param.moduleId=selectedNode[0].id;
        BusinessService.post(myRootUrl + url, param).success(function (data) {
            if(null==data) {
                return;
            }
            if(position=="1") {
                var targetNode = selectedNode[0].getPreNode();
                zTreeObj.moveNode(targetNode, selectedNode[0], "prev");
            }else if(position=="-1") {
                var targetNode = selectedNode[0].getNextNode();
                zTreeObj.moveNode(targetNode, selectedNode[0], "next");
            }
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



