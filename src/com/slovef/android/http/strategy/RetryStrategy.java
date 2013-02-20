package com.slovef.android.http.strategy;

import org.apache.http.HttpResponse;

import com.slovef.android.http.ConnectApi;
import com.slovef.android.http.HttpMgr;
/**
 * ���Բ���
 * �������������������һ������
 * @author zhoufang
 *
 */
public class RetryStrategy implements FailStrategy{

	public <X> X retry(HttpMgr httpMgr,ConnectApi<X> api) throws Exception{
		return httpMgr.executeOnce(api);
	}

	@Override
	public <X> X process(HttpMgr httpMgr, ConnectApi<X> api, HttpResponse resp) {
		try {
			return retry(httpMgr, api);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
