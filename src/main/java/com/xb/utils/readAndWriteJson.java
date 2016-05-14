package com.xb.utils;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Enumeration;

import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class ReadAndWriteJson {
	private static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ReadAndWriteJson.class);

	public static void writeFile(String filePath, String sets) throws IOException {
		FileWriter fw = new FileWriter(filePath);
		PrintWriter out = new PrintWriter(fw);
		out.write(sets);
		out.println();
		fw.close();
		out.close();
	}

	public static String readFile(String file) {
		StringBuilder sb = new StringBuilder();
		try {
			Enumeration<URL> ps = ReadAndWriteJson.class.getClassLoader().getResources(file);
			while (ps.hasMoreElements()) {
				URL url = ps.nextElement();
				LOGGER.info("类路径资源URL：" + url);

				FileInputStream fis = new FileInputStream(url.getFile());
				FileChannel fc = fis.getChannel();
				ByteBuffer bf = ByteBuffer.allocate((int) fc.size());
				fc.read(bf);
				bf.rewind();
				while (bf.hasRemaining()) {
					sb.append((char) bf.get());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static void main(String[] args) throws Exception {
		String s = readFile("D:\\workspace\\nlp\\src\\main\\resources\\pinyin_data\\hmm_start.json");

		JSONObject jsonObject = JSONObject.parseObject(s);

		JSONObject jsonObjectData = jsonObject.getJSONObject("data");

		System.out.println(jsonObjectData.size());
		for (java.util.Map.Entry<String, Object> entry : jsonObjectData.entrySet()) {
			System.out.print(entry.getKey() + "-" + entry.getValue() + "\t");
		}
		System.out.println();
		s = ReadAndWriteJson.readFile("D:\\workspace\\nlp\\src\\main\\resources\\pinyin_data\\hmm_py2hz.json");
		JSONObject pinyinJsonObject = JSONObject.parseObject(s);
		System.out.println(pinyinJsonObject.size());
		for (java.util.Map.Entry<String, Object> entry : pinyinJsonObject.entrySet()) {
			System.out.print(entry.getKey() + "-" + entry.getValue() + "\t");
		}

		System.out.println();
		s = ReadAndWriteJson.readFile("D:\\workspace\\nlp\\src\\main\\resources\\pinyin_data\\hmm_transition.json");
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

		s = ReadAndWriteJson.readFile("D:\\workspace\\nlp\\src\\main\\resources\\pinyin_data\\hmm_emission.json");
		JSONObject emissionJsonObject = JSONObject.parseObject(s);
		JSONObject emissionJsonObjectData = emissionJsonObject.getJSONObject("data");
		JSONObject pyJsonObject = emissionJsonObjectData.getJSONObject("你");
		System.out.println();
	}
}