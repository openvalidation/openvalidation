# Development Setup

### Build Requirements:
jdk 1.8.*  
Maven 3.4 or newer

### Installation

```bash
# clone the repo:
git clone https://github.com/openvalidation/openvalidation/`
# switch to the checkout
cd openvalidation
# compile and test the sources
mvn clean package
```

# Building and Testing
Build the project via Maven

```bash
mvn clean install
```

or

```bash
mvn clean package
```

Building with maven will automatically run all tests with the default profile.
Every change should pass integration tests, you can run them via the integration profile:

```bash
mvn test -Pintegration
```

If you encounter problems running integration tests
```
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M3:test (default-test) on project openvalidation-core: There are test failures.
```
try limiting maven fork count to 0:
```bash
mvn -Pintegration test -DforkCount=0
```
Refer to [this ticket](https://github.com/carlossg/docker-maven/issues/90) for more details.

## Generate and run integrations tests (standalone)

To generate integration tests, use ``GeneratorCLI``, the CLI will require 2 parameters:
- Path to the test resource file folder (default ``OpenValidation\openvalidation-integration-tests/src/test/resources``)
- Path to generate test Classes to (default ``OpenValidation\openvalidation-integration-tests\target``)

After generating the files, these can be run via ``io.openvalidation.integration.tests.IntegrationTestsRunner`` (most IDEs require you to mark ``OpenValidation\openvalidation-integration-tests\target\generated-test-sources\java`` as generated sources folder, so your builder can discover them)

When changing tests, a manual delete of the contents of ``OpenValidation\openvalidation-integration-tests\target\generated-test-sources\java`` might be needed, as only files with a reference in test cases will be overwritten. We recommend setting up your IDE to delete the contents of that folder every time you re-generate the tests. 

## Data types in integration tests
We strongly recommend the usage of decimal number types for numbers in integration tests. Following example schema (`{value:1.0}` and `{value:1}`)
will generate different Models. If no decimal point is specified, an integer will be generated as model data type. These integers are subject to the usual arithmetic operation limitations that the target language provides (i.e. in java, all divisions and datatype conversions will round down any decimal points, so `1.5 + 1.5 equal 2` will be true).

# Coding Guidelines

## Logging
This project uses [`java.util.logging.Logger`](https://docs.oracle.com/javase/7/docs/api/java/util/logging/Logger.html) for displaying runtime information.

### Levels
- `Level.FINE`: Exception Stacktraces, Background Information for Exceptions in Tests
- `Level.SEVERE`: Parameter Informaton that caused exception, Exception Error Message
 
## Code Documentation
The project uses [these guidelines](/docs/javadoc_guidelines.md) for *JavaDoc* comments in source files.

## Formatting

We use the [format-maven-plugin](https://github.com/coveooss/fmt-maven-plugin) during build which makes use of google-java-format to enforce consisten formatting across the codebase.

To trigger autoformat manually run `mvn com.coveo:fmt-maven-plugin:format` in the project root directory.

## Checkstyle

The [Checkstyle Maven Plugin](https://maven.apache.org/plugins/maven-checkstyle-plugin/index.html) can be run via `mvn checkstyle:check`.
It uses a [modified](build-tools/src/main/resources/google_checks.xml) [google java style](https://google.github.io/styleguide/javaguide.html) configuration.

## Code Generation

### Add new functions to HUMLFramework

Please refer to [HUMLFramework Contribution](/docs/HUMLFramework.md).
