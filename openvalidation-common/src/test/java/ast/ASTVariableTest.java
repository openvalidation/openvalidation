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

package ast;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import io.openvalidation.common.ast.ASTVariable;
import org.junit.jupiter.api.Test;

class ASTVariableTest {

  @Test
  void test_setName_codeSafeName_with_only_characters() {
    // assemble
    String name = "name";
    ASTVariable variable = new ASTVariable();

    // act
    variable.setName(name);

    // assert
    assertThat(variable.getCodeSafeName(), is(name));
  }

  @Test
  void test_setName_codeSafeName_with_whitespace() {
    // assemble
    String name = "the name of the variable";
    ASTVariable variable = new ASTVariable();

    // act
    variable.setName(name);

    // assert
    assertThat(variable.getCodeSafeName(), is("the_20_name_20_of_20_the_20_variable"));
  }

  @Test
  void test_setName_codeSafeName_with_ampersand() {
    // assemble
    String name = "this&that";
    ASTVariable variable = new ASTVariable();

    // act
    variable.setName(name);

    // assert
    assertThat(variable.getCodeSafeName(), is("this_26_that"));
  }

  @Test
  void test_setName_codeSafeName_with_plus() {
    // assemble
    String name = "this+that";
    ASTVariable variable = new ASTVariable();

    // act
    variable.setName(name);

    // assert
    assertThat(variable.getCodeSafeName(), is("this_2b_that"));
  }

  @Test
  void test_setName_codeSafeName_with_uncommon_chars() {
    // assemble
    String name = "kaldırılmamıştır";
    ASTVariable variable = new ASTVariable();

    // act
    variable.setName(name);

    // assert
    assertThat(variable.getCodeSafeName(), is("kald_131_r_131_lmam_131__15f_t_131_r"));
  }
}
