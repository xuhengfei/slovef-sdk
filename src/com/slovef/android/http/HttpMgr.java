package com.slovef.android.http;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import com.slovef.android.http.strategy.FailStrategy;


public class HttpMgr {

	private Map<String, HttpClient> httpclientMap=new HashMap<String, HttpClient>();
	private FailStrategy failStrategy;
	
	private boolean enableMock=false;
	
	public HttpMgr(){
	}
	/**
	 * 执行一个Api请求
	 * 当请求结果为null时，会触发执行 失败策略处理 FailStrategy
	 * @param <X>
	 * @param api
	 * @return
	 * @throws Exception
	 */
	public <X>X execute(ConnectApi<X> api) throws Exception{
		if(api instanceof HttpMockable){
			return ((HttpMockable<X>)api).mock();
		}
		if(enableMock && api instanceof HttpCacheable){
			X ret=((HttpCacheable<X>)api).getCache();
			if(ret!=null){
				return ret;
			}
		}
		HttpUriRequest request=api.getHttpRequest();
		if(httpclientMap.get(request.getURI().getHost())==null){
			httpclientMap.put(request.getURI().getHost(), new DefaultHttpClient());
		}
		HttpClient client=httpclientMap.get(request.getURI().getHost());
		HttpResponse resp=client.execute(request);
		X result=api.getResponseParser().parse(resp);
		if(result==null && failStrategy!=null){
			result=failStrategy.process(this,api,resp);
		}
		return result;
	}
	/**
	 * 单纯的执行一次Api请求，不会触发 FailStrategy
	 * @param <X>
	 * @param api
	 * @return
	 * @throws Exception
	 */
	public <X>X executeOnce(ConnectApi<X> api) throws Exception{
		if(api instanceof HttpMockable){
			return ((HttpMockable<X>)api).mock();
		}
		if(enableMock && api instanceof HttpCacheable){
			X ret=((HttpCacheable<X>)api).getCache();
			if(ret!=null){
				return ret;
			}
		}
		HttpUriRequest request=api.getHttpRequest();
		if(httpclientMap.get(request.getURI().getHost())==null){
			httpclientMap.put(request.getURI().getHost(), new DefaultHttpClient());
		}
		HttpClient client=httpclientMap.get(request.getURI().getHost());
		HttpResponse resp=client.execute(request);
		return api.getResponseParser().parse(resp);
	}

	public void setEnableMock(boolean enableMock) {
		this.enableMock = enableMock;
	}

	public void setFailStrategy(FailStrategy failStrategy) {
		this.failStrategy = failStrategy;
	}

	
}
