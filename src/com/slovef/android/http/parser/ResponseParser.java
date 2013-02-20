package com.slovef.android.http.parser;

import org.apache.http.HttpResponse;

public interface ResponseParser<X> {
	X parse(HttpResponse resp) throws Exception;
}
