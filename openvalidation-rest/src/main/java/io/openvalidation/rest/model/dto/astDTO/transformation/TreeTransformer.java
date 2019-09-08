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

package io.openvalidation.rest.model.dto.astDTO.transformation;

import io.openvalidation.common.ast.*;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.model.OpenValidationResult;
import io.openvalidation.rest.model.dto.astDTO.*;
import io.openvalidation.rest.model.dto.astDTO.element.CommentNode;
import io.openvalidation.rest.model.dto.astDTO.element.RuleNode;
import io.openvalidation.rest.model.dto.astDTO.element.UnkownNode;
import io.openvalidation.rest.model.dto.astDTO.element.VariableNode;
import io.openvalidation.rest.model.dto.astDTO.operation.NodeMapper;
import io.openvalidation.rest.service.OVParams;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TreeTransformer {
  private List<ASTVariable> variables;
  private List<ASTItem> astItems;
  private OVParams parameters;

  public TreeTransformer(OpenValidationResult result, List<ASTItem> astItemList, OVParams parameters) {
    this.variables = result.getASTModel() != null
            ? result.getASTModel().getVariables()
            : new ArrayList<>();

    this.astItems = astItemList;
    this.parameters = parameters;
  }

  public MainNode transform(String documentText) {
    MainNode mainNode = new MainNode();
    if (this.astItems == null || this.astItems.size() == 0) return mainNode;

    ArrayList<DocumentSection> documentSections =
        new DocumentSplitter(documentText).splitDocument();
    if (documentSections.size() != this.astItems.size()) return new MainNode();

    // Set variables as declarations
    mainNode.addDeclarations(
        this.variables.stream().map(Variable::new).collect(Collectors.toList()));

    for (int index = 0; index < this.astItems.size(); index++) {
      ASTItem element = this.astItems.get(index);
      DocumentSection section = documentSections.get(index);

      GenericNode node = null;

      if (element instanceof ASTRule) {
        node = new RuleNode((ASTRule) element, section, this.parameters.getCulture());
      } else if (element instanceof ASTVariable) {
        node = new VariableNode((ASTVariable) element, section);
      } else if (element instanceof ASTComment) {
        node = new CommentNode((ASTComment) element, section);
      } else if (element instanceof ASTOperandBase) {
        node = new UnkownNode(NodeMapper.createOperand((ASTOperandBase) element, section));
      }

      if (node != null) {
        mainNode.addScope(node);
      }
    }

    return mainNode;
  }
}
