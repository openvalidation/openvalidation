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

package io.openvalidation.core.preprocessing;

import io.openvalidation.common.model.PreProcessorContext;
import io.openvalidation.common.utils.ReflectionUtils;
import io.openvalidation.core.preprocessing.steps.*;
import java.util.ArrayList;
import java.util.List;

public class PreProcessorStepFactory {

  public static List<PreProcessorStepBase> create(PreProcessorContext ctx) {
    List<PreProcessorStepBase> allSteps = new ArrayList<>();

    Class<? extends PreProcessorStepBase>[] stepClasses =
        new Class[] {
          PreProcessorIncludeResolutionStep.class,
          PreProcessorAliasResolutionStep.class,
          PreProcessorKeywordCollisionStep.class,
          PreProcessorVariableNamesStep.class,
          PreProcessorLastParagraphCleanup.class
        };

    try {
      for (Class cls : stepClasses) {
        allSteps.add(createStep(cls, ctx));
      }
    } catch (Exception e) {
      throw new RuntimeException("while creating preprocessing steps.", e);
    }

    return allSteps;
  }

  public static <T extends PreProcessorStepBase> T createStep(Class<T> cls, PreProcessorContext ctx)
      throws Exception {
    PreProcessorStepBase ste = ReflectionUtils.createInstance(cls);
    ste.setContext(ctx);

    return (T) ste;
  }
}
