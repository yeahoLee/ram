package com.mine.product.czmtr.ram.flowable.utils;


import com.vgtech.platform.common.utility.VGUtility;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
public class HttpUtil {

	/**
	 * 向指定的URL发送GET方法的请求
	 * @param url    发送请求的URL
	 * @param Authorization  请求参数
	 * @return       远程资源的响应结果
	 */
	public static String sendGet(String url,String Authorization) {
		String result = "";
		BufferedReader bufferedReader = null;
		try {
			//1、读取初始URL
			String urlNameString = url;
			//2、将url转变为URL类对象
			URL realUrl = new URL(urlNameString);
			HttpURLConnection urlConnection = (HttpURLConnection) realUrl.openConnection(); //打开连接
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setRequestMethod("GET");
			urlConnection.setUseCaches(false);

			//3、打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			//4、设置通用的请求属性
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("charset", "UTF-8");
			if(!VGUtility.isEmpty(Authorization)) {
				connection.addRequestProperty("Authorization", Authorization);
			}

			//5、建立实际的连接
			connection.connect();

			//6、定义BufferedReader输入流来读取URL的响应内容 ，UTF-8是后续自己加的设置编码格式，也可以去掉这个参数
			bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
			String line = "";
			while(null != (line = bufferedReader.readLine())) {
				result += line;
			}
		}catch (Exception e) {
			throw new RuntimeException("发送GET请求出现异常！！！"  + e);
		}finally {        //使用finally块来关闭输入流
			try {
				if(null != bufferedReader) {
					bufferedReader.close();
				}
			}catch (Exception e2) {
				throw new RuntimeException("发送GET请求出现异常！！！"  + e2);
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 *
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public  String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！"+e);
			e.printStackTrace();
		}
		//使用finally块来关闭输出流、输入流
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static String getLocalUrl(HttpServletRequest request) {
 		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
	
 
}
