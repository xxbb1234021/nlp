package com.xb.pattern.hmm.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.xb.bean.hmm.Hmm;
import com.xb.constant.Constant;
import com.xb.constant.HmmSegmentConstant;
import com.xb.pattern.hmm.Director;
import com.xb.services.hmm.HmmService;

/**
 * Created by kevin on 2016/1/21.
 */
public class TestHmmSegmentWordsBuilder2 {
	@Test
	public void testHmmSegment() {
		String words = "杭州都下雪了";
		//words = encode(words);
		HmmSegmentWordsBuilder2 builder = HmmSegmentWordsBuilder2.getInstance(Constant.HMM_SEGMENT_TRAINDATA2);
		Director director = new Director(builder);
		director.construct();

		Hmm h = new Hmm();

		int[] obs = new int[words.length()];
		for (int i = 0; i < words.length(); i++) {
			String word = words.charAt(i) + "";
			if (StringUtils.isBlank(word)) {
				continue;
			}
			obs[i] = builder.getWordPositionMap().get(word) == null ? 1 : builder.getWordPositionMap().get(word);
		}

		h.setObs(obs);

		int[] states = new int[4];
		for (int i = 0; i < 4; i++) {
			states[i] = i;
		}
		h.setStates(states);
		h.setStartProb(builder.getPrioriProbability());
		h.setTransProb(builder.getTransformProbability());
		h.setEmitProb(builder.getEmissionProbability());

		//		HmmService hs = new HmmService();
		//		Integer[] result = hs.caculateHmmResult(h);

		StringBuilder sb = new StringBuilder();
		int[] result = compute(obs, states, builder.getPrioriProbability(), builder.getTransformProbability(),
				builder.getEmissionProbability());
		for (int i = 0; i < result.length; i++) {
			System.out.print(result[i] + " ");
			sb.append(builder.getDiffWordTag()[result[i]]);
		}
		System.out.println();

		String newSeqChar = sb.toString();
		System.out.println(newSeqChar);

		//Viterbi(builder.getPrioriProbability(),builder.getTransformProbability(),builder.getEmissionProbability(),obs);
		compute(obs, states, builder.getPrioriProbability(), builder.getTransformProbability(),
				builder.getEmissionProbability());
	}

	public static int[] compute(int[] obs, int[] states, double[] start_p, double[][] trans_p, double[][] emit_p) {
		int _max_states_value = 0;
		for (int s : states) {
			_max_states_value = Math.max(_max_states_value, s);
		}
		++_max_states_value;
		double[][] V = new double[obs.length][_max_states_value];
		int[][] path = new int[_max_states_value][obs.length];

		for (int y : states) {
			V[0][y] = start_p[y] + emit_p[y][obs[0]];
			path[y][0] = y;
		}

		for (int t = 1; t < obs.length; ++t) {
			int[][] newpath = new int[_max_states_value][obs.length];

			for (int y : states) {
				double prob = Double.MAX_VALUE;
				int state;
				for (int y0 : states) {
					double nprob = V[t - 1][y0] + trans_p[y0][y] + emit_p[y][obs[t]];
					if (nprob < prob) {
						prob = nprob;
						state = y0;
						// 记录最大概率
						V[t][y] = prob;
						// 记录路径
						System.arraycopy(path[state], 0, newpath[y], 0, t);
						newpath[y][t] = y;
					}
				}
			}

			path = newpath;
		}

		double prob = Double.MAX_VALUE;
		int state = 0;
		for (int y : states) {
			if (V[obs.length - 1][y] < prob) {
				prob = V[obs.length - 1][y];
				state = y;
			}
		}

		return path[state];
	}

	public static String viterbi(double[] PI, double[][] A, double[][] B, int[] sentences) {
		StringBuilder ret = new StringBuilder();
		double[][] matrix = new double[PI.length][sentences.length];
		int[][] past = new int[PI.length][sentences.length];

		int supplementStartColumn = -1;
		BigDecimal[][] supplement = null; //new BigDecimal[][];

		for (int row = 0; row < matrix.length; row++)
			matrix[row][0] = PI[row] * B[row][sentences[0]];

		for (int col = 1; col < sentences.length; col++) {
			if (supplementStartColumn > -1) { //Use supplement BigDecimal matrix
				for (int row = 0; row < matrix.length; row++) {
					BigDecimal max = new BigDecimal(0d);
					int last = -1;
					for (int r = 0; r < matrix.length; r++) {
						BigDecimal value = supplement[r][col - 1 - supplementStartColumn].multiply(
								new BigDecimal(A[r][row])).multiply(new BigDecimal(B[row][sentences[col]]));
						if (value.compareTo(max) > 0) {
							max = value;
							last = r;
						}
					}
					supplement[row][col - supplementStartColumn] = max;
					past[row][col] = last;
				}
			} else {
				boolean switchSupplement = false;
				for (int row = 0; row < matrix.length; row++) {
					double max = 0;
					int last = -1;
					for (int r = 0; r < matrix.length; r++) {
						double value = matrix[r][col - 1] * A[r][row] * B[row][sentences[col]];
						if (value > max) {
							max = value;
							last = r;
						}
					}
					matrix[row][col] = max;
					past[row][col] = last;
					if (max < 1E-250)
						switchSupplement = true;
				}

				//Really small data, should switch to supplement BigDecimal matrix now, or we will loose accuracy soon
				if (switchSupplement) {
					supplementStartColumn = col;
					supplement = new BigDecimal[PI.length][sentences.length - supplementStartColumn];
					for (int row = 0; row < matrix.length; row++) {
						supplement[row][col - supplementStartColumn] = new BigDecimal(matrix[row][col]);
					}
				}
			}
		}

		int index = -1;
		if (supplementStartColumn > -1) {
			BigDecimal max = new BigDecimal(0d);
			int column = supplement[0].length - 1;
			for (int row = 0; row < supplement.length; row++) {
				if (supplement[row][column].compareTo(max) > 0) {
					max = supplement[row][column];
					index = row;
				}
			}
		} else {
			double max = 0;
			for (int row = 0; row < matrix.length; row++)
				if (matrix[row][sentences.length - 1] > max) {
					max = matrix[row][sentences.length - 1];
					index = row;
				}
		}

		for (int i = 0; i < matrix.length; i++)
			System.out.println(Arrays.toString(matrix[i]));

		ret.append(HmmSegmentConstant.HMM_SEGMENT_MAP.get(index));
		for (int col = sentences.length - 1; col >= 1; col--) {
			index = past[index][col];
			ret.append(HmmSegmentConstant.HMM_SEGMENT_MAP.get(index));
		}

		System.out.println(ret.reverse().toString());
		return ret.reverse().toString();
	}

	private String encode(String content) {
		if (content == null || "".equals(content.trim()))
			return null;
		//分词后的文本，去掉标点符号
		content = content.replaceAll("\\pP", " ").trim();

		StringBuilder sb = new StringBuilder();
		//  String[] terms=content.split("\\s{1,}");
		int start, end, len;
		start = end = 0;
		len = content.length();
		//根据空格对文本进行分词
		while (end < len) {
			if (Character.isWhitespace(content.charAt(end))) {
				if (end > start) {
					//得到一个词
					//  insertWithContent(content,sb,start,end);
					insert(sb, start, end);
					++end;
					start = end;

				} else {
					++start;
					++end;
				}

			} else {
				++end;
			}
		}
		//insertWithContent(content,sb,start,end);
		insert(sb, start, end);

		return sb.toString();
	}

	private void insert(StringBuilder sb, int start, int end) {
		if (end - start > 1) {
			sb.append('B');
			for (int i = 0; i < end - start - 2; ++i) {
				sb.append('M');
			}
			sb.append('E');
		} else {
			sb.append('S');
		}
	}
}