package com.xb.text;

import com.xb.algoritm.text.CosineSimilar;
import com.xb.algoritm.text.TfIdf;
import com.xb.constant.FileConstant;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

/**
 * Created by kevin on 2016/8/7 0007.
 */
public class TestCosineSimilar {
	@Test public void testTfidf() {
		Double result = CosineSimilar
				.cosSimilarity("关于王立军，有几个基本事实。首先，1月28日我是初次听到此事，并不相信谷开来会杀人，我跟11·15杀人案无关，我不是谷开来11·15杀人罪的共犯，这个大家都认可",
						"实际上免他是有这些原因的，绝不只是一个谷开来的原因。这是多因一果。");
		System.out.println(result);
	}
}
