package org.rakietowa.proploc.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.rakietowa.proploc.data.IPropContainer;
import org.rakietowa.proploc.data.IPropertyPersister;

public class ProplocIntegrator {

	private IPropertyPersister propPersister;

	public ProplocIntegrator(IPropertyPersister persisterImpl) {
		propPersister = persisterImpl;
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
				IPropContainer base = propPersister.readPropertyFile(currFiles.get(baseFile).getPath());
				IPropContainer lang = propPersister.readPropertyFile(currFiles.get(fname).getPath());
				IPropContainer newTrans = propPersister.readPropertyFile(translatedFiles.get(fname).getPath());

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
