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

package io.openvalidation.common.validation;

import io.openvalidation.common.exceptions.OpenValidationException;
import io.openvalidation.common.model.Language;
import io.openvalidation.common.model.Languages;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Validator {

  public static final String CANNOT_BE_EMPTY_MESSAGE = " cannot be empty";

  public static void mustBeExistingLanguage(String value, String paramName)
      throws OpenValidationException {
    Language lang = Languages.getLanguage(value);
    if (lang == null)
      throw new OpenValidationException(
          paramName + " is not a valid language.",
          getUserErrorMessage(ValidatorErrorKind.NotValid, paramName));
  }

  public static void shouldNotBeEmpty(String value, String paramName)
      throws OpenValidationException {
    shouldNotBeEmpty(value, paramName, getUserErrorMessage(null, paramName));
  }

  public static void shouldNotBeEmpty(String value, String paramName, ValidatorErrorKind errorKind)
      throws OpenValidationException {
    shouldNotBeEmpty(value, paramName, getUserErrorMessage(errorKind, paramName));
  }

  public static void shouldNotBeEmpty(String value, String paramName, String userMessage)
      throws OpenValidationException {
    if (value == null || value.trim().length() == 0)
      throw new OpenValidationException(paramName + CANNOT_BE_EMPTY_MESSAGE, userMessage);
  }

  public static void contentsShouldNotBeEmpty(String[] values, String paramName)
      throws OpenValidationException {
    if (values != null && values.length > 0) {
      for (int i = 0; i < values.length; i++) {
        shouldNotBeEmpty(
            values[i], paramName + ":" + i, getUserErrorMessage(null, paramName + ":" + i));
      }
    }
  }

  public static void shouldNotBeEmpty(Object value, String paramName)
      throws OpenValidationException {
    shouldNotBeEmpty(value, paramName, getUserErrorMessage(null, paramName));
  }

  public static void shouldNotBeEmpty(Object value, String paramName, ValidatorErrorKind errorKind)
      throws OpenValidationException {
    shouldNotBeEmpty(value, paramName, getUserErrorMessage(errorKind, paramName));
  }

  public static void shouldNotBeEmpty(Object value, String paramName, String userMessage)
      throws OpenValidationException {
    if (value == null)
      throw new OpenValidationException(paramName + CANNOT_BE_EMPTY_MESSAGE, userMessage);
  }

  public static <T> void shouldNotBeEmpty(List<T> value, String paramName)
      throws OpenValidationException {
    shouldNotBeEmpty(value, paramName, getUserErrorMessage(null, paramName));
  }

  public static <T> void shouldNotBeEmpty(
      List<T> value, String paramName, ValidatorErrorKind errorKind)
      throws OpenValidationException {
    shouldNotBeEmpty(value, paramName, getUserErrorMessage(errorKind, paramName));
  }

  public static <T> void shouldNotBeEmpty(List<T> value, String paramName, String userMessage)
      throws OpenValidationException {
    if (value == null || value.size() < 1)
      throw new OpenValidationException(paramName + " cannot be empty", userMessage);
  }

  public static <T> void shouldHaveSizeOf(T[] value, int size, String paramName)
      throws OpenValidationException {
    shouldHaveSizeOf(
        Arrays.stream(value).collect(Collectors.toList()),
        size,
        paramName,
        getUserErrorMessage(null, paramName));
  }

  public static <T> void shouldHaveSizeOf(List<T> value, int size, String paramName)
      throws OpenValidationException {
    shouldHaveSizeOf(value, size, paramName, getUserErrorMessage(null, paramName));
  }

  public static <T> void shouldHaveSizeOf(
      List<T> value, int size, String paramName, ValidatorErrorKind errorKind)
      throws OpenValidationException {
    shouldHaveSizeOf(value, size, paramName, getUserErrorMessage(errorKind, paramName));
  }

  public static <T> void shouldHaveSizeOf(
      List<T> value, int size, String paramName, String userMessage)
      throws OpenValidationException {
    shouldNotBeEmpty(value, paramName, userMessage);

    if (value.size() != size)
      throw new OpenValidationException(paramName + " cannot be empty", userMessage);
  }

  public static <T> void shouldHaveSizeBetween(
      T[] value, int minSize, int maxSize, String paramName) throws OpenValidationException {
    shouldHaveSizeBetween(
        Arrays.stream(value).collect(Collectors.toList()),
        minSize,
        maxSize,
        paramName,
        getUserErrorMessage(null, paramName));
  }

  public static <T> void shouldHaveSizeBetween(
      List<T> value, int minSize, int maxSize, String paramName, String userMessage)
      throws OpenValidationException {
    shouldNotBeEmpty(value, paramName, userMessage);

    if (value.size() < minSize || value.size() > maxSize)
      throw new OpenValidationException(
          paramName + " must have valid size between " + minSize + ", " + maxSize, userMessage);
  }

  public static void shouldBeTrue(boolean value, String paramName) throws OpenValidationException {
    if (!value) throw new OpenValidationException(paramName + " is not true");
  }

  public static void shouldBeFals(boolean value, String paramName) throws OpenValidationException {
    if (value) throw new OpenValidationException(paramName + " is not false");
  }

  public static <T> void shouldEquals(
      T leftOperand, T rightOperand, String leftName, String rightName)
      throws OpenValidationException {
    boolean result = false;

    if (leftOperand != null && rightOperand != null) result = leftOperand.equals(rightOperand);

    if (leftOperand == null && rightOperand == null) result = true;

    if (!result)
      throw new OpenValidationException(
          getUserErrorMessage(ValidatorErrorKind.Equals, leftName, rightName));
  }

  public static <T> void shouldNotEquals(
      T leftOperand, T rightOperand, String leftName, String rightName)
      throws OpenValidationException {
    boolean result = true;

    if (leftOperand != null && rightOperand != null) result = leftOperand.equals(rightOperand);

    if (leftOperand == null && rightOperand == null) result = true;

    if (result)
      throw new OpenValidationException(
          getUserErrorMessage(ValidatorErrorKind.NotEquals, leftName, rightName));
  }

  public static String getUserErrorMessage(
      ValidatorErrorKind kind, String paramName, String paramName2) {
    return String.format(getUserErrorMessage(kind), paramName, paramName2);
  }

  public static String getUserErrorMessage(ValidatorErrorKind kind, String paramName) {
    return String.format(getUserErrorMessage(kind), paramName);
  }

  public static String getUserErrorMessage(ValidatorErrorKind kind) {
    if (kind == null) return "%s should not be empty.";

    switch (kind) {
      case Required:
        return "%s required.";
      case NotSet:
        return "No %s was set.";
      case Equals:
        return "%s should equal %s";
      case NotEquals:
        return "%s should not be equal %s";
      case Empty:
        return "%s should be empty.";
      case NotEmpty:
        return "%s should not be empty.";
      case NotValid:
        return "%s is not valid.";
      default:
        return "%s should not be empty.";
    }
  }

  public enum ValidatorErrorKind {
    Required,
    NotSet,
    Equals,
    NotEquals,
    Empty,
    NotEmpty,
    NotValid
  }
}
