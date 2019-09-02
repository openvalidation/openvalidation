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
import static org.hamcrest.Matchers.*;

import io.openvalidation.common.exceptions.OpenValidationException;
import io.openvalidation.core.Engine;
import io.openvalidation.core.OpenValidationOptions;
import org.junit.jupiter.api.Test;

public class EngineValidationTest {

  @Test
  public void generateCode_emptyRuleShouldThrowOpenValidationException() throws Exception {
    Throwable expectedException = null;
    Engine engine = new Engine(null, null, null, null);

    try {
      engine.generateCode("", null, null, false);
    } catch (OpenValidationException e) {
      expectedException = e;
    }

    assertThat(expectedException, instanceOf(OpenValidationException.class));
    // Assert.assertTrue(expectedException.getMessage().contains(Validator.CANNOT_BE_EMPTY_MESSAGE));
    // assertThat(expectedException.getMessage().matches(Validator.CANNOT_BE_EMPTY_MESSAGE));
  }

  @Test
  public void validate_ASTModelShouldNotBeEmpty() throws Exception {
    Throwable expectedException = null;
    Engine engine = new Engine(null, null, null, null);

    try {
      engine.validate(null);
    } catch (OpenValidationException e) {
      expectedException = e;
    }

    assertThat(expectedException, instanceOf(OpenValidationException.class));
    // Assert.assertTrue(expectedException.getMessage().contains(Validator.CANNOT_BE_EMPTY_MESSAGE));
  }

  @Test
  public void preprocess_WorkingDirectories_ExpectNullPointerException() throws Exception {
    Throwable expectedException = null;
    Engine engine = new Engine(null, null, null, new OpenValidationOptions());

    try {
      engine.preprocess("", null);
    } catch (OpenValidationException e) {
      expectedException = e;
    } catch (NullPointerException e) {
      expectedException = e;
    }
    assertThat(expectedException, instanceOf(NullPointerException.class));
  }

  @Test
  public void preprocess_WorkingDirectories_WorkingDirectoriesCannotBeEmpty() throws Exception {
    Throwable expectedException = null;
    OpenValidationOptions options = new OpenValidationOptions();
    options.setWorkingDirectories(new String[2]);
    Engine engine = new Engine(null, null, null, options);

    try {
      engine.preprocess("", null);
    } catch (OpenValidationException e) {
      expectedException = e;
    }

    assertThat(expectedException, instanceOf(OpenValidationException.class));
  }
}
