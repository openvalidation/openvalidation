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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.openvalidation.common.exceptions.OpenValidationException;
import io.openvalidation.common.model.Languages;
import io.openvalidation.common.model.OpenValidationResult;
import io.openvalidation.core.Aliases;
import io.openvalidation.core.OpenValidation;
import io.openvalidation.core.OpenValidationOptions;
import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ParamValidationTest {

  // TODO @Testing: Refactorn: Exception soll beim konfigurieren von openvalidation kommen, bei
  // generateCode() darf keine Exception geworfen werden, es sei denn es wurde fileoutput gewählt
  // und das Schreiben scheitert
  @Test
  public void noOptionsShouldThrowOpenValidationException() {
    Throwable expectedException = null;
    OpenValidation openvalidation =
        OpenValidation.createDefault(); // new OpenValidation(null, null, null, null);
    openvalidation.setOptions(null);
    OpenValidationResult res = new OpenValidationResult();
    try {
      res = openvalidation.generateCode(false);
    } catch (Exception e) {
      expectedException = e;
    }

    assertNull(expectedException);
    assertTrue(res.hasErrors());
    //        assertThat(expectedException.getMessage(), is("Options cannot be empty"));
  }

  // TODO @Testing: neue Tests die sicherstellen, dass die Exception im Result angehängt und nicht
  // weitergereicht wird
  @Test
  public void noLanguageShouldThrowOpenValidationException() {

    Throwable expectedException = null;
    OpenValidation openvalidation =
        OpenValidation.createDefault(); // new OpenValidation(null, null, null, null);
    OpenValidationOptions options = new OpenValidationOptions();
    OpenValidationResult res = new OpenValidationResult();

    options.setLanguage(null);
    openvalidation.setOptions(options);

    try {
      res = openvalidation.generateCode(false);
    } catch (Exception e) {
      expectedException = e;
    }

    assertNull(expectedException);
    assertTrue(res.hasErrors());
    //        assertThat(expectedException, instanceOf(OpenValidationException.class));
    //        assertThat(expectedException.getMessage(), is("Language cannot be empty"));
  }

  @Test
  public void noRuleContentShouldThrowOpenValidationException() throws Exception {

    Throwable expectedException = null;
    OpenValidation openvalidation =
        OpenValidation.createDefault(); // new OpenValidation(null, null, null, null);
    openvalidation.createDefault();
    OpenValidationOptions options = new OpenValidationOptions();
    options.setLanguage(Languages.getLanguage("Java"));
    OpenValidationResult res;

    try {
      options.setRuleOption(null);
      openvalidation.setOptions(options);
    } catch (Exception e) {
      expectedException = e;
    }

    res = openvalidation.generateCode(false);

    assertTrue(res.hasErrors());
    assertThat(expectedException, instanceOf(OpenValidationException.class));
    //        assertThat(expectedException.getMessage(), is("the Rule Set cannot be empty"));
  }

  @Test
  public void noSchemaShouldThrowOpenValidationException() throws Exception {

    Throwable expectedException = null;
    OpenValidation openvalidation =
        OpenValidation.createDefault(); // new OpenValidation(null, null, null, null);
    openvalidation.createDefault();
    OpenValidationOptions options = new OpenValidationOptions();
    OpenValidationResult res;

    options.setLanguage(Languages.getLanguage("Java"));
    options.setLocale("de");
    options.setRuleOption("WENN die Nachricht IST GLEICH hallo DANN Hallo Welt");
    String schema = null;

    try {
      options.setSchema(schema);
      openvalidation.setOptions(options);

    } catch (Exception e) {
      expectedException = e;
    }

    res = openvalidation.generateCode(false);

    assertTrue(res.hasErrors());
    assertThat(expectedException, instanceOf(OpenValidationException.class));
    //        assertNull(expectedException);
    //        assertThat(expectedException.getMessage(), is("Schema cannot be empty"));
  }

  @Test
  public void noLocaleShouldThrowOpenValidationException() throws Exception {

    Throwable expectedException = null;
    OpenValidation openvalidation =
        OpenValidation.createDefault(); // new OpenValidation(null, null, null, null);
    openvalidation.createDefault();
    OpenValidationOptions options = new OpenValidationOptions();
    OpenValidationResult res;

    options.setLanguage(Languages.getLanguage("Java"));
    options.setRuleOption("WENN die Nachricht IST GLEICH hallo DANN Hallo Welt");
    options.setSchema("{Message : \"Text\"}");
    Locale locale = null;

    try {
      options.setLocale(locale);
      openvalidation.setOptions(options);
    } catch (Exception e) {
      expectedException = e;
    }
    res = openvalidation.generateCode(false);

    assertTrue(res.hasErrors());
    //        TODO TB 20.05.2019: Soll bei leerem Locale eine Exception geworfen werden oder nicht.
    // Aktuell: nicht
    //        assertThat(expectedException, instanceOf(OpenValidationException.class));
    //        assertThat(expectedException.getMessage(), is("Culture cannot be empty"));
  }

  @Test
  public void noParamsShouldNotThrowOpenValidationException() throws Exception {

    Throwable expectedException = null;
    OpenValidation openvalidation =
        OpenValidation.createDefault(); // new OpenValidation(null, null, null, null);
    openvalidation.createDefault();
    OpenValidationOptions options = new OpenValidationOptions();
    OpenValidationResult res = new OpenValidationResult();

    options.setLanguage(Languages.getLanguage("Java"));
    options.setRuleOption("WENN die Nachricht IST GLEICH hallo DANN Hallo Welt");
    options.setSchema("{Message : \"Text\"}");

    options.setParam(null, null);
    openvalidation.setOptions(options);

    try {
      res = openvalidation.generateCode(false);
    } catch (Exception e) {
      expectedException = e;
    }
    assertNull(expectedException);
    assertTrue(res.hasErrors());
  }

  @Test
  public void emptyRuleContentShouldThrowOpenValidationException() throws Exception {

    Throwable expectedException = null;
    OpenValidation openvalidation =
        OpenValidation.createDefault(); // new OpenValidation(null, null, null, null);
    openvalidation.createDefault();
    OpenValidationOptions options = new OpenValidationOptions();
    options.setLanguage(Languages.getLanguage("Java"));
    OpenValidationResult res;

    try {
      options.setRuleOption("");
      openvalidation.setOptions(options);
    } catch (Exception e) {
      expectedException = e;
    }

    res = openvalidation.generateCode(false);

    assertTrue(res.hasErrors());
    assertThat(expectedException, instanceOf(OpenValidationException.class));
    //        assertThat(expectedException.getMessage(), is("the Rule Set cannot be empty"));
  }

  // TODO @Testing: Refactorn: Exception soll beim konfigurieren von openvalidation kommen, bei
  // generateCode() darf keine Exception geworfen werden, es sei denn es wurde fileoutput gewählt
  // und das Schreiben scheitert
  @Test
  public void emptySchemaContentShouldThrowOpenValidationException() throws Exception {

    Throwable expectedException = null;
    OpenValidation openvalidation =
        OpenValidation.createDefault(); // new OpenValidation(null, null, null, null);
    openvalidation.createDefault();
    OpenValidationOptions options = new OpenValidationOptions();

    options.setLanguage(Languages.getLanguage("Java"));
    options.setRuleOption("WENN die Nachricht IST GLEICH hallo DANN Hallo Welt");

    try {
      options.setSchema("");
      openvalidation.setOptions(options);
      openvalidation.generateCode(false);
    } catch (Exception e) {
      expectedException = e;
    }
    assertThat(expectedException, instanceOf(OpenValidationException.class));
  }

  // TODO @Testing: Refactorn: Exception soll beim konfigurieren von openvalidation kommen, bei
  // generateCode() darf keine Exception geworfen werden, es sei denn es wurde fileoutput gewählt
  // und das Schreiben scheitert
  @Test
  public void emptyLocaleShouldThrowOpenValidationException() throws Exception {

    Throwable expectedException = null;
    OpenValidation openvalidation =
        OpenValidation.createDefault(); // new OpenValidation(null, null, null, null);
    openvalidation.createDefault();
    OpenValidationOptions options = new OpenValidationOptions();
    options.setLanguage(Languages.getLanguage("Java"));

    try {
      options.setLocale(" ");
      openvalidation.setOptions(options);
      openvalidation.generateCode(false);
    } catch (Exception e) {
      expectedException = e;
    }
    assertThat(expectedException, instanceOf(OpenValidationException.class));
  }

  // TODO @Testing: Refactorn: Exception soll beim konfigurieren von openvalidation kommen, bei
  // generateCode() darf keine Exception geworfen werden, es sei denn es wurde fileoutput gewählt
  // und das Schreiben scheitert
  @Test
  public void wrongLocaleShouldThrowOpenValidationException() throws Exception {

    Throwable expectedException = null;
    String wrongLocale = "43534534634";
    OpenValidation openvalidation =
        OpenValidation.createDefault(); // new OpenValidation(null, null, null, null);
    openvalidation.createDefault();
    OpenValidationOptions options = new OpenValidationOptions();
    options.setRuleOption("WENN die Nachricht IST GLEICH hallo DANN Hallo Welt");
    options.setSchema("{Message : \"Text\"}");

    try {
      options.setLocale(wrongLocale);
      openvalidation.setOptions(options);
      openvalidation.generateCode(false);
    } catch (Exception e) {
      expectedException = e;
    }
    assertThat(expectedException, instanceOf(OpenValidationException.class));
    assertThat(
        expectedException.getMessage(),
        is(
            "Language: '"
                + wrongLocale
                + "' could not be found. The following languages are currently available "
                + Aliases.availableCultures));
  }

  // TODO @Testing: Refactorn: Exception soll beim konfigurieren von openvalidation kommen, bei
  // generateCode() darf keine Exception geworfen werden, es sei denn es wurde fileoutput gewählt
  // und das Schreiben scheitert
  @Test
  public void wrongLocaleShouldThrowOpenValidationException_short() throws Exception {

    Throwable expectedException = null;
    String wrongLocale = "d";
    OpenValidation openvalidation =
        OpenValidation.createDefault(); // new OpenValidation(null, null, null, null);
    openvalidation.createDefault();
    OpenValidationOptions options = new OpenValidationOptions();
    options.setRuleOption("WENN die Nachricht IST GLEICH hallo DANN Hallo Welt");
    options.setSchema("{Message : \"Text\"}");

    try {
      options.setLocale(wrongLocale);
      openvalidation.setOptions(options);
      openvalidation.generateCode(false);
    } catch (Exception e) {
      expectedException = e;
    }
    assertThat(expectedException, instanceOf(OpenValidationException.class));
    assertThat(
        expectedException.getMessage(),
        is(
            "Language: '"
                + wrongLocale
                + "' could not be found. The following languages are currently available "
                + Aliases.availableCultures));
  }

  // TODO @Testing: Refactorn: Exception soll beim konfigurieren von openvalidation kommen, bei
  // generateCode() darf keine Exception geworfen werden, es sei denn es wurde fileoutput gewählt
  // und das Schreiben scheitert
  @Test
  public void wrongLocaleShouldThrowOpenValidationException_long() throws Exception {

    Throwable expectedException = null;
    String wrongLocale = "dee";
    OpenValidation openvalidation =
        OpenValidation.createDefault(); // new OpenValidation(null, null, null, null);
    openvalidation.createDefault();
    OpenValidationOptions options = new OpenValidationOptions();
    options.setRuleOption("WENN die Nachricht IST GLEICH hallo DANN Hallo Welt");
    options.setSchema("{Message : \"Text\"}");

    try {
      options.setLocale(wrongLocale);
      openvalidation.setOptions(options);
      openvalidation.generateCode(false);
    } catch (Exception e) {
      expectedException = e;
    }
    assertThat(expectedException, instanceOf(OpenValidationException.class));
    assertThat(
        expectedException.getMessage(),
        is(
            "Language: '"
                + wrongLocale
                + "' could not be found. The following languages are currently available "
                + Aliases.availableCultures));
  }

  // TODO @Testing: Refactorn: Exception soll beim konfigurieren von openvalidation kommen, bei
  // generateCode() darf keine Exception geworfen werden, es sei denn es wurde fileoutput gewählt
  // und das Schreiben scheitert
  @Test
  public void wrongLocaleCharShouldThrowOpenValidationException() throws Exception {

    Throwable expectedException = null;
    String wrongLocale = "?";
    OpenValidation openvalidation =
        OpenValidation.createDefault(); // new OpenValidation(null, null, null, null);
    openvalidation.createDefault();
    OpenValidationOptions options = new OpenValidationOptions();
    options.setRuleOption("WENN die Nachricht IST GLEICH hallo DANN Hallo Welt");
    options.setSchema("{Message : \"Text\"}");

    try {
      options.setLocale(wrongLocale);
      openvalidation.setOptions(options);
      openvalidation.generateCode(false);
    } catch (Exception e) {
      expectedException = e;
    }
    assertThat(expectedException, instanceOf(OpenValidationException.class));
    assertThat(
        expectedException.getMessage(),
        is(
            "Language: '"
                + wrongLocale
                + "' could not be found. The following languages are currently available "
                + Aliases.availableCultures));
  }

  // TODO @Testing: Refactorn: Exception soll beim konfigurieren von openvalidation kommen, bei
  // generateCode() darf keine Exception geworfen werden, es sei denn es wurde fileoutput gewählt
  // und das Schreiben scheitert
  @Test
  public void wrongJSONSchemaContentShouldThrowOpenValidationException() throws Exception {

    Throwable expectedException = null;
    OpenValidation openvalidation =
        OpenValidation.createDefault(); // new OpenValidation(null, null, null, null);
    openvalidation.createDefault();
    OpenValidationOptions options = new OpenValidationOptions();

    options.setLanguage(Languages.getLanguage("Java"));
    options.setRuleOption("WENN die Nachricht IST GLEICH hallo DANN Hallo Welt");

    try {
      options.setSchema("Message : \"Text\"}");
      openvalidation.setOptions(options);
      openvalidation.generateCode(false);
    } catch (Exception e) {
      expectedException = e;
    }
    assertThat(expectedException, instanceOf(OpenValidationException.class));
    assertThat(
        expectedException.getMessage(),
        containsString(
            "invalid JSON Schema Format. Should be json data or json schema in json or yaml format!"));
  }

  // TODO @Testing: Refactorn: Exception soll beim konfigurieren von openvalidation kommen, bei
  // generateCode() darf keine Exception geworfen werden, es sei denn es wurde fileoutput gewählt
  // und das Schreiben scheitert
  @Test
  public void wrongYAMLSchemaContentShouldThrowOpenValidationException() throws Exception {

    Throwable expectedException = null;
    OpenValidation openvalidation =
        OpenValidation.createDefault(); // new OpenValidation(null, null, null, null);
    openvalidation.createDefault();
    OpenValidationOptions options = new OpenValidationOptions();

    options.setLanguage(Languages.getLanguage("Java"));
    options.setRuleOption("WENN die Nachricht IST GLEICH hallo DANN Hallo Welt");

    try {
      options.setSchema("Message : \"Text");
      openvalidation.setOptions(options);
      openvalidation.generateCode(false);
    } catch (Exception e) {
      expectedException = e;
    }
    assertThat(expectedException, instanceOf(OpenValidationException.class));
    assertThat(
        expectedException.getMessage(),
        containsString(
            "invalid JSON Schema Format. Should be json data or json schema in json or yaml format!"));
  }

  @Test
  public void enLocaleShouldNotThrowOpenValidationException() throws Exception {

    Throwable expectedException = null;
    String enLocale = "en";
    OpenValidation openvalidation =
        OpenValidation.createDefault(); // new OpenValidation(null, null, null, null);
    openvalidation.createDefault();
    OpenValidationOptions options = new OpenValidationOptions();
    options.setRuleOption("IF the Message EQUALS hallo THEN Hello World");
    options.setSchema("{Message : \"Text\"}");

    try {
      options.setLocale(enLocale);
      openvalidation.setOptions(options);
      openvalidation.generateCode(false);
    } catch (Exception e) {
      expectedException = e;
    }
    assertThat(expectedException, is(nullValue()));
  }
}
