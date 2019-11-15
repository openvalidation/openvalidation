# HUMLFramework Contribution

## Reference to testing repository

Take the notes on [openvalidation-framework-tests](https://github.com/openvalidation/openvalidation-framework-tests) as a cross reference.
Tests for generated sources are in that repository.

## General information

Contribution to the Framework mainly involves changing templates in ``/openvalidation-generation/src/main/resources``.

openValidation uses [handlebars](https://handlebarsjs.com/reference.html) as template engine, similar to [Swagger Codegen](https://github.com/swagger-api/swagger-codegen).

## Adding functions to existing HUMLFramework templates

As in reference to [Add Features to HUMLFramework](https://github.com/openvalidation/openvalidation-framework-tests/blob/master/NewFeatures.md).

We'd recommend you start implementing your Feature from the Endpoint backwards (read: start by writing the code that should be generated, and work your way back towards the language transformer from there). The test repository readme linked above guides you to the location where tests for those functions are located, and how to test for them.

This means you should create the desired functionality in the HUMLFramework-template (core-functions) first, and then in the generator-template for validators (extended-functions).
The ``framework.hbs`` file is specific to every language (or wraps another variant, like node around js), any new function can simply be inserted into the Framework and tested in the test repository.

 In the Validator-template (``main.hbs``), the ``ASTModel`` (often just called model) provided for code generation is iterated through, and all model elements will be inserted according to the corresponding ``CLASS.hbs`` template in the template folder.
 
 For instance, a``ASTOperandFunction``-element in model will call the template ``astoperandfunction.hbs``, which in turn, looks similar to something like this: ``huml.{{name}}({{#parameters}}{{tmpl}}{{#unless @last}}, {{/unless}}{{/parameters}})``. The template accesses members of the class to generate code. In this example, name is a field filled with the Function, this corresponds to the name in the Framework, like the ``SUMOF()`` function has the name ``SUMOF``.
 
 As this is analog for all classes, only use the modification of the AST structure as a last resort to solving problems. If you want to have a specif instance of something, you could just create a new ``ASTElement``-class, that inherits from an existing class and enhances their functionality.
 Staying with our Function example, you could create a new entry in the ``Functions``-enum, and create a specific ``ReturnTypeResolver`` (this is set in ``ASTOperandFunctionBuilder``, switched by ``name``-member) in oder to create a specific function, rather than creating a new item in the AST that directly extends ``ASTITem`` or ``ÀSTOperandBase``.
 However, you can also extend ``ASTOperandFunction`` with a new class. When all Functions of the base class are intact, the generator will treat it as it's base class unless a specific generation override exists.
 
 We wouldn't recommend creating sub classes for every new feature, as this also increases the amount of builders, which all have to be maintained manually. Try to keep the work done in the Transformers as generic as possible.
 
 You can create Tests for ASTClasses and their integrity analog to ``/openvalidation-common/src/test/java/ast/ASTModelTest.java``, the model builder might need some adjustments, depending on what kind of function you added.
 
 After adding a function, you can add an Alias to your function by adding it to the ``aliases-resource-bundles`` (``C:\git\OpenValidation\openvalidation-core\src\main\resources\aliases``), this bundle contains multiple files, one for each language.
 
 You can test the entire process from rule input to ASTValidation in ``end2ast``-tests (like ``/openvalidation-core/src/test/java/end2ast/lambdas/FirstFunctionsTests.java``).
 
 When you commit your changes, open a pull request on github. This will trigger a pipeline that also contains additional integration tests, that are not part of the maven profile for integration tests.