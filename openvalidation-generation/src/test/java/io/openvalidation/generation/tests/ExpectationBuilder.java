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

package io.openvalidation.generation.tests;

import io.openvalidation.common.model.Language;
import io.openvalidation.common.model.Languages;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

public class ExpectationBuilder {
  List<Arguments> _arguments = new ArrayList<>();

  public ExpectationBuilder javaResult(String content) {
    this._arguments.add(getArgument(Languages.getLanguage("Java"), content));

    return this;
  }

  public ExpectationBuilder javascriptResult(String content) {
    this._arguments.add(getArgument(Languages.getLanguage("JavaScript"), content));

    return this;
  }

  public ExpectationBuilder csharpResult(String content) {
    this._arguments.add(getArgument(Languages.getLanguage("CSharp"), content));
    return this;
  }

  public ExpectationBuilder nodejsScript(String content) {
    this._arguments.add(getArgument(Languages.getLanguage("Node"), content));
    return this;
  }

  public Arguments getArgument(Language language, String content) {
    return Arguments.of(language.getName().toLowerCase(), content);
  }

  public Stream<Arguments> toStream() {
    return Stream.of(_arguments.toArray(new Arguments[0]));
  }

  public static ExpectationBuilder newExpectation() {
    return new ExpectationBuilder();
  }
}
