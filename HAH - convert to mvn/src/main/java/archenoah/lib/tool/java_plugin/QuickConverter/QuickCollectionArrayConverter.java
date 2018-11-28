package archenoah.lib.tool.java_plugin.QuickConverter;

import java.util.Collection;
import java.util.HashSet;

public class QuickCollectionArrayConverter {

    public static int[] toIntArray(Collection<Integer> coll) {
	Object[] objArr = coll.toArray();
	int[] arr = new int[objArr.length];
	for (int i = 0; i < objArr.length; i++) {
	    arr[i] = (Integer) objArr[i];
	}
	return arr;
    }

    public static HashSet<Integer> toHashSet(int[] arr) {

	HashSet<Integer> hs = new HashSet<Integer>();
	for (int i = 0; i < arr.length; i++) {
	    hs.add(arr[i]);
	}

	return hs;

    }
}
