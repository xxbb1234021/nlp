package com.xb.utils;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.slf4j.LoggerFactory;

public class FileNIOUtil {
	private static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FileNIOUtil.class);

	private static final int BUFFER_AREA = 2 * 1024;

	public static void writeFile(String path, String context, String charCode) {
		byte[] b = null;
		FileOutputStream fos = null;
		FileChannel fc = null;
		File file = null;
		try {
			Enumeration<URL> ps = FileNIOUtil.class.getClassLoader().getResources(path);
			while (ps.hasMoreElements()) {
				URL url = ps.nextElement();
				LOGGER.info("类路径资源URL：" + url);
				b = context.getBytes(charCode);
				file = new File(url.getFile());
				fos = new FileOutputStream(file.getAbsoluteFile(), true);
				fc = fos.getChannel();
				ByteBuffer bb = ByteBuffer.allocate(b.length);
				bb.put(b);
				bb.flip();
				fc.write(bb);
				fc.close();
				fos.close();
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	public static List<String> dirList(String path) {
		List<String> fileList = new ArrayList<String>();
		try {
			Enumeration<URL> ps = FileNIOUtil.class.getClassLoader().getResources(path);
			while (ps.hasMoreElements()) {
				URL url = ps.nextElement();
				LOGGER.info("类路径资源URL：" + url);
				File file = new File(url.getFile());
				Path dir = Paths.get(file.getAbsolutePath());
				DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
				for (Path filePath : stream) {
					if (Files.isDirectory(filePath)) {
						LOGGER.info("Directory:" + filePath.getFileName());
						dirList(filePath.toString());
					} else {
						LOGGER.info("File:" + filePath.getFileName());
						fileList.add(path + "/" + filePath.getFileName());
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		}
		return fileList;
	}

	public static String readFile(String path, String charCode) {
		//		Charset charset = Charset.forName(charCode);
		//		CharsetDecoder decoder = charset.newDecoder();
		//		int bytes = 0;
		//		FileInputStream fis = null;
		//		FileChannel fileChannel = null;
		//		ByteBuffer byteBuffer = null;
		//		CharBuffer charBuffer = null;
		//
		//		StringBuilder sb = new StringBuilder();
		//		try {
		//			Enumeration<URL> ps = FileNIOUtil.class.getClassLoader().getResources(path);
		//			while (ps.hasMoreElements()) {
		//				URL url = ps.nextElement();
		//				LOGGER.info("类路径资源URL：" + url);
		//
		//				fis = new FileInputStream(URLDecoder.decode (url.getFile(),charCode));
		//				fileChannel = fis.getChannel();
		//				byteBuffer = ByteBuffer.allocate(BUFFER_AREA);
		//				charBuffer = CharBuffer.allocate(BUFFER_AREA);
		//				bytes = fileChannel.read(byteBuffer);
		//
		//				while (bytes != -1) {
		//					byteBuffer.flip();
		//					decoder.decode(byteBuffer, charBuffer, false);
		//					charBuffer.flip();
		//
		//					sb.append(charBuffer);
		//					charBuffer.clear();
		//					byteBuffer.clear();
		//					bytes = fileChannel.read(byteBuffer);
		//				}
		//				if (fis != null) {
		//					fis.close();
		//				}
		//			}
		//		} catch (Exception e) {
		//			LOGGER.error("", e);
		//		}

		StringBuilder sb = new StringBuilder();
		try {
			Enumeration<URL> ps = FileNIOUtil.class.getClassLoader().getResources(path);
			while (ps.hasMoreElements()) {
				URL url = ps.nextElement();
				LOGGER.info("类路径资源URL：" + url);
				File file = new File(url.getFile());
				List<String> lines = Files.readAllLines(Paths.get(URLDecoder.decode (file.getAbsolutePath(),charCode)), Charset.forName(charCode));
				for (int i = 0; i < lines.size(); i++) {
					sb.append(lines.get(i));
				}
			}
		} catch (IOException e) {
			LOGGER.error("", e);
		}
		return sb.toString();
	}

	public static List<String> readFileLine(String path, String charCode) {
		try {
			Enumeration<URL> ps = FileNIOUtil.class.getClassLoader().getResources(path);
			while (ps.hasMoreElements()) {
				URL url = ps.nextElement();
				LOGGER.info("类路径资源URL：" + url);
				File file = new File(url.getFile());
				return Files.readAllLines(Paths.get(URLDecoder.decode (file.getAbsolutePath(),charCode)), Charset.forName(charCode));
			}
		} catch (IOException e) {
			LOGGER.error("", e);
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		writeFile("d:\\DEMO3.JSON", "fjdskfjsl发打发打发士大夫", "utf-8");

		//dirList("article");

		/*
		String s = readFile("D:\\workspace\\nlp\\src\\main\\resources\\pinyin_data\\hmm_start.json");

		JSONObject jsonObject = JSONObject.parseObject(s);

		JSONObject jsonObjectData = jsonObject.getJSONObject("data");

		System.out.println(jsonObjectData.size());
		for (java.util.Map.Entry<String, Object> entry : jsonObjectData.entrySet()) {
			System.out.print(entry.getKey() + "-" + entry.getValue() + "\t");
		}
		System.out.println();
		s = FileNIOUtil.readFile("D:\\workspace\\nlp\\src\\main\\resources\\pinyin_data\\hmm_py2hz.json");
		JSONObject pinyinJsonObject = JSONObject.parseObject(s);
		System.out.println(pinyinJsonObject.size());
		for (java.util.Map.Entry<String, Object> entry : pinyinJsonObject.entrySet()) {
			System.out.print(entry.getKey() + "-" + entry.getValue() + "\t");
		}

		System.out.println();
		s = FileNIOUtil.readFile("D:\\workspace\\nlp\\src\\main\\resources\\pinyin_data\\hmm_transition.json");
		JSONObject transitionJsonObject = JSONObject.parseObject(s);
		JSONObject transitionJsonObjectData = transitionJsonObject.getJSONObject("data");
		System.out.println(transitionJsonObjectData.size());
		//		for (java.util.Map.Entry<String, Object> entry : transitionJsonObjectData.entrySet()) {
		//			System.out.print(entry.getKey() + "-" + entry.getValue() + "\t");
		//		}
		System.out.println(transitionJsonObjectData.getString("打"));
		JSONObject lastJsonObject = transitionJsonObjectData.getJSONObject("打");
		Object o = lastJsonObject.get("你");
		if (o == null) {
			System.out.println("null");
		} else {
			System.out.println(o.toString());
		}
		System.out.println();

		s = FileNIOUtil.readFile("D:\\workspace\\nlp\\src\\main\\resources\\pinyin_data\\hmm_emission.json");
		JSONObject emissionJsonObject = JSONObject.parseObject(s);
		JSONObject emissionJsonObjectData = emissionJsonObject.getJSONObject("data");
		JSONObject pyJsonObject = emissionJsonObjectData.getJSONObject("你");
		System.out.println();
		*/

	}
}