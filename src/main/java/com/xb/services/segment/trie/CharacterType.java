package com.xb.services.segment.trie;

public class CharacterType
{
	public static boolean isCharSeperator(char c)
	{
		return "\u3002\uFF01\uFF1F\uFF1A\uFF1B\u3001\uFF0C\uFF08\uFF09\u300A\u300B\u3010\u3011{}\u201C\u201D\u2018\u2019!?:;,()<>[]{}\"'\n\r\t "
				.indexOf(c) != -1;
	}

	public static boolean isCharChinese(char c)
	{
		return c >= '\u4E00' && c <= '\u9FBF';
	}

	public static boolean isCharOther(char c)
	{
		return !isCharSeperator(c) && !isCharChinese(c);
	}
}