/**
   Copyright 2017 Wadim Dziedzic

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package org.rakietowa.proploc;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.rakietowa.proploc.data.IPropertyPersister;
import org.rakietowa.proploc.data.impl.FilePropertyPersister;
import org.rakietowa.proploc.impl.PropComparator;
import org.rakietowa.proploc.impl.ProplocImpl;

/**
 * Property based l10n helper.
 * 
 * @author wdziedzi
 *
 */
public class ProplocMain {

	public static void main(String[] args) throws ConfigurationException {
		if (args.length < 3) {
			printInfo();
			return;
		}
		
		IPropertyPersister persisterImpl = new FilePropertyPersister();

		if (args[0].equals("-diff")) {
			// compare properties
			PropComparator comp = new PropComparator(args[1], args[2], persisterImpl);
			comp.compareFilesWithPrint();
		} else if (args[0].equals("-totr")) {
			// create files with untranslated strings
			ProplocImpl impl = new ProplocImpl(persisterImpl);
			impl.findUntranslated(args[1], args[2]);
		} else if (args[0].equals("-check")) {
			// find duplicates, ...
		} else if (args[0].equals("-integrate")) {
			if(args.length < 4) {
				printInfo();
				return;
			}
			ProplocImpl impl = new ProplocImpl(persisterImpl);
			impl.integrateTranslated(args[1], args[2], args[3]);
		}

	}

	private static void printInfo() {
		System.out.println("Helper for property based l10n - version 0.2.\n" + "Usage: proploc.jar COMMAND\n"
				+ "Commands:\n" 
				+ " -diff FILE FILE\t\t\t compares two property files\n"
				+ " -totr PREV_DIR CURR_DIR \t\t finds keys to be translated for all language in CURR_DIR and saves them in new .properties files\n"
				+ " -integrate CURR_DIR TR_DIR DEST_DIR\t integrates ready translations (TR_DIR) into files from CURR_DIR and writes results in DEST_DIR\n"
				);
	}
}
