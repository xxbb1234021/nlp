package com.xb.constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.xb.bean.syntax.SyntaxRules;

/**
 * Created by kevin on 2016/3/15.
 */
public class SyntaxConstant {
	public static final List<SyntaxRules> SYNTAX_RULES = initSyntaxRulesWithProb();

	/**
	 * 初始化语法规则，以语法规则中右边第一项为依据按字母从小到大顺序排列
	 * @return syntaxRulesList
	 */
	public static final List<SyntaxRules> initSyntaxRules() {
		List<SyntaxRules> syntaxRules = new ArrayList<SyntaxRules>();

		SyntaxRules c = new SyntaxRules("S -> NP VP");
		syntaxRules.add(c);
		c = new SyntaxRules("S -> VP");
		syntaxRules.add(c);
		c = new SyntaxRules("S -> NP");
		syntaxRules.add(c);
		c = new SyntaxRules("NP -> n n");//名词性短语
		syntaxRules.add(c);
		c = new SyntaxRules("NP -> vn n");
		syntaxRules.add(c);
		c = new SyntaxRules("NP -> n vn");
		syntaxRules.add(c);
		c = new SyntaxRules("NP -> a n");
		syntaxRules.add(c);
		c = new SyntaxRules("NP -> r n");
		syntaxRules.add(c);
		c = new SyntaxRules("NP -> m n");
		syntaxRules.add(c);
		c = new SyntaxRules("NP -> m q n");
		syntaxRules.add(c);
		c = new SyntaxRules("NP -> n u n");
		syntaxRules.add(c);
		c = new SyntaxRules("NP -> a u n");
		syntaxRules.add(c);
		c = new SyntaxRules("TP -> t t");//时间短语
		syntaxRules.add(c);
		c = new SyntaxRules("TP -> t t t");
		syntaxRules.add(c);
		c = new SyntaxRules("FP -> n f");//方位短语
		syntaxRules.add(c);
		c = new SyntaxRules("FP -> ns f");
		syntaxRules.add(c);
		c = new SyntaxRules("VP -> d v");//动词短语
		syntaxRules.add(c);
		c = new SyntaxRules("VP -> a v");
		syntaxRules.add(c);
		c = new SyntaxRules("VP -> PP v");
		syntaxRules.add(c);
		c = new SyntaxRules("VP -> ad v");
		syntaxRules.add(c);
		c = new SyntaxRules("VP -> v n");
		syntaxRules.add(c);
		c = new SyntaxRules("VP -> v NP");
		syntaxRules.add(c);
		c = new SyntaxRules("VP -> V n");
		syntaxRules.add(c);
		c = new SyntaxRules("VP -> V NP");
		syntaxRules.add(c);
		c = new SyntaxRules("VP -> v a");
		syntaxRules.add(c);
		c = new SyntaxRules("VP -> v v");
		syntaxRules.add(c);
		c = new SyntaxRules("AP -> a a");//形容词性短语
		syntaxRules.add(c);
		c = new SyntaxRules("AP -> d a");
		syntaxRules.add(c);
		c = new SyntaxRules("DP -> d d");//副词性短语
		syntaxRules.add(c);
		c = new SyntaxRules("PP -> p n");//介词短语
		syntaxRules.add(c);
		c = new SyntaxRules("PP -> p r");
		syntaxRules.add(c);
		c = new SyntaxRules("PP -> p VP");
		syntaxRules.add(c);
		c = new SyntaxRules("QP -> m q");//量词短语
		syntaxRules.add(c);

		Collections.sort(syntaxRules);

		return syntaxRules;
	}

	/**
	 * 初始化语法规则，以语法规则中右边第一项为依据按字母从小到大顺序排列
	 * @return syntaxRulesList
	 */
	public static final List<SyntaxRules> initSyntaxRulesWithProb() {
		List<SyntaxRules> syntaxRules = new ArrayList<SyntaxRules>();

		SyntaxRules c = new SyntaxRules("S -> NP VP", 0.7);
		syntaxRules.add(c);
		c = new SyntaxRules("S -> VP", 0.2);
		syntaxRules.add(c);
		c = new SyntaxRules("S -> NP", 0.1);
		syntaxRules.add(c);
		c = new SyntaxRules("NP -> noun", 0.3);
		syntaxRules.add(c);
		c = new SyntaxRules("NP -> adj noun", 0.2);
		syntaxRules.add(c);
		c = new SyntaxRules("NP -> DJ", 0.2);
		syntaxRules.add(c);
		c = new SyntaxRules("NP -> DJ NP", 0.3);
		syntaxRules.add(c);
		c = new SyntaxRules("DJ -> VP de", 0.4);
		syntaxRules.add(c);
		c = new SyntaxRules("DJ -> NP de", 0.6);
		syntaxRules.add(c);
		c = new SyntaxRules("VP -> VC NP", 1.0);
		syntaxRules.add(c);
		c = new SyntaxRules("VC -> vt adj", 0.3);
		syntaxRules.add(c);
		c = new SyntaxRules("VC -> VC utl", 0.5);
		syntaxRules.add(c);
		c = new SyntaxRules("VC -> vt", 0.2);
		syntaxRules.add(c);

		Collections.sort(syntaxRules);

		return syntaxRules;
	}

	public static void main(String args[]) {
		List<SyntaxRules> list = initSyntaxRulesWithProb();
		for (SyntaxRules c : list) {
			System.out.println(c.getSyntax() + " " + c.getProb());
		}
	}
}
