package org.rakietowa.proploc.data;

import java.util.List;

public interface IPropContainer {

	List<String> getKeys();

	String getStringValue(String key);

}