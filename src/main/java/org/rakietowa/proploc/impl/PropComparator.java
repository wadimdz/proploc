package org.rakietowa.proploc.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.rakietowa.proploc.data.PropFile;

public class PropComparator {
	private PropFile left;
	private PropFile right;

	public PropComparator(String leftName, String rightName) throws ConfigurationException {
		left = new PropFile(leftName);
		right = new PropFile(rightName);
	}

	public PropComparator(PropFile leftProp, PropFile rightProp) {
		left = leftProp;
		right = rightProp;
	}

	public void compareFilesWithPrint() {

		List<String> removedList = new ArrayList<String>(left.getKeys());
		List<String> oList = new ArrayList<String>(left.getKeys());
		List<String> newList = new ArrayList<String>(right.getKeys());
		List<String> nList = new ArrayList<String>(right.getKeys());

		// removed set
		removedList.removeAll(newList);

		// new keys
		newList.removeAll(oList);

		// keys in previous and current version
		nList.removeAll(newList);

		// changed keys
		Map<String, String[]> changed = new LinkedHashMap<String, String[]>();
		for (String key : nList) {
			String oldVal = left.getStringValue(key);
			String newVal = right.getStringValue(key);

			if (!oldVal.equals(newVal)) {
				changed.put(key, new String[] { oldVal, newVal });
			}
		}

		System.out.println("\n<<<<<<<<<<<<<<< ONLY IN LEFT");
		printList(removedList);

		System.out.println("\n>>>>>>>>>>>>>>> ONLY IN RIGHT");
		printList(newList);

		System.out.println("\n=============== DIFFERENCES");
		for (String string : changed.keySet()) {
			System.out.println(string);
			System.out.println("OLD: " + changed.get(string)[0]);
			System.out.println("NEW: " + changed.get(string)[1]);
		}

	}

	private void printList(List<String> nSet) {
		for (String string : nSet) {
			System.out.println(string);
		}
	}

	public List<String> getKeysOnlyInRight() {
		List<String> rightList = new ArrayList<String>(right.getKeys());

		rightList.removeAll(left.getKeys());
		return rightList;
	}

	public Collection<? extends String> getKeysWithChangedContent() {
		List<String> changed = new ArrayList<>();
		for (String key : right.getKeys()) {
			if (left.getKeys().contains(key)) {
				String oldVal = left.getStringValue(key);
				String newVal = right.getStringValue(key);

				if (!oldVal.equals(newVal)) {
					changed.add(key);
				}
			}

		}

		return changed;
	}

	public Collection<? extends String> getKeysWithSameValues() {
		List<String> same = new ArrayList<>();
		for (String key : right.getKeys()) {
			if (left.getKeys().contains(key)) {
				String oldVal = left.getStringValue(key);
				String newVal = right.getStringValue(key);

				if (oldVal.equals(newVal)) {
					same.add(key);
				}
			}		
		}
		return same;
	}

}
