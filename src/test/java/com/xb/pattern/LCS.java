package com.xb.pattern;

import java.util.ArrayList;
import java.util.List;

public class LCS {
  
    //http://www.java3z.com/cwbwebhome/article/article5/5829.html?id=1547  
    public static List<String> getLCSstring(char[] str1, char[] str2) {
        int i, j;  
        int len1, len2;  
        len1 = str1.length;  
        len2 = str2.length;  
        int maxLen = len1 > len2 ? len1 : len2;  
        int[] max = new int[maxLen];  
        int[] maxIndex = new int[maxLen];  
        int[] c = new int[maxLen];  
        List<String> list = new ArrayList<>();
  
        for (i = 0; i < len2; i++) {  
            for (j = len1 - 1; j >= 0; j--) {  
                if (str2[i] == str1[j]) {  
                    if ((i == 0) || (j == 0))  
                        c[j] = 1;  
                    else  
                        c[j] = c[j - 1] + 1;  
                } else {  
                    c[j] = 0;  
                }  
  
                if (c[j] > max[0]) {   //如果是大于那暂时只有一个是最长的,而且要把后面的清0;  
                    max[0] = c[j];  
                    maxIndex[0] = j;  
  
                    for (int k = 1; k < maxLen; k++) {  
                        max[k] = 0;  
                        maxIndex[k] = 0;  
                    }  
                } else if (c[j] == max[0]) {   //有多个是相同长度的子串  
                    for (int k = 1; k < maxLen; k++) {  
                        if (max[k] == 0) {  
                            max[k] = c[j];  
                            maxIndex[k] = j;  
                            break;  //在后面加一个就要退出循环了  
                        }  
  
                    }  
                }  
            }  
        }  
  
        for (j = 0; j < maxLen; j++) {  
            if (max[j] > 0) {  
  
                StringBuffer sb = new StringBuffer();  
                for (i = maxIndex[j] - max[j] + 1; i <= maxIndex[j]; i++) {  
                    sb.append(str1[i]);  
                }  
                String lcs = sb.toString();  
                list.add(lcs);  
            }  
        }  
        return list;  
    }  
  
    //http://blog.csdn.net/rabbitbug/article/details/1740557  
    //http://www.java3z.com/cwbwebhome/article/article5/5829.html?id=1547  
    public static void main(String[] args) {  
  
        String str1 = new String("我和你去吹吹风");
        String str2 = new String("我们一起去吹吹风");
        List<String> list = getLCSstring(str1.toCharArray(), str2.toCharArray());  
        for (int i = 0; i < list.size(); i++) {  
            System.out.println("第" + (i + 1) + "个公共子串:" + list.get(i));  
        }  
  
        str1 = new String("adbab1234");  
        str2 = new String("adbbf123s4a");  
        list = getLCSstring(str1.toCharArray(), str2.toCharArray());  
        for (int i = 0; i < list.size(); i++) {  
            System.out.println("第" + (i + 1) + "个公共子串:" + list.get(i));  
        }  
  
    }  
} 