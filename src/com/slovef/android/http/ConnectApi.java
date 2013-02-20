package com.slovef.android.http;

import org.apache.http.client.methods.HttpUriRequest;

import com.slovef.android.http.parser.ResponseParser;


public interface ConnectApi<X> {

	HttpUriRequest getHttpRequest();
	/**
	 * ����null��ʾ�����쳣�����ᴥ��FailStrategy���д���
	 * @return
	 */
	ResponseParser<X> getResponseParser();
}
