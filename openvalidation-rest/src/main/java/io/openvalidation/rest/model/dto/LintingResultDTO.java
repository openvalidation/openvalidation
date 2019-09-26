package io.openvalidation.rest.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.openvalidation.common.ast.*;
import io.openvalidation.common.ast.operand.ASTOperandStatic;
import io.openvalidation.common.ast.operand.ASTOperandStaticString;
import io.openvalidation.common.converter.SchemaConverterFactory;
import io.openvalidation.common.data.DataSchema;
import io.openvalidation.common.exceptions.ASTValidationException;
import io.openvalidation.common.exceptions.ASTValidationSummaryException;
import io.openvalidation.common.exceptions.OpenValidationException;
import io.openvalidation.common.model.CodeGenerationResult;
import io.openvalidation.common.model.OpenValidationResult;
import io.openvalidation.common.utils.LINQ;
import io.openvalidation.rest.model.dto.astDTO.GenericNode;
import io.openvalidation.rest.model.dto.astDTO.MainNode;
import io.openvalidation.rest.model.dto.astDTO.Range;
import io.openvalidation.rest.model.dto.astDTO.element.RuleNode;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;
import io.openvalidation.rest.model.dto.astDTO.transformation.RangeGenerator;
import io.openvalidation.rest.model.dto.astDTO.transformation.TreeTransformer;
import io.openvalidation.rest.model.dto.schema.SchemaDTO;
import io.openvalidation.rest.service.OVParams;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LintingResultDTO {

    @JsonAlias("static_strings")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> staticStrings;

    @JsonAlias("main_ast_node")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private MainNode mainAstNode;

    @JsonAlias("schema_list")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private SchemaDTO schema;

    private List<OpenValidationExceptionDTO> errors;

    public LintingResultDTO() {
        // serializable
    }

    public LintingResultDTO(
            OpenValidationResult ovResult, OVParams parameters, List<ASTItem> astItemList) {
        if (ovResult == null)
            throw new IllegalArgumentException("OpenValidationResult should not be null");

        TreeTransformer transformator = new TreeTransformer(ovResult, astItemList, parameters);
        MainNode node = transformator.transform(parameters.getRule());
        this.setMainAstNode(node);

        try {
            DataSchema schema = SchemaConverterFactory.convert(parameters.getSchema());
            this.setSchema(new SchemaDTO(schema));
        } catch (Exception ex) {
            System.err.print("Schema could not be generated");
        }

        int actionErrorIndex = 0;
        this.errors = new ArrayList<>();
        for (OpenValidationException error: ovResult.getErrors()) {
            if (error instanceof ASTValidationException) {
                //default position for errors that can't be mapped to a specific element
                if (((ASTValidationException) error).getItem() == null) {
                    errors.add(new OpenValidationExceptionDTO(error.getMessage(), new Range(0,0,0,1)));
                    continue;
                }

                ASTItem item = ((ASTValidationException) error).getItem();
                if (item instanceof ASTActionError) {
                    List<GenericNode> fittingRules =
                            node.getScopes().stream().filter(rule -> rule instanceof RuleNode
                                    && ((RuleNode) rule).getErrorNode() != null
                                    && ((RuleNode) rule).getErrorNode().getErrorMessage() != null
                                    && ((RuleNode) rule).getErrorNode().getErrorMessage().equals(item.getOriginalSource()))
                                    .collect(Collectors.toList());
                    if (fittingRules.size() > actionErrorIndex) {
                        errors.add(new OpenValidationExceptionDTO(error.getMessage(), fittingRules.get(actionErrorIndex).getRange()));
                        actionErrorIndex++;
                        continue;
                    }
                }

                String sourceString = item.getOriginalSource();
                if (sourceString.isEmpty()) continue;

                if (item instanceof ASTGlobalElement) {
                    int position = item.getGlobalPosition();

                    if (position > node.getScopes().size()) {
                        DocumentSection newSection = new RangeGenerator(parameters.getRule()).generate(sourceString);
                        errors.add(new OpenValidationExceptionDTO(error.getMessage(), newSection.getRange()));
                        continue;
                    }

                    GenericNode generatedNode = node.getScopes().get(position - 1);
                    DocumentSection newSection = new RangeGenerator(generatedNode.getLines(), generatedNode.getRange()).generate(sourceString);
                    errors.add(new OpenValidationExceptionDTO(error.getMessage(), newSection.getRange()));
                } else {
                    DocumentSection newSection = new RangeGenerator(parameters.getRule()).generate(sourceString);
                    errors.add(new OpenValidationExceptionDTO(error.getMessage(), newSection.getRange()));
                }
            } else if (error instanceof ASTValidationSummaryException) {
                String sourceString = ((ASTValidationSummaryException) error).getModel().getOriginalSource();
                if (sourceString.isEmpty()) continue;

                DocumentSection newSection = new RangeGenerator(parameters.getRule()).generate(sourceString);
                errors.add(new OpenValidationExceptionDTO(error.getMessage(), newSection.getRange()));
            } else {
                errors.add(new OpenValidationExceptionDTO(error.getMessage()));
            }
        }

        ASTModel ast = ovResult.getASTModel();
        if (ast != null) {
            this.setStaticStrings(
                    LINQ.select(ast.collectItemsOfType(ASTOperandStaticString.class), ASTOperandStatic::getValue));
        }
    }

    public SchemaDTO getSchema() {
        return schema;
    }

    public void setSchema(SchemaDTO schema) {
        this.schema = schema;
    }

    public List<String> getStaticStrings() {
        return staticStrings;
    }

    public void setStaticStrings(List<String> staticStrings) {
        this.staticStrings = staticStrings;
    }

    public MainNode getMainAstNode() {
        return mainAstNode;
    }

    public void setMainAstNode(MainNode mainAstNode) {
        this.mainAstNode = mainAstNode;
    }

    public List<OpenValidationExceptionDTO> getErrors() {
        return errors;
    }

    public void setErrors(List<OpenValidationExceptionDTO> errors) {
        this.errors = errors;
    }
}
