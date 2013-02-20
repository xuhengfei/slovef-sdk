package com.slovef.android.http.strategy;

import org.apache.http.HttpResponse;

import com.slovef.android.http.ConnectApi;
import com.slovef.android.http.HttpMgr;

/**
 * 当请求失败时的处理策略
 * 举例使用场景：登陆过期时，自动重登陆
 * 				网络失败时，进行一次重试
 * @author zhoufang
 *
 */
public interface FailStrategy{
	<X>X process(HttpMgr httpMgr,ConnectApi<X> api,HttpResponse resp);
}
