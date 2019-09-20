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

import io.openvalidation.common.ast.ASTActionError
import io.openvalidation.common.ast.ASTComparisonOperator
import io.openvalidation.common.ast.*
import io.openvalidation.common.ast.condition.ASTCondition
import io.openvalidation.common.ast.condition.ASTConditionBase
import io.openvalidation.common.ast.operand.*
import io.openvalidation.common.ast.operand.*
import io.openvalidation.common.ast.operand.lambda.ASTOperandLambdaProperty
import io.openvalidation.common.ast.operand.property.ASTOperandProperty
import io.openvalidation.common.data.DataPropertyType
import io.openvalidation.common.utils.Constants
import io.openvalidation.core.OpenValidation
import org.junit.jupiter.api.Test

import io.openvalidation.common.unittesting.astassertion.ModelRootAssertion.assertResult
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*

class ASTModelTests {

    @Test
    @Throws(Exception::class)
    fun should_build_right_ast() {
        val plainRule = """
            SUM OF   portfolio.shares.percentage
                AS   TOTAL_SUM
                
                IF   a TOTAL_SUM of all shares is NOT EQUALS 100 %
              THEN   The total sum of all portfolio shares should be exactly 100%
              
              IF   the name of product EQUALS TikTok
            THEN   i do not like it
                        """

        val schema = """
                    {
                        name: "string",
                        portfolio:{
                            shares:[
                            {
                                percentage: 20
                            }]
                        }
                    }
                    """
                //"{name:\"string\", portfolio:{shares:[{percentage:20}]}}"

        val ov = OpenValidation.createDefault()
        ov.setLocale("en")
        ov.setSchema(schema)
        ov.setRule(plainRule)

        val result = ov.generateCode(true)

        assertResult(result)
                .sizeOfElements(3)
                .sizeOfRules(2)
                .sizeOfVariables(1)
                .firstVariable("TOTAL_SUM")
                        .function("SUM_OF")
                                .hasPreprocessedSource(Constants.FUNCTION_TOKEN + "sum_of" + Constants.KEYWORD_SYMBOL + "SUM_20_OF   portfolio.shares.percentage")
                                .firstArrayOfFunction()
                                        .firstProperty("portfolio.shares")
                        .parentFunction()
                                .secondPropertyLambda("percentage")


        val model = result.astModel

        assertThat(model, notNullValue())
        assertThat(model!!.elements, hasSize(3))
        assertThat(model.rules, hasSize(2))

        //variable
        assertThat(model.variables, hasSize(1))

        val variable = model.variables[0]
        assertThat(variable, notNullValue())
        assertThat(variable.name, `is`("TOTAL_SUM"))
        assertThat<ASTOperandBase>(variable.value, notNullValue())
        assertThat<ASTOperandBase>(variable.value, instanceOf<ASTOperandBase>(ASTOperandFunction::class.java))

        val function = variable.value as ASTOperandFunction

        assertThat(function.name, `is`("SUM_OF"))
        assertThat(function.preprocessedSource, `is`(Constants.FUNCTION_TOKEN + "sum_of" + Constants.KEYWORD_SYMBOL + "SUM_20_OF   portfolio.shares.percentage"))

        assertThat<List<ASTOperandBase>>(function.parameters, notNullValue())
        assertThat<List<ASTOperandBase>>(function.parameters, hasSize<ASTOperandBase>(1))

        assertThat<ASTOperandBase>(function.parameters[0], notNullValue())
        assertThat<ASTOperandBase>(function.parameters[0], instanceOf<ASTOperandBase>(ASTOperandFunction::class.java))

        //function
        val innerArrFunction = function.parameters[0] as ASTOperandFunction

        assertThat(innerArrFunction, notNullValue())
        assertThat(innerArrFunction.name, `is`("GET_ARRAY_OF"))
        assertThat<List<ASTOperandBase>>(innerArrFunction.parameters, notNullValue())
        assertThat<List<ASTOperandBase>>(innerArrFunction.parameters, hasSize<ASTOperandBase>(2))
        assertThat<ASTOperandBase>(innerArrFunction.parameters[0], instanceOf<ASTOperandBase>(ASTOperandProperty::class.java))
        assertThat<ASTOperandBase>(innerArrFunction.parameters[1], instanceOf<ASTOperandBase>(ASTOperandLambdaProperty::class.java))

        assertThat((innerArrFunction.parameters[0] as ASTOperandProperty).pathAsString, `is`("portfolio.shares"))
        assertThat((innerArrFunction.parameters[1] as ASTOperandLambdaProperty).property.pathAsString, `is`("percentage"))
        //assertThat((innerArrFunction.parameters[1] as ASTOperandProperty).lambdaToken, `is`("s"))


        //first rule
        val rule = model.rules[0]
        assertThat(rule, notNullValue())
        assertThat(rule.action, notNullValue())
        assertThat(rule.action, instanceOf(ASTActionError::class.java))
        assertThat((rule.action as ASTActionError).errorMessage.toLowerCase(), `is`("The total sum of all portfolio shares should be exactly 100%".toLowerCase()))

        assertThat<ASTConditionBase>(rule.condition, notNullValue())
        assertThat<ASTConditionBase>(rule.condition, instanceOf<ASTConditionBase>(ASTCondition::class.java))

        val condition = rule.condition as ASTCondition

        assertThat<ASTOperandBase>(condition.leftOperand, notNullValue())
        assertThat<ASTOperandBase>(condition.leftOperand, instanceOf<ASTOperandBase>(ASTOperandVariable::class.java))
        assertThat((condition.leftOperand as ASTOperandVariable).variableName, `is`("TOTAL_SUM"))
        assertThat((condition.leftOperand as ASTOperandVariable).dataType, `is`(DataPropertyType.Decimal))

        assertThat(condition.operator, `is`(ASTComparisonOperator.NOT_EQUALS))

        assertThat<ASTOperandBase>(condition.rightOperand, notNullValue())
        assertThat<ASTOperandBase>(condition.rightOperand, instanceOf<ASTOperandBase>(ASTOperandStaticNumber::class.java))
        assertThat((condition.rightOperand as ASTOperandStaticNumber).numberValue, `is`(100.0))


        //second rule
        val second = model.rules[1]
        assertThat(second, notNullValue())
        assertThat(second.action, notNullValue())
        assertThat(second.action, instanceOf(ASTActionError::class.java))
        assertThat((second.action as ASTActionError).errorMessage, `is`("i do not like it"))

        assertThat<ASTConditionBase>(second.condition, notNullValue())
        assertThat<ASTConditionBase>(second.condition, instanceOf<ASTConditionBase>(ASTCondition::class.java))

        val secondcondition = second.condition as ASTCondition

        assertThat<ASTOperandBase>(secondcondition.leftOperand, notNullValue())
        assertThat<ASTOperandBase>(secondcondition.leftOperand, instanceOf<ASTOperandBase>(ASTOperandProperty::class.java))
        assertThat((secondcondition.leftOperand as ASTOperandProperty).pathAsString, `is`("name"))

        assertThat(secondcondition.operator, `is`(ASTComparisonOperator.EQUALS))

        assertThat<ASTOperandBase>(secondcondition.rightOperand, notNullValue())
        assertThat<ASTOperandBase>(secondcondition.rightOperand, instanceOf<ASTOperandBase>(ASTOperandStaticString::class.java))
        assertThat((secondcondition.rightOperand as ASTOperandStaticString).value, `is`("TikTok"))
    }
}
