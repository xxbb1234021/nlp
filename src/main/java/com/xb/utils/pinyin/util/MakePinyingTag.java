package com.xb.utils.pinyin.util;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by kevin on 2016/1/19.
 */
public class MakePinyingTag {
	static List<String> list = new ArrayList<String>();
	Map<String, String> hanziToPinyingMap = new HashMap<String, String>();

	public void getPinyingInfo() {

	}

	public void readHanziToPinyingFile() {
		try {
			File file = new File("D:\\workspace\\nlp\\src\\main\\resources\\pinyin\\pinyin.utf8");
			InputStream is = new FileInputStream(file);

			BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			String words = "";

			String[] wordSplit = null;
			char[] hanzi = null;
			String hanzis = "";
			String pinying = "";
			while ((words = br.readLine()) != null) {
				wordSplit = words.trim().split(" ");
				hanzis = wordSplit[1];
				pinying = wordSplit[0];
				hanzi = hanzis.toCharArray();
				for (int i = 0; i < hanzi.length; i++) {
					if (StringUtils.isBlank(hanzi[i] + "")) {
						continue;
					}
					hanziToPinyingMap.put(hanzi[i] + "", pinying);
				}
			}
			//System.out.println(hanziToPinyingMap.get("来"));
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<String> readHanziToPinyingFile(String filename, String charset) {
		try {
			File file = new File(filename);
			InputStream is = new FileInputStream(file);

			BufferedReader br = new BufferedReader(new InputStreamReader(is, charset));
			String words = "";

			String[] wordSplit = null;
			char[] hanzi = null;
			String hanzis = "";
			String pinying = "";
			StringBuilder sb = null;
			while ((words = br.readLine()) != null) {
				sb = new StringBuilder();
				if (StringUtils.isBlank(words)) {
					continue;
				}
				hanzi = words.trim().toCharArray();
				for (int i = 0; i < hanzi.length; i++) {
					if (StringUtils.isBlank(hanzi[i] + "")) {
						continue;
					}
					if (hanziToPinyingMap.get(hanzi[i] + "") == null) {
						continue;
					}
					sb.append(hanziToPinyingMap.get(hanzi[i] + "")).append("/").append(hanzi[i]).append(" ");
				}
				list.add(sb.toString());
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}

	public void readCatalogFile(String filepath) {

		try {
			File file = new File(filepath);
			if (!file.isDirectory()) {
				System.out.println("文件");
				System.out.println("path=" + file.getPath());
				System.out.println("absolutepath=" + file.getAbsolutePath());
				System.out.println("name=" + file.getName());
			} else if (file.isDirectory()) {
				System.out.println("文件夹");
				String[] filelist = file.list();
				for (int i = 0; i < filelist.length; i++) {
					File readfile = new File(filepath + "\\" + filelist[i]);
					if (!readfile.isDirectory()) {
						System.out.println("path=" + readfile.getPath());
						System.out.println("absolutepath=" + readfile.getAbsolutePath());
						System.out.println("name=" + readfile.getName());
						readHanziToPinyingFile(readfile.getAbsolutePath(), "GBK");
					} else if (readfile.isDirectory()) {
						readCatalogFile(filepath + "\\" + filelist[i]);
					}
				}

			}

		} catch (Exception e) {
			System.out.println("readfile()   Exception:" + e.getMessage());
		}
	}

	public void writeCorpusToFile(String file, String[] example) {
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(file));
			for (String string : example) {
				writer.println(string);
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] arg) {
		MakePinyingTag m = new MakePinyingTag();
		m.readHanziToPinyingFile();
		//String filepath = "D:\\workspace\\nlp\\src\\main\\resources\\pinying\\C000020";
		//m.readCatalogFile(filepath);
		String filename = "D:\\workspace\\nlp\\src\\main\\resources\\pinyin\\sentence.txt";
		m.readHanziToPinyingFile(filename, "utf-8");
		m.writeCorpusToFile("D:\\workspace\\nlp\\src\\main\\resources\\pinyin\\sentence_new.txt", list.toArray(new String[list.size()]));
	}
}
