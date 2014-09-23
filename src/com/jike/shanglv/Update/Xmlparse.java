package com.jike.shanglv.Update;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Xmlparse {
	public static UpdateNode readXML(InputStream inStream,String name) {
		try {
			// 创建解析器
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser saxParser = spf.newSAXParser();

			// 设置解析器的相关特性，true表示开启命名空间特性
//			saxParser.setProperty("http://xml.org/sax/features/namespaces",true);
			XMLContentHandler handler = new XMLContentHandler();
			saxParser.parse(inStream, handler);
			inStream.close();
			List<UpdateNode> list=handler.getUpdateNodes();
			UpdateNode node=new UpdateNode();
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getName().equals(name)) {
					node=list.get(i);
				}
			}
			return node;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据URL得到输入流
	 * @param urlStr
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public InputStream getInputStreamFromUrl(String urlStr)throws MalformedURLException, IOException {
	    URL url = new URL(urlStr);
	    HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
	    InputStream inputStream = urlConn.getInputStream();
	    return inputStream;
	}
	InputStream is;
	
	public void getUpdateNode() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Message msg = new Message();
					is= getInputStreamFromUrl("http://dn-rocky.qbox.me/shanglvguanjiaversion.xml");
					msg.what = 1;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				UpdateNode node=readXML(is,"jike");
				Log.v(node.getContent(),node.getVersion());
				break;
			}
		}
	};


}
