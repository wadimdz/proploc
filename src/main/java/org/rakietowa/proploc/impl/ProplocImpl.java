package org.rakietowa.proploc.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.rakietowa.proploc.data.IPropertyPersister;
import org.rakietowa.proploc.data.PropFile;

public class ProplocImpl {

	private IPropertyPersister propPersister;

	public ProplocImpl(IPropertyPersister persisterImpl) {
		propPersister = persisterImpl;
	}

	public void findUntranslated(String prev, String curr) {

		Map<String, File> prevFiles = propPersister.listFilesInDir(new File(prev));
		Map<String, File> currFiles = propPersister.listFilesInDir(new File(curr));

		List<String> basePropNames = findBaseProperties(currFiles.keySet());

		for (String baseFile : basePropNames) {
			System.out.println("Found " + baseFile + ". Looking for translations:");
			processOneProperty(baseFile, currFiles, prevFiles);
		}

	}

	public void integrateTranslated(String currentDir, String translationDir, String destDir) {
		Map<String, File> currFiles = propPersister.listFilesInDir(new File(currentDir));
		Map<String, File> translatedFiles = propPersister.listFilesInDir(new File(translationDir));

		List<String> basePropNames = findBaseProperties(currFiles.keySet());
		for (String baseFile : basePropNames) {
			updateOneFile(baseFile, currFiles, translatedFiles, destDir);

		}
	}

	private void updateOneFile(String baseFile, Map<String, File> currFiles, Map<String, File> translatedFiles,
			String destDir) {
		// find nnn_ll.properties files for baseFile
		String pattern = baseFile.replace(".properties", "_..\\.properties");

		for (String fname : currFiles.keySet()) {
			if (fname.matches(pattern)) {
				System.out.print(" * processing " + fname + "... ");
				if (!translatedFiles.containsKey(fname)) {
					System.out.println(" no new translations found. Skipped.");
					continue;
				}
				PropFile base = propPersister.readPropertyFile(currFiles.get(baseFile).getPath());
				PropFile lang = propPersister.readPropertyFile(currFiles.get(fname).getPath());
				PropFile newTrans = propPersister.readPropertyFile(translatedFiles.get(fname).getPath());

				Map<String, String> newContents = new LinkedHashMap<>();
				for (String key : base.getKeys()) {
					String value = newTrans.getStringValue(key) != null ? newTrans.getStringValue(key)
							: lang.getStringValue(key);
					if (value != null && StringUtils.isNotEmpty(value.trim())) {
						newContents.put(key, value);
					}
				}

				if (!newContents.isEmpty()) {
					System.out.println(" [OK]");
					writePropertyFile(fname, destDir, newContents);
				}
			}
		}

	}

	private void writePropertyFile(String fname, String destDir, Map<String, String> newContents) {
		propPersister.savePropertyFile(destDir + "/" + fname, newContents);
	}

	private void processOneProperty(String baseFile, Map<String, File> currFiles, Map<String, File> prevFiles) {
		// find nnn_ll.properties files for baseFile
		String pattern = baseFile.replace(".properties", "_..\\.properties");
		PropFile base = propPersister.readPropertyFile(currFiles.get(baseFile).getPath());

		for (String fname : currFiles.keySet()) {
			if (fname.matches(pattern)) {
				System.out.print(" * processing " + fname + "... ");
				PropFile lang = propPersister.readPropertyFile(currFiles.get(fname).getPath());
				PropComparator pc = new PropComparator(lang, base);

				// find keys from base that are not present in lang
				Set<String> result = new LinkedHashSet<>(pc.getKeysOnlyInRight());

				// add those with same text in base and lang
				result.addAll(pc.getKeysWithSameValues());

				// add changed (base vs old) keys
				if (prevFiles.containsKey(baseFile)) {
					pc = new PropComparator(propPersister.readPropertyFile(prevFiles.get(baseFile).getPath()), base);
					result.addAll(pc.getKeysWithChangedContent());
				}

				// save to lang-untranslated file
				dumpUntranslatedMessages(base, fname, result);
				System.out.println("done.");
			}
		}
	}

	private void dumpUntranslatedMessages(PropFile base, String fname, Collection<String> result) {
		Map<String, String> props = new LinkedHashMap<>();
		for (String key : result) {
			props.put(key, base.getStringValue(key));
		}

		propPersister.savePropertyFile("UNTRANSLATED_" + fname, props);
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
