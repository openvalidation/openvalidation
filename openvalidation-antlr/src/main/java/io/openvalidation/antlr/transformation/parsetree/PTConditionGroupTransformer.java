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

import io.openvalidation.antlr.generated.mainLexer;
import io.openvalidation.antlr.generated.mainParser;
import io.openvalidation.antlr.transformation.TransformerBase;
import io.openvalidation.antlr.transformation.TransformerContext;
import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.condition.ASTConditionBase;
import io.openvalidation.common.ast.condition.ASTConditionConnector;
import io.openvalidation.common.ast.condition.ASTConditionGroup;
import io.openvalidation.common.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class PTConditionGroupTransformer
    extends TransformerBase<
        PTConditionGroupTransformer, ASTConditionGroup, mainParser.Condition_groupContext> {

  public PTConditionGroupTransformer(
      mainParser.Condition_groupContext treeCntx, TransformerContext fctx) {
    super(treeCntx, fctx);
  }

  @Override
  public ASTConditionGroup transform() throws Exception {
    List<ASTCondition> conditionList = new ArrayList<>();

    AtomicReference<ASTConditionConnector> connector = new AtomicReference<>();
    AtomicReference<String> connectorSource = new AtomicReference<>();

    this.eachTreeChild(
        c -> {
          if (ParseTreeUtils.getSymbol(c) == mainLexer.COMBINATOR) {
            connector.set(ParseTreeUtils.getCombinator(c));
            connectorSource.set(c.getText());
          }

          if (c instanceof mainParser.Condition_exprContext) {
            ASTItem condition = this.createASTItem(c);
            ASTConditionConnector cntr = connector.get();

            if (condition instanceof ASTCondition) {
              ASTCondition cnd = (ASTCondition) condition;
              if (cntr != null) {
                cnd.setConnector(cntr);
                cnd.setSource(
                    ParseTreeUtils.getTextFromConnectorAndNode(
                        (mainParser.Condition_exprContext) c));
              }
              cnd.setIndentationLevel(factoryCntx.getCurrentConditionIndentation());
              conditionList.add(cnd);
            } else if (condition != null) {
              PTConditionTransformer conditionTrns = new PTConditionTransformer(null, factoryCntx);
              condition = conditionTrns.transformImplicitCondition(condition, c);

              if (condition != null) {
                ASTCondition cnd = (ASTCondition) condition;
                if (cntr != null) {
                  cnd.setConnector(cntr);
                  cnd.setSource(
                      ParseTreeUtils.getTextFromConnectorAndNode(
                          (mainParser.Condition_exprContext) c));
                }
                cnd.setIndentationLevel(factoryCntx.getCurrentConditionIndentation());
                conditionList.add(cnd);
              }
            }

            //                if (condition == null)
            //                    throw new ASTValidationException("unknown condition within
            // condition group.", null);

            connector.set(null);
            factoryCntx.setTrailingWhitespaceAsCurrentIndentation(c.getText());
          }
        });

    factoryCntx.setCurrentConditionIndentation(0);
    ASTConditionGroup result = createNestedStructure(conditionList);

    // result.getAllConditions();
    result.setSource(this.antlrTreeCntx.getText());

    // result.UpdateSources();
    UpdateSubGroupSources(result);

    return result;
  }

  private void UpdateSubGroupSources(ASTConditionGroup group) {
    String prevSource = "";
    String totalSource =
        StringUtils.isNullOrEmpty(group.getPreprocessedSource())
            ? this.antlrTreeCntx.getText()
            : group.getPreprocessedSource();
    for (ASTConditionBase condition : group.getConditions()) {

      if (condition instanceof ASTConditionGroup) {
        ASTConditionGroup cond = (ASTConditionGroup) condition;
        UpdateSubGroupSources(cond);

        if (StringUtils.isNullOrEmpty(condition.getPreprocessedSource())) {

          int startIndex =
              (StringUtils.isNullOrEmpty(prevSource))
                  ? 0
                  : totalSource.indexOf(prevSource) + prevSource.length();
          ASTConditionBase lastMember = cond.getConditions().get(cond.getConditions().size() - 1);
          String source =
              totalSource.substring(
                  startIndex,
                  totalSource.indexOf(lastMember.getPreprocessedSource())
                      + lastMember.getPreprocessedSource().length());

          cond.setSource(source);
        }
      }

      prevSource = condition.getPreprocessedSource();
    }
  }

  private void normalize(List<ASTCondition> list) {
    int lowestIndent = Integer.MAX_VALUE;

    for (int i = 1; i < list.size(); i++) {
      if (list.get(i).getIndentationLevel() < lowestIndent) {
        lowestIndent = list.get(i).getIndentationLevel();
      }
    }

    // reduce level of all conditions by lowestIndent
    for (int i = 1; i < list.size(); i++) {
      list.get(i).setRelativeIndentationLevel(list.get(i).getIndentationLevel() - lowestIndent);
    }
  }

  private ASTConditionGroup createNestedStructure(List<ASTCondition> conditions) {
    ASTConditionGroup conditionGroup = createNestedStructureRecursive(conditions);

    // danach noch Gruppen mit nur einem ASTCondition entpacken
    ASTConditionBase result = unpackSingleElementGroups(conditionGroup);
    updateAllSubgroupsConnectors(result);

    return wrapIfNecessary(result);
  }

  private ASTConditionGroup createNestedStructureRecursive(List<ASTCondition> conditions) {
    if (conditions.size() > 0) {

      ASTConditionGroup group = new ASTConditionGroup();
      List<ASTCondition> remainingConditions = conditions;
      normalize(remainingConditions);

      int lowestIndentationLevel = getLowestIndentationLevel(remainingConditions);
      int subgroupStartIndex = getSubGroupStartIndex(remainingConditions, lowestIndentationLevel);

      int subgroupEndIndex =
          getIndexOfReturningToLowestIndentation(
              remainingConditions, subgroupStartIndex, lowestIndentationLevel);

      boolean subGroupExists = subgroupStartIndex > -1;

      while (subGroupExists) {
        List<ASTCondition> initialConditionsOnSameLevel =
            remainingConditions.subList(0, subgroupStartIndex);
        addEachConditionAsExpressionGroupWithSingleElement(group, initialConditionsOnSameLevel);

        // determine encapsulation of subgroup recursively
        List<ASTCondition> subGroupConditions =
            remainingConditions.subList(subgroupStartIndex, subgroupEndIndex);
        ASTConditionGroup subGroup = createNestedStructureRecursive(subGroupConditions);
        group.addCondition(subGroup);

        // update list
        remainingConditions =
            remainingConditions.subList(subgroupEndIndex, remainingConditions.size());
        // find indexes
        lowestIndentationLevel = getLowestIndentationLevel(remainingConditions);
        subgroupStartIndex = getSubGroupStartIndex(remainingConditions, lowestIndentationLevel);
        subgroupEndIndex =
            getIndexOfReturningToLowestIndentation(
                remainingConditions, subgroupStartIndex, lowestIndentationLevel);

        // update while condition
        subGroupExists = subgroupStartIndex != -1;
      }

      // when no deeper indentation is found with all remaining
      addEachConditionAsExpressionGroupWithSingleElement(group, remainingConditions);

      return group;
    } else {
      throw new IllegalStateException("size of condition list is 0");
    }
  }

  private int getSubGroupStartIndex(
      List<ASTCondition> remainingConditions, int lowestIndentationLevel) {
    int subgroupStartIndex =
        getIndexOfFirstDeeperIndentation(remainingConditions, lowestIndentationLevel);
    if (subgroupStartIndex > 0) {
      subgroupStartIndex--;
    }

    return subgroupStartIndex;
  }

  private void addEachConditionAsExpressionGroupWithSingleElement(
      ASTConditionGroup expressionGroup, List<ASTCondition> conditionList) {
    for (ASTCondition condition : conditionList) {
      // create single element group
      ASTConditionGroup singleElementGroup = new ASTConditionGroup();
      singleElementGroup.addCondition(condition);
      // add group
      expressionGroup.addCondition(singleElementGroup);
    }
  }

  private int getIndexOfReturningToLowestIndentation(
      List<ASTCondition> conditionList, int startIndex, int lowestIndentationLevel) {
    if (startIndex >= conditionList.size() || startIndex < 0) {
      return -1;
    }

    for (int i = startIndex + 1; i < conditionList.size(); i++) {
      if (conditionList.get(i).getRelativeIndentationLevel() <= lowestIndentationLevel) {
        return i;
      }
    }

    return conditionList.size();
  }

  private int getIndexOfFirstDeeperIndentation(
      List<ASTCondition> conditionList, int lowestIndentationLevel) {
    for (int i = 0; i < conditionList.size(); i++) {
      if (conditionList.get(i).getRelativeIndentationLevel() > lowestIndentationLevel) {
        return i;
      }
    }

    // when no deeper indentation found
    return -1;
  }

  private int getLowestIndentationLevel(List<ASTCondition> conditionList) {
    if (conditionList.size() > 0) {
      int min = conditionList.get(0).getRelativeIndentationLevel();

      for (ASTCondition condition : conditionList) {
        if (condition.getRelativeIndentationLevel() < min) {
          min = condition.getRelativeIndentationLevel();
        }
      }

      return min;
    } else {
      return Integer.MAX_VALUE;
    }
  }

  private ASTConditionBase unpackSingleElementGroups(ASTConditionGroup group) {
    for (int i = 0; i < group.getConditions().size(); i++) {
      ASTConditionBase currentElement = group.getConditions().get(i);

      if (currentElement instanceof ASTConditionGroup) {
        ASTConditionGroup currentGroup = (ASTConditionGroup) currentElement;

        int nrOfContainedElements = currentGroup.getConditions().size();
        boolean firstElementIsCondition =
            currentGroup.getConditions().get(0) instanceof ASTCondition;

        if (nrOfContainedElements == 1 && firstElementIsCondition) {
          // ASTConditionGroup is replaced by the single ASTCondition it contains
          group.getConditions().set(i, currentGroup.getConditions().get(0));
        } else {
          ASTConditionBase unpackedSubGroup = unpackSingleElementGroups(currentGroup);
          group.getConditions().set(i, unpackedSubGroup);
        }
      }
    }

    // only used for the ASTConditionGroup that wraps everything (most outer)
    if (group.getConditions().size() == 1) {
      return group.getConditions().get(0);
    }
    return group;
  }

  // jede ConditionGroup kriegt als logischen Operator den Operator des ersten, in ihr enthaltenen
  // Conditions. Das ASTCondition verliert zudem seinen Operator

  private void updateAllSubgroupsConnectors(ASTConditionBase conditionBase) {
    if (conditionBase instanceof ASTConditionGroup) {
      ASTConditionGroup parentGroup = (ASTConditionGroup) conditionBase;
      updateOperator(parentGroup);

      updateOperatorsOfSubgroups(parentGroup);
    }
  }

  private void updateOperatorsOfSubgroups(ASTConditionGroup parentGroup) {
    for (ASTConditionBase subExpression : parentGroup.getConditions()) {
      if (subExpression instanceof ASTConditionGroup) {
        updateAllSubgroupsConnectors(subExpression);
      }
    }
  }

  private void updateOperator(ASTConditionGroup group) {
    ASTConditionBase firstElem = group.getConditions().get(0);

    while (!(firstElem instanceof ASTCondition)) {
      List<ASTConditionBase> subGroupConditions = ((ASTConditionGroup) firstElem).getConditions();
      firstElem = subGroupConditions.get(0);
    }

    // when ASTCondition is found eventually
    ASTConditionConnector connector = firstElem.getConnector();
    firstElem.setConnector(null);
    group.setConnector(connector);
  }

  private ASTConditionGroup wrapIfNecessary(ASTConditionBase result) {
    if (result instanceof ASTCondition) {
      ASTConditionGroup wrappedCondition = new ASTConditionGroup();
      wrappedCondition.addCondition(result);
      return wrappedCondition;
    } else {
      return (ASTConditionGroup) result;
    }
  }
}
