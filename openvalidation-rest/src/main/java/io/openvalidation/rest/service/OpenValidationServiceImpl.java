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

import io.openvalidation.common.exceptions.OpenValidationException;
import io.openvalidation.common.model.Languages;
import io.openvalidation.common.model.OpenValidationResult;
import io.openvalidation.core.OpenValidation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class OpenValidationServiceImpl implements OpenValidationService {

  @Override
  public OpenValidationResult generate(OVParams params)
      throws OpenValidationResponseStatusException {
    return this.generate(params, true);
  }

  @Override
  public OpenValidationResult generate(OVParams params, boolean withCode)
      throws OpenValidationResponseStatusException {
    try {
      OpenValidation ov = OpenValidation.createDefault();

      ov.setLanguage(Languages.getLanguage(params.getLanguage()))
          .setLocale(params.getCulture())
          .setRule(params.getRule())
          .setSchema(params.getSchema());

      return ov.generate(!withCode);
    } catch (OpenValidationException exp) {
      OpenValidationResult result = new OpenValidationResult();
      result.addError(exp);
      return result;
    } catch (Exception e) {
      throw new OpenValidationResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          "We are sorry, something went wrong while processing your request. This could be an Error in the "
              + "OpenValidation core or an Error in the RestService unhandled error in OpenValidation core. "
              + "Please try again later to see if the error was fixed or submit a bug report.\n\nReason: "
              + e.getMessage(),
          e);
    }
  }
}
