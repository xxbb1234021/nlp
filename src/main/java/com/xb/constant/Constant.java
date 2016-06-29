package com.xb.constant;

public class Constant
{
	//贝叶斯训练数据文件夹
	public static final String BAYESTRAINDATA = "trainningSet";
	
	//trie 树 文件夹
	public static final String WORD_TRIE_TREE = "dict/chiCoreDict.utf8";

	//trie 树 文件夹
	public static final String WORD_NEW_TEXT = "dict/newtext.utf8";

	//trie 树 文件夹
	public static final String PINYIN_TRIE_TREE = "pinyin/pinyin2.utf8";
	
	//double trie 树 数据文件夹
	public static final String DOUBLE_TRIE_TREE = "dict/doubleArrayChiCoreDict.utf8";

	//词性标注语料库
	public static final String WORD_TAG_TRAINDATA = "tag/199801train.utf8";

	//拼音标注语料库
	//public static final String PINYIN_TAG_TRAINDATA = "pinyin/outfile.utf8";

	//拼音标注语料库
	public static final String PINYIN_TAG_TRAINDATA = "pinyin/sentence_new.txt";

	public static final String PINYIN_TAG_START = "pinyin_data/hmm_start.json";

	public static final String PINYIN_TAG_PY2HZ = "pinyin_data/hmm_py2hz.json";

	public static final String PINYIN_TAG_TRANSITION = "pinyin_data/hmm_transition.json";

	public static final String PINYIN_TAG_EMISSION = "pinyin_data/hmm_emission.json";

	//中文编码
	public static final String HMM_SEGMENT_CHINESECODE = "hmmseg/chinesecode.utf8";

	//hmmsegmet训练数据
	public static final String HMM_SEGMENT_TRAINDATA = "hmmseg/msr_training.utf8";

	//hmm已分好词的数据
	public static final String HMM_SEGMENT_TRAINDATA2 = "hmmseg/segment.utf8";

	//pcfg数据
	public static final String PCFG_DATA = "pcfg/pcfg.utf8";

	public static final String CHARSET_UTF8= "UTF-8";

	public static final String CHARSET_GBK= "GBK";

	public static final String TRIE_CATEGORY_WORD = "word";

	public static final String TRIE_CATEGORY_PINYIN = "pinyin";

	public static final String TRIE_CATEGORY_SYNTAX = "syntax";

	//所有文章的目录
	public static final String ARTICLE_DIR = "article";
	//把所有文章处理后写入sentence文件
	public static final String ARTICLE_DIR_SENTENCE = "pinyin/sentence.txt";
}
