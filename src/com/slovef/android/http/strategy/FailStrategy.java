package com.slovef.android.http.strategy;

import org.apache.http.HttpResponse;

import com.slovef.android.http.ConnectApi;
import com.slovef.android.http.HttpMgr;

/**
 * ������ʧ��ʱ�Ĵ������
 * ����ʹ�ó�������½����ʱ���Զ��ص�½
 * 				����ʧ��ʱ������һ������
 * @author zhoufang
 *
 */
public interface FailStrategy{
	<X>X process(HttpMgr httpMgr,ConnectApi<X> api,HttpResponse resp);
}
