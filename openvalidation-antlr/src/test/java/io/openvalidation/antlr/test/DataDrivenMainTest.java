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

package io.openvalidation.antlr.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class DataDrivenMainTest {
  @TestFactory
  List<DynamicTest> DataDrivenMainTest() {
    List<DynamicTest> tests = new ArrayList<>();
    try {
      for (DataDrivenMainTestContainer test : DataDrivenMainTestContainer.LoadTests()) {
        DynamicTest dynamic =
            DynamicTest.dynamicTest(
                test.getName(),
                () -> {
                  if (test.isVerbose()) {
                    test.printOutput();
                    test.printIgnoredTestStats();
                  }

                  assertThat(test.getActual(), equalTo(test.getExpected()));
                });

        tests.add(dynamic);
      }
    } catch (Exception e) {

    }
    return tests;
  }
}
