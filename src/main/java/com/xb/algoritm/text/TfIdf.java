package com.xb.algoritm.text;

import com.xb.algoritm.segment.MaxMatchingWordSegmenter;
import com.xb.constant.Constant;
import com.xb.constant.FileConstant;
import com.xb.utils.FileNIOUtil;
import com.xb.utils.SegWordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by kevin on 2016/8/7 0007.
 */
public class TfIdf {
	private static Logger LOGGER = LoggerFactory.getLogger(TfIdf.class);

	//所有文件tf结果.key:文件名,value:该文件tf
	private static Map<String, Map<String, Double>> allTfMap = new HashMap<String, Map<String, Double>>();

	//所有文件分词结果.key:文件名,value:该文件分词统计
	private static Map<String, Map<String, Integer>> allSegsMap = new HashMap<String, Map<String, Integer>>();

	//所有文件分词的idf结果.key:文件名,value:词w在整个文档集合中的逆向文档频率idf (Inverse Document Frequency)，即文档总数n与词w所出现文件数docs(w, D)比值的对数
	private static Map<String, Double> idfMap = new HashMap<String, Double>();

	//统计单词的TF-IDF  key:文件名 value:该文件tf-idf
	private static Map<String, Map<String, Double>> tfIdfMap = new HashMap<String, Map<String, Double>>();

	/**
	 * 获取目录下所有文件中的句子
	 * @return
	 */
	public static List<String> genSentence(String dir) {
		List<String> list = new ArrayList<String>();

		List<String> fileList = FileNIOUtil.dirList(dir);
		for(String file : fileList) {
			String content = FileNIOUtil.readFile(file, Constant.CHARSET_UTF8);

			list.add(content);
		}
		return list;
	}

	/**
	 * 得到所有文件的tf
	 * @param dir
	 */
	public static void allTf(String dir) {
		int index = 0;
		List<String> sentenceList = genSentence(dir);
		for(String s : sentenceList) {
			Map<String, Integer> segs = SegWordUtils.segWords(s, false);
			String name = String.valueOf(++index);
			allSegsMap.put(name, segs);
			allTfMap.put(name, tf(segs));
		}
	}

	/**
	 * 分词结果转化为tf,公式为:tf(w,d) = count(w, d) / size(d)
	 * 即词w在文档d中出现次数count(w, d)和文档d中总词数size(d)的比值
	 * @param segWordsResult
	 * @return
	 */
	private static Map<String, Double> tf(Map<String, Integer> segWordsResult) {
		Map<String, Double> tf = new HashMap<String, Double>();
		if (segWordsResult == null || segWordsResult.size() == 0) {
			return tf;
		}
		Double size = Double.valueOf(segWordsResult.size());
		Set<String> keys = segWordsResult.keySet();
		for(String key : keys) {
			Integer value = segWordsResult.get(key);
			tf.put(key, Double.valueOf(value) / size);
		}
		return tf;
	}

	/**
	 * 词w在整个文档集合中的逆向文档频率idf (Inverse Document Frequency)，
	 * 即文档总数n与词w所出现文件数docs(w, D)比值的对数
	 * idf = log(n / docs(w, D))
	 * @param allSegsMap
	 * @return
	 */
	public static void idf(Map<String, Map<String, Integer>> allSegsMap) {
		Map<String, Integer> containWordOfAllDocNumberMap = containWordOfAllDocNumber(allSegsMap);
		Set<String> words = containWordOfAllDocNumberMap.keySet();
		Double wordSize = Double.valueOf(containWordOfAllDocNumberMap.size());
		for(String word : words) {
			Double number = Double.valueOf(containWordOfAllDocNumberMap.get(word));
			idfMap.put(word, Math.log(wordSize / (number + 1.0d)));
		}
	}

	/**
	 * 统计包含单词的文档数  key:单词  value:包含该词的文档数
	 * @param allSegsMap
	 * @return
	 */
	private static Map<String, Integer> containWordOfAllDocNumber(Map<String, Map<String, Integer>> allSegsMap) {
		Map<String, Integer> containWordOfAllDocNumberMap = new HashMap<String, Integer>();
		if (allSegsMap == null || allSegsMap.size() == 0) {
			return containWordOfAllDocNumberMap;
		}

		Set<String> fileNameSet = allSegsMap.keySet();
		for(String fileName : fileNameSet) {
			Map<String, Integer> fileSegs = allSegsMap.get(fileName);
			//获取该文件分词为空或为0,进行下一个文件
			if (fileSegs == null || fileSegs.size() == 0) {
				continue;
			}
			//统计每个分词的idf
			Set<String> segs = fileSegs.keySet();
			for(String seg : segs) {
				if (containWordOfAllDocNumberMap.containsKey(seg)) {
					containWordOfAllDocNumberMap.put(seg, containWordOfAllDocNumberMap.get(seg) + 1);
				} else {
					containWordOfAllDocNumberMap.put(seg, 1);
				}
			}
		}
		return containWordOfAllDocNumberMap;
	}

	/**
	 * tf-idf模型根据tf和idf为每一个文档d和由关键词w[1]…w[k]组成的查询串q计算一个权值，
	 * 用于表示查询串q与文档d的匹配度
	 * @param allTfMap
	 * @param idf
	 */
	public static void tfIdf(Map<String, Map<String, Double>> allTfMap, Map<String, Double> idf) {
		Set<String> fileNameSet = allTfMap.keySet();
		for(String fileName : fileNameSet) {
			Map<String, Double> tfMap = allTfMap.get(fileName);
			Map<String, Double> docTfIdf = new HashMap<String, Double>();
			Set<String> words = tfMap.keySet();
			for(String word : words) {
				Double tfValue = Double.valueOf(tfMap.get(word));
				Double idfValue = idf.get(word);
				docTfIdf.put(word, tfValue * idfValue);
			}
			tfIdfMap.put(fileName, docTfIdf);
		}
	}

	public static Map<String, Map<String, Double>> getAllTfMap() {
		return allTfMap;
	}

	public static Map<String, Map<String, Integer>> getAllSegsMap() {
		return allSegsMap;
	}

	public static Map<String, Double> getIdfMap() {
		return idfMap;
	}

	public static Map<String, Map<String, Double>> getTfIdfMap() {
		return tfIdfMap;
	}
}
