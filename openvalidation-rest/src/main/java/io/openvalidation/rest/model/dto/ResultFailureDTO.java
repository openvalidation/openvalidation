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

package io.openvalidation.rest.model.dto;

public class ResultFailureDTO {

  //    List<ErrorDTO> errors = new ArrayList<>();
  //
  //    public ResultFailureDTO() {
  //        // serializable
  //    }
  //
  //    public ResultFailureDTO(OpenValidationResult ovResult) {
  //        if (ovResult == null) throw new IllegalArgumentException("OpenValidationResult should
  // not be null");
  //        if (!ovResult.hasErrors()) {
  //            //TODO Exception typ überprüfen
  //            throw new IllegalArgumentException("Cannot create ResultFailureDTO for
  // OpenValidationResult. OpenValidationResult does not contain any errors.");
  //        }
  //        //TODO überprüfen ob alle Errors erfasst wurden
  //        errors = ovResult.getErrors().stream().map((exception) -> {
  //            return new ErrorDTO(exception.getUserMessage(), exception.getMessage());
  //        }).collect(Collectors.toList());
  //    } //TODO: verbose
  //
  //    public List<ErrorDTO> getErrors() {
  //        return errors;
  //    }
  //
  //    //nested Class public oder setter entfernen
  //    public void setErrors(List<ErrorDTO> errors) {
  //        this.errors = errors;
  //    }
  //
  //    private static class ErrorDTO {
  //        String userMessage;
  //        String message;
  //
  //        public ErrorDTO(String userMessage, String message) {
  //            this.userMessage = userMessage;
  //            this.message = message;
  //        }
  //
  //        public String getUserMessage() {
  //            return userMessage;
  //        }
  //
  //        public String getMessage() {
  //            return message;
  //        }
  //    }

}
