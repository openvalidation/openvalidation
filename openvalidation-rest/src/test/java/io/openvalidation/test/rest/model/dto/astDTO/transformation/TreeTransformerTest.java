// package io.openvalidation.test.rest.model.dto.astDTO.transformation;
//
// import static org.hamcrest.MatcherAssert.assertThat;
// import static org.hamcrest.Matchers.*;
//
// import io.openvalidation.common.ast.ASTItem;
// import io.openvalidation.common.data.DataPropertyType;
// import io.openvalidation.common.model.OpenValidationResult;
// import io.openvalidation.rest.model.dto.UnkownElementParser;
// import io.openvalidation.rest.model.dto.astDTO.GenericNode;
// import io.openvalidation.rest.model.dto.astDTO.MainNode;
// import io.openvalidation.rest.model.dto.astDTO.Range;
// import io.openvalidation.rest.model.dto.astDTO.element.RuleNode;
// import io.openvalidation.rest.model.dto.astDTO.element.UnkownNode;
// import io.openvalidation.rest.model.dto.astDTO.element.VariableNode;
// import io.openvalidation.rest.model.dto.astDTO.operation.ConditionNode;
// import io.openvalidation.rest.model.dto.astDTO.operation.ConnectedOperationNode;
// import io.openvalidation.rest.model.dto.astDTO.operation.OperationNode;
// import io.openvalidation.rest.model.dto.astDTO.operation.operand.FunctionOperandNode;
// import io.openvalidation.rest.model.dto.astDTO.operation.operand.OperandNode;
// import io.openvalidation.rest.model.dto.astDTO.operation.operand.OperatorNode;
// import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;
// import io.openvalidation.rest.model.dto.astDTO.transformation.TreeTransformer;
// import io.openvalidation.rest.service.OVParams;
// import io.openvalidation.rest.service.OpenValidationServiceImpl;
// import java.util.ArrayList;
// import java.util.List;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.junit.jupiter.MockitoExtension;
//
// @ExtendWith(MockitoExtension.class)
// public class TreeTransformerTest {
//
//  @InjectMocks private OpenValidationServiceImpl ovService;
//
//  @Test
//  public void transform_null_expect_empty_mainNode() {
//    TreeTransformer parser = new TreeTransformer(null, null, null);
//
//    MainNode expected = new MainNode();
//    MainNode actual = parser.transform();
//
//    assertThat(actual, is(expected));
//  }
//
//  @Test
//  public void transform_single_static_string_operand_expect_static_string_operandNode()
//      throws Exception {
//    String documentText = "Test";
//    OVParams parameter = new OVParams(documentText, "{ age: 30 }", "en", "Java");
//    OpenValidationResult result = ovService.generate(parameter);
//    List<ASTItem> astItemList =
//        new UnkownElementParser(result.getASTModel(), parameter).generate(ovService);
//
//    TreeTransformer parser = new TreeTransformer(result, astItemList, parameter);
//
//    MainNode expectedNode = new MainNode();
//    expectedNode.setRange(new Range(0, 0, 0, 4));
//
//    List<String> expectedLines = new ArrayList<>();
//    expectedLines.add(documentText);
//
//    DocumentSection expectedSection = new DocumentSection(new Range(0, 0, 0, 4), expectedLines);
//    OperandNode expectedScope = new OperandNode(DataPropertyType.String, expectedSection);
//    GenericNode wrapperScope = new UnkownNode(expectedScope, expectedSection);
//    expectedNode.addScope(wrapperScope);
//
//    MainNode actual = parser.transform();
//
//    assertThat(actual, is(expectedNode));
//  }
//
//  @Test
//  public void transform_single_decimal_operand_expect_static_string_operandNode() throws Exception
// {
//    String documentText = "age";
//    OVParams parameter = new OVParams(documentText, "{ age: 30 }", "en", "Java");
//    OpenValidationResult result = ovService.generate(parameter);
//    List<ASTItem> astItemList =
//        new UnkownElementParser(result.getASTModel(), parameter).generate(ovService);
//
//    TreeTransformer parser = new TreeTransformer(result, astItemList, parameter);
//
//    MainNode expectedNode = new MainNode();
//    expectedNode.setRange(new Range(0, 0, 0, 3));
//
//    List<String> expectedLines = new ArrayList<>();
//    expectedLines.add(documentText);
//
//    DocumentSection expectedSection = new DocumentSection(new Range(0, 0, 0, 3), expectedLines);
//    OperandNode expectedScope = new OperandNode(DataPropertyType.Decimal, expectedSection);
//    GenericNode wrapperScope = new UnkownNode(expectedScope, expectedSection);
//    expectedNode.addScope(wrapperScope);
//
//    MainNode actual = parser.transform();
//
//    assertThat(actual, is(expectedNode));
//  }
//
//  @Test
//  public void transform_single_string_operand_expect_decimal_operandNode() throws Exception {
//    String documentText = "age";
//    OVParams parameter = new OVParams(documentText, "{ age: thirty }", "en", "Java");
//    OpenValidationResult result = ovService.generate(parameter);
//    List<ASTItem> astItemList =
//        new UnkownElementParser(result.getASTModel(), parameter).generate(ovService);
//
//    TreeTransformer parser = new TreeTransformer(result, astItemList, parameter);
//
//    MainNode expectedNode = new MainNode();
//    expectedNode.setRange(new Range(0, 0, 0, 3));
//
//    List<String> expectedLines = new ArrayList<>();
//    expectedLines.add(documentText);
//
//    DocumentSection expectedSection = new DocumentSection(new Range(0, 0, 0, 3), expectedLines);
//    OperandNode expectedScope = new OperandNode(DataPropertyType.String, expectedSection);
//    GenericNode wrapperScope = new UnkownNode(expectedScope, expectedSection);
//    expectedNode.addScope(wrapperScope);
//
//    MainNode actual = parser.transform();
//
//    assertThat(actual, is(expectedNode));
//  }
//
//  @Test
//  public void transform_single_function_operand_expect_decimal_operandNode() throws Exception {
//    String documentText = "sum of age";
//    OVParams parameter = new OVParams(documentText, "{ age: thirty }", "en", "Java");
//    OpenValidationResult result = ovService.generate(parameter);
//    List<ASTItem> astItemList =
//        new UnkownElementParser(result.getASTModel(), parameter).generate(ovService);
//
//    TreeTransformer parser = new TreeTransformer(result, astItemList, parameter);
//    MainNode parsedNode = parser.transform();
//
//    assertThat(parsedNode.getScopes().size(), is(1));
//
//    GenericNode unknown = parsedNode.getScopes().get(0);
//    assertThat(unknown, instanceOf(UnkownNode.class));
//
//    GenericNode operation = ((UnkownNode) unknown).getContent();
//    assertThat(operation, instanceOf(FunctionOperandNode.class));
//    assertThat(((FunctionOperandNode) operation).getParameters().size(), is(1));
//    assertThat(
//        ((FunctionOperandNode) operation).getParameters().get(0), instanceOf(OperandNode.class));
//  }
//
//  @Test
//  public void transform_single_string_operand_expect_expect_correct_nodes() throws Exception {
//    String documentText = "age smaller 18";
//    OVParams parameter = new OVParams(documentText, "{ age: 23 }", "en", "Java");
//    OpenValidationResult result = ovService.generate(parameter);
//    List<ASTItem> astItemList =
//        new UnkownElementParser(result.getASTModel(), parameter).generate(ovService);
//
//    TreeTransformer parser = new TreeTransformer(result, astItemList, parameter);
//    MainNode parsedNode = parser.transform();
//
//    assertThat(parsedNode.getScopes().size(), is(1));
//
//    GenericNode unknown = parsedNode.getScopes().get(0);
//    assertThat(unknown, instanceOf(UnkownNode.class));
//
//    GenericNode operation = ((UnkownNode) unknown).getContent();
//    assertThat(operation, instanceOf(OperationNode.class));
//    assertThat(((OperationNode) operation).getLeftOperand(), instanceOf(OperandNode.class));
//    assertThat(((OperationNode) operation).getOperator(), instanceOf(OperatorNode.class));
//    assertThat(((OperationNode) operation).getRightOperand(), instanceOf(OperandNode.class));
//  }
//
//  @Test
//  public void transform_connected_operation_with_empty_second_operation_expect_correct_nodes()
//      throws Exception {
//    String documentText = "age smaller 18 and ";
//    OVParams parameter = new OVParams(documentText, "{ age: 23 }", "en", "Java");
//    OpenValidationResult result = ovService.generate(parameter);
//    List<ASTItem> astItemList =
//        new UnkownElementParser(result.getASTModel(), parameter).generate(ovService);
//
//    TreeTransformer parser = new TreeTransformer(result, astItemList, parameter);
//
//    MainNode parsedNode = parser.transform();
//
//    assertThat(parsedNode.getScopes().size(), is(1));
//
//    GenericNode unknown = parsedNode.getScopes().get(0);
//    assertThat(unknown, instanceOf(UnkownNode.class));
//
//    GenericNode connectedOperation = ((UnkownNode) unknown).getContent();
//    assertThat(connectedOperation, instanceOf(ConnectedOperationNode.class));
//
//    int length = ((ConnectedOperationNode) connectedOperation).getConditions().size();
//    assertThat(length, is(2));
//
//    GenericNode firstOperation =
//        ((ConnectedOperationNode) connectedOperation).getConditions().get(0);
//    assertThat(((OperationNode) firstOperation).getLeftOperand(), instanceOf(OperandNode.class));
//    assertThat(((OperationNode) firstOperation).getOperator(), instanceOf(OperatorNode.class));
//    assertThat(((OperationNode) firstOperation).getRightOperand(), instanceOf(OperandNode.class));
//
//    GenericNode secondOperation =
//        ((ConnectedOperationNode) connectedOperation).getConditions().get(1);
//    assertThat(((OperationNode) secondOperation).getLeftOperand(), nullValue());
//    assertThat(((OperationNode) secondOperation).getOperator(), nullValue());
//    assertThat(((OperationNode) secondOperation).getRightOperand(), nullValue());
//  }
//
//  @Test
//  public void transform_connected_operation_with_second_operation_expect_correct_nodes()
//      throws Exception {
//    String documentText = "age smaller 18 and age smaller 18";
//    OVParams parameter = new OVParams(documentText, "{ age: 23 }", "en", "Java");
//    OpenValidationResult result = ovService.generate(parameter);
//    List<ASTItem> astItemList =
//        new UnkownElementParser(result.getASTModel(), parameter).generate(ovService);
//
//    TreeTransformer parser = new TreeTransformer(result, astItemList, parameter);
//
//    MainNode parsedNode = parser.transform();
//
//    assertThat(parsedNode.getScopes().size(), is(1));
//
//    GenericNode unknown = parsedNode.getScopes().get(0);
//    assertThat(unknown, instanceOf(UnkownNode.class));
//
//    GenericNode connectedOperation = ((UnkownNode) unknown).getContent();
//    assertThat(connectedOperation, instanceOf(ConnectedOperationNode.class));
//
//    int length = ((ConnectedOperationNode) connectedOperation).getConditions().size();
//    assertThat(length, is(2));
//
//    GenericNode firstOperation =
//        ((ConnectedOperationNode) connectedOperation).getConditions().get(0);
//    assertThat(((OperationNode) firstOperation).getLeftOperand(), instanceOf(OperandNode.class));
//    assertThat(((OperationNode) firstOperation).getOperator(), instanceOf(OperatorNode.class));
//    assertThat(((OperationNode) firstOperation).getRightOperand(), instanceOf(OperandNode.class));
//
//    GenericNode secondOperation =
//        ((ConnectedOperationNode) connectedOperation).getConditions().get(1);
//    assertThat(((OperationNode) secondOperation).getLeftOperand(), instanceOf(OperandNode.class));
//    assertThat(((OperationNode) secondOperation).getOperator(), instanceOf(OperatorNode.class));
//    assertThat(((OperationNode) secondOperation).getRightOperand(),
// instanceOf(OperandNode.class));
//  }
//
//  @Test
//  public void transform_connected_operation_with_empty_third_operation_expect_correct_nodes()
//      throws Exception {
//    String documentText = "age smaller 18 and age smaller 18 or";
//    OVParams parameter = new OVParams(documentText, "{ age: 23 }", "en", "Java");
//    OpenValidationResult result = ovService.generate(parameter);
//    List<ASTItem> astItemList =
//        new UnkownElementParser(result.getASTModel(), parameter).generate(ovService);
//
//    TreeTransformer parser = new TreeTransformer(result, astItemList, parameter);
//
//    MainNode parsedNode = parser.transform();
//
//    assertThat(parsedNode.getScopes().size(), is(1));
//
//    GenericNode unknown = parsedNode.getScopes().get(0);
//    assertThat(unknown, instanceOf(UnkownNode.class));
//
//    GenericNode connectedOperation = ((UnkownNode) unknown).getContent();
//    assertThat(connectedOperation, instanceOf(ConnectedOperationNode.class));
//
//    int length = ((ConnectedOperationNode) connectedOperation).getConditions().size();
//    assertThat(length, is(3));
//
//    GenericNode firstOperation =
//        ((ConnectedOperationNode) connectedOperation).getConditions().get(0);
//    assertThat(((OperationNode) firstOperation).getLeftOperand(), instanceOf(OperandNode.class));
//    assertThat(((OperationNode) firstOperation).getOperator(), instanceOf(OperatorNode.class));
//    assertThat(((OperationNode) firstOperation).getRightOperand(), instanceOf(OperandNode.class));
//
//    GenericNode secondOperation =
//        ((ConnectedOperationNode) connectedOperation).getConditions().get(1);
//    assertThat(((OperationNode) secondOperation).getLeftOperand(), instanceOf(OperandNode.class));
//    assertThat(((OperationNode) secondOperation).getOperator(), instanceOf(OperatorNode.class));
//    assertThat(((OperationNode) secondOperation).getRightOperand(),
// instanceOf(OperandNode.class));
//
//    GenericNode thirdOperation =
//        ((ConnectedOperationNode) connectedOperation).getConditions().get(2);
//    assertThat(((OperationNode) thirdOperation).getLeftOperand(), nullValue());
//    assertThat(((OperationNode) thirdOperation).getOperator(), nullValue());
//    assertThat(((OperationNode) thirdOperation).getRightOperand(), nullValue());
//  }
//
//  @Test
//  public void transform_simple_rule_expect_static_string_operandNode() throws Exception {
//    String documentText = "If age smaller 18 then error";
//    OVParams parameter = new OVParams(documentText, "{ age: 23 }", "en", "Java");
//    OpenValidationResult result = ovService.generate(parameter);
//    List<ASTItem> astItemList =
//        new UnkownElementParser(result.getASTModel(), parameter).generate(ovService);
//
//    TreeTransformer parser = new TreeTransformer(result, astItemList, parameter);
//
//    MainNode parsedNode = parser.transform();
//
//    assertThat(parsedNode.getScopes().size(), is(1));
//
//    GenericNode unknown = parsedNode.getScopes().get(0);
//    assertThat(unknown, instanceOf(RuleNode.class));
//
//    ConditionNode operation = ((RuleNode) unknown).getCondition();
//    assertThat(operation, instanceOf(OperationNode.class));
//
//    assertThat(((OperationNode) operation).getLeftOperand(), instanceOf(OperandNode.class));
//    assertThat(((OperationNode) operation).getOperator(), instanceOf(OperatorNode.class));
//    assertThat(((OperationNode) operation).getRightOperand(), instanceOf(OperandNode.class));
//  }
//
//  @Test
//  public void transform_rule_with_connected_operation_expect_static_string_operandNode()
//      throws Exception {
//    String documentText = "If age smaller 18 and age smaller 128 then error";
//    OVParams parameter = new OVParams(documentText, "{ age: 23 }", "en", "Java");
//    OpenValidationResult result = ovService.generate(parameter);
//    List<ASTItem> astItemList =
//        new UnkownElementParser(result.getASTModel(), parameter).generate(ovService);
//
//    TreeTransformer parser = new TreeTransformer(result, astItemList, parameter);
//
//    MainNode parsedNode = parser.transform();
//
//    assertThat(parsedNode.getScopes().size(), is(1));
//
//    GenericNode unknown = parsedNode.getScopes().get(0);
//    assertThat(unknown, instanceOf(RuleNode.class));
//
//    ConditionNode connectedOperation = ((RuleNode) unknown).getCondition();
//    assertThat(connectedOperation, instanceOf(ConnectedOperationNode.class));
//
//    int length = ((ConnectedOperationNode) connectedOperation).getConditions().size();
//    assertThat(length, is(2));
//
//    GenericNode firstOperation =
//        ((ConnectedOperationNode) connectedOperation).getConditions().get(0);
//    assertThat(((OperationNode) firstOperation).getLeftOperand(), instanceOf(OperandNode.class));
//    assertThat(((OperationNode) firstOperation).getOperator(), instanceOf(OperatorNode.class));
//    assertThat(((OperationNode) firstOperation).getRightOperand(), instanceOf(OperandNode.class));
//
//    GenericNode secondOperation =
//        ((ConnectedOperationNode) connectedOperation).getConditions().get(1);
//    assertThat(((OperationNode) secondOperation).getLeftOperand(), instanceOf(OperandNode.class));
//    assertThat(((OperationNode) secondOperation).getOperator(), instanceOf(OperatorNode.class));
//    assertThat(((OperationNode) secondOperation).getRightOperand(),
// instanceOf(OperandNode.class));
//  }
//
//  @Test
//  public void transform_variable_with_connected_operation_expect_static_string_operandNode()
//      throws Exception {
//    String documentText = "age smaller 18 and age smaller 18 as tmp_parsing";
//    OVParams parameter = new OVParams(documentText, "{ age: 23 }", "en", "Java");
//    OpenValidationResult result = ovService.generate(parameter);
//    List<ASTItem> astItemList =
//        new UnkownElementParser(result.getASTModel(), parameter).generate(ovService);
//
//    TreeTransformer parser = new TreeTransformer(result, astItemList, parameter);
//
//    MainNode parsedNode = parser.transform();
//
//    assertThat(parsedNode.getScopes().size(), is(1));
//
//    GenericNode unknown = parsedNode.getScopes().get(0);
//    assertThat(unknown, instanceOf(VariableNode.class));
//
//    OperandNode connectedOperation = ((VariableNode) unknown).getValue();
//    assertThat(connectedOperation, instanceOf(ConnectedOperationNode.class));
//
//    int length = ((ConnectedOperationNode) connectedOperation).getConditions().size();
//    assertThat(length, is(2));
//
//    GenericNode firstOperation =
//        ((ConnectedOperationNode) connectedOperation).getConditions().get(0);
//    assertThat(((OperationNode) firstOperation).getLeftOperand(), instanceOf(OperandNode.class));
//    assertThat(((OperationNode) firstOperation).getOperator(), instanceOf(OperatorNode.class));
//    assertThat(((OperationNode) firstOperation).getRightOperand(), instanceOf(OperandNode.class));
//
//    GenericNode secondOperation =
//        ((ConnectedOperationNode) connectedOperation).getConditions().get(1);
//    assertThat(((OperationNode) secondOperation).getLeftOperand(), instanceOf(OperandNode.class));
//    assertThat(((OperationNode) secondOperation).getOperator(), instanceOf(OperatorNode.class));
//    assertThat(((OperationNode) secondOperation).getRightOperand(),
// instanceOf(OperandNode.class));
//  }
// }
