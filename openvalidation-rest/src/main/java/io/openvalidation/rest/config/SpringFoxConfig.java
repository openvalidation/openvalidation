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

package io.openvalidation.rest.config;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SpringFoxConfig extends WebMvcConfigurationSupport {

  private TypeResolver typeResolver;

  public SpringFoxConfig(@Autowired TypeResolver typeResolver) {
    this.typeResolver = typeResolver;
  }

  @Bean
  public Docket openValidationGeneratorApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.any())
        .build()
        .pathMapping("/")
        .genericModelSubstitutes(ResponseEntity.class)
        .useDefaultResponseMessages(true)
        //                .globalResponseMessage(RequestMethod.GET,
        //                        newArrayList(new ResponseMessageBuilder()
        //                                .code(500)
        //                                .message("500 message")
        //                                .responseModel(new ModelRef("Error"))
        //                                .build()))
        //                .securitySchemes(newArrayList(apiKey()))
        //                .securityContexts(newArrayList(securityContext()))
        //                .enableUrlTemplating(true)
        //                .globalOperationParameters(
        //                        newArrayList(new ParameterBuilder()
        //                                .name("someGlobalParameter")
        //                                .description("Description of someGlobalParameter")
        //                                .modelRef(new ModelRef("string"))
        //                                .parameterType("query")
        //                                .required(true)
        //                                .build()))
        .tags(new Tag("OpenValidation REST SERVICE", "Leverages the OpenValidation Java library"))
    //                .additionalModels(typeResolver.resolve(AdditionalModel.class))
    ;
  }

  @Override
  protected void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");
    registry
        .addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
  }

  private ApiKey apiKey() {
    return new ApiKey("mykey", "api_key", "header");
  }
}
