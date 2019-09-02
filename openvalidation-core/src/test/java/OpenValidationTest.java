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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.interfaces.IOpenValidationGenerator;
import io.openvalidation.common.interfaces.IOpenValidationParser;
import io.openvalidation.common.interfaces.IOpenValidationPreprocessor;
import io.openvalidation.common.model.Language;
import io.openvalidation.common.model.OpenValidationResult;
import io.openvalidation.common.model.PreProcessorContext;
import io.openvalidation.core.OpenValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OpenValidationTest {

  @Mock IOpenValidationPreprocessor preprocessor;

  @Mock IOpenValidationParser parser;

  @Mock IOpenValidationGenerator generator;

  @InjectMocks OpenValidation ovInstance;

  @Test
  public void schould_executes_all_components() throws Exception {
    ASTModel ast = new ASTModel();
    Language lang = ovInstance.getOptions().getLanguage();
    ovInstance.setRule("test");
    ovInstance.setSchema("{Wohnort : \"Berlin\"}");
    String expectation = "CODE WAS GENERATED";

    when(preprocessor.process(any(String.class), any(PreProcessorContext.class)))
        .thenReturn("preprocessor");
    when(parser.parse(any(String.class), any())).thenReturn(ast);
    when(generator.generate(any(ASTModel.class), any(Language.class))).thenReturn(expectation);

    OpenValidationResult result = ovInstance.generateCode(false);

    assertThat(result, is(notNullValue()));
    assertThat(result.getResults().size(), is(2));
    assertThat(expectation, is(result.getResults().get(0).getCode()));
  }

  @Test
  public void schould_getsTheResult_blackbox() throws Exception {

    ASTModel ast = new ASTModel();
    Language lang = ovInstance.getOptions().getLanguage();
    ovInstance.setRule("test");
    ovInstance.setSchema("{Wohnort : \"Berlin\"}");
    String expectation = "CODE WAS GENERATED";

    when(preprocessor.process(any(String.class), any(PreProcessorContext.class)))
        .thenReturn("preprocessor");
    when(parser.parse(any(String.class), any())).thenReturn(ast);
    when(generator.generate(any(ASTModel.class), any(Language.class))).thenReturn(expectation);

    OpenValidationResult result = ovInstance.generateCode(false);

    assertThat(result, is(notNullValue()));
    assertThat(result.getResults().size(), is(2));
    assertThat(expectation, is(result.getResults().get(0).getCode()));
  }

  @Test
  public void schould_get_singlefile_result() throws Exception {

    ASTModel ast = new ASTModel();
    ovInstance.setRule("test");
    ovInstance.setSchema("{Wohnort : \"Berlin\"}");
    ovInstance.setSingleFile(true);
    String expectation = "CODE WAS GENERATED";

    when(preprocessor.process(any(String.class), any(PreProcessorContext.class)))
        .thenReturn("preprocessor");
    when(parser.parse(any(String.class), any())).thenReturn(ast);
    when(generator.generate(any(ASTModel.class), any(Language.class))).thenReturn(expectation);

    OpenValidationResult result = ovInstance.generateCode(false);

    assertThat(result, is(notNullValue()));
    assertThat(result.getResults().size(), is(1));
    assertThat(expectation, is(result.getResults().get(0).getCode()));
  }

  @Test
  public void schould_get_2_results() throws Exception {

    ASTModel ast = new ASTModel();
    ovInstance.setRule("test");
    ovInstance.setSchema("{Wohnort : \"Berlin\"}");
    ovInstance.setSingleFile(false);
    String expectation = "CODE WAS GENERATED";

    when(preprocessor.process(any(String.class), any(PreProcessorContext.class)))
        .thenReturn("preprocessor");
    when(parser.parse(any(String.class), any())).thenReturn(ast);
    when(generator.generate(any(ASTModel.class), any(Language.class))).thenReturn(expectation);
    when(generator.generateFramework(any(ASTModel.class), any(Language.class)))
        .thenReturn("HUMLFramework");

    OpenValidationResult result = ovInstance.generateCode(false);

    assertThat(result, is(notNullValue()));
    assertThat(result.getResults().size(), is(2));
    assertThat(expectation, is(result.getResults().get(0).getCode()));
    assertThat(result.getResults().get(1).getCode(), is(notNullValue()));
    assertThat(result.getResults().get(1).getCode(), is("HUMLFramework"));
  }

  private PreProcessorContext getPreprocessorContext() {
    PreProcessorContext ctx = new PreProcessorContext();

    return ctx;
  }
}
