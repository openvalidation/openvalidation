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

package io.openvalidation.rest.service;

import io.openvalidation.common.model.OpenValidationResult;

public interface OpenValidationService {

  // TODO funktion des Service muss genauer spezifiziert werden (constraints an parameter und Umgang
  // mit invaliden parametern

  OpenValidationResult generate(OVParams openValidationParameters) throws Exception;

  //    OpenValidationResult generate(Map<String, String> parameterMap) throws Exception;

  // CodeGenerationResult generateFramework(OpenValidationParameters openValidationParameters,
  // ASTModel astModel) throws Exception;

  //    CodeGenerationResult generateFramework(Map<String, String> parameterMap, ASTModel astModel)
  // throws Exception;

  //    OpenValidationResult generateCode(OpenValidationParameters openValidationParameters) throws
  // Exception;

  // TODO verify with ilja
  //    OpenValidationResult generateCode(Map<String, String> parameterMap) throws Exception;

  //    CodeGenerationResult generateValidatorFactory(OpenValidationParameters
  // openValidationParameters, Map<String, Object> factoryParameters) throws Exception;

  //    CodeGenerationResult generateValidatorFactory(Map<String, String> parameterMap, Map<String,
  // Object> factoryParameters) throws Exception;
}
