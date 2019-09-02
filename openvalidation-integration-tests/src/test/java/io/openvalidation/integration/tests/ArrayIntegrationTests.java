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

package io.openvalidation.integration.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import io.openvalidation.integration.generator.ModelGenerator;
import io.openvalidation.integration.tests.dummy.CustomModel;
import org.junit.jupiter.api.Test;

public class ArrayIntegrationTests {

  @Test
  public void should_returns_where() throws Exception {
    CustomModel cm =
        ModelGenerator.fromJson(
            "{addresses:[{city:'Dortmund',sizeOfCity:6},{city:'Berlin',sizeOfCity:2}]}",
            CustomModel.class);

    HUMLFramework huml = new HUMLFramework();

    huml.appendRule(
        "",
        new String[] {"name"},
        "EXPECTED ERROR",
        (CustomModel model) ->
            huml.GREATER_THAN(
                huml.SUM_OF(
                    huml.getArrayOf(
                        huml.where(model.getAddresses(), m -> huml.EQUALS(m.getCity(), "Dortmund")),
                        a -> a.getSizeOfCity())),
                5.0),
        false);

    commonAssert(huml.validate(cm));
  }

  @Test
  public void should_returns_firs() throws Exception {
    CustomModel cm =
        ModelGenerator.fromJson(
            "{addresses:[{city:'Köln',sizeOfCity:6},{city:'Dortmund',sizeOfCity:2}]}",
            CustomModel.class);

    HUMLFramework huml = new HUMLFramework();

    huml.appendRule(
        "",
        new String[] {"name"},
        "EXPECTED ERROR",
        (CustomModel model) ->
            huml.EQUALS(huml.FIRST(model.getAddresses(), fitem -> fitem.getSizeOfCity()), 6),
        false);

    commonAssert(huml.validate(cm));
  }

  @Test
  public void should_returns_last() throws Exception {
    CustomModel cm =
        ModelGenerator.fromJson(
            "{addresses:[{city:'Köln',sizeOfCity:6},{city:'Dortmund',sizeOfCity:2}]}",
            CustomModel.class);

    HUMLFramework huml = new HUMLFramework();

    huml.appendRule(
        "",
        new String[] {"name"},
        "EXPECTED ERROR",
        (CustomModel model) ->
            huml.EQUALS(huml.LAST(model.getAddresses(), fitem -> fitem.getSizeOfCity()), 2),
        false);

    commonAssert(huml.validate(cm));
  }

  @Test
  public void should_returns_first_2_items() throws Exception {
    CustomModel cm =
        ModelGenerator.fromJson(
            "{addresses:[{city:'Dortmund'},{city:'Köln'},{city:'Dortmund'},{city:'Köln'},{city:'Dortmund'}]}",
            CustomModel.class);

    HUMLFramework huml = new HUMLFramework();

    huml.appendRule(
        "",
        new String[] {"name"},
        "EXPECTED ERROR",
        (CustomModel model) ->
            huml.EQUALS(
                huml.sizeOf(
                    huml.FIRST(
                        huml.where(
                            model.getAddresses(), whr -> huml.EQUALS(whr.getCity(), "Dortmund")),
                        frst -> frst.getCity(),
                        2)),
                2),
        false);

    commonAssert(huml.validate(cm));
  }

  @Test
  public void should_returns_last_2_items() throws Exception {
    CustomModel cm =
        ModelGenerator.fromJson(
            "{addresses:[{city:'Dortmund'},{city:'Köln'},{city:'Dortmund'},{city:'Köln'},{city:'Dortmund'}]}",
            CustomModel.class);

    HUMLFramework huml = new HUMLFramework();

    huml.appendRule(
        "",
        new String[] {"name"},
        "EXPECTED ERROR",
        (CustomModel model) ->
            huml.EQUALS(
                huml.sizeOf(
                    huml.LAST(
                        huml.where(
                            model.getAddresses(), whr -> huml.EQUALS(whr.getCity(), "Dortmund")),
                        frst -> frst.getCity(),
                        2)),
                2),
        false);

    commonAssert(huml.validate(cm));
  }

  private void commonAssert(HUMLFramework.OpenValidationSummary summary) {
    assertThat(summary, notNullValue());
    assertThat("NO VALIDATION ERROR MESSAGE RECEIVED", summary.hasErrors(), is(true));
    assertThat("ERROR MESSAGES SHOULD NOT BE EMPTY", summary.getErrors(), notNullValue());
    assertThat(
        "ERROR MESSAGES SHOULD CONTAINS AT LEAST 1 MESSAGE",
        summary.getErrors().length,
        greaterThan(0));

    assertThat("FIRST ERROR MESSAGE SHOULD NOT BE NULL", summary.getErrors()[0], notNullValue());
    assertThat(
        "ACTUAL ERROR MESSAGE UNEQUALS THE EXPECTED ONE",
        summary.getErrors()[0].getError(),
        is("EXPECTED ERROR"));
  }
}
