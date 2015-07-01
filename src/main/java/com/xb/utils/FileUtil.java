package com.xb.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileUtil
{
	public static ArrayList<String> read(String path)
	{
		if (path == null)
			return null;
		File file = new File(path);

		if (!file.isFile())
		{
			System.out.println("  -ERROR 系统找不到指定的文件路径：" + path);
			return null;
		}

		ArrayList contents = new ArrayList();
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

	public static boolean write(String path, ArrayList<String> contents)
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
}