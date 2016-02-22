package com.xb.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 2016/1/23.
 */
public class MakeHmmSegmentCorpus {

	public List<String> readTrainData() {
		String line;
		BufferedReader br = null;
		List<String> textList = new ArrayList<String>();
		try {
			File file = new File("D:\\workspace\\nlp\\src\\main\\resources\\hmmseg\\msr_training.utf8");
			InputStream is = new FileInputStream(file);

			br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			while ((line = br.readLine()) != null) {
				textList.add(line.replaceAll("\\pP", " ").trim());
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return textList;
	}

	public List<String> segmentWord(List<String> textList) {
		List<String> list = new ArrayList<String>();
		for (String text : textList) {
			StringBuilder sb = new StringBuilder();
			String[] words = text.split(" ");
			for (int i = 0; i < words.length; i++) {
				String word = words[i].trim();
				int length = word.length();
				if (length < 1)
					continue;
				if (length == 1) {
					sb.append(word).append("/S").append(" ");
				} else {
					for (int j = 0; j < word.length(); j++) {
						if (j == 0) {
							sb.append(word.charAt(j)).append("/B").append(" ");

						} else if (j == word.length() - 1) {
							sb.append(word.charAt(j)).append("/E").append(" ");

						} else {
							sb.append(word.charAt(j)).append("/M").append(" ");
						}

					}
				}
			}
			list.add(sb.toString());
		}

		return list;
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

	public static void main(String[] args) {
		MakeHmmSegmentCorpus m = new MakeHmmSegmentCorpus();
		List<String> list = m.readTrainData();
		List<String> segmentWordList = m.segmentWord(list);
		m.writeCorpusToFile("D:\\workspace\\nlp\\src\\main\\resources\\hmmseg\\segment.utf8",
				segmentWordList.toArray(new String[segmentWordList.size()]));
	}
}
