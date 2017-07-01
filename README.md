## proploc - Java properties l10n tool

Extracts new/untranslated messages from language properties when during
development only master files were updated by developers. Extracted messages can be sent to
translators and later reintegrated in sources.

## Example
Precondition: master property files have no language suffix (for example: mytexts.properties) 
and properties files with other languages have one (for example: mytexts_pl.properties).


If You have two directories with properties from previous (RelX) and current (RelY) release:

```
java -jar proploc.jar -totr RelX RelY
```

would produce a set of files named "UNTRANSLATED_<original_filename>_<lang>.properties".

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
