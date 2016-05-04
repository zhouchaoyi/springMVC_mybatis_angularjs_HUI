/**
 * Created by Administrator on 2016/4/12.
 */

//虚拟路径
var myRootUrl="/stock";
//登录页地址
var loginUrl=myRootUrl+"/admin/login.html";

function getQueryString(name) {
    var url=window.location.href;
    //console.log(url);
    url=url.substr(url.indexOf("?")+1);
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = url.match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}

function trimStr(str){
    return str.replace(/(^\s*)|(\s*$)/g,"");
}
