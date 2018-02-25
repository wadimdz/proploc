package org.rakietowa.proploc.data;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileHandler;

public class FilePropertyPersister implements IPropertyPersister {

	@Override
	public Map<String, File> listFilesInDir(File directory) {
		Map<String, File> ret = new HashMap<>();
		for (final File fileEntry : directory.listFiles()) {
			if (!fileEntry.isDirectory() && fileEntry.getName().matches(".*\\.properties")) {
				ret.put(fileEntry.getName(), fileEntry);
			}
		}
		return ret;
	}

	@Override
	public PropFile readPropertyFile(String path) {
		try {
			Parameters parameters = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(
					PropertiesConfiguration.class);
			builder.configure(parameters.fileBased().setFileName(path));
			FileBasedConfiguration conf = builder.getConfiguration();

			return new PropFile(conf);
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void savePropertyFile(String filePath, Map<String, String> data) {

		Parameters params = new Parameters();
		File propertiesFile = new File(filePath);

		try {
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(
					PropertiesConfiguration.class).configure(params.fileBased());
			PropertiesConfiguration config = (PropertiesConfiguration) builder.getConfiguration();
			FileHandler handler = new FileHandler(config);

			for (String key : data.keySet()) {
				config.addProperty(key, data.get(key));
			}

			if (!config.isEmpty()) {
				handler.save(propertiesFile);
			}
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
