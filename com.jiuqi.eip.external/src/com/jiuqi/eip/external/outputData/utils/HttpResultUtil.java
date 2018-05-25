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
	 * ����ʽ��Get
	 * @param sendUrl
	 * 				��������url
	 * @return
	 *              JSON����
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
	 *            ��������url
	 * @param params
	 *            ��ʼ����
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
	 * ��ָ�� URL ����POST����������
	 * 
	 * @param url
	 *            ��������� URL
	 * @param param
	 *            ����������������Ӧ���� name1=value1&name2=value2 ����ʽ��
	 * @return ������Զ����Դ����Ӧ���
	 */
	public static String sendPost(String url, String param) throws Exception {
		OutputStreamWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// �򿪺�URL֮�������
			URLConnection conn = realUrl.openConnection();
			// ����ͨ�õ���������
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "application/json;charset=UTF-8");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// ����POST�������������������
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// ��ȡURLConnection�����Ӧ�������
			out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			// �����������
			out.append(param);
			// flush������Ļ���
			out.flush();
			// ����BufferedReader����������ȡURL����Ӧ
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			throw new Exception("���� POST �������ӳ����쳣��");
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				throw new Exception("post����:�ر������쳣");
			}
		}
		return result;
	}
}
