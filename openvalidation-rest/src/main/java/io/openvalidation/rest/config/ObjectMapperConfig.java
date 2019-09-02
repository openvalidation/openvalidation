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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class ObjectMapperConfig extends WebMvcConfigurationSupport {

  @Autowired private ObjectMapper serializingObjectMapper;

  @Override
  protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    /*
     * Code-Snipped taken from https://github.com/springfox/springfox/issues/2493
     * Problem: Springfox swagger-ui uses WebMVC. WebMvc introduces additional ObjectMappers and disables AutoConfiguration
     * When providing a custom ObjectMapper or using Spring configuration properties to customize the default
     * Spring Boot ObjectMapper. The following Snipped will add the default Spring ObjectMapper in the list of message
     * converters for WebMvc
     */
    MappingJackson2HttpMessageConverter messageConverter =
        new MappingJackson2HttpMessageConverter();
    messageConverter.setObjectMapper(serializingObjectMapper);
    converters.add(messageConverter);
    super.configureMessageConverters(converters);
  }
}
