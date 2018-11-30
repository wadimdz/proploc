package org.rakietowa.proploc.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.rakietowa.proploc.data.IPropContainer;
import org.rakietowa.proploc.data.IPropertyPersister;

public class PropComparator {
	private IPropContainer left;
	private IPropContainer right;
	private IPropertyPersister propPersister;

	public PropComparator(String leftName, String rightName, IPropertyPersister persister) {
		propPersister = persister;
		left = propPersister.readPropertyFile(leftName);
		right = propPersister.readPropertyFile(rightName);
	}

	public PropComparator(IPropContainer leftProp, IPropContainer rightProp) {
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

	public Collection<String> getKeysWithChangedContent() {
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

	public Collection<String> getKeysWithSameValues() {
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
