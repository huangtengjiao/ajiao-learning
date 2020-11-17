import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class LikeHashMap extends HashMap {
	public Set keySet() {
		Set set = super.keySet();
		TreeSet tSet = null;
		if (set != null) {
			// 对已存在的key进行排序
			tSet = new TreeSet(set);
		}
		return tSet;
	}

	public List<Object> get(String key, boolean like) {
		List<Object> value = new ArrayList<Object>();
		// 是否为模糊搜索
		if (like) {
			List<String> keyList = new ArrayList<String>();
			TreeSet<String> treeSet = (TreeSet) this.keySet();
			for (String string : treeSet) {
				// 通过排序后,key是有序的.
				if (string.indexOf(key) != -1) {
					keyList.add(string);
					value.add(this.get(string));
				} else if (string.indexOf(key) == -1 && keyList.size() == 0) {
					// 当不包含这个key时而且key.size()等于0时,说明还没找到对应的key的开始
					continue;
				} else {
					// 当不包含这个key时而且key.size()大于0时,说明对应的key到当前这个key已经结束.不必要在往下找
					break;
				}
			}
			keyList.clear();
			keyList = null;
		} else {
			value.add(this.get(key));
		}
		return value;
	}

	public static void main(String[] args) {
		LikeHashMap hMap = new LikeHashMap();
		for (int i = 0; i < 1000; i++) {
			hMap.put("A_" + i, "BBBS" + i);
		}
		long time = System.currentTimeMillis();
		System.out.println(hMap.get("A", true).size());
		System.out.println(System.currentTimeMillis() - time);
	}
}