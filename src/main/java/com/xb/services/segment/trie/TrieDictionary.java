package com.xb.services.segment.trie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.xb.bean.trie.TrieNode;

public class TrieDictionary
{
	private static TrieDictionary dict = null;
	private TrieNode root = new TrieNode();

	private TrieDictionary(String fn)
	{
		importDict(fn);
	}

	public static TrieDictionary getInstance(String fileName)
	{
		if (dict == null)
		{
			synchronized (TrieDictionary.class)
			{
				if (dict == null)
				{
					dict = new TrieDictionary(fileName);
				}
			}
		}
		return dict;
	}

	private boolean importDict(String fileName)
	{
		try
		{
			//System.out.println(this.getClass().getResource("/").getPath() + fileName);
			File file = new File(this.getClass().getResource("/").getPath() + fileName);
			//InputStream is = getClass().getResourceAsStream(fileName);
			InputStream is = new FileInputStream(file);

			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String word = "";

			while ((word = br.readLine()) != null)
			{
				addWord(word);
			}
			br.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 添加新词
	 * @param word
	 */
	public void addWord(String word)
	{
		TrieNode node = this.root;
		for (int i = 0; i < word.length(); i++)
		{
			char c = word.charAt(i);
			TrieNode pNode = node.getChilds().get(Character.valueOf(c));
			if (pNode == null)
			{
				pNode = new TrieNode(c);
				node.getChilds().put(Character.valueOf(c), pNode);
			}
			node = pNode;
		}
		node.setBound(true);
	}

	public boolean contains(String word)
	{
		TrieNode node = this.root;

		int i = 0;
		for (i = 0; i < word.length(); i++)
		{
			char c = word.charAt(i);
			TrieNode pNode = node.getChilds().get(Character.valueOf(c));
			if (pNode == null)
				break;
			node = pNode;
		}

		return (i == word.length()) && (node.isBound());
	}

	public TrieNode getRoot()
	{
		return this.root;
	}
}