package com.slovef.android.http.parser;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;

public abstract class StringParser<X> implements ResponseParser<X>{

	@Override
	public X parse(HttpResponse resp) throws Exception {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(resp
				.getEntity().getContent()));
		String line = "";
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		return parse(sb.toString());
	}
	
	public abstract X parse(String content)  throws Exception;

}
