package org.rakietowa.proploc.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class PropFile {
	private Parameters parameters = new Parameters();

	private FileBasedConfigurationBuilder<FileBasedConfiguration> builder;

	private Configuration conf;

	public PropFile(String fname) throws ConfigurationException {
		builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class);
		builder.configure(parameters.fileBased().setFileName(fname));
		conf = builder.getConfiguration();
	}

	public Configuration getConfiguration() {
		return conf;
	}

	public List<String> getKeys() {
		List<String> ret = new ArrayList<String>();
		Iterator<String> it = conf.getKeys();

		while (it.hasNext()) {
			ret.add(it.next());
		}

		return ret;
	}

	public String getStringValue(String key) {
		return conf.getString(key);
	}

}
