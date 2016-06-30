package com.xb.business.hmm.builderImpl;

import org.junit.Test;

import com.xb.algoritm.cyk.Cyk;
import com.xb.bean.syntax.CykModel;
import com.xb.bean.trie.SyntaxTrieNode;
import com.xb.business.trie.TrieDictionaryContext;
import com.xb.business.trie.impl.SyntaxTrieDictionary;
import com.xb.constant.Constant;
import com.xb.utils.SyntaxTreeUtils;

/**
 * Created by kevin on 2016/3/24.
 */
public class TestCYK {

	@Test
	public void testCYK() {
		//加载词典
		String dictionaryName = Constant.PCFG_DATA;
		SyntaxTrieDictionary dict = SyntaxTrieDictionary.getInstance(dictionaryName);
		//
		//		SyntaxTrieNode root = dict.getNodeRoot();

		TrieDictionaryContext context = new TrieDictionaryContext(dict);
		SyntaxTrieNode r = (SyntaxTrieNode) context.getRoot();
		//List<SyntaxTrieNode> currentNode = dict.search(root, new String[]{"d", "v", "d", "a"});
		//SyntaxTrieNode currentNode = dict.search(root, new String[]{"r"});
		System.out.println();

		//String s = "咬|v 死|v 猎人|m 的|u 狗|n";
		String s = "咬|vt 死|adj 了|utl 猎人|noun 的|de 狗|noun";
		CykModel cykModel = Cyk.cykParsing(s, dict, r);
		SyntaxTreeUtils.getResultTree(cykModel);

		//		CykBaseModelBuilder c = CykModelBuilder.getInstance(Constant.PCFG_DATA);
		//
		//		CykModelBuilder builder = CykModelBuilder.getInstance(Constant.WORD_TAG_TRAINDATA);
		//		Director director = new Director(builder);
		//		director.constructCykModel();
		//
		//		ChomskModel cm = new ChomskModel();
		//		cm.setIndexList(builder.getIndexList());
		//		cm.setIndexMap(builder.getIndexMap());
		//		cm.setSyntaxRulesList(builder.getSyntaxRulesList());

		/*
		for (Map.Entry<String, Integer> entry : cm.getIndexMap().entrySet()) {
			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}

		System.out.println(Cyk.getMatchingChar("w", "", cm));
		*/
		//		String s = "我们|r 即将|d 以|p 昂扬|v 的|u 斗志|n 迎来|v 新|a 的|u 一年|m";
		//		Cyk.cykParsing(s, cm);
	}

}
