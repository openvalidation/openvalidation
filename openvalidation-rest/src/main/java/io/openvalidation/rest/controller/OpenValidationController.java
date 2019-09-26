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

import io.openvalidation.common.model.OpenValidationResult;
import io.openvalidation.rest.model.dto.GenerationResultDTO;
import io.openvalidation.rest.service.OVParams;
import io.openvalidation.rest.service.OpenValidationResponseStatusException;
import io.openvalidation.rest.service.OpenValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin
@RestController
@RequestMapping("/")
public class OpenValidationController {

  @Autowired private OpenValidationService ovService;

  @PostMapping
  public ResponseEntity<GenerationResultDTO> generate(@RequestBody OVParams parameters) {
    if (parameters == null || parameters.isEmpty()) {
      throw new ResponseStatusException(
          HttpStatus.UNPROCESSABLE_ENTITY,
          "You did not provide any parameters for the generation request. You can browse the API on /swagger-ui.html");
    }

    OpenValidationResult result;

    try {
      result = ovService.generate(parameters);
    } catch (Exception e) {
      e.printStackTrace();
      throw new OpenValidationResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          "Sorry something went wrong. We are working hard to fix the Problem.",
          e);
    }

    GenerationResultDTO generationResultDTO = new GenerationResultDTO(result);
    if (result.hasErrors()) {
      return new ResponseEntity<>(generationResultDTO, HttpStatus.I_AM_A_TEAPOT);
    }

    return new ResponseEntity<>(generationResultDTO, HttpStatus.OK);
  }
}
