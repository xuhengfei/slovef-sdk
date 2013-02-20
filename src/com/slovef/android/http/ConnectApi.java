package com.slovef.android.http;

import org.apache.http.client.methods.HttpUriRequest;

import com.slovef.android.http.parser.ResponseParser;


public interface ConnectApi<X> {

	HttpUriRequest getHttpRequest();
	/**
	 * 返回null表示请求异常，将会触发FailStrategy进行处理
	 * @return
	 */
	ResponseParser<X> getResponseParser();
}
