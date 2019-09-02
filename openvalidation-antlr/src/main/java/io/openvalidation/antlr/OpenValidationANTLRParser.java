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

package io.openvalidation.antlr;

import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.data.DataSchema;
import io.openvalidation.common.interfaces.IOpenValidationParser;
import io.openvalidation.common.log.ProcessLogger;

public class OpenValidationANTLRParser implements IOpenValidationParser {

  @Override
  public ASTModel parse(String plainRule, DataSchema schema) {
    try {
      ASTModel model = ANTLRExecutor.run(plainRule, schema);
      ProcessLogger.success(ProcessLogger.PARSER);

      return model;
    } catch (Exception e) {
      ProcessLogger.error(ProcessLogger.PARSER, e);

      //            if (e instanceof OpenValidationException) {
      //                throw new OpenValidationException(e.getMessage(), e)
      //            }

      throw e;
    }
  }
}
