package com.xb.utils;

import org.apache.commons.lang3.StringUtils;

public class TextUtils {
	public static boolean isCharSeperator(char c) {
		return "\u3002\uFF01\uFF1F\uFF1A\uFF1B\u3001\uFF0C\uFF08\uFF09\u300A\u300B\u3010\u3011{}\u201C\u201D\u2018\u2019!?:;,()<>[]{}\"'\n\r\t "
				.indexOf(c) != -1;
	}

	public static boolean isChineseChar(char c) {
		return c >= '\u4E00' && c <= '\u9FBF';
	}

	public static boolean isOtherChar(char c) {
		return !isCharSeperator(c) && !isChineseChar(c);
	}

	public static boolean isEnglishChar(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
	}

	public static boolean match(String src, int off, String dest) {
		int len = dest.length();
		int srcLen = src.length();
		for (int i = 0; i < len; i++) {
			if (srcLen <= off + i) {
				return false;
			}
			if (dest.charAt(i) != src.charAt(off + i)) {
				return false;
			}
		}
		return true;
	}
}