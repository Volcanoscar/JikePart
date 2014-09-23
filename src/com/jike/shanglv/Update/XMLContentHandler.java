package com.jike.shanglv.Update;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//SAX�ࣺDefaultHandler����ʵ����ContentHandler�ӿڡ���ʵ�ֵ�ʱ��ֻ��Ҫ�̳и��࣬������Ӧ�ķ������ɡ�
	public class XMLContentHandler extends DefaultHandler {

		private List<UpdateNode> upateNodes = null;
		private UpdateNode currentUpdateNode;
		private String tagName = null;// ��ǰ������Ԫ�ر�ǩ

		public List<UpdateNode> getUpdateNodes() {
			return upateNodes;
		}

		// �����ĵ���ʼ��֪ͨ���������ĵ��Ŀ�ͷ��ʱ�򣬵������������������������һЩԤ����Ĺ�����
		@Override
		public void startDocument() throws SAXException {
			upateNodes = new ArrayList<UpdateNode>();
		}

		// ����Ԫ�ؿ�ʼ��֪ͨ��������һ����ʼ��ǩ��ʱ�򣬻ᴥ���������������namespaceURI��ʾԪ�ص������ռ䣻
		// localName��ʾԪ�صı������ƣ�����ǰ׺����qName��ʾԪ�ص��޶�������ǰ׺����atts ��ʾԪ�ص����Լ���
		@Override
		public void startElement(String namespaceURI, String localName,
				String qName, Attributes atts) throws SAXException {

			if (localName.equals("upateNode")) {
				currentUpdateNode = new UpdateNode();
				currentUpdateNode.setName(atts.getValue("name"));
			}

			this.tagName = localName;
		}

		// �����ַ����ݵ�֪ͨ���÷�������������XML�ļ��ж��������ݣ���һ���������ڴ���ļ������ݣ�
		// �������������Ƕ������ַ�������������е���ʼλ�úͳ��ȣ�ʹ��new String(ch,start,length)�Ϳ��Ի�ȡ���ݡ�
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {

			if (tagName != null) {
				String data = new String(ch, start, length).trim();
				if (tagName.equals("version")) {
					this.currentUpdateNode.setVersion(data);
				} else if (tagName.equals("content")) {
					this.currentUpdateNode.setContent(data);
				}else if (tagName.equals("download_url")) {
					this.currentUpdateNode.setDownload_url(data);
				}else if (tagName.equals("updatetime")) {
					this.currentUpdateNode.setUpdatetime(data);
				}else if (tagName.equals("hotelcity")) {
					this.currentUpdateNode.setHotelcity(Integer.valueOf(data));
				}else if (tagName.equals("flightcity")) {
					this.currentUpdateNode.setFlightcity(Integer.valueOf(data));
				}else if (tagName.equals("iflightcity")) {
					this.currentUpdateNode.setIflightcity(Integer.valueOf(data));
				}else if (tagName.equals("traincity")) {
					this.currentUpdateNode.setTraincity(Integer.valueOf(data));
				}else if (tagName.equals("versionCode")) {
					this.currentUpdateNode.setVersionCode(Integer.valueOf(data));
				}
			}
		}

		// �����ĵ��Ľ�β��֪ͨ��������������ǩ��ʱ�򣬵���������������У�uri��ʾԪ�ص������ռ䣻
		// localName��ʾԪ�صı������ƣ�����ǰ׺����name��ʾԪ�ص��޶�������ǰ׺��
		@Override
		public void endElement(String uri, String localName, String name)
				throws SAXException {

			if (localName.equals("upateNode")) {
				upateNodes.add(currentUpdateNode);
				currentUpdateNode = null;
			}
			this.tagName = null;
		}
	}