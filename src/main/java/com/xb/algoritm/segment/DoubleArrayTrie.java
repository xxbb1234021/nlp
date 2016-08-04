package com.xb.algoritm.segment;

import com.xb.bean.doubletrie.DoubleTrieNode;
import com.xb.business.trie.impl.DoubleArrayTrieDictionary;
import com.xb.constant.FileConstant;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DoubleArrayTrie {
	private final static int BUF_SIZE = 16384;
	private final static int UNIT_SIZE = 8; // size of int + int

	private int check[];
	private int base[];

	private boolean used[];
	private int size;
	private int allocSize;
	private List<String> key;
	private int keySize;
	private int length[];
	private int value[];
	private int progress;
	private int nextCheckPos;
	// boolean no_delete_;
	int error;

	public static DoubleArrayTrieDictionary dict = null;

	public DoubleArrayTrie() {
		check = null;
		base = null;
		used = null;
		size = 0;
		allocSize = 0;
		// no_delete_ = false;
		error = 0;

		//加载词典  
        String dictionaryName = FileConstant.DOUBLE_TRIE_TREE;
        dict = DoubleArrayTrieDictionary.getInstance(dictionaryName);
	}

	// int (*progressfunc_) (size_t, size_t);

	// inline _resize expanded
	private int resize(int newSize) {
		int[] base2 = new int[newSize];
		int[] check2 = new int[newSize];
		boolean used2[] = new boolean[newSize];
		if (allocSize > 0) {
			System.arraycopy(base, 0, base2, 0, allocSize);
			System.arraycopy(check, 0, check2, 0, allocSize);
			System.arraycopy(used2, 0, used2, 0, allocSize);
		}

		base = base2;
		check = check2;
		used = used2;

		return allocSize = newSize;
	}

	private int fetch(DoubleTrieNode parent, List<DoubleTrieNode> siblings) {
		if (error < 0)
			return 0;

		int prev = 0;

		for (int i = parent.getLeft(); i < parent.getRight(); i++) {
			//System.out.println((length != null ? length[i] : key.get(i).length()));
			//System.out.println(parent.getDepth());
			if ((length != null ? length[i] : key.get(i).length()) < parent.getDepth())
				continue;

			String tmp = key.get(i);

			int cur = 0;
			if ((length != null ? length[i] : tmp.length()) != parent.getDepth())
				cur = tmp.charAt(parent.getDepth()) + 1;

			if (prev > cur) {
				error = -3;
				return 0;
			}

			if (cur != prev || siblings.size() == 0) {
				DoubleTrieNode tmpNode = new DoubleTrieNode();
				tmpNode.setDepth(parent.getDepth() + 1);
				tmpNode.setCode(cur);
				tmpNode.setLeft(i);
				if (siblings.size() != 0)
					siblings.get(siblings.size() - 1).setRight(i);

				siblings.add(tmpNode);
			}

			prev = cur;
		}

		if (siblings.size() != 0)
			siblings.get(siblings.size() - 1).setRight(parent.getRight());

		return siblings.size();
	}

	private int insert(List<DoubleTrieNode> siblings) {
		if (error < 0)
			return 0;

		int begin = 0;
		int pos = ((siblings.get(0).getCode() + 1 > nextCheckPos) ? siblings.get(0).getCode() + 1 : nextCheckPos) - 1;
		int nonzero_num = 0;
		int first = 0;

		if (allocSize <= pos)
			resize(pos + 1);

		// 此循环体的目标是找出满足base[begin + a1...an]  == 0的n个空闲空间,a1...an是siblings中的n个节点
		outer: while (true) {
			pos++;

			if (allocSize <= pos)
				resize(pos + 1);

			if (check[pos] != 0) {
				nonzero_num++;
				continue;
			} else if (first == 0) {
				nextCheckPos = pos;
				first = 1;
			}

			// 当前位置离第一个兄弟节点的距离
			begin = pos - siblings.get(0).getCode();
			if (allocSize <= (begin + siblings.get(siblings.size() - 1).getCode())) {
				// 防止progress产生除零错误
				double l = (1.05 > 1.0 * keySize / (progress + 1)) ? 1.05 : 1.0 * keySize / (progress + 1);
				resize((int) (allocSize * l));
			}

			if (used[begin])
				continue;

			for (int i = 1; i < siblings.size(); i++)
				if (check[begin + siblings.get(i).getCode()] != 0)
					continue outer;

			break;
		}

		// 从位置 next_check_pos 开始到 pos 间，如果已占用的空间在95%以上，下次插入节点时，直接从 pos 位置处开始查找
		if (1.0 * nonzero_num / (pos - nextCheckPos + 1) >= 0.95)
			nextCheckPos = pos;

		used[begin] = true;
		size = (size > begin + siblings.get(siblings.size() - 1).getCode() + 1) ? size : begin
				+ siblings.get(siblings.size() - 1).getCode() + 1;

		for (int i = 0; i < siblings.size(); i++)
			check[begin + siblings.get(i).getCode()] = begin;

		for (int i = 0; i < siblings.size(); i++) {
			List<DoubleTrieNode> new_siblings = new ArrayList<DoubleTrieNode>();

			if (fetch(siblings.get(i), new_siblings) == 0) {
				base[begin + siblings.get(i).getCode()] = (value != null) ? (-value[siblings.get(i).getLeft()] - 1)
						: (-siblings.get(i).getLeft() - 1);

				if (value != null && (-value[siblings.get(i).getLeft()] - 1) >= 0) {
					error = -2;
					return 0;
				}

				progress++;
				// if (progress_func_) (*progress_func_) (progress,
				// keySize);
			} else {
				int h = insert(new_siblings);
				base[begin + siblings.get(i).getCode()] = h;
			}
		}
		return begin;
	}

	void clear() {
		// if (! no_delete_)
		check = null;
		base = null;
		used = null;
		allocSize = 0;
		size = 0;
		// no_delete_ = false;
	}

	public int getUnitSize() {
		return UNIT_SIZE;
	}

	public int getSize() {
		return size;
	}

	public int getTotalSize() {
		return size * UNIT_SIZE;
	}

	public int getNonzeroSize() {
		int result = 0;
		for (int i = 0; i < size; i++)
			if (check[i] != 0)
				result++;
		return result;
	}

	public int build(List<String> key) {
		return build(key, null, null, key.size());
	}

	public int build(List<String> _key, int _length[], int _value[], int _keySize) {
		if (_keySize > _key.size() || _key == null)
			return 0;

		// progress_func_ = progress_func;
		key = _key;
		length = _length;
		keySize = _keySize;
		value = _value;
		progress = 0;

		resize(65536 * 32);

		base[0] = 1;
		nextCheckPos = 0;

		DoubleTrieNode rooNode = new DoubleTrieNode();
		rooNode.setLeft(0);
		rooNode.setRight(keySize);
		rooNode.setDepth(0);

		List<DoubleTrieNode> siblings = new ArrayList<DoubleTrieNode>();
		fetch(rooNode, siblings);
		insert(siblings);

		// size += (1 << 8 * 2) + 1; // ???
		// if (size >= allocSize) resize (size);

		used = null;
		key = null;

		return error;
	}

	public void open(String fileName) throws IOException {
		File file = new File(fileName);
		size = (int) file.length() / UNIT_SIZE;
		check = new int[size];
		base = new int[size];

		DataInputStream is = null;
		try {
			is = new DataInputStream(new BufferedInputStream(new FileInputStream(file), BUF_SIZE));
			for (int i = 0; i < size; i++) {
				base[i] = is.readInt();
				check[i] = is.readInt();
			}
		} finally {
			if (is != null)
				is.close();
		}
	}

	public void save(String fileName) throws IOException {
		DataOutputStream out = null;
		try {
			out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
			for (int i = 0; i < size; i++) {
				out.writeInt(base[i]);
				out.writeInt(check[i]);
			}
			out.close();
		} finally {
			if (out != null)
				out.close();
		}
	}

	public int exactMatchSearch(String key) {
		return exactMatchSearch(key, 0, 0, 0);
	}

	public int exactMatchSearch(String key, int pos, int len, int nodePos) {
		if (len <= 0)
			len = key.length();
		if (nodePos <= 0)
			nodePos = 0;

		int result = -1;

		char[] keyChars = key.toCharArray();

		int b = base[nodePos];
		int p;

		for (int i = pos; i < len; i++) {
			p = b + (keyChars[i]) + 1;
			if (b == check[p])
				b = base[p];
			else
				return result;
		}

		p = b;
		int n = base[p];
		if (b == check[p] && n < 0) {
			result = -n - 1;
		}
		return result;
	}

	public List<Integer> commonPrefixSearch(String key) {
		return commonPrefixSearch(key, 0, 0, 0);
	}

	public List<Integer> commonPrefixSearch(String key, int pos, int len, int nodePos) {
		if (len <= 0)
			len = key.length();
		if (nodePos <= 0)
			nodePos = 0;

		List<Integer> result = new ArrayList<Integer>();

		char[] keyChars = key.toCharArray();

		int b = base[nodePos];
		int n;
		int p;

		for (int i = pos; i < len; i++) {
			p = b;
			n = base[p];

			if (b == check[p] && n < 0) {
				result.add(-n - 1);
			}

			p = b + (keyChars[i]) + 1;
			if (b == check[p])
				b = base[p];
			else
				return result;
		}

		p = b;
		n = base[p];

		if (b == check[p] && n < 0) {
			result.add(-n - 1);
		}

		return result;
	}

	// debug
	public void dump() {
		for (int i = 0; i < size; i++) {
			System.err.println("i: " + i + " [" + base[i] + ", " + check[i] + "]");
		}
	}

	public static void main(String[] args) {
		DoubleArrayTrie d = new DoubleArrayTrie();
		System.out.println("字典词条：" + dict.getWords().size());

		String infoCharsetValue = "";
		String infoCharsetCode = "";
		for (Character c : dict.getCharset()) {
			infoCharsetValue += c.charValue() + "    ";
			infoCharsetCode += (int) c.charValue() + " ";
		}
		infoCharsetValue += '\n';
		infoCharsetCode += '\n';
		System.out.print(infoCharsetValue);
		System.out.print(infoCharsetCode);

		DoubleArrayTrie dat = new DoubleArrayTrie();
		System.out.println("是否错误: " + dat.build(dict.getWords()));
		System.out.println(dat);
		List<Integer> integerList = dat.commonPrefixSearch("一举成名天下知");
		for (int index : integerList) {
			System.out.println(dict.getWords().get(index));
		}
	}
}
