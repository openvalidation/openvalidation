/*
 *    Copyright 2019 BROCKHAUS AG
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import io.openvalidation.core.Aliases;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;

class AliasesDynamicAliasSubstitutionTest {

  @TestFactory
  Collection<DynamicTest> aliasSubstitutionTests_Locale() {
    Collection<DynamicTest> dynamicTests = new ArrayList<>();

    for (String localeString : Aliases.availableCultures) {
      Map<String, String> aliasesMap = Aliases.getAvailableAliases(localeString);

      // todo lazevedo 3.4.19
      for (String key : aliasesMap.keySet()) {
        String inputRule = "anything " + key + " something else";
        String expectedReplacement = aliasesMap.get(key);

        // working directory can be null because INCLUDEs are excluded
        Executable exec =
            () ->
                assertThat(
                    Aliases.resolve(inputRule, new Locale(localeString)),
                    containsString(expectedReplacement));

        String testName = "testReplacementOfKey_" + key;

        DynamicTest dynamicTest = DynamicTest.dynamicTest(testName, exec);

        dynamicTests.add(dynamicTest);
      }
    }
    return dynamicTests;
  }
}
