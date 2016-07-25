package com.xb.business.trie;

import com.xb.bean.trie.TrieNode;

public class TrieDictionaryContext {

	private TrieDictionary trieDictionary;

	public TrieDictionaryContext(TrieDictionary trieDictionary) {
		this.trieDictionary = trieDictionary;
	}

	public TrieNode getRoot() {
		return this.trieDictionary.getNodeRoot();
	}

	public TrieNode getReverseRoot() {
		return this.trieDictionary.getReverseNodeRoot();
	}

    public TrieDictionary getTrieDictionary(){
        return this.trieDictionary;
    }
}