# Paths of the REST-API

The REST-API can be found under the URI [api.openvalidation.io](http://api.openvalidation.io).
In the following the available will be explained further explained.
A technical documentation can be found under [swagger-ui](http://api.openvalidation.io/swagger-ui.html)

## POST /

This path is used to generate the code.

### Parameter 

Name | Description | Type
------ | ------ | ------
rule   | openVALIDATION-code  |  String
schema   | Schema of the OV-code  |  JSON
culture   | Language in which the code was written  |  String
language   | Programming Language which should be generated  |  String

### Result

Name | Description | Type
------ | ------ | ------
implementationResult   | generated code |  String
frameworkResult   | framework which is required by the generated code  |  String

## POST /aliases

The path expects the specification of the requested language and then returns all known aliases.

### Parameter 

Name | Description | Type
------ | ------ | ------
culture   | Language in which the aliases are requested  |  String

### Result

Name | Description | Type
------ | ------ | ------
aliases   | available aliases |  HashMap

## POST /completion

This path is required for the auto-complete function of the Language Server. 
Only the element at the requested position is sent to the REST API and will be parsed. Only the parsed element is returned.


### Parameter 

Name | Description | Type
------ | ------ | ------
rule   | openVALIDATION-code  |  String
schema   | Schema of the OV-code  |  JSON
culture   | Language in which the code was written  |  String
language   | Programming Language which should be generated  |  String

### Result

Name | Description | Type
------ | ------ | ------
scope   | parsed element of the given OV-code |  GenericNode

## POST /linting

The path described here is used to validate the code and return the parsing result.

### Parameter 

Name | Description | Type
------ | ------ | ------
rule   | openVALIDATION-code  |  String
schema   | Schema of the OV-code  |  JSON
culture   | Language in which the code was written  |  String
language   | Programming Language which should be generated  |  String

### Result

Name | Description | Type
------ | ------ | ------
mainAstNode   | modified AST of the parsed code  |  MainNode
schema   | parsed schema   |  SchemaType
errors   | error messages   |  Error[]
