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
        name: "请选择",
        open: true,
        icon: "../../images/module/manage.gif"
    }];
    //tree属性设置 结束=====================


    //表单页属性
    $scope.insertUrl="/permMgmt/addControlPanel.do";
    $scope.updateUrl="/permMgmt/updateControlPanel.do";
    $scope.queryUrl="/permMgmt/queryControlPanelById.do";

    $scope.model={};
    $scope.model.id=getQueryString("id");
    $scope.query={};
    $scope.query.parentId=getQueryString("parentId");
    $scope.query.classId=getQueryString("classId");
    //console.log($scope.query.parentId);
    $scope.model.parentId = -1;
    if($scope.query.parentId!=null) {
        $scope.model.parentId = parseInt($scope.query.parentId);
    }

    $scope.model.status=true;
    $scope.model.statusTxt="启用相关菜单 <span style='color:gray'>(如想关闭此菜单及关联子菜单，请取消勾选)</span>";
    $scope.options={};
    $scope.options.items=[];

    $scope.elementShow=false;

    //初始化数据
    $scope.initData=function() {
        if(null!=$scope.model.id&&$scope.model.id.length>0) { //编辑页面
            $scope.queryModelById();
        }else { //新增页面
            var sClassLevel=1;
            if(null!=$scope.query.parentId&&$scope.query.parentId.length>0) {
                if($scope.query.classId.length==20) {
                    $scope.elementShow=true;
                    $scope.model.statusTxt="启用";
                    sClassLevel=2;
                    //获取模块信息
                    $scope.loadModule();
                }
            }
            //获取父节点信息
            $scope.getParentItem(sClassLevel);
        }

    };

    //获取父节点信息
    $scope.getParentItem = function(sClassLevel) {
        var param={};
        param.currentPage=-1;
        param.pageSize=-1;
        param.orderBy="classId,1";
        param.classLevel=sClassLevel;
        var optionValue="itemId";
        var optionLabel="itemName";
        var url="/permMgmt/listControlPanel.do";

        $scope.loadSelectOptionData($scope.options,"items",url,param,optionValue,optionLabel,true);
    };

    $scope.queryModelById=function() {
        var param={};
        param.id=$scope.model.id;
        BusinessService.post(myRootUrl+$scope.queryUrl ,param).success(function (data) {
            //console.log(data.data);
            $scope.model.parentId = data.data.parentId;
            $scope.model.itemName = data.data.itemName;
            $scope.model.remark = data.data.remark;
            $scope.model.moduleId = data.data.moduleId;
            $scope.model.status = data.data.status==1?true:false;
            var classLevel=1;
            if(data.data.classId.length==30) {
                $scope.elementShow=true;
                $scope.model.statusTxt="启用";
                classLevel=2;
                //获取模块信息
                $scope.loadModule();
            }
            $scope.getParentItem(classLevel);
        });
    }

    $scope.beforeSubmitForm=function() {
        if(!$scope.model.moduleId) {
            $scope.model.moduleId="-1";
        }
        if(!$scope.elementShow) {
            $scope.model.moduleId="-1";
        }
        return true;
    };

    $scope.afterSubmitForm=function(data) {
        parent.window.location.href = parent.window.location.href;
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

            }).error(function(data, status, headers, config) {
                console.log("error<<<<");
            });
        }else {
            BusinessService.post(myRootUrl + $scope.insertUrl, $scope.model).success(function (data) {
                if(null==data) {
                    return;
                }
                $scope.afterSubmitForm(data);

            }).error(function(data, status, headers, config) {
                console.log("error<<<<");
            });
        }

    };


    $scope.loadSelectOptionData = function(obj,arrayName,url,param,optionValue,optionLabel,hasClassId) {
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
                            data.data.items[i][optionLabel] = "　　" + data.data.items[i][optionLabel];
                        }
                    }
                }
            }
            obj[arrayName]=data.data.items;
            var data={};
            data[optionValue]=-1;
            data[optionLabel]="-请选择-"
            obj[arrayName].unshift(data);

        });
    };

    $scope.loadModule=function() {
        var param={};
        param.currentPage=-1;
        param.pageSize=-1;
        param.orderBy="classId,1";
        param.searchStr="";
        var url="/permMgmt/listModule.do";
        BusinessService.post(myRootUrl + url, param).success(function (data) {
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
                obj=$scope.setIcon(item,obj);
                //console.log(obj);
                zNodes.push(obj);
            }
            zTreeObj = $.fn.zTree.init($("#moduleTree"), setting, zNodes);

            if(null!=$scope.model.id&&$scope.model.id.length>0) { //编辑页面
                var node = zTreeObj.getNodeByParam("id", $scope.model.moduleId, null);
                if(node.id=="-1") {
                    $("#citySel").attr("value", "请选择");
                }else {
                    var parentName = node.name;
                    $("#citySel").attr("value", parentName);
                }
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


