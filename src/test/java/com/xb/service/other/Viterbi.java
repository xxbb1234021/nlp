package com.xb.service.other;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

/**����ʵ����compute�㷨�Ĵ��Ա�ע
 * @author ygch
 *
 */
public class Viterbi {

	private String[] text; // ���ڴ洢ѵ�������еĴ���ʹ���
	private String[] phrase; // ���ڴ洢ѵ�������еĴ���
	private String[] characters; // ���ڴ洢�����ʵĴ��Ա�ע����,�����Ͽ��еĴ�������
	Hashtable<String, Integer> charactersHash = new Hashtable<String, Integer>(); // ����charactersHash���洢�����ʵĴ��Լ���Ƶ��
	Hashtable<String, Integer> phraseHash = new Hashtable<String, Integer>(); // ����phraseHash���洢��ͬ�Ĵ��鼰��Ƶ��
	Hashtable<String, Integer> transformFrequencyHash = new Hashtable<String, Integer>(); // ����transformFrequencyHash���洢ÿ�����ʵĴ��Լ���Ƶ��
	Hashtable<String, Integer> emissonFrequencyHash = new Hashtable<String, Integer>(); // ����transformFrequencyHash���洢ÿ�����ʵĴ��Լ���Ƶ��
	private int charactersNum = 0;
	private int phraseNum = 0;
	private String[] diffChars;// �洢��ͬ�Ĵ��Է���
	private String[] diffPhras;// �洢��ͬ�Ĵ���
	//Ϊ�˺���ļ����ٶȣ��������Ͽ��еĲ�ͬ�����λ�ã���diffPhras��Ӧ
	Hashtable<String, Integer> phrasePosition = new Hashtable<String, Integer>();
	private double[] prioriProbability;//���Ե��������
	private double[][] transformProbability;
	private double[][] emissionProbability;

	/**��ȡ���Ͽ�
	 * @param fileName
	 */
	public void readCorpus(String fileName) {
		// ----------------------------------------------------------------------------------
		// ͳ�Ƴ�ѵ�������д������༰��Ƶ��
		StringBuffer content = new StringBuffer();
		BufferedReader reader = null;
		try {
			// ��ȡ199801train.txt�ı��е����ݣ���������content���ַ�����
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "GB2312"));
			String line;
			while ((line = reader.readLine()) != null)
				content.append(line);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					System.out.println("close file error");
				}
			}
		}
		// ��ȡԤ�����Ͽ��е�һ������ͬ�Ĵ���(�Կո�ֿ�)�����������Ӧ�Ĵ���
		text = content.toString().split("\\s{1,}");
		// ȥ�����Ա�ע��ֻ�������
		phrase = content.toString().split("(/[a-z]*\\s{0,})");//"/"�������һ�����߶����ĸȻ���Ƕ���ո�
		// ��ȡ���Ͽ��д�ǰ��������д���Ĵ���
		characters = content.toString().split("[0-9|-]*/|\\s{1,}[^a-z]*"); //��ͷ�����ڻ��߿ո�+����ĸ��Ϊ�ָ���
	}

	/**�ú�������ͳ�Ʋ�ͬ�Ĵ��Լ���Ƶ��
	 * charactersHash�����治ͬ�Ĵ��Լ���Ƶ��
	 * diffChars��ֻ���治ͬ�Ĵ���
	 */
	public void characterSum() {
		for (int i = 1; i < characters.length; i++) {
			if (charactersHash.containsKey(characters[i])) {
				charactersHash.put(characters[i], charactersHash.get(characters[i]) + 1);
			} else {
				charactersHash.put(characters[i], 1);
			}
		}
		charactersNum = charactersHash.size();
		diffChars = new String[charactersNum];
		Enumeration<String> key = charactersHash.keys();
		for (int i = 0; i < charactersHash.size(); i++) {
			diffChars[i] = (String) key.nextElement();
		}
	}

	/**
	 * ͳ�����Ͽ��еĲ�ͬ����
	 * phraseHash��������������Ƶ��
	 * diffPhras��ֻ�������治ͬ�Ĵ���
	 * phrasePosition�����������diffPhras�е�λ��
	 */
	public void phraseSum() {
		for (int i = 0; i < phrase.length; i++) {
			if (phraseHash.containsKey(phrase[i])) {
				phraseHash.put(phrase[i], phraseHash.get(phrase[i]) + 1);
			} else {
				phraseHash.put(phrase[i], 1);
			}
		}
		phraseNum = phraseHash.size();
		diffPhras = new String[phraseNum];
		Enumeration<String> key = phraseHash.keys();
		for (int i = 0; i < phraseHash.size(); i++) {
			String str = (String) key.nextElement();
			diffPhras[i] = str;

			phrasePosition.put(str, i);
		}
	}

	/**
	 * ����ת��Ƶ��
	 */
	public void transformFrequencySum() {
		for (int i = 0; i < characters.length - 1; i++) {
			String temp = characters[i] + "," + characters[i + 1];
			if (transformFrequencyHash.containsKey(temp)) {
				transformFrequencyHash.put(temp, transformFrequencyHash.get(temp) + 1);
			} else {
				transformFrequencyHash.put(temp, 1);
			}
		}
	}

	/**
	 * ���㷢��Ƶ��
	 */
	public void emissonFrequencySum() {

		for (int i = 0; i < text.length; i++) {

			if (emissonFrequencyHash.containsKey(text[i])) {
				emissonFrequencyHash.put(text[i], emissonFrequencyHash.get(text[i]) + 1);
			} else {
				emissonFrequencyHash.put(text[i], 1);
			}
		}
	}

	/**
	 * ������Ե��������
	 * Ҳ����ͬ������Ԥ���г��ֵĴ���
	 */
	public void calculatePrioriProbability() {
		prioriProbability = new double[charactersNum];

		int allCharacterCount = 0;

		for (int i = 0; i < charactersNum; i++) {
			allCharacterCount += charactersHash.get(diffChars[i]);
		}

		for (int i = 0; i < charactersNum; i++) {
			prioriProbability[i] = charactersHash.get(diffChars[i]) * 1.0 / allCharacterCount;
		}
	}

	/**
	 * ����ת�Ƹ���
	 * ����ͳ�ƵĲ�ͬ���ԣ�������һ������ת�Ƶ���һ�����Եĸ���
	 */
	public void calculateTransformProbability() {
		transformProbability = new double[charactersNum][charactersNum];

		for (int i = 0; i < charactersNum; i++) {
			for (int j = 0; j < charactersNum; j++) {
				String front = diffChars[i];
				String last = diffChars[j];

				if (transformFrequencyHash.containsKey(front + "," + last)) {
					int numerator = transformFrequencyHash.get(front + "," + last);
					int denominator = charactersHash.get(front);
					transformProbability[i][j] = numerator * 100.0 / denominator;// ��Ϊ��Щ���ʺ�С��Ϊ�˾�ȷ������Ŵ�100��
				}
			}
		}
	}

	/**
	 * ���㷢�����
	 * �������Ͽ��еĲ�ͬ����ʹ��ԣ����㷢�����
	 * ����ĳ�������£���ĳ������ĸ���
	 */
	public void calculateEmissionProbability() {
		emissionProbability = new double[charactersNum][phraseNum];
		for (int i = 0; i < charactersNum; i++) {
			for (int j = 0; j < phraseNum; j++) {
				String chars = diffChars[i];
				String phras = diffPhras[j];

				if (emissonFrequencyHash.containsKey(phras + "/" + chars)) {
					int numerator = emissonFrequencyHash.get(phras + "/" + chars);
					int denominator = charactersHash.get(chars);
					emissionProbability[i][j] = numerator * 100.0 / denominator;// ��Ϊ��Щ���ʺ�С��Ϊ�˾�ȷ������Ŵ�100��
				}
			}
		}
	}

	/**���ǲ��õķִʷ����Ǵ������ң����ƥ��ģʽ�����ǳ����в��õ����Ͽ�ȴ������
	 * ��Сƥ��ģʽ���������ǳ��ηִʵĽ���п��ܲ������Ͽ��С��ڴ����ǽ����Ͽⲻ��ʶ���
	 * �����ٴν��зִʳ������㷨�ҵ�����Ĵʡ�
	 * @param seg
	 * @return
	 */
	public ArrayList<String> smallSeg(ArrayList<String> seg) {
		ArrayList<String> smallArrayList = new ArrayList<String>();
		for (int i = 0; i < seg.size(); i++) {
			String temp = seg.get(i);
			boolean canSpilt = false;
			int index = 0;
			if (phrasePosition.get(temp) == null) {
				for (int j = 1; j < temp.length(); j++) {
					if (phrasePosition.get(temp.substring(0, j)) != null
							&& phrasePosition.get(temp.substring(j)) != null) {
						canSpilt = true;
						index = j;
						break;
					}
				}
			}

			if (canSpilt) {
				smallArrayList.add(temp.substring(0, index));
				smallArrayList.add(temp.substring(index));
			} else {
				smallArrayList.add(temp);
			}
		}
		return smallArrayList;
	}

	/**����compute�㷨�Ը������ַ���������д��Ա�ע
	 * @param example��Ҫ��ע���ַ�������
	 */
	public void viterbi(String[] example) {
		//��������ĳ������iѡ�����j������ֵ
		double[][] value = new double[example.length][charactersNum];//�ڶ�ά��ʾ��ͬ�Ĵ��ԣ�Ҳ������״̬
		//��������valueȡ�����ֵʱ��ǰ��
		int[][] previous = new int[example.length][charactersNum];
		int position;
		//��ʼ����һ�����ڲ�ͬ�����µ�value�����䷢�����
		if (phrasePosition.get(example[0]) != null) {
			position = phrasePosition.get(example[0]);
			for (int j = 0; j < charactersNum; j++) {
				System.out.println(prioriProbability[j]);
				System.out.println(emissionProbability[j][position]);
				value[0][j] = prioriProbability[j] * emissionProbability[j][position];
			}
		} else {
			for (int j = 0; j < charactersNum; j++) {
				value[0][j] = 1;
			}
		}

		for (int i = 1; i < example.length; i++)// �ж��ٸ����󣬼����ٸ���ͬ�Ĵ���
		{
			if (phrasePosition.get(example[i]) == null) {
				for (int j = 0; j < charactersNum; j++) {
					value[i][j] = 1;
				}
				continue;
			}

			position = phrasePosition.get(example[i]);
			for (int j = 0; j < charactersNum; j++)// ÿ��������ܵ�״̬,������
			{
				double max = value[i - 1][0] * transformProbability[0][j] * emissionProbability[j][position];
				int index = 0;
				for (int k = 1; k < charactersNum; k++)// �ô���ǰһ��������ܵ�״̬
				{
					// ��i������ѡ����j������ֵ
					value[i][j] = value[i - 1][k] * transformProbability[k][j] * emissionProbability[j][position];
					if (value[i][j] > max) {
						index = k;
						max = value[i][j];
					}
				}

				previous[i][j] = index;
				value[i][j] = max;
			}
		}

		double max = -1;
		int index = 0;
		//�����ҵ����һ�����ڲ�ͬ�����µ����ֵ��Ȼ���ɴ����ֵ���ݲ����������ֵ
		for (int i = 0; i < charactersNum; i++) {
			if (max < value[example.length - 1][i]) {
				index = i;
				max = value[example.length - 1][i];
			}
		}

		for (int i = example.length - 1; i >= 0; i--) {
			example[i] = example[i].concat("/" + diffChars[index]);
			index = previous[i][index];
		}
	}

	/**�����Ա�ע��Ľ��д��file�ļ�
	 * @param file
	 * @param example
	 */
	public void writeToDestinationFile(String file, String[] example) {
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(file));
			for (String string : example) {
				writer.print(string + "  ");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Viterbi v = new Viterbi();
		v.readCorpus("./199801train.txt");
		v.characterSum();
		v.phraseSum();
		v.transformFrequencySum();
		v.emissonFrequencySum();
		v.calculateTransformProbability();
		v.calculateEmissionProbability();
		String[] example = { "����", "��ϯ", "��", "����", "����", "����", "������" };
		v.viterbi(example);
		v.writeToDestinationFile("destination.txt", example);
	}
}
