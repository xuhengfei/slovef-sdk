slovef-sdk
==========
Android客户端与后台服务通过http协议进行交互时，使用slovef-sdk可以使得编程更加简单

客户端与服务端，一个http请求，我们就认为是一个API

* 发起http请求
* 实现http登陆失效后的自动登陆
* 实现http请求的模拟返回
* 图片下载与缓存

##如何发起一个API请求
创建一个自定义Api类，继承ConnectApi
```java
    /**
   * 此API用于查询当前是否已经登陆
	 * 预期返回的结果是json格式
	 * 成功： {login:true}
	 * 失败: {login:false}
	 */
	public class IsLoginApi implements ConnectApi<Boolean>{
		public HttpUriRequest getHttpRequest() {
			//Http请求基于HttpClient，因此需要拼装一个HttpUriRequest
			HttpUriRequest req=new HttpGet("http://xxx.com/islogin.do");
			return req;
		}
		public ResponseParser<Boolean> getResponseParser() {
			return new JsonParser<Boolean>() {
				public Boolean parse(JSONObject json) throws Exception {
					return json.optBoolean("login");
				}
			};
		}
	}

```
发起Http请求，并获取结果
```java
    	HttpMgr httpMgr=new HttpMgr();
		IsLoginApi api=new IsLoginApi();
		Boolean isLogin=httpMgr.execute(api);
		System.out.println(isLogin);
```


##实现http登陆失效后的自动登陆
使用http协议与服务端交互，一般都需要登陆。

登陆信息在经过一段时间后，可能会过期失效，如果此时再发起需要登陆验证的请求，就会失败了

我们需要实现，当session失效时，自动进行一次登陆行为，登陆成功后再对之前的请求进行重试

这里我们通过自己实现一个HttpMgr里面的FailStrategy来完成自己的一个请求失败处理策略

```java
    class MyAutoLoginStrategy extends AutoLoginStrategy{
		public boolean isLogin(HttpResponse resp) {
			//根据相应结果判定，是否已登陆
			return false;
		}

		@Override
		public boolean doLogin(HttpMgr httpMgr) {
			try {
				return httpMgr.executeOnce(new LoginApi("zhangsan", "12345"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
		
	}
```
以上代码继承了AutoLoginStrategy，AutoLoginStrategy中已经实现了自动重试等功能，具体参见源码

在此处，只需要写清楚如何发起登陆请求即可

##实现http请求的模拟返回
客户端和后台一般都是同时进行开发的

当服务器端还没有提供好Api接口，而客户端需要使用时，就需要对服务器端的Api进行模拟

slovef-sdk提供非常方便的http请求模拟框架

* 创建一个需要模拟返回的Api，将此Api类实现HttpMockable接口
* 实现HttpMockable的mock()方法，此方法返回你想返回的格式
* 在创建HttpMgr时，设置httpMgr.setEnableMock(true)

```java
    class MockLoginApi implements ConnectApi<Boolean>,HttpMockable<Boolean>{
		public Boolean mock() {
			return true;//模拟返回true，表示登陆成功
		}
		public HttpUriRequest getHttpRequest() {
			return null;
		}
		public ResponseParser<Boolean> getResponseParser() {
			return null;
		}
	}
```

##图片下载与缓存

slovef-sdk自带了图片下载的Api：ImageLoadApi

默认此Api会自动将图片缓存到本地

当然也可以指定不缓存图片：new ImageLoadApi("http://xxx.com/abc.jpg",false)

有了这个Api，使用httpMgr.execute(api)就可以得到Bitmap对象了

除了图片缓存，其他http请求也可以进行缓存

只要将自己的Api实现HttpCacheable接口，即可。



