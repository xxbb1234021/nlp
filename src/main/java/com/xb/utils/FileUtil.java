package com.xb.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtil
{
	public static List<String> read(String path)
	{
		if (path == null)
			return null;
		File file = new File(path);

		if (!file.isFile())
		{
			System.out.println("  -ERROR 系统找不到指定的文件路径：" + path);
			return null;
		}

		List<String> contents = new ArrayList<String>();
		String line = "";
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(file));

			while ((line = br.readLine()) != null)
			{
				if ("".equals(line))
					continue;
				line = line.replace(new String(Character.toChars(12288)), "");

				line = line.replace(" ", "");
				contents.add(line);
			}

			br.close();
		}
		catch (IOException e)
		{
			e.printStackTrace(System.err);
		}
		return contents;
	}

	public static boolean write(String path, List<String> contents)
	{
		File file = new File(path);
		if (file.isDirectory())
			System.out.println("  -ERROR 系统找不到指定的文件路径：" + path);
		else
		{
			try
			{
				if (file.exists())
				{
					file.delete();
				}
				FileOutputStream os = new FileOutputStream(file);
				for (String cont : contents)
				{
					os.write(cont.getBytes());

					os.write("\r\n".getBytes());
				}
				os.close();
				return true;
			}
			catch (IOException e)
			{
				e.printStackTrace(System.err);
			}
		}
		return false;
	}

	public static int[][] readPerceptronFile(String fileName)
	{
		try
		{
			String line = "";
			List<int[]> list = new ArrayList<int[]>();
			
			//System.out.println(fileName);
			File file = new File(fileName);
			InputStream is = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			while ((line = br.readLine()) != null)
			{
				String[] chunk = line.trim().split(" ");
				int[] temp = new int[chunk.length];
				for (int i = 0; i < chunk.length; i++)
				{
					temp[i] = Integer.parseInt(chunk[i]);
				}
				
				list.add(temp);
			}
			br.close();
			
			int[][] result = (int[][])list.toArray(new int[0][0]);  
			return result;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}