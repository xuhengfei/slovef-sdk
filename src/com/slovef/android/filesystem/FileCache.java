package com.slovef.android.filesystem;

import java.util.concurrent.atomic.AtomicBoolean;

public class FileCache {

	private static String ROOT;
	
	private static AtomicBoolean initd=new AtomicBoolean(false);
	
	public static void init(String root){
		if(initd.get()){
			return;
		}
		if(!root.endsWith("/")){
			root=root+"/";
		}
		ROOT=root;
		initd.set(true);
	}
	
	public static String getCacheFolder(){
		if(!initd.get()){
			throw new RuntimeException("Please call init() first");
		}
		return ROOT;
	}
	
	/**
	 * Çå³ý»º´æ
	 */
	public static void clearCache(){
		if(!initd.get()){
			throw new RuntimeException("Please call init() first");
		}
		//TODO
	}
}
