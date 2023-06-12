/**
 * 自定义的fetch，用于返回自定义异常
 * @param url {String} 请求的url
 * @param rr {Object,undefined} 要添加的请求头
 * @param noAddHeader {boolean,undefined} 是否不添加用于标示的请求头
 *
 * */
import RandomAddress from "./RandomAddress.js";

export default async function nBGGFetch(url,rr,noAddHeader){
    if(!noAddHeader){
        if(!rr){
            rr = {headers:{"NewBingWeb":"true"}};
        }else if(!rr.headers){
            rr.headers = {"NewBingWeb":"true"};
        }else {
            rr.headers['NewBingWeb'] = "true";
        }
        rr.headers['randomAddress'] = RandomAddress.randomAddress;
    }
   let re = await fetch(url,rr)
   if(re.headers.get('NewBingError')){
       let json = await re.json();
       let error= new Error(json.message);
       error.value = json.value;
       error.isNewBingError = true;
       error.theType = json.type;//NewBing 自定义错误类型
       error.theData = json.data;//NewBing 自定义错误数据
       throw error;
   }
   return re;
}