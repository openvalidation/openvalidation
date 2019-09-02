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

package io.openvalidation.common.log.aspects;

//import io.openvalidation.common.transformation.ASTModel;
//import IOpenValidationGenerator;
//import Language;
//
//public aspect ProcessAspect {
//    final int MIN_BALANCE = 10;
//
//    pointcut callGenerate(ASTModel transformation, Language language, IOpenValidationGenerator generationProcess) :
//            call(boolean IOpenValidationGenerator.generate(ASTModel, Language)) && args(transformation, language) && target(generationProcess);
//
//    before(ASTModel transformation, Language language, IOpenValidationGenerator generationProcess) : callGenerate(transformation, language, generationProcess) {
//    }
//
//    boolean around(ASTModel transformation, Language language, IOpenValidationGenerator generationProcess) :
//            callGenerate(transformation, language, generationProcess) {
////        if (acc.balance < amount) {
////            return false;
////        }
//        return proceed(transformation, language, generationProcess);
//    }
//
//    after(ASTModel transformation, Language language, IOpenValidationGenerator generationProcess) : callGenerate(transformation, language, generationProcess) {
//    }
//}
