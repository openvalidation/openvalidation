package io.openvalidation.antlr.transformation.postprocessing;

import io.openvalidation.antlr.transformation.util.FunctionUtils;
import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.ASTVariable;
import io.openvalidation.common.ast.operand.ASTOperandBase;
import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.common.ast.operand.ASTOperandVariable;
import io.openvalidation.common.data.DataPropertyType;

import java.util.List;
import java.util.function.Predicate;

public class PostModelDataTypeResolver extends PostProcessorSubelementBase<ASTModel, ASTItem> {


    @Override
    protected Predicate<? super ASTItem> getFilter() {
        return x -> (x instanceof ASTVariable) || (x instanceof ASTOperandFunction) || (x instanceof ASTOperandVariable);
    }

    @Override
    protected void processItem(ASTItem item) {
        if (item instanceof ASTVariable) {
            resolve((ASTVariable) item);
        } else if (item instanceof ASTOperandFunction) {
            resolve((ASTOperandFunction) item);
        } else if (item instanceof ASTOperandVariable) {
            resolve((ASTOperandVariable) item);
        }
    }

    private void resolve(ASTVariable variable)
    {
        if(variable.getDataType() == null || variable.getDataType() == DataPropertyType.Unknown)
        {
            ASTOperandBase varContent = variable.getValue();
            if(varContent.getDataType() == null || varContent.getDataType() == DataPropertyType.Unknown)
            {
                processItem(varContent);
            }
        }
    }

    private void resolve(ASTOperandFunction function)
    {
        if(function.getDataType() == null || function.getDataType() == DataPropertyType.Unknown)
        {
            List<ASTOperandVariable> referencedVariables = function.getVariables();
            for(ASTOperandVariable var : referencedVariables)
            {
                if(var.getDataType() == null || var.getDataType() == DataPropertyType.Unknown)
                {
                    resolve(var);
                }
            }

            DataPropertyType type = FunctionUtils.resolveFunctionReturnType(function);
            function.setDataType(type);
        }
    }

    private void resolve(ASTOperandVariable variable)
    {
         if(variable.getDataType() == null || variable.getDataType() == DataPropertyType.Unknown)
        {
            DataPropertyType type;

            ASTVariable varContent = variable.getVariable();
            if(varContent.getDataType() == null || varContent.getDataType() == DataPropertyType.Unknown)
            {
                processItem(varContent);
            }
            type = varContent.getDataType();
            variable.setDataType(type);
        }
    }

}


