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
import io.openvalidation.common.model.OpenValidationResult;
import io.openvalidation.rest.model.dto.astDTO.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TreeTransformer {
  private List<ASTVariable> variables;
  private List<ASTItem> astItems;
  private TransformationParameter parameter;

  public TreeTransformer(
      OpenValidationResult result, List<ASTItem> astItemList, TransformationParameter parameter) {
    this.variables =
        result != null && result.getASTModel() != null
            ? result.getASTModel().getVariables()
            : new ArrayList<>();

    this.astItems = astItemList;
    this.parameter = parameter;
  }

  public MainNode transform() {
    MainNode mainNode = new MainNode();
    if (this.astItems == null || this.astItems.size() == 0) return mainNode;

    // Set variables as declarations
    mainNode.addDeclarations(
        this.variables.stream().map(Variable::new).collect(Collectors.toList()));

    ArrayList<DocumentSection> documentSections =
        new DocumentSplitter(parameter.getRule()).splitDocument();
    if (documentSections.size() != this.astItems.size()) return mainNode;

    for (int index = 0; index < this.astItems.size(); index++) {
      ASTItem element = this.astItems.get(index);

      DocumentSection section = documentSections.get(index);
      section.setItem(element);

      GenericNode node = NodeGenerator.generateNode(element, section, parameter);

      if (node != null) {
        mainNode.addScope(node);
      }
    }

    Range mainNodeRange = new Range();
    if (mainNode.getScopes().size() > 0) {
      mainNodeRange.setStart(mainNode.getScopes().get(0).getRange().getStart());
      mainNodeRange.setEnd(
          mainNode.getScopes().get(mainNode.getScopes().size() - 1).getRange().getEnd());
      mainNode.setRange(mainNodeRange);
    }

    return mainNode;
  }
}
