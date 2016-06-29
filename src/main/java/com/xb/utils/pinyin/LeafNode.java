package com.xb.utils.pinyin;

class LeafNode extends TrieNode {
	LeafNode(char k) {
		this.key = k;
		this.kind = NodeKind.LN;
	}
}