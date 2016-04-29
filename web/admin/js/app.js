/**
 * Created by Administrator on 2016/4/12.
 */
var myRootUrl="/stock";

function getQueryString(name) {
    var url=window.location.href;
    //console.log(url);
    url=url.substr(url.indexOf("?")+1);
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = url.match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}
