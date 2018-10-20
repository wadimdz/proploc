package org.rakietowa.proploc.data;

import java.io.File;
import java.util.Map;

public interface IPropertyPersister {
	
	Map<String, File> listFilesInDir(final File directory);
	
	IPropContainer readPropertyFile(String path);
	
	void savePropertyFile(String filePath, Map<String, String> data);
}
