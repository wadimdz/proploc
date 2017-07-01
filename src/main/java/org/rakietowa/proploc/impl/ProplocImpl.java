package org.rakietowa.proploc.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileHandler;
import org.rakietowa.proploc.data.PropFile;

public class ProplocImpl {

	public void findUntranslated(String prev, String curr) {

		Map<String, File> prevFiles = listFilesInDir(new File(prev));
		Map<String, File> currFiles = listFilesInDir(new File(curr));

		List<String> basePropNames = findBaseProperties(currFiles.keySet());

		for (String baseFile : basePropNames) {
			try {
				System.out.println("Found " + baseFile + ". Looking for translations:");
				processOneProperty(baseFile, currFiles, prevFiles);
			} catch (ConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void processOneProperty(String baseFile, Map<String, File> currFiles, Map<String, File> prevFiles)
			throws ConfigurationException {
		// find nnn_ll.properties files for baseFile
		String pattern = baseFile.replace(".properties", "_..\\.properties");
		PropFile base = new PropFile(currFiles.get(baseFile).getPath());

		for (String fname : currFiles.keySet()) {
			if (fname.matches(pattern)) {
				System.out.print(" * processing " + fname + "... ");
				PropFile lang = new PropFile(currFiles.get(fname).getPath());
				PropComparator pc = new PropComparator(lang, base);

				// find keys from base that are not present in lang
				List<String> result = pc.getKeysOnlyInRight();

				// add those with same text in base and lang
				result.addAll(pc.getKeysWithSameValues());

				// add changed (base vs old) keys
				if (prevFiles.containsKey(baseFile)) {
					pc = new PropComparator(new PropFile(prevFiles.get(baseFile).getPath()), base);
					result.addAll(pc.getKeysWithChangedContent());
				}

				// save to lang-untranslated file
				dumpUntranslatedMessages(base, fname, result);
				System.out.println("done.");
			}
		}

	}

	private void dumpUntranslatedMessages(PropFile base, String fname, List<String> result)
			throws ConfigurationException {
		Parameters params = new Parameters();
		File propertiesFile = new File("UNTRANSLATED_" + fname);

		FileBasedConfigurationBuilder<FileBasedConfiguration> builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(
				PropertiesConfiguration.class).configure(params.fileBased());
		PropertiesConfiguration config = (PropertiesConfiguration) builder.getConfiguration();
		FileHandler handler = new FileHandler(config);

		for (String key : result) {
			config.addProperty(key, base.getStringValue(key));
		}
		
		if (!config.isEmpty()) {
			handler.save(propertiesFile);
		}
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

	private Map<String, File> listFilesInDir(final File folder) {
		Map<String, File> ret = new HashMap<>();
		for (final File fileEntry : folder.listFiles()) {
			if (!fileEntry.isDirectory() && fileEntry.getName().matches(".*\\.properties")) {
				ret.put(fileEntry.getName(), fileEntry);
			}
		}
		return ret;
	}

}
