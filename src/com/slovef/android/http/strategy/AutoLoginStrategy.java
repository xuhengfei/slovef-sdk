package com.slovef.android.http.strategy;

import org.apache.http.HttpResponse;

import com.slovef.android.http.ConnectApi;
import com.slovef.android.http.HttpMgr;
/**
 * 自动重登陆策略
 * @author zhoufang
 *
 */
public abstract class AutoLoginStrategy extends RetryStrategy{

	@Override
	public <X> X process(HttpMgr httpMgr,ConnectApi<X> api, HttpResponse resp) {
		if(isLoginFail(resp)){
			if(doLogin(httpMgr)){
				try {
					retry(httpMgr, api);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public abstract boolean isLoginFail(HttpResponse resp);
	
	public abstract boolean doLogin(HttpMgr httpMgr);
}
