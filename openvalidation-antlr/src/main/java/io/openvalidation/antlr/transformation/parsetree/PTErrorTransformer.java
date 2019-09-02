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
import io.openvalidation.common.ast.ASTActionError;
import io.openvalidation.common.utils.Constants;
import io.openvalidation.common.utils.NumberParsingUtils;
import io.openvalidation.common.utils.StringUtils;
import org.antlr.v4.runtime.tree.TerminalNode;

public class PTErrorTransformer
    extends TransformerBase<PTErrorTransformer, ASTActionError, mainParser.ErrorContext> {

  public PTErrorTransformer(mainParser.ErrorContext treeCntx, TransformerContext fctx) {
    super(treeCntx, fctx);
  }

  @Override
  public ASTActionError transform() throws Exception {
    ASTActionError error = new ASTActionError();
    error.setSource(antlrTreeCntx.getText());

    if (this.antlrTreeCntx.unknown() != null)
      error.setErrorMessage(StringUtils.reverseKeywords(this.antlrTreeCntx.unknown().getText()));

    boolean hasErrorCode = false;
    Double ec = null;
    if (this.antlrTreeCntx.WITH_ERROR() != null) {
      hasErrorCode = true;
      ec = extractErrorCode(this.antlrTreeCntx.WITH_ERROR());
    } else if (this.antlrTreeCntx.unknown().WITH_ERROR(0) != null) {
      hasErrorCode = true;
      ec = extractErrorCode(this.antlrTreeCntx.unknown().WITH_ERROR(0));
      correctErrormessageFromFaultyErrorCode(error);
    }

    if (hasErrorCode) {
      if (ec != null) {
        error.setErrorCode(ec.intValue());
      } else {
        error.setErrorCode(-1);
      }
    }

    error.setSource(this.antlrTreeCntx.getText());

    return error;
  }

  private Double extractErrorCode(TerminalNode withError) {
    Double ec =
        NumberParsingUtils.extractNumber(
            withError.getText().replaceAll(Constants.ERRORCODE_TOKEN, ""));
    return ec;
  }

  private void correctErrormessageFromFaultyErrorCode(ASTActionError error) {
    int errorCodePos =
        error
            .getErrorMessage()
            .indexOf(
                StringUtils.reverseKeywords(this.antlrTreeCntx.unknown().WITH_ERROR(0).getText()));

    if (errorCodePos >= 0) {
      error.setErrorMessage(error.getErrorMessage().substring(0, errorCodePos));
    }
  }
}
