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

package io.openvalidation.common.unittesting.astassertion;

import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.utils.Console;
import io.openvalidation.common.utils.StringUtils;
import java.util.List;

public class ASTAssertionBase<P extends ASTAssertionBase> {
  // protected boolean _hasErrors = false;
  protected String _name = "";
  protected int _index = 0;
  protected ASTModel ast;
  protected P parent;

  public ASTAssertionBase(String name, ASTModel ast, P parent) {
    this(name, 0, ast, parent);
  }

  public ASTAssertionBase(String name, int index, ASTModel ast, P parent) {
    this._name = name;
    this._index = index;
    this.ast = ast;
    this.parent = parent;
  }

  protected void writeExpectedAndActual(String expected, String actual) {
    this.error(getExpectedPrefix() + expected + "\n" + getActualPrefix() + actual);
  }

  protected void writeExpected(String message) {
    this.error(getExpectedPrefix() + message);
  }

  protected void writeActual(String message) {
    this.error(getActualPrefix() + message);
  }

  protected void info(String message) {
    Console.print("\n" + message);
  }

  protected void error(String message) {
    // _hasErrors = true;

    try {
      Console.error("\n" + message);
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (ast != null) {
      System.out.println("\n\n" + ast.print() + "\n\n");
    } else System.out.println("\n\nAST MODEL is NULL\n\n");

    throw new RuntimeException();
  }

  protected String getExpectedPrefix() {
    return "EXPECTED  " + this._name + this.getPositionString();
  }

  protected String getActualPrefix() {
    return "ACTUAL    " + this._name + this.getPositionString();
  }

  protected String getPositionString() {

    if (!StringUtils.isNullOrEmpty(getIdentifier())) return " '" + getIdentifier() + "' ";
    else return (this._index > -1) ? " at Position [" + this._index + "] " : " ";
  }

  protected String getIdentifier() {
    return null;
  }

  protected ASTAssertionBase shouldNotBeEmpty() {
    return this;
  }

  public P parent() {
    return this.parent;
  }

  protected <A extends ASTAssertionBase> A parentAssertion(Class<A> cls) {
    shouldBeInstanceOf(parent(), cls, "PARENT");
    return (A) parent;
  }

  // asserts validators

  protected void shouldBeTrue(boolean value, String name) {
    if (!value) writeExpectedAndActual(name + " : true", name + " : false");
  }

  protected void shouldBeFalse(boolean value, String name) {
    if (value) writeExpectedAndActual(name + " : false", name + " : true");
  }

  protected void shouldNotBeNull(Object obj, String name) {
    if (obj == null) writeExpected(name + " should not be null");
  }

  protected void shouldNotBeEmpty(Object obj, String name) {
    if (obj == null) writeExpected(name + " should not be null");
  }

  protected void shouldNotBeEmpty(String obj, String name) {
    if (obj == null) writeExpected(name + " should not be null");
    if (obj.length() < 1) writeExpected(name + " should not be empty");
  }

  protected <A> void shouldNotBeEmpty(List<A> list, String name) {
    if (list == null) writeExpected(name + " should not be null");
    if (list.size() < 1) writeExpected(name + " should not be empty");
  }

  protected void shouldBeNull(Object obj, String name) {
    if (obj != null) writeExpected(name + " should be null or empty");
  }

  protected void shouldStartsWith(String actual, String expectedStart, String name) {
    if (!actual.startsWith(expectedStart))
      writeExpectedAndActual(name + " : " + expectedStart, name + " : " + actual);
  }

  protected void shouldEquals(String actual, String expected, String name) {
    if (!actual.equals(expected))
      writeExpectedAndActual(name + " : " + expected, name + " : " + actual);
  }

  protected <E extends Enum<E>> void shouldEquals(E actual, E expected, String name) {
    if (actual != expected) writeExpectedAndActual(name + " : " + expected, name + " : " + actual);
  }

  protected void shouldEquals(Double actual, Double expected, String name) {
    if (!actual.equals(expected))
      writeExpectedAndActual(name + " : " + expected, name + " : " + actual);
  }

  protected void shouldEquals(Integer actual, Integer expected, String name) {
    if (!actual.equals(expected))
      writeExpectedAndActual(name + " : " + expected, name + " : " + actual);
  }

  protected void shouldEquals(boolean actual, boolean expected, String name) {
    if (actual != expected) writeExpectedAndActual(name + " : " + expected, name + " : " + actual);
  }

  protected <C> void shouldBeInstanceOf(Object actual, Class<C> expected, String name) {
    shouldNotBeNull(actual, name);

    boolean isInstance = expected.isInstance(actual); // includes inheritance

    if (!isInstance) // actual.getClass().getName() != expected.getName()
    writeExpectedAndActual(
          name + " ist not of Type: " + expected.getSimpleName(),
          name + " is of Type: " + actual.getClass().getSimpleName());
  }

  protected void shouldContains(List<String> list, String expected, String name) {
    shouldNotBeEmpty(list, name);

    if (!list.contains(expected))
      writeExpectedAndActual(name + " " + expected, name + " : " + String.join(", ", list));
  }

  protected <A> void shouldHaveSizeOf(List<A> list, int expectedSize, String name) {
    shouldNotBeNull(list, "LIST with " + name);

    if (list.size() != expectedSize)
      writeExpectedAndActual(
          "SIZE of " + name + " : " + expectedSize, "SIZE of " + name + " is: " + list.size());
  }

  protected <A> void shouldHaveMinSizeOf(List<A> list, int expectedMinSize, String name) {
    shouldNotBeNull(list, "LIST with " + name);

    if (list.size() < expectedMinSize)
      writeExpectedAndActual(
          "SIZE of " + name + " : " + expectedMinSize, "SIZE of " + name + " : " + list.size());
  }

  protected <A> void shouldNotBeOutOfRange(List<A> list, int index) {
    shouldNotBeNull(list, "LIST");

    if (index < 0) writeExpected("INDEX should not be smaller than 0");

    if (index >= list.size())
      writeExpectedAndActual(" INDEX : " + index + " out of Range", " SIZE: " + list.size());
  }
}
