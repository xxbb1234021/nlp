package com.xb.algoritm.cyk;

import java.util.List;

import com.xb.bean.syntax.*;
import com.xb.bean.trie.SyntaxTrieNode;
import com.xb.business.trie.impl.SyntaxTrieDictionary;

/**
 * Created by kevin on 2016/3/24.
 */
public class Cyk {

	/**
	 * cyk算法剖析
	 * @param sentence
	 * @param stdict
	 */
	public static void cykParsing(String sentence, SyntaxTrieDictionary stdict, SyntaxTrieNode r) {
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
								//System.out.println();

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

		for (int i = 0; i < cykModel.getIndexLinkArray().length; i++) {
			for (int j = 0; j < cykModel.getIndexLinkArray()[i].length; j++) {
				for (int k = 0; k < cykModel.getIndexLinkArray()[i][j].size(); k++) {
					System.out.print(cykModel.getIndexLinkArray()[i][j].get(k).getPhrase() + " "
							+ cykModel.getIndexLinkArray()[i][j].get(k).getIndex() + " "
							+ cykModel.getIndexLinkArray()[i][j].get(k).getSecondPhrase() + " "
							+ cykModel.getIndexLinkArray()[i][j].get(k).getThirdPhrase() + "\t");

				}
				System.out.print("|||");
			}

			System.out.println();
		}

		System.out.println("============================================================");
		System.out.println("============================================================");
		System.out.println("============================================================");
		for (int i = 0; i < cykModel.getProbLinkArray().length; i++) {
			for (int j = 0; j < cykModel.getProbLinkArray()[i].length; j++) {
				for (int k = 0; k < cykModel.getProbLinkArray()[i][j].size(); k++) {
					System.out.print(cykModel.getProbLinkArray()[i][j].get(k).getPhrase() + " "
							+ cykModel.getProbLinkArray()[i][j].get(k).getProb() + "\t");

				}
				System.out.print("|||");
			}

			System.out.println();
		}

		printResult(cykModel);
	}

	/**
	 * 按句法规则查找句法
	 * @param x 横坐标
	 * @param y 纵坐标
	 * @param s 句法规则索引
	 * @return
	 */
	public static SytaxSubInfo findCoup(int x, int y, SyntaxIndexesLink s, CykModel cykModel) {
		int z = s.getIndex();
		SytaxSubInfo info = null;
		SyntaxIndexesLink syntaxIndexesLink = null;
		List<SyntaxIndexesLink> syntaxIndexesLinkList = null;
		if (z == y) {
			syntaxIndexesLinkList = cykModel.getIndexLinkArray()[x - 1][y - 1];
			for (int i = 0; i < syntaxIndexesLinkList.size(); i++) {
				syntaxIndexesLink = syntaxIndexesLinkList.get(i);
				if (syntaxIndexesLink.getPhrase().equals(s.getSecondPhrase())) {
					info = new SytaxSubInfo();
					info.getSyntaxIndexesLinkList().add(syntaxIndexesLink);
					info.getPointList().add(new XYPoint(x, y));
				}
			}
		} else {
			syntaxIndexesLinkList = cykModel.getIndexLinkArray()[x - 1][z - 1];
			info = new SytaxSubInfo();
			for (int i = 0; i < syntaxIndexesLinkList.size(); i++) {
				syntaxIndexesLink = syntaxIndexesLinkList.get(i);
				if (syntaxIndexesLink.getPhrase().equals(s.getSecondPhrase())) {
					info.getSyntaxIndexesLinkList().add(syntaxIndexesLink);
				}
			}

			syntaxIndexesLinkList = cykModel.getIndexLinkArray()[x + z - 1][y - z - 1];
			for (int i = 0; i < syntaxIndexesLinkList.size(); i++) {
				syntaxIndexesLink = syntaxIndexesLinkList.get(i);
				if (syntaxIndexesLink.getPhrase().equals(s.getThirdPhrase())) {
					info.getSyntaxIndexesLinkList().add(syntaxIndexesLink);
				}
			}

			info.getPointList().add(new XYPoint(x, z));
			info.getPointList().add(new XYPoint(x + z, y - z));
		}

		return info;
	}

	/**
	 * 获得句法规则
	 * @param sb
	 * @param x 横坐标
	 * @param y 纵坐标
	 * @param s 句法规则索引
	 * @return
	 */
	public static String getSyntaxRules(StringBuilder sb, int x, int y, SyntaxIndexesLink s, CykModel cykModel) {
		System.out.printf("(%s", s.getPhrase());
		sb.append("(").append(s.getPhrase());
		if (s.getIndex() == 0) {
			System.out.printf("(%s)", s.getSecondPhrase());
			sb.append("(").append(s.getSecondPhrase()).append(")");
		} else {
			SytaxSubInfo info = findCoup(x, y, s, cykModel);
			int size = info.getSyntaxIndexesLinkList().size();
			List<XYPoint> pointList = info.getPointList();
			if (size == 1) {
				getSyntaxRules(sb, pointList.get(0).getX(), pointList.get(0).getY(), info.getSyntaxIndexesLinkList()
						.get(0), cykModel);
			} else {
				getSyntaxRules(sb, pointList.get(0).getX(), pointList.get(0).getY(), info.getSyntaxIndexesLinkList()
						.get(0), cykModel);
				getSyntaxRules(sb, pointList.get(1).getX(), pointList.get(1).getY(), info.getSyntaxIndexesLinkList()
						.get(1), cykModel);
			}
		}
		System.out.print(")");
		sb.append(")");

		return sb.toString();
	}

	/**
	 * 输出结果
	 */
	public static void printResult(CykModel cykModel) {
		boolean flag = true;
		int size = cykModel.getIndexLinkArray().length;
		//for (int i = 0; i < size; i++) {
			List<SyntaxProbLink> probLinkList = cykModel.getProbLinkArray()[0][size-1];
			List<SyntaxIndexesLink> indexLinkList = cykModel.getIndexLinkArray()[0][size-1];
			StringBuilder sb = null;
			for (int j = 0; j < indexLinkList.size(); j++) {
				sb = new StringBuilder();
				if (indexLinkList.get(j).getPhrase().equals("S")) {
					flag = false;

					double prob = probLinkList.get(j).getProb();
					System.out.println(prob);
					String s = getSyntaxRules(sb, 1, size, indexLinkList.get(j), cykModel);
					System.out.println();
					//System.out.println(s);
				}
			}
			//System.out.println(sb.toString());
		//}
		if (flag) {
			System.out.println("该语句不能剖析");
		}
	}
}
