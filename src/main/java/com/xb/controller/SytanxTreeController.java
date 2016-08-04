package com.xb.controller;

import com.xb.algoritm.cyk.Cyk;
import com.xb.bean.syntax.CykModel;
import com.xb.bean.trie.SyntaxTrieNode;
import com.xb.business.trie.TrieDictionaryContext;
import com.xb.business.trie.impl.SyntaxTrieDictionary;
import com.xb.constant.FileConstant;
import com.xb.utils.SyntaxTreeUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kevin on 2016/6/15.
 */
@RestController
@EnableAutoConfiguration
public class SytanxTreeController {

	@RequestMapping("/genSytanxTree")
	public String genSytanxTree() {
        String dictionaryName = FileConstant.PCFG_DATA;
        SyntaxTrieDictionary dict = SyntaxTrieDictionary.getInstance(dictionaryName);

		TrieDictionaryContext context = new TrieDictionaryContext(dict);
		SyntaxTrieNode r = (SyntaxTrieNode)context.getRoot();

		String s = "咬|vt 死|adj 了|utl 猎人|noun 的|de 狗|noun";
		CykModel cykModel = Cyk.cykParsing(s, dict, r);
		return SyntaxTreeUtils.getResultTree(cykModel);
	}
}
