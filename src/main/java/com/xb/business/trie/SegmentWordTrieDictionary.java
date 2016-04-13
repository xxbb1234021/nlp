package com.xb.business.trie;

import java.util.List;

import com.xb.bean.trie.ParticipleTrieNode;
import com.xb.constant.Constant;
import com.xb.utils.res.AutoDetector;
import com.xb.utils.res.ResTools;
import com.xb.utils.res.ResourceLoader;
import org.apache.log4j.Logger;

public class SegmentWordTrieDictionary {
    private static Logger LOGGER = Logger.getLogger(SegmentWordTrieDictionary.class);

    private static SegmentWordTrieDictionary dict = null;
    private ParticipleTrieNode root = new ParticipleTrieNode();

    private SegmentWordTrieDictionary(String fn, String categroy) {
        importDict(fn, categroy);
    }

    public static SegmentWordTrieDictionary getInstance(String fileName, String category) {
        if (dict == null) {
            synchronized (SegmentWordTrieDictionary.class) {
                if (dict == null) {
                    dict = new SegmentWordTrieDictionary(fileName, category);
                }
            }
        }
        return dict;
    }

    private boolean importDict(String fileName, final String category) {
        AutoDetector.loadRes(new ResourceLoader() {
            @Override
            public void clear() {
            }

            @Override
            public void load(List<String> lines) {
                LOGGER.info("初始化");
                for (String line : lines) {
                    add(line);
                }
                LOGGER.info("初始化完毕，数据条数：" + lines.size());
            }

            @Override
            public void add(String line) {
                ParticipleTrieNode node = root;
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    ParticipleTrieNode pNode = node.getChilds().get(Character.valueOf(c));
                    if (pNode == null) {
                        pNode = new ParticipleTrieNode(Character.valueOf(c));
                        node.getChilds().put(Character.valueOf(c), pNode);
                    }
                    node = pNode;
                }
                node.setBound(true);
            }

            @Override
            public void remove(String line) {
            }

        }, ResTools.get(fileName, "classpath:" + fileName), Constant.CHARSET_UTF8);

        return true;
    }

    public boolean search(String words) {
        ParticipleTrieNode node = this.root;

        int i = 0;
        for (i = 0; i < words.length(); i++) {
            char c = words.charAt(i);
            ParticipleTrieNode pNode = node.getChilds().get(Character.valueOf(c));
            if (pNode == null)
                break;
            node = pNode;
        }

        return (i == words.length()) && (node.isBound());
    }

    public ParticipleTrieNode getRoot() {
        return this.root;
    }
}