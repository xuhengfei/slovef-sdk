package com.slovef.android.http.parser;

import org.json.JSONObject;

public abstract class JsonParser<X> extends StringParser<X>{

	@Override
	public X parse(String content)  throws Exception{
		if(content==null){
			return null;
		}
		JSONObject json;
		json = new JSONObject(content);
		try {
			return parse(json);
		} catch (Exception e) {
			throw e;
		}
	}

	public abstract X parse(JSONObject json)  throws Exception;
}
