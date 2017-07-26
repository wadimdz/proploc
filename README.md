## proploc - Java properties l10n tool

Extracts new/changed messages from language properties when during
development only master files were updated by developers. Extracted messages can be sent to
translators and later reintegrated in sources.

## Example
Precondition: master property files have no language suffix (for example: mytexts.properties) 
and properties files with other languages have one (for example: mytexts_pl.properties).

### Extracting
If You have two directories with properties from previous (RelX) and current (RelY) release:

```
java -jar proploc.jar -totr RelX RelY
```

would produce a set of files named "UNTRANSLATED_<original_filename>_<lang_code>.properties" containing properties from <original_filename>.properties which have no translation in language-specific properties file or the master text was changed between RelX and RelY.

### Integration
When translations are done, then out them in a directory (with "<original_filename>_<lang_code>.properties" names) and run:

```
java -jar proploc.jar -integrate RelY translations RelYnew
```

Then the "RelYnew" directory will contain translated files which use strings from translated files.  

## License

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
