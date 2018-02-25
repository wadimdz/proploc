package org.rakietowa.proploc.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration2.Configuration;

public class PropFile {
	private Configuration conf;

	public PropFile(Configuration configuration) {
		conf = configuration;
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
