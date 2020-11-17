
import java.util.ArrayList;

/* 
 * 无数据结构设计下的蛮力中文键树 
 */
public class TrieTree_1 {
	private static TrieNode root = null;
	ArrayList<String> searchResult = new ArrayList<String>();
	StringBuffer tempWord = new StringBuffer();
	int start = 0;

	public TrieTree_1() {
		root = new TrieNode(null);
	}

	public void insert(String key) {
		TrieNode p = root;
		String tempWord;
		boolean contains;
		TrieNode tempNode;
		for (int i = 0; i < key.length(); i++) {
			tempWord = String.valueOf(key.charAt(i));
			contains = false;
			for (TrieNode tn : p.ptr) {
				if (tn.value.equals(tempWord)) {
					p = tn;
					contains = true;
					break;
				}
			}
			if (!contains) {
				tempNode = new TrieNode(tempWord);
				p.ptr.add(tempNode);
				p = tempNode;
			}
		}
	}
	
	public ArrayList<String> hit(String key) { // 模糊查询就是这个方法，打个比方比如key是"ap"，那么ArrayList里就有{"apple","application"}
		TrieNode p = root;
		String temp;
		boolean contains = false;
		for (int i = 0; i < key.length(); i++) {
			temp = String.valueOf(key.charAt(i));
			contains = false;
			for (TrieNode tn : p.ptr) {
				if (tn.value.equals(temp)) {
					p = tn;
					contains = true;
					break;
				}
			}
			if (contains) {
				continue;
			} else {
				break;
			}
		}
		if (contains) {
			if (!(p.ptr.isEmpty())) {
				// 查找到关键字
				searchResult.clear();
				tempWord.delete(0, tempWord.length());
				tempWord.append(key);
				start = key.length();
				traverseTree(p);
			} else {
				// 已经查找到键树的底部
				return null;
			}
		} else {
			// 没有查找到相应关键字
			return null;
		}
		return searchResult;
	}

	public ArrayList<String> search(String key) { // 模糊查询就是这个方法，打个比方比如key是"ap"，那么ArrayList里就有{"apple","application"}
		TrieNode p = root;
		String temp;
		boolean contains = false;
		for (int i = 0; i < key.length(); i++) {
			temp = String.valueOf(key.charAt(i));
			contains = false;
			for (TrieNode tn : p.ptr) {
				if (tn.value.equals(temp)) {
					p = tn;
					contains = true;
					break;
				}
			}
			if (contains) {
				continue;
			} else {
				break;
			}
		}
		if (contains) {
			if (!(p.ptr.isEmpty())) {
				// 查找到关键字
				searchResult.clear();
				tempWord.delete(0, tempWord.length());
				tempWord.append(key);
				start = key.length();
				traverseTree(p);
			} else {
				// 已经查找到键树的底部
				return null;
			}
		} else {
			// 没有查找到相应关键字
			return null;
		}
		return searchResult;
	}

	private void traverseTree(TrieNode p) {
		if (!(p.ptr.isEmpty())) {
			for (TrieNode tn : p.ptr) {
				tempWord.append(tn.value);
				start++;
				traverseTree(tn);
				start--;
				tempWord.delete(start, tempWord.length());
			}
		} else {
			searchResult.add(tempWord.toString());
		}
	}

	public static void main(String[] args) {
		String info = "向这棵键树中插入新节点是很简单的，比如插入的新内容是一个\"apple\"的单词，那么将这个字符串依次拆开，逐个在键树中向下寻找（在ArrayList中遍历比较），并最终决定放不放就行（放就add,不放就下一层或者结束），具体实现就是后文中的insert(String key)方法。同样的道理，查找也很简单";

		TrieTree_1 t = new TrieTree_1();
		t.insert(info);

		ArrayList<String> list = t.search("简");
		System.out.println(list);
	}
}

class TrieNode {
	public String value;
	public ArrayList<TrieNode> ptr = null;

	public TrieNode(String value) {
		this.value = value;
		ptr = new ArrayList<TrieNode>();
	}
}