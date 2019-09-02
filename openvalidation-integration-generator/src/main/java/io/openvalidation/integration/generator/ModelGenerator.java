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

package io.openvalidation.integration.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.codemodel.JCodeModel;
import io.openvalidation.common.utils.JsonUtils;
import java.io.File;
import java.io.IOException;
import org.jsonschema2pojo.*;
import org.jsonschema2pojo.rules.RuleFactory;

public class ModelGenerator {

  public static void generate(String json, String outDir, String packageName, String className)
      throws IOException {
    convert2JSON(json, new File(outDir), packageName, className);
  }

  public static void convert2JSON(
      String json, File outputPojoDirectory, String packageName, String className)
      throws IOException {
    JCodeModel codeModel = new JCodeModel();

    GenerationConfig config =
        new DefaultGenerationConfig() {
          @Override
          public boolean isGenerateBuilders() { // set config option by overriding method
            return true;
          }

          public SourceType getSourceType() {
            return SourceType.JSON;
          }

          @Override
          public char[] getPropertyWordDelimiters() {
            return new char[] {'-', ' '};
          }

          @Override
          public boolean isUseBigDecimals() {
            return true;
          }
        };

    String jsn = JsonUtils.loadJson(json).toString();

    SchemaMapper mapper =
        new SchemaMapper(
            new RuleFactory(config, new Jackson2Annotator(config), new SchemaStore()),
            new SchemaGenerator());
    mapper.generate(codeModel, className, packageName, jsn);

    codeModel.build(outputPojoDirectory);
  }

  public static <T> T fromJson(String json, Class<T> type) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    String jsn = JsonUtils.loadJson(json).toString();

    return mapper.readValue(jsn, type);
  }
}
