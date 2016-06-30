package com.xb.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.xb.bean.syntax.*;
import com.xb.bean.tree.TreeRoot;

/**
 * Created by kevin on 2016/6/30.
 */
public class SyntaxTreeUtils {
	private static Logger LOGGER = LoggerFactory.getLogger(SyntaxTreeUtils.class);

	public static String getResultTree(CykModel cykModel) {
		boolean flag = true;
		int size = cykModel.getIndexLinkArray().length;
		List<SyntaxProbLink> probLinkList = cykModel.getProbLinkArray()[0][size - 1];
		List<SyntaxIndexesLink> indexLinkList = cykModel.getIndexLinkArray()[0][size - 1];
		StringBuilder sb = null;
		double tempProb = 0.0;
		int index = 0;
		for (int j = 0; j < indexLinkList.size(); j++) {
			sb = new StringBuilder();
			if (indexLinkList.get(j).getPhrase().equals("S")) {
				flag = false;

				double prob = probLinkList.get(j).getProb();
				if (prob > tempProb) {
					index = j;
					tempProb = prob;
				}
			}
		}
		if (flag) {
			LOGGER.info("该语句不能剖析");
		}

		String s = getSyntaxRules(sb, 1, size, indexLinkList.get(index), cykModel);
		return getTreeRoot(s);
	}

	private static TreeRoot doGetTreeRoot(List<TreeRoot> treeRootList) {
		GenSytanxTree genFrontSytanxTree = new GenSytanxTree(treeRootList, 0).invoke();
		int index = genFrontSytanxTree.getIndex();
		TreeRoot frontLastTreeRoot = genFrontSytanxTree.getLastTreeRoot();

		List<TreeRoot> subList = treeRootList.subList(index, treeRootList.size());
		GenSytanxTree genBackSytanxTree = new GenSytanxTree(subList, 0).invoke();
		index = genBackSytanxTree.getIndex();
		TreeRoot backLastTreeRoot = genBackSytanxTree.getLastTreeRoot();
		List<TreeRoot> newSubList = subList.subList(index, subList.size());

		TreeRoot middleTreeRoot = newSubList.get(0);
		middleTreeRoot.getChildren().add(frontLastTreeRoot);
		middleTreeRoot.getChildren().add(backLastTreeRoot);

		TreeRoot root = new TreeRoot(newSubList.get(1).getName());
		List<TreeRoot> treeList = new ArrayList<TreeRoot>();
		treeList.add(middleTreeRoot);
		root.setChildren(treeList);
		return root;
	}

	private static class GenSytanxTree {
		private List<TreeRoot> treeRootList;
		private TreeRoot lastTreeRoot;
		private int index;

		public GenSytanxTree(List<TreeRoot> treeRootList, int index) {
			this.treeRootList = treeRootList;
			this.index = index;
		}

		public TreeRoot getLastTreeRoot() {
			return lastTreeRoot;
		}

		public int getIndex() {
			return index;
		}

		public GenSytanxTree invoke() {
			Stack<TreeRoot> treeRootStack = new Stack<TreeRoot>();
			for (int i = index, size = treeRootList.size(); i < size; i++) {
				TreeRoot element = treeRootList.get(i);
				if (element.getName().equals("division")) {
					index = i + 1;
					break;
				} else if (element.getChildren().size() > 0) {
					treeRootStack.push(element);
				} else if (element.getChildren().size() == 0) {
					for (int j = 0, stackSize = treeRootStack.size(); j < stackSize; j++) {
						element.getChildren().add(treeRootStack.get(j));
					}

					if (treeRootStack.size() > 1) {
						//Collections.reverse(element.getChildren());
					}

					if (lastTreeRoot != null) {
						element.getChildren().add(lastTreeRoot);
						Collections.reverse(element.getChildren());
					}

					if (treeRootStack.size() == 0) {
						element.getChildren().add(lastTreeRoot.getChildren().get(0));
						lastTreeRoot.getChildren().remove(0);
						Collections.reverse(element.getChildren());
					}

					treeRootStack.clear();

					lastTreeRoot = element;
				}
			}

			return this;
		}
	}

	public static String getTreeRoot(String s) {
		List<TreeRoot> treeList = new ArrayList<TreeRoot>();
		TreeRoot treeRoot = new TreeRoot();
		List<TreeRoot> treeRootList = new ArrayList<TreeRoot>();

		Stack<Character> characterStack = new Stack<Character>();
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != ')') {
				characterStack.push(Character.valueOf(s.charAt(i)));
			} else {
				TreeRoot tree = new TreeRoot();
				treeList = new ArrayList<TreeRoot>();
				String temp = "";
				Character lastPop = null;
				Character pop = null;
				int stackSize = characterStack.size();
				for (int j = stackSize; j > 0; j--) {
					pop = characterStack.pop();
					if (Character.valueOf(pop).equals('(')) {
						if (TextUtils.isChineseChar(lastPop)) {
							tree.setName(new StringBuilder(temp).reverse().toString());
							treeList.add(tree);
							temp = "";
							i++;
							continue;
						} else {
							break;
						}
					} else {
						if (!pop.equals('(')) {
							temp += pop;
							lastPop = pop;
						} else {
							continue;
						}
					}
				}

				if (!"".equals(temp)) {
					String reverse = new StringBuilder(temp).reverse().toString();
					treeRoot = new TreeRoot(reverse);
					treeRoot.setChildren(treeList);
					treeRootList.add(treeRoot);

					if (characterStack.size() == 5) {
						treeRoot = new TreeRoot("division");
						treeRootList.add(treeRoot);
					}
				}
			}
		}

		TreeRoot root = doGetTreeRoot(treeRootList);
		String jsonTeach = JSON.toJSONString(root);

		return jsonTeach;
	}

	/**
	 * 按句法规则查找句法
	 *
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
	 *
	 * @param sb
	 * @param x  横坐标
	 * @param y  纵坐标
	 * @param s  句法规则索引
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
}
