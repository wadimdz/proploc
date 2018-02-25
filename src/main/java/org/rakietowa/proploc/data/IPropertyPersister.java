package org.rakietowa.proploc.data;

import java.io.File;
import java.util.Map;

public interface IPropertyPersister {
	
	Map<String, File> listFilesInDir(final File directory);
	
	PropFile readPropertyFile(String path);
	
	void savePropertyFile(String filePath, Map<String, String> data);
}
