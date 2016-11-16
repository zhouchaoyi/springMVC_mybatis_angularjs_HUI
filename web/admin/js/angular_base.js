/**
 * Created by Administrator on 2016/5/6.
 */

var app = angular.module("myApp",[]).config(function($httpProvider) {

    $httpProvider.defaults.headers
        .common['Accept'] = 'application/json; charset=utf-8';
    $httpProvider.defaults.headers.post['Content-Type'] = 'application/json; charset=UTF-8';
    $httpProvider.defaults.transformRequest = [function(data) {
        if(null!=data) {
            if (null != getCookie("token")) {
                data.token = getCookie("token");
            }
            //console.log(data.token);
            var jsonStr = JSON.stringify(data);
            return jsonStr;
        }
    }];
    $httpProvider.defaults.transformResponse = [function(data) {
        var json = "";
        try {
            json = JSON.parse(data);
        }catch(e) { //如果不是json数据，把数据直接返回
            return data;
        }
        if(json.status.errorCode=="000001") {
            alert(json.status.errorMsg);
            return null;
        }else if(json.status.errorCode=="000002") {
            alert(json.status.errorMsg);
            top.location.href=loginUrl;
        }else if(json.status.errorCode=="000003") {
            alert(json.status.errorMsg);
            top.location.href=loginUrl;
        }else if(json.status.errorCode=="000000") {
            if(json.data.token!=null&&json.data.token!=undefined) {
                var exp = getParamFromToken(json.data.token,"exp");
                setCookie("token",json.data.token,exp);
            }
        }
        return json;
    }];

});

//流水业务类
app.factory('BusinessService', ['$http', function ($http) {
    var get = function (url,params) {
        return $http({
            params: params,
            url: url,
            method: 'GET'
        });
    };

    var post = function (url, business) {
        return $http.post(url, business);
    };

    return {
        get: function (url,params) {
            return get(url,params);
        },
        post: function (url, business) {
            return post(url, business);
        }
    };
}]);

//输出html
app.filter('html', ['$sce', function ($sce) {
    return function (text) {
        return $sce.trustAsHtml(text);
    }
}]);

//表格组件
app.directive("myGrid",function($compile){
   return {
       restrict : 'E',
       templateUrl: '../ngTemplate/grid.html',
       replace : true,
       scope: {
           gid: '='
       },
       transclude:true,
       controller :function($scope, $element, $attrs, $transclude){
           //console.log("come in controller<<<<<<");
           $scope.gid.columns=[];
           this.addColumn = function(columnObj) {
               $scope.gid.columns.push(columnObj);
               //console.log($scope.columns);
           };
       },
       link: function($scope, iElement, iAttrs, controller) {
           //console.log("come in link<<<<<<");
           //console.log($scope.gid);
           //表格相关属性和方法
           $scope.gid.items={};
           $scope.gid.checkAllVal=false;
           $scope.gid.checkAll=function() {
               for(var i=0;i<$scope.gid.items.length;i++) {
                   $scope.gid.items[i].checked=$scope.gid.checkAllVal;
               }
           };
           $scope.gid.checkItem=function(checked) {
               if(!checked) {
                   $scope.gid.checkAllVal=false;
               }
           };
           $scope.gid.pages=1;
           $scope.gid.gridPrompt=true;
           $scope.gid.gridPromptTxt="数据加载中......";
           $scope.gid.listItems=function() {
               $scope.gid.gridPrompt=true;
               $scope.gid.gridPromptTxt="数据加载中......";
               $scope.gid.checkAllVal=false;
               $scope.gid.param.currentPage=$scope.gid.param.currentPage?$scope.gid.param.currentPage:1;
               $scope.gid.param.pageSize=$scope.gid.param.pageSize?$scope.gid.param.pageSize:10;
               $scope.gid.param.orderBy=$scope.gid.param.orderBy?$scope.gid.param.orderBy:"";
               $scope.gid.param.searchStr=$scope.gid.searchStr?$scope.gid.searchStr:"";

               $scope.gid.loadGridData();
           };
           $scope.gid.setGridData = function(data) {
               //console.log(data);
               if(null==data) {
                   return;
               }
               if(data.data.total>0) {
                   $scope.gid.gridPrompt = false;
               }else {
                   $scope.gid.gridPrompt = true;
                   $scope.gid.gridPromptTxt="没有符合条件的记录";
               }
               $scope.gid.items = data.data.items;
               for (var i = 0; i < $scope.gid.items.length; i++) {
                   $scope.gid.items[i].checked = false;
               }
               if($scope.gid.changeGridData) {
                   $scope.gid.changeGridData();
               }
               $scope.gid.pages = data.data.pages;
               if ($scope.gid.pages > 1) {
                   laypage({
                       cont: 'pagingDiv',
                       pages: $scope.gid.pages, //可以叫服务端把总页数放在某一个隐藏域，再获取。假设我们获取到的是18
                       curr: function () { //通过url获取当前页，也可以同上（pages）方式获取
                           return $scope.gid.param.currentPage;
                       }(),
                       jump: function (e, first) { //触发分页后的回调
                           if (!first) { //一定要加此判断，否则初始时会无限刷新
                               $scope.gid.param.currentPage = e.curr;
                               $scope.gid.checkAllVal=false;
                               $scope.gid.listItems();
                           }
                       }
                   });
               } else {
                   $("#pagingDiv").html("");
               }
               //重新编译，使动态插入的ng-click等生效
               //console.log(document.querySelector("table tr"));
               //var newElem = $compile(document.querySelector("table tr"))($scope);
               //console.log(newElem);
               //iElement.contents().remove();
               //iElement.append(newElem);

           };
           $scope.gid.searchKeyup = function(e){
               var keycode = window.event?e.keyCode:e.which;
               if(keycode==13){
                   $scope.gid.listItems();
               }
           };

           //执行获取表格数据的函数
           $scope.gid.listItems();
           //=====================表格相关属性和方法 结束
       }
   }
});


app.directive("myGridColumn",function() {
    return {
        restrict: 'E',
        require : '^myGrid',
        scope: {
            headtext: '@headtext',
            datafield: '@datafield',
            width: '@width',
            align: '@align',
            itemrender: '@itemrender'
        },
        link: function ($scope, iElement, iAttrs, controller) {
            //console.log("come in link<<<<<<<");
            var columnObj = {};
            columnObj.headtext=$scope.headtext;
            columnObj.datafield = $scope.datafield;
            //if($scope.datafield&&$scope.datafield.indexOf("item.")!=-1) {
            //    columnObj.datafield = $scope.datafield;
            //}else {
            //    columnObj.datafield = "item." + $scope.datafield;
            //}
            columnObj.width=$scope.width;
            if($scope.align) {
                columnObj.align=$scope.align;
            }else {
                columnObj.align="center"; //默认居中
            }
            columnObj.itemrender=$scope.itemrender;
            controller.addColumn(columnObj);
        }
    }
});
