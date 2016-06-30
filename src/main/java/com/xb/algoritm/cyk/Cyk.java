package com.xb.algoritm.cyk;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xb.bean.syntax.CykModel;
import com.xb.bean.syntax.SyntaxIndexesLink;
import com.xb.bean.syntax.SyntaxProbLink;
import com.xb.bean.trie.SyntaxTrieNode;
import com.xb.business.trie.impl.SyntaxTrieDictionary;
import com.xb.utils.SyntaxTreeUtils;

/**
 * Created by kevin on 2016/3/24.
 */
public class Cyk {
	private static Logger LOGGER = LoggerFactory.getLogger(Cyk.class);

	/**
	 * cyk算法剖析
	 *
	 * @param sentence
	 * @param stdict
	 */
	public static CykModel cykParsing(String sentence, SyntaxTrieDictionary stdict, SyntaxTrieNode r) {
		String[] words = sentence.split(" ");
		int wordsLen = words.length;
		String[] word = new String[wordsLen];
		String[] attr = new String[wordsLen];
		String[] wordAndAttr = null;
		for (int i = 0; i < wordsLen; i++) {
			wordAndAttr = words[i].split("\\|");
			word[i] = wordAndAttr[0];
			attr[i] = wordAndAttr[1];
		}

		CykModel cykModel = new CykModel(wordsLen);

		SyntaxProbLink syntaxProbLink = null;
		SyntaxIndexesLink syntaxIndexesLink = null;
		List<SyntaxProbLink> probLinkArrayTemp = null;
		List<SyntaxIndexesLink> indexLinkArrayTemp = null;
		SyntaxProbLink syntaxProbLinkTemp = null;
		SyntaxIndexesLink syntaxIndexesLinkTemp = null;
		double prob = 0.0;

		//初始化第一列
		for (int i = 0; i < wordsLen; i++) {
			probLinkArrayTemp = cykModel.getProbLinkArray()[i][0];
			indexLinkArrayTemp = cykModel.getIndexLinkArray()[i][0];
			probLinkArrayTemp.add(new SyntaxProbLink(attr[i], 1));
			indexLinkArrayTemp.add(new SyntaxIndexesLink(attr[i], 0, word[i], word[i], ""));
			for (int j = 0; j < probLinkArrayTemp.size(); j++) {
				syntaxProbLinkTemp = probLinkArrayTemp.get(j);
				SyntaxTrieNode sytaxTrieNode = stdict.search(r, new String[] { syntaxProbLinkTemp.getPhrase() });
				if (sytaxTrieNode != null) {
					prob = sytaxTrieNode.getProb() * syntaxProbLinkTemp.getProb();
					syntaxProbLink = new SyntaxProbLink(sytaxTrieNode.getParent().getKey(), prob);
					probLinkArrayTemp.add(syntaxProbLink);

					syntaxIndexesLink = new SyntaxIndexesLink(sytaxTrieNode.getParent().getKey(), 1, "",
							syntaxProbLinkTemp.getPhrase(), "");
					indexLinkArrayTemp.add(syntaxIndexesLink);
				}
			}
		}

		List<SyntaxProbLink> elem = null;
		List<SyntaxProbLink> elem1 = null;
		List<SyntaxProbLink> elem2 = null;
		List<SyntaxProbLink> elem3 = null;
		List<SyntaxIndexesLink> elem4 = null;
		for (int j = 2; j <= wordsLen; j++) {
			for (int i = 1; i <= wordsLen - j + 1; i++) {
				for (int k = 1; k <= j - 1; k++) {
					elem1 = cykModel.getProbLinkArray()[i - 1][k - 1];
					elem2 = cykModel.getProbLinkArray()[i + k - 1][j - k - 1];
					for (int l = 0; l < elem1.size(); l++) {
						for (int m = 0; m < elem2.size(); m++) {
							SyntaxTrieNode sytaxTrieNode = stdict.search(r, new String[] { elem1.get(l).getPhrase(),
									elem2.get(m).getPhrase() });
							if (sytaxTrieNode != null) {
								boolean flag = true;
								prob = sytaxTrieNode.getProb() * elem1.get(l).getProb() * elem2.get(m).getProb();
								syntaxProbLinkTemp = new SyntaxProbLink(sytaxTrieNode.getParent().getParent().getKey(),
										prob);
								syntaxIndexesLinkTemp = new SyntaxIndexesLink(sytaxTrieNode.getParent().getParent()
										.getKey(), k, "", sytaxTrieNode.getParent().getKey(), sytaxTrieNode.getKey());

								elem = cykModel.getProbLinkArray()[i - 1][j - 1];
								elem4 = cykModel.getIndexLinkArray()[i - 1][j - 1];
								for (int n = 0; n < elem.size(); n++) {
									if (elem.get(n).getPhrase().equals(sytaxTrieNode.getParent().getParent().getKey())) {
										flag = false;
										if (elem.get(n).getProb() < prob) {
											elem.remove(n);
											elem4.remove(n);

											elem.add(syntaxProbLinkTemp);
											elem4.add(syntaxIndexesLinkTemp);
											break;
										}
									}
								}
								if (flag) {
									elem.add(syntaxProbLinkTemp);
									elem4.add(syntaxIndexesLinkTemp);
								}
							}
						}
					}
				}

				elem3 = cykModel.getProbLinkArray()[i - 1][j - 1];
				elem4 = cykModel.getIndexLinkArray()[i - 1][j - 1];
				for (int k = 0; k < elem3.size(); k++) {
					SyntaxTrieNode sytaxTrieNode = stdict.search(r, new String[] { elem3.get(k).getPhrase() });
					if (sytaxTrieNode != null) {
						boolean flag = true;
						prob = sytaxTrieNode.getProb() * elem3.get(0).getProb();
						syntaxProbLinkTemp = new SyntaxProbLink(sytaxTrieNode.getParent().getKey(), prob);
						syntaxIndexesLinkTemp = new SyntaxIndexesLink(sytaxTrieNode.getParent().getKey(), j, "",
								sytaxTrieNode.getKey(), "");
						for (int l = 0; l < elem3.size(); l++) {
							if (elem3.get(l).getPhrase().equals(sytaxTrieNode.getParent().getParent().getKey())) {
								flag = false;
								if (elem3.get(l).getProb() < prob) {
									elem3.remove(l);
									elem4.remove(l);

									elem3.add(syntaxProbLinkTemp);
									elem4.add(syntaxIndexesLinkTemp);
									break;
								}
							}
						}
						if (flag) {
							elem3.add(syntaxProbLinkTemp);
							elem4.add(syntaxIndexesLinkTemp);
						}
					}
				}
			}
		}

		return cykModel;
	}

	/**
	 * 输出结果
	 */
	public static void printResult(CykModel cykModel) {
		boolean flag = true;
		int size = cykModel.getIndexLinkArray().length;
		//for (int i = 0; i < size; i++) {
		List<SyntaxProbLink> probLinkList = cykModel.getProbLinkArray()[0][size - 1];
		List<SyntaxIndexesLink> indexLinkList = cykModel.getIndexLinkArray()[0][size - 1];
		StringBuilder sb = null;
		for (int j = 0; j < indexLinkList.size(); j++) {
			sb = new StringBuilder();
			if (indexLinkList.get(j).getPhrase().equals("S")) {
				flag = false;

				double prob = probLinkList.get(j).getProb();
				System.out.println(prob);
				String s = SyntaxTreeUtils.getSyntaxRules(sb, 1, size, indexLinkList.get(j), cykModel);
				System.out.println();
				LOGGER.info(s);
			}
		}
		LOGGER.info(sb.toString());
		//}
		if (flag) {
			System.out.println("该语句不能剖析");
		}
	}
}
