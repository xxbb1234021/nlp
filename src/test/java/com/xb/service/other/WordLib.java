package com.xb.service.other;

import java.io.*;
import java.util.*;

/**����������Ƕ�ȡ�ʿ⣬Ȼ���һ���ַ���зִʣ������Ǵ������ң����ƥ��
 * @author ygch
 * 
 */
public class WordLib
{
	public static final int BEGIN = 19968, LAST = 40896;
	ArrayList<String> wordLib = new ArrayList<String>();
	// ��ɢ�б���ʿ���������ͬ�Ĵ���ĸ������ڶ��ֲ�����
	Hashtable<String, Integer> groupLength = new Hashtable<String, Integer>();
	// ���������ڴʿ��е�λ�ã����ڼ���
	Hashtable<String, Integer> groupStart = new Hashtable<String, Integer>();
	// �������ڴ����е�������ĳ���
	Hashtable<String, Integer> groupMaxLength = new Hashtable<String, Integer>();

	/**
	 * �ú���������ȡ�ִ��õĴʿ�
	 * 
	 * @param lib���ʿ���ļ���
	 */
	public void readLib(String lib)
	{
		try
		{
			//BufferedReader bfl = new BufferedReader(new FileReader(lib));
			//BufferedReader bfl = new BufferedReader(new InputStreamReader(new FileInputStream(new File(lib)), "UTF-8"));

			File file = new File(lib);
			InputStream is = new FileInputStream(file);
			BufferedReader bfl = new BufferedReader(new InputStreamReader(is, "GBK"));

			String lineString;
			String first = "һ";
			int count = 0;// ��������һ�����ĸ����������д����������ͬ��
			int i = 0;
			int maxLength = 0;
			groupStart.put(first, 0);
			while ((lineString = bfl.readLine()) != null)
			{
				String temp = lineString.substring(0, 1);

				// ������һ����ͬ������ʱ����ζ�������Ѿ���ɶ�һ������ɨ�裬���������Ϣ
				if (!temp.equals(first))
				{
					groupLength.put(first, count);
					groupMaxLength.put(first, maxLength);
					groupStart.put(temp, i);
					// ����ر�������
					maxLength = 0;
					count = 1;
					first = temp;
				}
				else// ͳ�����д���ĸ���
				{
					count++;
				}
				// �ʿ����еĶ�����������֣�������ڱ�ĵط����ڴ�����
				int index = lineString.indexOf(" ");
				if (index != -1)
				{
					lineString = lineString.substring(0, index);
				}
				
				if (maxLength < lineString.length())// �������д������󳤶�
				{
					maxLength = lineString.length();
				}
				wordLib.add(i++, lineString);
			}
			groupLength.put(first, count);// ��Ҫ�������һ����������
			groupMaxLength.put(first, maxLength);
			bfl.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * �ж��Ƿ�Ϊ���Ĵ�
	 */
	public static boolean isCHWord(char s)
	{
		boolean res = false;
		int code = (int) s;
		if (code >= WordLib.BEGIN && code <= WordLib.LAST)
			res = true;
		return res;
	}

	/**
	 * ��Ŀ���ַ���зִʣ����õķ����Ǵ��������ƥ��ԭ��
	 * 
	 * @param source
	 *            ��Ҫ�ִʵ��ַ�
	 * @return�����طִʵĽ��
	 */
	public ArrayList<String> segmentation(String source)
	{
		ArrayList<String> seg = new ArrayList<String>();
		for (int i = 0; i < source.length(); i++)
		{
			String temp = source.substring(i, i + 1);
//			System.out.println("temp is " + temp);
			// ����Ǻ��֣�������
			int front = i;
			if (!isCHWord(temp.charAt(0)))
			{
				// System.out.println("add noise " + temp);
				//ע����i�Ĵ�С����Ҫ�����ַ�ĳ��ȡ�
				while (i<source.length()-1&&!isCHWord(source.charAt(++i)))//��������������Ǻ��֣�����һ��ͳһ����
					;
				if(i==source.length()-1)
				{
					seg.add(source.substring(front));
					break;
				}
				if (source.substring(front, i).trim().length() != 0)
				{
					seg.add(source.substring(front, i--));//��Ϊ�ǿ���䣬��������ӣ����1
				}
				else//������ͳ�ƷǺ��ֵ�ʱ��i����ǰ����һ������Ҫ�˻�ȥ
				{
					i--;
				}
				continue;
			}

			int position = 0;
			boolean found = false;
			// �������ƥ��ԭ����Ҵ���
			for (int j = groupMaxLength.get(temp); j >= 1; j--)
			{
				String w;
				if ((i + j) > source.length())
					w = source.substring(i).trim();
				else
					w = source.substring(i, i + j).trim();
				
				int previous;
				if ((previous = hasWord(w)) < 0)// ���û�иó��ȵģ�������Ҹ�̳��ȵ�
				{
					continue;
				}
				found = true;
				position = previous;
				break;
			}
			
			if (found)
			{
				seg.add(wordLib.get(position));
				i += wordLib.get(position).length() - 1;
			}
			else//�Ҳ����ͽ���������Ϊһ���ָ�
			{
				seg.add(temp);
			}
		}
		return seg;
	}

	/**
	 * �����Ƿ����Ŀ���,���������ڴʱ��е�����,�����򷵻�-1�����������ö��ֲ���
	 * 
	 * @param w
	 *            ��Ҫ���ҵ��ַ�
	 * @return���������д����е�λ��
	 */
	public int hasWord(String w)
	{
		int res = -1;
		if (w != null && w.length() > 0)
		{
			String head = w.substring(0, 1);
			int length = groupLength.get(head);
			int start = groupStart.get(head);
			int end = start + length - 1;
			if (start != -1 && end != -1)
			{
				if (start == end)
				{
					if (wordLib.get(start).equals(w))
						res = start;
				}
				else
				{
					while (start <= end)
					{
						int mid = (start + end) / 2;
						int comp = w.compareTo(wordLib.get(mid));
						if (comp == 0)
							return mid;
						else if (comp > 0)
							start = mid + 1;
						else
							end = mid - 1;
					}
					
					if (w.equals(wordLib.get(start)))
						return start;
					if (w.equals(wordLib.get(end)))
						return end;
				}
			}
		}
		return res;
	}
	
	/**���ļ��ж�ȡҪ���зִʵ��ַ�
	 * @param file
	 * @return
	 */
	public String readSourceFile(String file)
	{
		StringBuffer sBuffer=new StringBuffer();
		try
		{
			//BufferedReader bfl = new BufferedReader(new FileReader(file));

			File f = new File(file);
			InputStream is = new FileInputStream(f);
			BufferedReader bfl = new BufferedReader(new InputStreamReader(is, "GBK"));

			String lineString;
			while ((lineString = bfl.readLine()) != null)
			{
				sBuffer.append(lineString);
			}
			bfl.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return sBuffer.toString();
	}
	
	/**���ִʺ�Ľ��д��file�ļ�
	 * @param file
	 * @param example
	 */
	public void writeToSegmentationFile(String file,String[] example)
	{
		try
		{
			PrintWriter writer=new PrintWriter(new FileWriter(file));
			for(String string:example)
			{
				writer.println(string);
			}
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		WordLib wl = new WordLib();
		wl.readLib("lib.txt");
		ArrayList<String> seg = wl.segmentation("����һ���У��й�ĸĸ￪�ź��ִ��������ǰ����");
		for (int i = 0; i < seg.size(); i++)
		{
			System.out.println(seg.get(i));
		}
	}
}
