# Integration

In the following, we will examine cases on how to integrate openValidation into your existing solution and Draw comparisions to conventional (often integrated) validation solutions.

## Generate HUMLFramework for rest clients and servers in JavaScript

In this first example, we will generate our Framework and Rules for a web client, and the rules will be executed client side in the browser.

For this, the single file option `-f` in the CLI is recommended, as it will reduce the size of the js file you will have to bundle.

Include the generated file. Now, you can simply add code similar to the following to your post method:

```javascript
var result = HUMLValidator.validate(response.body);

if(result.hasErrors)
    //your error handleing code here, result contains a summary on which rules failed
else
    //no Validation Errors, data is valid
```

This works similar for the server, but instead of checking the response body, you can check the request body, and return a matching return code based on input recieved

### On http status codes

If you handle an error server side, and want send a REST response, we generally encourage you to send codes 400 (Bad Request) or 422 (Unprocessable Entity) when validation fails.
However, a general solution, what to send, is not defined as a global standard. Always check with your implementation and make sure to define, what status codes can be send depending on the input, within your Interface.

Many REST APIs just return a code 200, and have an error in the response body - this can lead to confusing your API consumers, and we generally don't recommend this. It is, however, a very wide spread practice, should you work on an existing API solution.

## General Information on packages

When generating Frameworks in any language, the result is (unless otherwise specified) a combination of 2 files.

The first of those Files is a file named `HUMLFramework` and one file named `OpenValidation`.

The Framework file is a static file added to all generated rulesets, and therefore does only needs to be generated once.

Attention for JavaScript users: The Framework for javascript is exported as a singleton as default. This might be a potential problem when using multiple rule sets serverside.

The second file generated is 
