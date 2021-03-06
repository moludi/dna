package com.jiuqi.eip.external.outputData.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

public class HttpResultUtil {
	/**
	 * 请求方式：Get
	 * @param sendUrl
	 * 				发送请求url
	 * @return
	 *              JSON对象
	 * @throws Exception
	 */
	public static String sendGet(String sendUrl) throws Exception {
		String r = new String();
		String strLine = new String();
		URL url = new URL(sendUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		InputStream in = connection.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		while ((strLine = reader.readLine()) != null) {
			r += strLine + "\n";
		}
		return r;
	}
	/**
	 * 
	 * @param url
	 *            发送请求url
	 * @param params
	 *            初始参数
	 * @return
	 * @throws Exception
	 */
	public static String sendPost(String url, Map<String, Object> params) throws Exception {
		StringBuffer buffer = new StringBuffer();
		for (Entry<String, Object> entry : params.entrySet()) {
			buffer.append("&" + entry.getKey() + "=" + entry.getValue());
		}
		return sendPost(url,buffer.toString());
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
	public static String sendPost(String url, String param) throws Exception {
		OutputStreamWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "application/json;charset=UTF-8");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			// 发送请求参数
			out.append(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			throw new Exception("发送 POST 请求连接出现异常！");
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				throw new Exception("post方法:关闭连接异常");
			}
		}
		return result;
	}
}
