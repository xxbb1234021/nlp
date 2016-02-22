package com.xb.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kevin on 2016/1/13.
 */
public class WordTaggingConstant {

	public static final Map<String, String> WORD_TAGGING_MAP = initWordTagMap();

	public static final Map<String, String> initWordTagMap() {
		Map<String, String> wordTagMap = new HashMap<String, String>();
		wordTagMap.put("a", "形容词");//最/d  大/a  的/u
		wordTagMap.put("ad", "副形词");//一定/d  能够/v  顺利/ad  实现/v  。/w
		wordTagMap.put("ag", "形容词性语素");//喜/v  煞/ag  人/n
		wordTagMap.put("al", "形容词性惯用语");
		wordTagMap.put("an", "名形词");//人民/n  的/u  根本/a  利益/n  和/c 国家/n  的/u  安稳/an  。/w
		wordTagMap.put("b", "区别词");//副/b  书记/n  王/nr  思齐/nr
		wordTagMap.put("begin", "仅用于终##终");
		wordTagMap.put("bg", "区别语素");
		wordTagMap.put("bl", "区别词性惯用语");
		wordTagMap.put("c", "连词");//全军/n  和/c  武警/n  先进/a  典型/n  代表/n
		wordTagMap.put("cc", "并列连词");
		wordTagMap.put("d", "副词");//两侧/f  台柱/n  上/f  分别/d  雄踞/v  着/u
		wordTagMap.put("dg", "副语素");//用/v  不/d  甚/dg  流利/a  的/u  中文/nz  主持/v  节目/n  。/w
		wordTagMap.put("dl", "连词");
		wordTagMap.put("e", "叹词");//嗬/e  ！/w
		wordTagMap.put("end", "仅用于始##始");
		wordTagMap.put("f", "方位词");//从/p  一/m  大/a  堆/q  档案/n  中/f  发现/v  了/u
		wordTagMap.put("g", "学术词汇");
		wordTagMap.put("gb", "生物相关词汇");
		wordTagMap.put("gbc", "生物类别");
		wordTagMap.put("gc", "化学相关词汇");
		wordTagMap.put("gg", "地理地质相关词汇");
		wordTagMap.put("gi", "计算机相关词汇");
		wordTagMap.put("gm", "数学相关词汇");
		wordTagMap.put("gp", "物理相关词汇");
		wordTagMap.put("h", "前缀");//目前/t  各种/r  非/h  合作制/n  的/u  农产品/n
		wordTagMap.put("i", "成语");//提高/v  农民/n  讨价还价/i  的/u  能力/n  。/w
		wordTagMap.put("j", "简称略语");//民主/ad  选举/v  村委会/j  的/u  工作/vn
		wordTagMap.put("k", "后缀");//权责/n  明确/a  的/u  逐级/d  授权/v  制/k
		wordTagMap.put("l", "习用语");//是/v  建立/v  社会主义/n  市场经济/n 体制/n  的/u  重要/a  组成部分/l  。/w
		wordTagMap.put("m", "数词");//科学技术/n  是/v  第一/m  生产力/n
		wordTagMap.put("mg", "数语素");
		wordTagMap.put("Mg", "甲乙丙丁之类的数词");
		wordTagMap.put("mq", "数量词");
		wordTagMap.put("n", "名词");//希望/v  双方/n  在/p  市政/n  规划/vn
		wordTagMap.put("nb", "生物名");
		wordTagMap.put("nba", "动物名");
		wordTagMap.put("nbc", "动物纲目");
		wordTagMap.put("nbp", "植物名");
		wordTagMap.put("nf", "食品，比如“薯片”");
		wordTagMap.put("ng", "名词性语素");//就此/d  分析/v  时/Ng  认为/v
		wordTagMap.put("nh", "医药疾病等健康相关名词");
		wordTagMap.put("nhd", "疾病");
		wordTagMap.put("nhm", "药品");
		wordTagMap.put("ni", "机构相关（不是独立机构名）");
		wordTagMap.put("nic", "下属机构");
		wordTagMap.put("nis", "机构后缀");
		wordTagMap.put("nit", "教育相关机构");
		wordTagMap.put("nl", "名词性惯用语");
		wordTagMap.put("nm", "物品名");
		wordTagMap.put("nmc", "化学品名");
		wordTagMap.put("nn", "工作相关名词");
		wordTagMap.put("nnd", "职业");
		wordTagMap.put("nnt", "职务职称");
		wordTagMap.put("nr", "人名");//建设部/nt  部长/n  侯/nr  捷/nr
		wordTagMap.put("nr1", "复姓");
		wordTagMap.put("nr2", "蒙古姓名");
		wordTagMap.put("nrf", "音译人名");
		wordTagMap.put("nrj", "日语人名");
		wordTagMap.put("ns", "地名");//北京/ns  经济/n  运行/vn  态势/n  喜人/a
		wordTagMap.put("nsf", "音译地名");
		wordTagMap.put("nt", "机构团体名");//[冶金/n  工业部/n  洛阳/ns  耐火材料/l  研究院/n]nt
		wordTagMap.put("ntc", "公司名");
		wordTagMap.put("ntcb", "银行");
		wordTagMap.put("ntcf", "工厂");
		wordTagMap.put("ntch", "酒店宾馆");
		wordTagMap.put("nth", "医院");
		wordTagMap.put("nto", "政府机构");
		wordTagMap.put("nts", "中小学");
		wordTagMap.put("ntu", "大学");
		wordTagMap.put("nx", "字母专名");//ＡＴＭ/nx  交换机/n
		wordTagMap.put("nz", "其他专名");//德士古/nz  公司/n
		wordTagMap.put("o", "拟声词");//汩汩/o  地/u  流/v  出来/v
		wordTagMap.put("p", "介词");//往/p  基层/n  跑/v  。/w
		wordTagMap.put("pba", "介词“把”");
		wordTagMap.put("pbei", "介词“被”");
		wordTagMap.put("q", "量词");//不止/v  一/m  次/q  地/u  听到/v  ，/w
		wordTagMap.put("qg", "量词语素");
		wordTagMap.put("qt", "时量词");
		wordTagMap.put("qv", "动量词");
		wordTagMap.put("r", "代词");//有些/r  部门/n
		wordTagMap.put("rg", "代词性语素");
		wordTagMap.put("Rg", "古汉语代词性语素");
		wordTagMap.put("rr", "人称代词");
		wordTagMap.put("ry", "疑问代词");
		wordTagMap.put("rys", "处所疑问代词");
		wordTagMap.put("ryt", "时间疑问代词");
		wordTagMap.put("ryv", "谓词性疑问代词");
		wordTagMap.put("rz", "指示代词");
		wordTagMap.put("rzs", "处所指示代词");
		wordTagMap.put("rzt", "时间指示代词");
		wordTagMap.put("rzv", "谓词性指示代词");
		wordTagMap.put("s", "处所词");//移居/v  海外/s  。/w
		wordTagMap.put("t", "时间词");//当前/t  经济/n  社会/n  情况/n
		wordTagMap.put("tg", "时间词性语素");//秋/Tg  冬/tg  连/d  旱/a
		wordTagMap.put("u", "助词");//工作/vn  的/u  政策/n
		wordTagMap.put("ud", "结构助词");// 有/v  心/n  栽/v  得/ud  梧桐树/n
		wordTagMap.put("ug", "时态助词");//你/r  想/v  过/ug  没有/v
		wordTagMap.put("uj", "结构助词的");//迈向/v  充满/v  希望/n  的/uj  新/a  世纪/n
		wordTagMap.put("ul", "时态助词了");//完成/v  了/ ul
		wordTagMap.put("uv", "结构助词地");//满怀信心/l  地/uv  开创/v  新/a  的/u  业绩/n
		wordTagMap.put("uz", "时态助词着");// 眼看/v  着/uz
		wordTagMap.put("uj", "助词");
		wordTagMap.put("ul", "连词");
		wordTagMap.put("uv", "连词");
		wordTagMap.put("v", "动词");//举行/v  老/a  干部/n  迎春/vn  团拜会/n
		wordTagMap.put("vd", "副动词");//强调/vd  指出/v
		wordTagMap.put("vf", "趋向动词");
		wordTagMap.put("vg", "动词性语素");//做好/v  尊/vg  干/j  爱/v  兵/n  工作/vn
		wordTagMap.put("vi", "不及物动词（内动词）");
		wordTagMap.put("vl", "动词性惯用语");
		wordTagMap.put("vn", "名动词");//股份制/n  这种/r  企业/n  组织/vn  形式/n  ，/w
		wordTagMap.put("vx", "形式动词");
		wordTagMap.put("w", "标点符号");
		wordTagMap.put("z", "状态词");//势头/n  依然/z  强劲/a  ；/w
		wordTagMap.put("wb", "百分号千分号，全角：％ ‰   半角：%");
		wordTagMap.put("wd", "逗号，全角：， 半角：,");
		wordTagMap.put("wf", "分号，全角：； 半角： ;");
		wordTagMap.put("wh", "单位符号，全角：￥ ＄ ￡  °  ℃  半角：$");
		wordTagMap.put("wj", "句号，全角：。");
		wordTagMap.put("wky", "右括号，全角：） 〕  ］ ｝ 》  】 〗 〉 半角： ) ] { >");
		wordTagMap.put("wkz", "左括号，全角：（ 〔  ［  ｛  《 【  〖 〈   半角：( [ { <");
		wordTagMap.put("wm", "冒号，全角：： 半角： :");
		wordTagMap.put("wn", "顿号，全角：、");
		wordTagMap.put("wp", "破折号，全角：——   －－   ——－   半角：—  —-");
		wordTagMap.put("ws", "省略号，全角：……  …");
		wordTagMap.put("wt", "叹号，全角：！");
		wordTagMap.put("ww", "问号，全角：？");
		wordTagMap.put("wyy", "右引号，全角：” ’ 』");
		wordTagMap.put("wyz", "左引号，全角：“ ‘ 『");
		wordTagMap.put("x", "非语素字");//生产/v  的/u  ５Ｇ/nx  、/w  ８Ｇ/nx 型/k  燃气/n  热水器/n
		wordTagMap.put("xu", "网址URL");
		wordTagMap.put("xx", "非语素字");
		wordTagMap.put("y", "语气词(delete yg)");
		wordTagMap.put("yg", "语气语素");
		wordTagMap.put("z", "状态词");
		wordTagMap.put("zg", "状态词");

		return wordTagMap;
	}
}
