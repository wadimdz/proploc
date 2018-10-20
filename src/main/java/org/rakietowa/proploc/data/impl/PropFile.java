package org.rakietowa.proploc.data.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration2.Configuration;
import org.rakietowa.proploc.data.IPropContainer;

public class PropFile implements IPropContainer {
	private Configuration conf;

	public PropFile(Configuration configuration) {
		conf = configuration;
	}

	public Configuration getConfiguration() {
		return conf;
	}

	/* (non-Javadoc)
	 * @see org.rakietowa.proploc.data.IPropContainer#getKeys()
	 */
	@Override
	public List<String> getKeys() {
		List<String> ret = new ArrayList<String>();
		Iterator<String> it = conf.getKeys();

		while (it.hasNext()) {
			ret.add(it.next());
		}

		return ret;
	}

	/* (non-Javadoc)
	 * @see org.rakietowa.proploc.data.IPropContainer#getStringValue(java.lang.String)
	 */
	@Override
	public String getStringValue(String key) {
		return conf.getString(key);
	}

}
