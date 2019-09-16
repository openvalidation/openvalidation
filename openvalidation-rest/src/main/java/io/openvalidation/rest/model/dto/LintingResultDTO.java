package io.openvalidation.rest.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.openvalidation.common.ast.*;
import io.openvalidation.common.ast.operand.ASTOperandStatic;
import io.openvalidation.common.ast.operand.ASTOperandStaticString;
import io.openvalidation.common.converter.SchemaConverterFactory;
import io.openvalidation.common.data.DataSchema;
import io.openvalidation.common.model.CodeGenerationResult;
import io.openvalidation.common.model.OpenValidationResult;
import io.openvalidation.common.utils.LINQ;
import io.openvalidation.rest.model.dto.astDTO.MainNode;
import io.openvalidation.rest.model.dto.astDTO.transformation.TreeTransformer;
import io.openvalidation.rest.model.dto.schema.SchemaDTO;
import io.openvalidation.rest.service.OVParams;

import java.util.List;

public class LintingResultDTO {
    @JsonAlias("variable_names")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> variableNames;

    @JsonAlias("static_strings")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> staticStrings;

    @JsonAlias("main_ast_node")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private MainNode mainAstNode;

    @JsonAlias("schema_list")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private SchemaDTO schema;

    public LintingResultDTO() {
        // serializable
    }

    public LintingResultDTO(
            OpenValidationResult ovResult, OVParams parameters, List<ASTItem> astItemList) {
        if (ovResult == null)
            throw new IllegalArgumentException("OpenValidationResult should not be null");

        CodeGenerationResult frameworkGenerationResult = ovResult.getFrameworkResult();

        TreeTransformer transformator = new TreeTransformer(ovResult, astItemList, parameters);
        MainNode node = transformator.transform(parameters.getRule());
        this.setMainAstNode(node);

        try {
            DataSchema schema = SchemaConverterFactory.convert(parameters.getSchema());
            this.setSchema(new SchemaDTO(schema));
        } catch (Exception ex) {
            System.err.print("Schema could not be generated");
        }

        ASTModel ast = ovResult.getASTModel();

        if (ast != null) {
            this.setVariableNames(LINQ.select(ast.getVariables(), ASTGlobalNamedElement::getName));
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

    public List<String> getVariableNames() {
        return variableNames;
    }

    public void setVariableNames(List<String> variableNames) {
        this.variableNames = variableNames;
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
}
