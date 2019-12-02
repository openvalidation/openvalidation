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

package io.openvalidation.antlr;

import io.openvalidation.antlr.generated.mainBaseListener;
import io.openvalidation.antlr.generated.mainParser;
import io.openvalidation.antlr.transformation.TransformerContext;
import io.openvalidation.antlr.transformation.parsetree.PTModelTransformer;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.data.DataSchema;
import io.openvalidation.common.log.ProcessLogger;

public class MainASTBuildListener extends mainBaseListener {
  private PTModelTransformer _factory;
  private ASTModel _ast;
  private TransformerContext _transformerContext;
  private DataSchema _schema;

  public MainASTBuildListener(DataSchema schema) {
    this._schema = (schema != null) ? schema : new DataSchema();
  }

  @Override
  public void exitMain(mainParser.MainContext ctx) {
    try {
      _schema.complete(NamesExtractor.getNames(ctx));

      _transformerContext = new TransformerContext(_schema);

      _factory = new PTModelTransformer(ctx, _transformerContext);
      _ast = _factory.transform();

      ProcessLogger.success(ProcessLogger.PARSER_MAIN);

    } catch (Exception e) {
      ProcessLogger.error(ProcessLogger.PARSER_MAIN, e);
      throw new RuntimeException(e);
    }
  }

  public ASTModel getAST() {
    return _ast;
  }

  public TransformerContext get_transformerContext() {
    return _transformerContext;
  }
}
