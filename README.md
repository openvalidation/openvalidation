OpenVALIDATION enables you to generate validation rules from natural language-like expressions in English and German without any knowledge in programming.
The validation rules can be automatically translated by openVALIDATION into Java, JavaScript or C#, with more languages to come!

![first screen](/docs/first-screen.png)

# Getting Started

This readme provides a brief overview. For detailed documentation and guides go [here](https://docs.openvalidation.io), to try it out directly in the browser go [here](http://playground.openvalidation.io/#/).

### Download & Installation
Download the openVALIDATION CLI [here](https://repo1.maven.org/maven2/io/openvalidation/openvalidation-cli/0.0.1/openvalidation-cli-0.0.1.jar) (requires the Java SE 8 runtime environment).

The openVALIDATION Java API is also available via the Maven Central Repository. Add this to your `pom.xml`:
```xml
<project>
    [...]
    <dependencies>
        [...]
        <dependency>
            <groupId>io.openvalidation</groupId>
            <artifactId>openvalidation-core</artifactId>
            <version>0.0.1</version>
        </dependency>
        <dependency>
            <groupId>io.openvalidation</groupId>
            <artifactId>openvalidation-common</artifactId>
            <version>0.0.1</version>
        </dependency>
        [...]
    </dependencies>
    [...]
</project>
```
For now openVALIDATION is developed and tested for `jdk 1.8.*` only.

### Using the CLI
We will use the rule `your age HAS to be greater than 22` to validate the data `{ age: 21 }`. The rule will be translated into code that validates the data according to our defined rule.
```bash
java -jar openvalidation-cli/target/openvalidation.jar --culture en --language javascript --rule "your age HAS to be greater than 22" --schema "{ age: 21 }" --output age_check.js
```
If you check `age_check.js` you will see the generated code:
```js
var HUMLValidator = function() {
    var huml = new HUMLFramework();
            huml.appendRule("",
                   ["age"],
                   "your age HAS to be greater than 22",
                   function(model) { return huml.LESS_OR_EQUALS(model.age, 22.0); },
                   false
                );
    this.validate = function(model){
        return huml.validate(model);
    }
}
```
As this is a validation rule, we will throw an error `your age HAS to be greater than 22` if the criteria specified in the rule is not met.

Besides raw strings the `--rule` flag also accepts paths to files containing the rules.

The `--schema` flag also accepts paths to files containing the data (like YAML or JSON) or URLs to websites containing the raw data.

# Features

The construction of an openVALIDATION ruleset is fairly simple. Each ruleset makes use of 3 basic elements: validation rules, variables and alternatively comments. This set of elements allows you to construct complex rulesets you can use to customise the validation of data as you like. To get a better feeling for how these elements interact we listed a few examples below.

## Rules

Rules are the heart of every ruleset and consist of two parts. An error message that is displayed if the data does not conform to the rule and a condition which contains the validation logic that will validate the data and thus triggers the error message in case the validation fails. There are two ways to define a rule. For the first, let\'s take a look at a simple example.

The data to be validated is:
```json
{
    "weather": "cloudy"
}
```
And our rule is:
```
If    the weather if rainy
then  Don't forget your umbrella!
```

As you can see the first kind of rule starts with an `If` followed by its condition containing the validation logic `the weather is rainy` and is concluded by the keyword `then` followed by the error message, in our case `Don't forget your umbrella!`. Notice that the `is` acts as an operator and is only one of many ways to express equality in the openVALIDATION language. The error is triggered if the condition is true. This rule would roughly translate to:
```java
if(model.getWeather() == "rainy")
{
    throw new Exception("Don't forget your umbrella!");
}
```

There's also a second way to express rules in openVALIDATION. Let's have another look at an example.
```
The weather must be rainy.
```
This kind of rule is special in the way that it contains the keyword `must` and lacks an error message. However, the whole rule itself can be seen as the error message. In fact an explicit error message initiated by a `then` is not allowed. The `must` operator implies an equality operator between `weather` and `sunny`. Another very important trait of this sort of rule is the way its logic is translated into code compared to the previous kind of rule.
```java
if(model.getWeather() != "rainy")
{
    throw new Exception("The weather must be rainy.");
}
``` 
You can see that a rule formulated in this way behaves a bit differently than an `if-then-rule`. The most important observation is, that the implied equality is negated when translated.

Furthermore it is possible to construct complex condition groups (by using `and` and `or`) and build conditions from arithmetic expressions, lambdas, variable references and other property accessors (like `weather`).

## Variables

Variables can be used to hold simple values or store complex expressions. Each variable ends on the keyword `as` followed by its name. An example.
```
today's weather is sunny as sunny_day
```
The condition `weather == sunny` is now known under the name `sunny_day` and can be used in multiple other instances, e.g. in a rule:
```
If    today is a sunny_day 
then  Sunscreen is important!
```

# Code generation parameters
Following ``-p``-Parameters are considered for code generation:

- model_type: Name of the Model class used in validation Rules
- model_type_namespace: Namespace or package of model_type class. In Java, this must include the Name of model_type.
- generated_class_namespace: defines the namespace or package of the generated Validator and Framework. if this is not set, a namespace or package called ``Validation`` will be created.

Set name and value with ``name=value`` and concatinate multiple options with ``;``, 
like: 
```cmd
-p "model_type=Model;generated_class_namespace=OpenValidationFramework_CSharp;model_type_namespace=OpenValidationFramework_CSharp.Data"
```

## Getting involved

Please refer to our [contribution guidelines](CONTRIBUTING.md).
