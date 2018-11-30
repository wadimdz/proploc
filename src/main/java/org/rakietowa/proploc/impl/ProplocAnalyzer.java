package org.rakietowa.proploc.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.rakietowa.proploc.data.IPropContainer;
import org.rakietowa.proploc.data.IPropertyPersister;
import org.rakietowa.proploc.data.impl.FindUntranslatedParam;

public class ProplocAnalyzer {

	private IPropertyPersister propPersister;

	private FindUntranslatedParam parameters;

	public ProplocAnalyzer(IPropertyPersister persisterImpl) {
		propPersister = persisterImpl;
	}

	public void findUntranslated(FindUntranslatedParam param) {

		parameters = param;

		Map<String, File> prevFiles = propPersister.listFilesInDir(new File(parameters.getPreviousDir()));
		Map<String, File> currFiles = propPersister.listFilesInDir(new File(parameters.getCurrentDir()));

		List<String> basePropNames = findBaseProperties(currFiles.keySet());

		for (String baseFile : basePropNames) {
			System.out.println("Found " + baseFile + ". Looking for translations:");
			processOneProperty(baseFile, currFiles, prevFiles);
		}
	}

	private void processOneProperty(String baseFile, Map<String, File> currFiles, Map<String, File> prevFiles) {
		// find nnn_ll.properties files for baseFile
		String pattern = baseFile.replace(".properties", "_..\\.properties");
		final IPropContainer baseCurrent = propPersister.readPropertyFile(currFiles.get(baseFile).getPath());
		final IPropContainer basePrevious = propPersister.readPropertyFile(prevFiles.get(baseFile).getPath());

		final Set<String> changedInBase = new LinkedHashSet<>();
		if (prevFiles.containsKey(baseFile)) {
			PropComparator baseComp = new PropComparator(basePrevious, baseCurrent);
			changedInBase.addAll(baseComp.getKeysWithChangedContent());
		}

		for (String fname : currFiles.keySet()) {
			if (fname.matches(pattern)) {
				System.out.print(" * processing " + fname + "... ");
				IPropContainer lang = propPersister.readPropertyFile(currFiles.get(fname).getPath());
				PropComparator pc = new PropComparator(lang, baseCurrent);

				// add changed (base vs old) keys
				Set<String> result = new LinkedHashSet<>(changedInBase);

				// find keys from base that are not present in lang
				result.addAll(pc.getKeysOnlyInRight());

				// save to lang-untranslated file
				saveMessagesToFile(baseCurrent, fname, result);
				System.out.println("done.");

				// those with same text in base and lang will be written to
				// separate file
				Collection<String> keysWithSameValues = pc.getKeysWithSameValues();
				if (!keysWithSameValues.isEmpty()) {
					final String untrFileName = "untr_" + fname;
					System.out.println(" [WARN] Possibly untranslated keys written to: " + untrFileName + "");
					saveMessagesToFile(baseCurrent, untrFileName, keysWithSameValues);
				}
			}
		}
	}

	private void saveMessagesToFile(IPropContainer base, String fname, Collection<String> result) {
		Map<String, String> props = new LinkedHashMap<>();
		for (String key : result) {
			// if there was a translation before then add it too
			String value = base.getStringValue(key);
			props.put(key, value);
		}

		propPersister.savePropertyFile(fname, props);
	}

	private List<String> findBaseProperties(Set<String> keySet) {
		List<String> ret = new ArrayList<>();
		for (String fname : keySet) {
			// ignore all nnnn_ll.properties files
			if (!fname.matches(".*_..\\.properties")) {
				ret.add(fname);
			}
		}
		return ret;
	}
}
