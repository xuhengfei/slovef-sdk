package com.slovef.android.http.connectapi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import com.slovef.android.filesystem.FileCache;
import com.slovef.android.http.ConnectApi;
import com.slovef.android.http.HttpCacheable;
import com.slovef.android.http.parser.ResponseParser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class ImageLoadApi implements ConnectApi<Bitmap>,HttpCacheable<Bitmap>{

	private static ImageCache cache=new ImageCache();
	private boolean needCache=true;
	private String url;
	public ImageLoadApi(String url){
		this.url=url;
	}
	public ImageLoadApi(String url,boolean needCache){
		this.url=url;
		this.needCache=needCache;
	}
	@Override
	public HttpUriRequest getHttpRequest() {
		return new HttpGet(url);
	}

	@Override
	public ResponseParser<Bitmap> getResponseParser() {
		return new ImageParser();
	}
	
	public class ImageParser implements ResponseParser<Bitmap>{
		@Override
		public Bitmap parse(HttpResponse resp) throws Exception {
			InputStream is=resp.getEntity().getContent();
			byte[] bytes=new byte[1024];
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			int count=0;
			while((count=is.read(bytes))!=-1){
				bos.write(bytes,0,count);
			}
			byte[] byteArray=bos.toByteArray();
			Bitmap bmp=BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
			if(bmp!=null){
				cache.set(ImageLoadApi.this.url, bmp);
			}
			return bmp;
		}
	}

	@Override
	public Bitmap getCache() {
		if(needCache){
			return cache.get(url);
		}
		return null;
	}

	
	public static class ImageCache {

		private Map<String, Bitmap> cache=new HashMap<String, Bitmap>();
		private String path;
		public ImageCache(){
			path=FileCache.getCacheFolder()+"image-cache/";
		}
		
		public void set(String url,Bitmap bitmap){
			try {
				cache.remove(url);
				String value=MD5(url);
				File f=new File(path+value);
				f.createNewFile();
				FileOutputStream fos=new FileOutputStream(f);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 60, fos);
				fos.flush();
				fos.close();
			} catch (Exception e) {
			}
		}
		
		public Bitmap get(String url){
			try {
				if(cache.containsKey(url)){
					return cache.get(url);
				}
				String filePath=path+MD5(url);
				if(!new File(filePath).exists()){
					return null;
				}
				FileInputStream fis=new FileInputStream(filePath);
				Bitmap bmp= BitmapFactory.decodeStream(fis);
				if(bmp!=null){
					cache.put(url, bmp);
				}
				return bmp;
			} catch (FileNotFoundException e) {
				return null;
			}
		}

		public final String MD5(String s) { 
		    String result = "";
		    char hexDigits[] = 
		                  { '0', '1', '2', '3', 
		              '4', '5', '6', '7',
		              '8', '9', 'a', 'b', 
		              'c', 'd', 'e', 'f' };    
		    try {   
		     
		        byte[] strTemp = s.getBytes();      
		        MessageDigest mdTemp = MessageDigest.getInstance("MD5");    
		        mdTemp.update(strTemp);    
		        byte[] md = mdTemp.digest();    
		        int j = md.length;    
		        char str[] = new char[j * 2];    
		        int k = 0;    
		        for (int i = 0; i < j; i++) {    
		            byte b = md[i];       
		            str[k++] = hexDigits[b >> 4 & 0xf];    
		            str[k++] = hexDigits[b & 0xf];    
		        }    
		        result = new String(str);
		    } catch (Exception e) 
		        {e.printStackTrace();}    
		    return result;
		    }   
	}
}
