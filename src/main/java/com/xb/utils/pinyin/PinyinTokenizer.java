package com.xb.utils.pinyin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class PinyinTokenizer {
	StandardTree trie = new StandardTree();

	public PinyinTokenizer(String filename) {
		initPY(filename);
	}

	public String[] tokenize(String pinyin) {
		String[] array = pinyin.split("[\\s\\pP~]+");
		List forwordTokenizeRes = new ArrayList();
		List backwordTokenizeRes = new ArrayList();

		for (String token : array) {
			List tmpForwordTokenizeRes = getPathMax(token, this.trie);
			List tmpBackwordTokenizeRes = getPathopposite(token, this.trie);
			forwordTokenizeRes.addAll(tmpForwordTokenizeRes);
			backwordTokenizeRes.addAll(tmpBackwordTokenizeRes);
		}

		if (forwordTokenizeRes.size() > backwordTokenizeRes.size()) {
			return (String[]) backwordTokenizeRes.toArray(new String[0]);
		}
		return (String[]) forwordTokenizeRes.toArray(new String[0]);
	}

	private void initPY(String filename) {
		try {
			BufferedReader reader = getReader(filename, "UTF-8");
			String temp = null;
			while ((temp = reader.readLine()) != null) {
				temp = temp.trim();
				this.trie.insert(temp);
			}
			reader.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private List<String> getPathMax(String a, StandardTree trie) {
		List result = new ArrayList();
		for (int i = 0; i < a.length(); i++) {
			int step = 8;
			if (a.length() - i < 8)
				;
			for (step = a.length() - i; step > 0; step--) {
				String pyword = a.substring(i, i + step);
				if (trie.fullMatch(pyword)) {
					result.add(pyword);
					i = i + step - 1;
					break;
				}
			}
		}

		return result;
	}

	private List<String> getPathopposite(String a, StandardTree trie) {
		List resultOpp = new ArrayList();
		List result = new ArrayList();
		for (int i = a.length(); i > 0; i--) {
			int step = 8;
			if (i < 8)
				;
			for (step = i; step > 0; step--) {
				String pyword = a.substring(i - step, i);
				if (trie.fullMatch(pyword)) {
					result.add(pyword);
					i = i - step + 1;
					break;
				}
			}
		}

		for (int i = result.size() - 1; i >= 0; i--) {
			resultOpp.add(result.get(i));
		}

		return resultOpp;
	}

	private BufferedReader getReader(String haspath, String charEncoding) {
		BufferedReader br = null;
		File f = new File(haspath);
		if (f.isFile()) {
			try {
				try {
					br = new BufferedReader(new InputStreamReader(new FileInputStream(f), charEncoding));
				} catch (UnsupportedEncodingException ex) {
					ex.printStackTrace();
				}
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			}
		}
		if (br != null) {
			return br;
		}
		return null;
	}
}