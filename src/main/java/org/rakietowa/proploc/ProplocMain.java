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
			System.out.println("Helper for property based l10n - version 0.1.\n" + "Usage: proploc.jar COMMAND\n"
					+ "Commands:\n" + " -diff FILE FILE\t\t compares two property files\n"
					+ " -totr PREV_DIR CURR_DIR \t finds keys to be translated for all language in CURR_DIR and saves them in new .properties files\n");

			return;
		}

		if (args[0].equals("-diff")) {
			// compare properties
			PropComparator comp = new PropComparator(args[1], args[2]);
			comp.compareFilesWithPrint();
		} else if (args[0].equals("-totr")) {
			// create files with untranslated strings
			ProplocImpl impl = new ProplocImpl();
			impl.findUntranslated(args[1], args[2]);
		} else if (args[0].equals("-check")) {
			// find duplicates, ...
		}

	}
}
