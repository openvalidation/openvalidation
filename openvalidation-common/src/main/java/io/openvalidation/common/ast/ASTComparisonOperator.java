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

package io.openvalidation.common.ast;

import io.openvalidation.common.data.DataPropertyType;

public enum ASTComparisonOperator {
  GREATER_THAN,
  GREATER_OR_EQUALS,
  LESS_THAN,
  LESS_OR_EQUALS,
  EQUALS,
  NOT_EQUALS,
  ONE_OF,
  AT_LEAST_ONE_OF,
  ALL_OF,
  NONE_OF,
  IS_BETWEEN,
  CONTAINS,
  EXISTS,
  NOT_EXISTS,
  IS, // ASTComparisonOperator.IS is never used, this defaults to EQUALS
  SUM_OF,
  EMPTY,
  NOT_EMPTY;

  private ASTComparisonOperator invert;

  static {
    GREATER_THAN.invert = LESS_OR_EQUALS;
    LESS_OR_EQUALS.invert = GREATER_THAN;
    GREATER_OR_EQUALS.invert = LESS_THAN;
    LESS_THAN.invert = GREATER_OR_EQUALS;
    EQUALS.invert = NOT_EQUALS;
    NOT_EQUALS.invert = EQUALS;
    ONE_OF.invert = NONE_OF;
    NONE_OF.invert = ONE_OF;
    EXISTS.invert = NOT_EXISTS;
    NOT_EXISTS.invert = EXISTS;
  }

  public ASTComparisonOperator invert() {
    return invert;
  }

  private boolean isSimpleComparisonOperator;

  static {
    GREATER_THAN.isSimpleComparisonOperator = true;
    GREATER_OR_EQUALS.isSimpleComparisonOperator = true;
    LESS_THAN.isSimpleComparisonOperator = true;
    LESS_OR_EQUALS.isSimpleComparisonOperator = true;
    EQUALS.isSimpleComparisonOperator = true;
    NOT_EQUALS.isSimpleComparisonOperator = true;
    ONE_OF.isSimpleComparisonOperator = false;
    AT_LEAST_ONE_OF.isSimpleComparisonOperator = false;
    ALL_OF.isSimpleComparisonOperator = false;
    NONE_OF.isSimpleComparisonOperator = false;
    IS_BETWEEN.isSimpleComparisonOperator = false;
    CONTAINS.isSimpleComparisonOperator = false;
    EXISTS.isSimpleComparisonOperator = false;
    NOT_EXISTS.isSimpleComparisonOperator = false;
    IS.isSimpleComparisonOperator =
        true; // ASTComparisonOperator.IS is never used.needsTwoOperands = true; this defaults to
    // EQUALS
    SUM_OF.isSimpleComparisonOperator = false;
    EMPTY.isSimpleComparisonOperator = false;
    NOT_EMPTY.isSimpleComparisonOperator = false;
  }

  public boolean isSimpleComparisonOperator() {
    return isSimpleComparisonOperator;
  }

  private DataPropertyType validDataType;

  static {
    GREATER_THAN.validDataType = DataPropertyType.Decimal;
    GREATER_OR_EQUALS.validDataType = DataPropertyType.Decimal;
    LESS_THAN.validDataType = DataPropertyType.Decimal;
    LESS_OR_EQUALS.validDataType = DataPropertyType.Decimal;
    EQUALS.validDataType = DataPropertyType.Object;
    NOT_EQUALS.validDataType = DataPropertyType.Object;
    ONE_OF.validDataType = DataPropertyType.Array;
    AT_LEAST_ONE_OF.validDataType = DataPropertyType.Array;
    ALL_OF.validDataType = DataPropertyType.Array;
    NONE_OF.validDataType = DataPropertyType.Array;
    IS_BETWEEN.validDataType = DataPropertyType.Unknown;
    CONTAINS.validDataType = DataPropertyType.String;
    EXISTS.validDataType = DataPropertyType.Array;
    NOT_EXISTS.validDataType = DataPropertyType.Array;
    IS.validDataType = DataPropertyType.Object;
    SUM_OF.validDataType = DataPropertyType.String;
    EMPTY.validDataType = DataPropertyType.String;
    NOT_EMPTY.validDataType = DataPropertyType.String;
  }

  public DataPropertyType validDataType() {
    return validDataType;
  }
}
// {{#resolve-operator operator}}{{/resolve-operator}}
