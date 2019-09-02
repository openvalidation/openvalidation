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

import com.fasterxml.jackson.annotation.JsonAlias;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.model.CodeGenerationResult;
import io.openvalidation.common.model.Language;
import io.openvalidation.common.model.OpenValidationResult;
import io.openvalidation.core.OpenValidation;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO desc. TODO some stuff in this class may be obsolete if OpenValidation can handle all
 * parameters in a flat Map
 */
public class OpenValidationParameters {

  public static final String MODEL_TYPE = "model_type";
  public static final String GENERATED_CLASS_NAMESPACE = "generated_class_namespace";
  public static final String GENERATED_CLASS_NAME = "generated_class_name";
  private OpenValidation ov = null;

  // TODO custom aliases
  //    @JsonAlias("no_ast")
  //    private boolean noAst;
  @JsonAlias("disable_framework_generation")
  private Boolean disableFrameworkGeneration = false;

  private Boolean verbose =
      false; // todo evtl aufteilen in verbose switch für OpenValidation und Fehlermeldung
  private String rule, schema, culture;

  @JsonAlias(MODEL_TYPE)
  private String modelType;

  @JsonAlias(GENERATED_CLASS_NAMESPACE)
  private String generatedClassNamespace;

  @JsonAlias(GENERATED_CLASS_NAME)
  private String generatedClassName;

  private Language language;

  //    @JsonAlias("openvalidation_parameters")
  //    private Map<String, Object> ovParams;

  //    //todo externalize strings (e.g. application.yml)
  //    private static final String RULE = "rule";
  //    private static final String SCHEMA = "schema";
  //    private static final String CULTURE = "culture";
  //    private static final String LANGUAGE = "language";
  //    private static final String NO_AST = "no_ast"; // todo valid?
  //    private static final String DISABLE_FRAMEWORK_GENERATION = "disable_framework_generation";
  //    private static final String MODEL_PACKAGE = "modelPackage";
  //    private static final String PACKAGE = "package";
  //    private static final String CUSTOM_NAME = "customName";
  //    private static final String VERBOSE = "verbose";
  //    private Map<String, String> coreParams = new HashMap<>();
  //
  //    private Map<String, Object> embeddedParams = new HashMap<>();
  //    //TODO double check compatibility of String and Object
  //
  //    private boolean disableFrameworkGeneration = false;
  //    private boolean verbose; //TODO disable verbose if it has no effect

  public OpenValidationParameters() {
    // serializable
  }

  public OpenValidationParameters(
      OpenValidation ov,
      boolean disableFrameworkGeneration,
      boolean verbose,
      String rule,
      String schema,
      String culture,
      String modelType,
      String generatedClassNamespace,
      String generatedClassName,
      Language language) {
    this.ov = ov;
    this.disableFrameworkGeneration = disableFrameworkGeneration;
    this.verbose = verbose;
    this.rule = rule;
    this.schema = schema;
    this.culture = culture;
    this.modelType = modelType;
    this.generatedClassNamespace = generatedClassNamespace;
    this.generatedClassName = generatedClassName;
    this.language = language;
  }

  // TODO falls benötigt selber machen
  //    public OpenValidationParameters(Map<String, String> params) {
  //
  //    }
  //
  //    public OpenValidationParameters(Map<String, Object> params) {
  //
  //    }
  //
  //
  //    public OpenValidationParameters(Map<String, String> params, Map<>)
  //
  //    /**
  //     * Convenience funktion to create {@link OpenValidationParameters} from a flat Map of
  // parameters
  //     *
  //     * @param flatParams a {@link Map} containing all keys with their respective values.
  //     * @return An instance of {@link OpenValidationParameters} that matches the provided Map
  //     */
  //    public static OpenValidationParameters of(Map<String, String> flatParams) {
  //        Builder builder = new Builder();
  //        for (Map.Entry<String, String> entry : flatParams.entrySet())
  //
  //            //it may be neccessary to add the locale if very exotic languages introduce exotic
  // keys
  //            switch (entry.getKey().toLowerCase()) {
  //                case RULE:
  //                    //todo npe
  //                case SCHEMA:
  //                case CULTURE:
  //                case LANGUAGE:
  //                    builder.addCoreParameter(entry.getKey(), entry.getValue());
  //                    break;
  //                case MODEL_PACKAGE:
  //                case PACKAGE:
  //                case CUSTOM_NAME:
  //                    builder.addEmbeddedParameter(entry.getKey(),
  // entry.getValue().toLowerCase());
  //                    break;
  //                case NO_AST:
  //                    builder.setNoAst(Boolean.valueOf(entry.getValue().toLowerCase()));
  //                case DISABLE_FRAMEWORK_GENERATION:
  //
  // builder.setDisableFrameworkGeneration(Boolean.valueOf(entry.getValue().toLowerCase()));
  //                    break;
  //                case VERBOSE:
  //                    builder.setVerbose(Boolean.valueOf(entry.getValue().toLowerCase()));
  //                    // TODO Handle Format Exception
  //                    break;
  //                default:
  //                    //todo use logger
  //                    System.out.println("ignored parameter: " + entry.getKey() + "=" +
  // entry.getValue() + ".");
  //            }
  //        return builder.build();
  //    }

  //    public static Language parseLanguage(String name) {
  //        if (name == null) return null;
  //        //TODO refactor and move to enum
  //        switch (name.toLowerCase()) {
  //            case "java":
  //                return Language.Java;
  //            case "csharp":
  //                return Language.CSharp;
  //            case "javascript":
  //                return Language.JavaScript;
  //            case "node":
  //                return Language.Node;
  //            default:
  //                //todo add custom exception
  //                throw new RuntimeException();
  //        }
  //    }

  //    /**
  //     * Apply all parameters to the given instance of {@link OpenValidation}. Note that the
  // configured instance won't have
  //     * the {@link #DISABLE_FRAMEWORK_GENERATION} property set, because this property is not
  // configured on the instance,
  //     * but provided as a parameter for {@link OpenValidation#generate(boolean)}.
  //     *
  //     * @param openValidation The instance of OpenValidation, that should be configured
  //     * @return the configured instance. This instance instance is the same instance that was
  // provided.
  //     */
  //    public OpenValidation applyTo(OpenValidation openValidation) throws Exception {
  //        openValidation.setLanguage(parseLanguage(coreParams.get(LANGUAGE)));
  //        openValidation.setLocale(coreParams.get(CULTURE));
  //        openValidation.setRule(coreParams.get(RULE));
  //        openValidation.setSchema(coreParams.get(SCHEMA));
  //        // TODO aliases
  //        openValidation.setParams(embeddedParams);// todo double check
  //        // todo options are not supported yet    openValidation.setOptions()
  //        openValidation.setVerbose(verbose); // todo check if verbose changes result
  //        return openValidation;
  //    }

  /**
   * Apply all parameters to the given instance of {@link OpenValidation}. Note that the configured
   * instance won't have the {@link #disableFrameworkGeneration} property set, because this property
   * is not configured on the instance, but provided as a parameter for {@link
   * OpenValidation#generate(boolean)}.
   *
   * @param openValidation The instance of OpenValidation, that should be configured
   * @return the configured instance. This instance instance is the same instance that was provided.
   */
  public OpenValidation applyTo(OpenValidation openValidation) throws Exception {
    openValidation.setLanguage(this.language);
    openValidation.setLocale(this.culture);
    openValidation.setRule(this.rule);
    openValidation.setSchema(this.schema);
    // TODO aliases
    Map<String, Object> embedded = this.params();
    if (!embedded.isEmpty()) openValidation.setParams(embedded); // todo double check

    // todo check if verbose changes result
    return openValidation;
  }

  public OpenValidationResult generate() throws Exception {
    ov = OpenValidation.createDefault();
    OpenValidationResult result = this.applyTo(ov).generate(this.disableFrameworkGeneration);
    //        if (!this.isVerbose()) removeStacktrace(result);
    return result;
  }

  private Map<String, Object> params() {
    Map<String, Object> result = new HashMap<>();
    if (this.modelType != null) result.put(MODEL_TYPE, this.modelType);
    if (this.generatedClassNamespace != null)
      result.put(GENERATED_CLASS_NAMESPACE, this.generatedClassNamespace);
    if (this.generatedClassName != null) result.put(GENERATED_CLASS_NAME, this.generatedClassName);
    return result;
  }

  //    private static OpenValidationResult removeStacktrace(OpenValidationResult result) {
  //        result.getErrors().forEach((error) -> {
  //            Field stacktrace = error.getClass().getSuperclass().getDeclaredField("stacktrace");
  //            f1.setAccessible(true);
  //            f1.set(cc, "reflecting on life");
  //            String str1 = (String) f1.get(cc);
  //            System.out.println("field: " + str1);
  //        });
  //
  //
  //        Field f1 = result.getClass().getSuperclass().getDeclaredField("a_field");
  //        f1.setAccessible(true);
  //        f1.set(cc, "reflecting on life");
  //        String str1 = (String) f1.get(cc);
  //        System.out.println("field: " + str1);
  //    }

  //    public Builder builder() {
  //        return new Builder();
  //    }

  //    /**
  //     * Convenience method to create a flat parameter map from this instance. Note that concrete
  // types are removed (e.g.
  //     * booleans will be mapped to string). This behavior can be changed by changing the type of
  // the Map to Map<String, Object).
  //     * The result map will contain contain the keys {@link #VERBOSE}, {@link #NO_AST} and {@link
  // #DISABLE_FRAMEWORK_GENERATION}
  //     * with their default value (false) even if the {@link OpenValidationParameters} were
  // created without specifying the
  //     * two properties.
  //     *
  //     * @return a flat representation of all parameters as {@link HashMap\<String, String\>}
  //     */
  //    public Map<String, String> flatParams() throws OpenValidationResponseStatusException {
  //        //TODO catch parsing exceptions or bubble up to exception handler
  //        Map<String, String> flatParams = new HashMap<>();
  //        flatParams.putAll(coreParams);
  //        // flatParams.putAll(embeddedParams); TODO add mapping from string to object
  //        flatParams.put(VERBOSE, Boolean.toString(verbose));
  //        flatParams.put(DISABLE_FRAMEWORK_GENERATION,
  // Boolean.toString(disableFrameworkGeneration));
  //        return flatParams;
  //    }

  public boolean isDisableFrameworkGeneration() {
    return disableFrameworkGeneration == null ? false : this.disableFrameworkGeneration;
  }

  public OpenValidationParameters setDisableFrameworkGeneration(
      boolean disableFrameworkGeneration) {
    this.disableFrameworkGeneration = disableFrameworkGeneration;
    return this;
  }

  public boolean isVerbose() {
    return verbose == null ? false : this.verbose;
  }

  public OpenValidationParameters setVerbose(boolean verbose) {
    this.verbose = verbose;
    return this;
  }

  public String getRule() {
    return rule;
  }

  public OpenValidationParameters setRule(String rule) {
    this.rule = rule;
    return this;
  }

  public String getSchema() {
    return schema;
  }

  public OpenValidationParameters setSchema(String schema) {
    this.schema = schema;
    return this;
  }

  public String getCulture() {
    return culture;
  }

  public OpenValidationParameters setCulture(String culture) {
    this.culture = culture;
    return this;
  }

  public String getModelType() {
    return modelType;
  }

  public OpenValidationParameters setModelType(String modelType) {
    this.modelType = modelType;
    return this;
  }

  public String getGeneratedClassNamespace() {
    return generatedClassNamespace;
  }

  public OpenValidationParameters setGeneratedClassNamespace(String generatedClassNamespace) {
    this.generatedClassNamespace = generatedClassNamespace;
    return this;
  }

  public String getGeneratedClassName() {
    return generatedClassName;
  }

  public OpenValidationParameters setGeneratedClassName(String generatedClassName) {
    this.generatedClassName = generatedClassName;
    return this;
  }

  public Language getLanguage() {
    return language;
  }

  public OpenValidationParameters setLanguage(Language language) {
    this.language = language;
    return this;
  }

  public OpenValidation getOpenValidationInstance() {
    return ov;
  }

  public OpenValidationParameters setOpenValidationInstance(OpenValidation ov) {
    this.ov = ov;
    return this;
  }

  public CodeGenerationResult generateFramework(ASTModel astModel) throws Exception {
    if (ov == null) ov = OpenValidation.createDefault();
    applyTo(ov);
    return ov.generateFramework(astModel);
  }

  public OpenValidationResult generateCode() throws Exception {
    if (ov == null) ov = OpenValidation.createDefault();
    applyTo(ov);
    return ov.generateCode(this.disableFrameworkGeneration);
  }

  public CodeGenerationResult generateValidatorFactory(Map<String, Object> params)
      throws Exception {
    if (ov == null) ov = OpenValidation.createDefault();
    applyTo(ov);
    return ov.generateValidatorFactory(params);
  }

  public boolean isEmpty() {
    return this.ov == null
        && this.disableFrameworkGeneration == null
        && this.verbose == null
        && this.rule == null
        && this.schema == null
        && this.culture == null
        && this.modelType == null
        && this.generatedClassNamespace == null
        && this.generatedClassName == null
        && this.language == null;
  }

  public static class Builder {
    private OpenValidation ov;
    private boolean disableFrameworkGeneration;
    private boolean verbose;
    private String rule;
    private String schema;
    private String culture;
    private String modelType;
    private String generatedClassNamespace;
    private String generatedClassName;
    private Language language;

    public Builder setOpenValidationInstance(OpenValidation ov) {
      this.ov = ov;
      return this;
    }

    public Builder setDisableFrameworkGeneration(boolean disableFrameworkGeneration) {
      this.disableFrameworkGeneration = disableFrameworkGeneration;
      return this;
    }

    public Builder setVerbose(boolean verbose) {
      this.verbose = verbose;
      return this;
    }

    public Builder setRule(String rule) {
      this.rule = rule;
      return this;
    }

    public Builder setSchema(String schema) {
      this.schema = schema;
      return this;
    }

    public Builder setCulture(String culture) {
      this.culture = culture;
      return this;
    }

    public Builder setModelType(String modelType) {
      this.modelType = modelType;
      return this;
    }

    public Builder setGeneratedClassNamespace(String generatedClassNamespace) {
      this.generatedClassNamespace = generatedClassNamespace;
      return this;
    }

    public Builder setGeneratedClassName(String generatedClassName) {
      this.generatedClassName = generatedClassName;
      return this;
    }

    public Builder setLanguage(Language language) {
      this.language = language;
      return this;
    }

    public OpenValidationParameters build() {
      return new OpenValidationParameters(
          ov,
          disableFrameworkGeneration,
          verbose,
          rule,
          schema,
          culture,
          modelType,
          generatedClassNamespace,
          generatedClassName,
          language);
    }

    //        //TODO could add more convenience methods to set concrete parameters directly (e.g.
    // setRule()), but this would result to more side effects between setters and developers should
    // be able to work with maps.
    //
    //        private boolean disableFrameworkGeneration = false;
    //        private boolean verbose = false;
    ////        private boolean noAst = false;
    //        private OpenValidation ov = null;
    //
    //        public Builder setCoreParams(Map<String, String> coreParams) {
    //            this.coreParams = coreParams;
    //            return this;
    //        }
    //
    //        public Builder setEmbeddedParams(Map<String, Object> embeddedParams) {
    //            this.embeddedParams = embeddedParams;
    //            return this;
    //        }
    //
    //        //could add another convenience function that takes all values for all known keys
    //        public Builder addEmbeddedParameter(String key, Object value) {
    //            this.embeddedParams.put(key, value);
    //            return this;
    //        }
    //
    //        public Builder addCoreParameter(String key, String value) {
    //            this.coreParams.put(key, value);
    //            return this;
    //        }
    //
    //        public Builder setVerbose(boolean verbose) {
    //            this.verbose = verbose;
    //            return this;
    //        }
    //
    //        public Builder setDisableFrameworkGeneration(boolean disableFrameworkGeneration) {
    //            this.disableFrameworkGeneration = disableFrameworkGeneration;
    //            return this;
    //        }
    //
    //        /**
    //         * Build the configured instance. No sanity checks are applied.
    //         *
    //         * @return a new instance of {@link OpenValidationParameters} matching the builder
    // configuration
    //         */
    //        public OpenValidationParameters build() {
    //            OpenValidationParameters ovParams = new OpenValidationParameters();
    //            ovParams.setCoreParams(this.coreParams);
    //            ovParams.setEmbeddedParams(this.embeddedParams);
    //            ovParams.setDisableFrameworkGeneration(this.disableFrameworkGeneration);
    //            ovParams.setVerbose(this.verbose);
    //            ovParams.setOpenValidationInstance(ov);
    //            return ovParams;
    //        }
    //
    //        public void setNoAst(boolean noAst) {
    //            this.noAst = noAst;
    //        }
    //
    //        public void setOpenValidationInstance(OpenValidation ov) {
    //            this.ov = ov;
    //        }
  }
}
