package com.xb.text;

import com.xb.algoritm.text.TfIdf;
import com.xb.constant.FileConstant;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

/**
 * Created by kevin on 2016/8/7 0007.
 */
public class TestTfidf {
	@Test public void testTfidf() {
		System.out.println("tf--------------------------------------");
		TfIdf.allTf(FileConstant.ARTICLE_DIR);
		Map<String, Map<String, Double>> allTfMap = TfIdf.getAllTfMap();
		Set<String> fileList = allTfMap.keySet();
		//		for (String fileName : fileList) {
		//			Map<String, Double> tfMap = allTfMap.get(fileName);
		//			Set<String> words = tfMap.keySet();
		//			for (String word : words) {
		//				System.out.println("fileName:" + fileName + "     word:" + word + "      tf:" + tfMap.get(word));
		//			}
		//		}

		System.out.println("idf--------------------------------------");
		TfIdf.idf(TfIdf.getAllSegsMap());
		Map<String, Double> idfMap = TfIdf.getIdfMap();
		Set<String> words = idfMap.keySet();
		//		for (String word : words) {
		//			System.out.println("word:" + word + "     tf:" + idfMap.get(word));
		//		}

		System.out.println("tf-idf--------------------------------------");
		TfIdf.tfIdf(allTfMap, idfMap);
		Map<String, Map<String, Double>> tfIdfMap = TfIdf.getTfIdfMap();
		Set<String> files = tfIdfMap.keySet();
		for(String fileName : files) {
			Map<String, Double> tfIdf = tfIdfMap.get(fileName);
			Set<String> segs = tfIdf.keySet();
			for(String word : segs) {
				System.out.println("fileName:" + fileName + "     word:" + word + "        tf-idf:" + tfIdf.get(word));
			}
		}
	}
}
