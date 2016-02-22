package com.xb.service.other;

import java.util.ArrayList;

/** ����Ŀʵ���˴��Ա�ע������ʵ�ֵĲ�����:
 * 1) ��Ҫ������ַ���зִʣ��ִʵķ����Ǵ������ң����ƥ�䡣
 * 2) ����ֳ����Ͽ⹹��ѵ���ִ�ϵͳ��Ȼ�󽫷ֺôʵ��ַ�������д��Ա�ע
 * ע�����ڴʿ�������ḻ����Ŀ�ڷִʷ��洦��Ϻã������������Ͽ�ϲ�����ڽ��д��Ա�ע����
 * ���еĽϲ
 * @author ygch
 *
 */
public class Main
{
	/**
	 * ���й�����п��ܻ��������޸�jvm��С�ķ����ǣ� ��Eclipse�е�����Ϊ:����->��ѡ��->JAVA->�Ѱ�װ��JRE,
	 * ��ȱʡ��VM�Ա���������:-Xmx256M
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		// ���ʿ⣬�����ļ��ķִ�
		System.out.println("Read word library");
		WordLib wl = new WordLib();
		wl.readLib("D:\\workspace\\nlp\\target\\classes\\tag\\lib.utf8");
		
		System.out.println("Read source file");
		String source = wl.readSourceFile("D:\\workspace\\nlp\\target\\classes\\tag\\source.utf8");
		
		System.out.println("Start to segmetation");
		ArrayList<String> seg = wl.segmentation(source.trim());
		
		String[] example = new String[seg.size()];
		for (int i = 0; i < example.length; i++)
		{
			example[i] = seg.get(i);
		}
		wl.writeToSegmentationFile("D:\\workspace\\nlp\\target\\classes\\tag\\segmentation.txt", example);

		// �Էִʽ��б�ע
		System.out.println("Read corpus");
		Viterbi v = new Viterbi();
		v.readCorpus("D:\\workspace\\nlp\\target\\classes\\tag\\199801train.utf8");
		
		System.out.println("Initialize compute system");
		v.characterSum();
		v.phraseSum();
		v.transformFrequencySum();
		v.emissonFrequencySum();
		v.calculatePrioriProbability();
		v.calculateTransformProbability();
		v.calculateEmissionProbability();
		
		// String[] example = { "���", "��ϯ", "��","����", "����", "����", "�����" };
		
		ArrayList<String> smallArrayList=v.smallSeg(seg);
		String[] example2 = new String[smallArrayList.size()];
		for (int i = 0; i < example2.length; i++)
		{
			example2[i] = smallArrayList.get(i);
		}
		wl.writeToSegmentationFile("D:\\workspace\\nlp\\target\\classes\\tag\\smallSegmentation.utf8", example2);
		System.out.println("Start to pos tag");
		v.viterbi(example2);
		
		System.out.println("Writ to destination file");
		v.writeToDestinationFile("D:\\workspace\\nlp\\target\\classes\\tag\\destination.utf8", example2);
		System.out.println("Finished.");
	}
}
