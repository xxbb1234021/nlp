package com.xb.business.trie.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DoubleArrayTrieDictionary {
	private static DoubleArrayTrieDictionary dict = null;
	private List<String> words = new ArrayList<String>();
	private Set<Character> charset = new HashSet<Character>();

	private DoubleArrayTrieDictionary(String fn) {
		importDict(fn);
	}

	public static DoubleArrayTrieDictionary getInstance(String fileName) {
		if (dict == null) {
			synchronized (DoubleArrayTrieDictionary.class) {
				if (dict == null) {
					dict = new DoubleArrayTrieDictionary(fileName);
				}
			}
		}
		return dict;
	}

	private boolean importDict(String fileName) {
		try {
			//System.out.println(this.getClass().getResource("/").getPath() + fileName);
			File file = new File(this.getClass().getResource("/").getPath() + fileName);
			//InputStream is = getClass().getResourceAsStream(fileName);
			InputStream is = new FileInputStream(file);

			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String word = "";

			while ((word = br.readLine()) != null) {
				addWord(word);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 添加新词
	 * @param word
	 */
	public void addWord(String word) {
		words.add(word);
		for (char c : word.toCharArray()) {
			charset.add(c);
		}
	}

	public List<String> getWords() {
		return words;
	}

	public void setWords(List<String> words) {
		this.words = words;
	}

	public Set<Character> getCharset() {
		return charset;
	}

	public void setCharset(Set<Character> charset) {
		this.charset = charset;
	}

}