package com.org.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;


/**
 * @author Nano
 *
 * XML������
 */
public class XmlUtils {
	
	public static JSONObject getDocumentFromRequest(HttpServletRequest request) throws IOException {
		Document doc = XmlUtils.read(request.getInputStream());
		JSONObject xmlJson = XmlUtils.documentToJSON(doc);
		return xmlJson;
	}
	
	public static JSONObject documentToJSON(Document doc){
		JSONObject jsonObject = new JSONObject();
		if(doc != null){
			Element root = doc.getRootElement();
			List<?> childList = root.getChildren();
			Element e = null;
			for (int i = 0; i < childList.size(); i++) {
				e = (Element) childList.get(i);
				jsonObject.put(e.getName(), e.getText()); 
			}
		}
		return jsonObject;
	}

	/**
	 * @param xmlContent xml��ʽ���ַ���
	 */
	public static Document read(String xmlContent,String encode){
		ByteArrayInputStream in;
		try {
			in = new ByteArrayInputStream(xmlContent.getBytes(encode));
			return read(in);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	     return null;
	}
	
	/**
	 * @param xmlByte xml��ʽ�ֽ�
	 * 
	 */
	public static Document read(byte[] xmlByte){
		ByteArrayInputStream in = new ByteArrayInputStream(xmlByte);
		return read(in);
	}
	
	/**
	 * @param file xml�ļ�
	 */
	public static Document read(File file){
		try {
			FileInputStream in = new FileInputStream(file);
			return read(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param xmlDoc  Document
	 * @param encoding �ַ�������
	 * @return ת��Ϊxml�ַ���
	 */
	public static String toXmlContent(Document xmlDoc, String encoding) { 
	        ByteArrayOutputStream byteRep = new ByteArrayOutputStream(); 
	        Format format = Format.getPrettyFormat(); 
	        format.setEncoding(encoding); 
	        XMLOutputter docWriter = new XMLOutputter(format); 
	        try {
				docWriter.output(xmlDoc, byteRep);
			} catch (IOException e) {
				e.printStackTrace();
			} 
	        return byteRep.toString(); 
	 } 
	
	public static Document read(InputStream in){
		SAXBuilder sb = new SAXBuilder();
		Document doc = null;
		try {
			 doc = sb.build(in);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return doc;
	}
}
