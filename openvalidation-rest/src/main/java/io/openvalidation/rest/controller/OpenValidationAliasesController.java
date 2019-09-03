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

package io.openvalidation.rest.controller;

import io.openvalidation.core.Aliases;
import io.openvalidation.rest.service.OVParamsCultureOnly;
import io.openvalidation.rest.service.OpenValidationResponseStatusException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/aliases")
public class OpenValidationAliasesController {

  @PostMapping
  public ResponseEntity<Map<String, String>> generate(@RequestBody OVParamsCultureOnly language) {
    if (Aliases.availableCultures.contains(language.getCulture()))
      return new ResponseEntity<>(
          Aliases.getAvailableAliases(language.getCulture()), HttpStatus.OK);

    throw new OpenValidationResponseStatusException(
        HttpStatus.BAD_REQUEST, "The given language is not supported");
  }
}
