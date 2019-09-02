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

package io.openvalidation.antlr.transformation.parsetree;

import io.openvalidation.antlr.generated.mainParser;
import io.openvalidation.antlr.transformation.TransformerBase;
import io.openvalidation.antlr.transformation.TransformerContext;
import io.openvalidation.common.ast.ASTGlobalElement;
import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.builder.ASTModelBuilder;
import io.openvalidation.common.exceptions.ASTValidationException;
import io.openvalidation.common.exceptions.ASTValidationSummaryException;
import io.openvalidation.common.utils.StringUtils;
import java.util.Arrays;
import java.util.stream.Collectors;

public class PTModelTransformer
    extends TransformerBase<PTModelTransformer, ASTModel, mainParser.MainContext> {

  public PTModelTransformer(mainParser.MainContext ctx, TransformerContext fctx) {
    super(ctx, fctx);
  }

  @Override
  public ASTModel transform() throws Exception {
    ASTModelBuilder builder =
        this.createBuilder(ASTModelBuilder.class, antlrTreeCntx.getText().replaceAll("<EOF>", ""));
    ASTValidationSummaryException exceptionSummary = new ASTValidationSummaryException();

    this.eachTreeChild(
        c -> {
          // set position of global Element.
          int index = this.antlrTreeCntx.children.indexOf(c);
          if (index > 0) index = index / 2; // paragraph has also an own index

          try {
            ASTItem item = this.createASTItem(c);

            if (item != null && item instanceof ASTGlobalElement) {
              item.setGlobalPosition(index + 1);
              builder.with((ASTGlobalElement) item);
            }

          } catch (ASTValidationException exp) {
            exceptionSummary.add(exp);
          } catch (Exception exp) {
            if (exp.getCause() != null && exp.getCause() instanceof ASTValidationException) {
              ASTValidationException valex = (ASTValidationException) exp.getCause();
              valex.setGlobalElementPosition(index);
              exceptionSummary.add(valex);
            } else {
              exceptionSummary.add(new ASTValidationException(exp.getMessage(), null));
              exceptionSummary.add(
                  new ASTValidationException(
                      StringUtils.join(
                          Arrays.stream(exp.getStackTrace())
                              .map(s -> s.toString())
                              .collect(Collectors.toList()),
                          "\n"),
                      null));
            }
          }
        });

    if (exceptionSummary.hasErrors()) {
      exceptionSummary.setModel(this.postprocess(builder.getModel()));
      throw exceptionSummary;
    }

    return this.postprocess(builder.getModel());
  }
}
