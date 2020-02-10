<br/><br/>
<p aling="center"><img src="/docs/ci/openvalidation-nlc.png" alt="openVALIDATION a natural language no-code compiler"/></p>

<br/><br/><br/><br/><br/>

<p>&nbsp;<br/></p>
<p align="center">
  <a href="https://dev.azure.com/validaria/openvalidation/_build/latest?definitionId=1&branchName=master"><img src="https://img.shields.io/azure-devops/build/validaria/openvalidation/1/master?style=flat-square&label=Azure%20DevOps%20Build" alt="Build Status"></a>
  <a href="https://img.shields.io/azure-devops/tests/validaria/openvalidation/1?compact_message"><img src="https://img.shields.io/azure-devops/tests/validaria/openvalidation/1?compact_message&style=flat-square" alt="Azure DevOps tests (compact)"></a> 
  <a href="https://search.maven.org/search?q=g:io.openvalidation"><img src="https://img.shields.io/maven-central/v/io.openvalidation/openvalidation?style=flat-square" alt="Maven Central"></a> 
  <a href="https://twitter.com/openVALIDATION"><img src="https://img.shields.io/twitter/follow/openVALIDATION?style=flat-square&logo=twitter" alt="Follow us on Twitter"></a> 
 <br/><br/>
 <a href="https://openvalidation.io" target="_blank">openvalidation.io</a> | <a href="https://playground.openvalidation.io" target="_blank">playground.openvalidation.io</a> | <a href="https://docs.openvalidation.io" target="_blank">docs.openvalidation.io</a>
  <br/><br/>
   <span>a natural language (no-code) compiler for validation rules</span>  
</p>

<br/><br/><br/><br/>



## contents
<br/><br/>

- [introduction](#introduction)
- [install](#install)
  - [npm](#npm)
  - [jar](#jar)
  - [docker image](#docker-image)
- [run](#run)
  - [npm](#npm-cli)
  - [jar](#executable-jar)
  - [docker container](#docker-container)
  - [result](#result)  
  - [cli parameters](#cli-parameters)    
- [integrate](#integrate)
- [samples](#samples)
- [understand](#understand)
- [try](#try)
- [contribute](#contribute)
- [contact](#contact)
- [license](#license)

<br/><br/><br/><br/>

## introduction
<br/><br/>
<a href="https://openvalidation.io" target="_blank" rel="noopener noreferrer"><img width="200" src="docs/ci/logo-v2.png" alt="a natural language compiler for validation rules"></a>

<br/>

openVALIDATION enables programming validation rules using natural language, such as German or English. 

The rules recorded in natural language are readable not only by humans but also by the machines and therefore no longer need to be programmed by a software developer. This task is now taken over by openVALIDATION compiler. With integrated code generators, corresponding source code in the desired programming language can be generated automatically, such as Java, C#, JavaScript or Python, with more to come. This code can then be integrated into any application (services, frontends, middleware). 

Write once, DONT CODE and run everywhere!

<br/><br/><br/><br/>

## install
<br/><br/>
### npm

install via npm as global cli command

```bash
npm i openvalidation -g
```
<br/><br/>
### jar

or just download the executable jar [here](https://downloadarchive.blob.core.windows.net/openvalidation-generator/openvalidation.jar) or via curl:

```
curl 'https://downloadarchive.blob.core.windows.net/openvalidation-generator/openvalidation.jar' --output openvalidation.jar
```

<br/><br/>
### docker image

```
docker pull openvalidation/openvalidation-rest
```

<br/><br/><br/><br/>
## run
<br/><br/>
### npm cli

```bash
openvalidation -r "user's age should not be less than 18 years" -s "{age:0}" -c en -l javascript
```

<br/><br/>
### executable jar

```
java -jar openvalidation.jar -r "user's age should not be less than 18 years" -s "{age : 0}"  -c en -l javascript
```

<br/><br/>

### docker container
```

docker run -p 7070:80  openvalidation/openvalidation-rest

curl -X POST -H "Content-Type: application/json" -d "{\"rule\":\"user's age should not be less than 18 years\",\"culture\":\"en\",\"language\":\"JavaScript\",\"schema\":\"{age:0}\"}" http://localhost:7070/

```

<br/><br/>
### result

The following JavaScript code will be generated:

```javascript


var HUMLValidator = function() {
    var huml = new HUMLFramework();
    
        huml.appendRule("",
            ["age"],
            "user&#x27;s age should not be less than 18 years",
            function(model) { 
                return huml.LESS_THAN(model.age, 18.0);
            },
            false
        );

    this.validate = function(model){
        return huml.validate(model);
    }
}

```

The generated code does not depend on the 3'rd party libraries at all. Therefore, a custom framework is generated in addition to the rules. This framework contains a basic architecture to integrate the generated validation rules more easily into other systems.

<br/><br/>
### cli parameters

<br/>

**-r** (--rule)

validation rule in a natural language

**-s** (--schema)

schema in JSON Schema or JSON Object format

**-c** (--culture)

culture code of the natural language. For example **de** for German or **en** for English. 
The culture of **current system** will be used as Default if the parameter -c was not specified.
**en** is the absolute fallback if specified culture not supported by openVALIDATION.

**-l** (--language)

the programming language of the generation output. 
**Java** is a default language. Available: Java, JavaScript, CSharp, (Python and Rust are still in development)


**-o** (--output)

The Output option defines a directory where the generated code files are stored. Without specifying the output parameter, the generated code is only displayed in the console. If no output directory is specified, the result will only be displayed in the console. 


See more <a href="https://docs.openvalidation.io/openvalidation-cli" target="_blank">CLI Options...</a> at <a href="https://docs.openvalidation.io">docs.openvalidation.io</a>

<br/><br/><br/><br/>
## integrate
<br/><br/>

The first step is to generate a rule in e.g. nodejs. To generate code files, the output directory must be defined with the parameter -o.


```bash
openvalidation -r "user's age should not be less than 18 years" -s "{age:0}" -c en -l node -o ./
```

the compiler generates 2 code files OpenValidation.js (the actual rules) and HUMLFramework.js (the generic framework)

Now you can integrate the generated code into a NodeJS application:


```javascript

var openVALIDATION = require('./OpenValidation.js');

var data = {name:'Alex', age:17};

var validationRESULT = openVALIDATION.validate(data);

if (validationRESULT.hasErrors) {
  console.log(validationRESULT.errors);
} else {
  console.log("this data is valid!");
}


```


Further integration examples can be found [here](https://docs.openvalidation.io/openvalidation-integration).  


<br/><br/><br/><br/>
## samples
<br/><br/>

Here are examples of different validation rules:

<br/>
<table cellpadding="25">
  <tr>
    <th>rule</th>
    <th>schema</th>
    <th>description</th>
  </tr>
  <tr>
    <td>
      <br/><br/>
      the <i>name</i> <b>should</b> be <i>Alex</i>
      <br/><br/>
    </td>
    <td>{name:''}</td>
    <td>simple rule. The rule itself is also the error message</td>    
  </tr>
  <tr>
    <td>
      <br/><br/>
      <b>if</b> the <i>name</i> <b>is not</b> <i>Alex</i><br/> <b>then</b> the given name is not Alex
    <br/><br/>
    </td>
    <td>{name:''}</td>
    <td>simple if/then rule. The text after <b>then</b> is the error message</td>    
  </tr>  
  <tr>
    <td>
      <br/><br/>
      a <i>name</i> <b>must</b> be <i>Alex</i>, <i>Peter</i> or <i>Helmut</i>
    <br/><br/>
    </td>
    <td>{name:''}</td>
    <td>condition with multiple(OR) values</td>    
  </tr>   
  <tr>
    <td>
      <br/><br/>
      <i>Berlin</i> <b>as</b> <i>capital city</i><br/><br/>
      the <i>location</i> <b>has</b> to be a <i>capital city</i>
    <br/><br/>
    </td>
    <td>{location:''}</td>
    <td>domain specific expression as variable</td>    
  </tr>   
  <tr>
    <td>
      <br/><br/>
      the <i>age</i> <b>is smaller</b> than <i>18</i> years <b>as</b> <i>underage</i>
<br/><br/>
      the user <b>must not</b> be <i>underage</i><br/>
      <b>and</b> his <i>name</i> <b>should</b> be <i>Alex</i>
      <br/><br/>
    </td>
    <td>{age:0, name:''}</td>
    <td>preconditions as variable</td>    
  </tr>   
  <tr>
    <td>
      <br/><br/>
      user's <i>age</i> <b>-</b> <i>18</i> years <b>as</b> <i>actual work experience</i>
      <br/><br/>
    </td>
    <td>{age:0}</td>
    <td>arithmetic</td>    
  </tr>
  <tr>
    <td>
      <br/><br/>
      <i>age</i> and <b>smaller as operator</b> <i>younger</i>
      <br/><br/>
      user <b>must not</b> be <i>younger</i> than <i>18</i> years
      <br/><br/>
    </td>
    <td>{age:0}</td>
    <td>semantic, domain specific comparison operator</td>    
  </tr>   
  <tr>
    <td>
      <br/><br/>
      <b>first</b> item <b>from</b> <i>names</i> <b>as</b> <i>Boss</i>
<br/><br/>    
      the <i>Boss</i> <b>should</b> be <i>Alex</i>
      <br/><br/>
    </td>
    <td>{names:['Alex','Peter','Helmut']}</td>
    <td>first item from list</td>    
  </tr>     
  <tr>
    <td>
      <br/><br/>
      <b>first</b> number <b>from</b> <i>numbers</i> <b>with</b> a value <b>bigger</b> than 3 <b>as</b> <i>magic number</i>
<br/><br/>    
      the <i>magic number</i> <b>has</b> to be <i>4</i>
      <br/><br/>
    </td>
    <td>{numbers:[1,2,3,4,5,6,7]}</td>
    <td>filtering the list</td>    
  </tr>  
  
</table>

<br/><br/><br/><br/>
## understand
<br/><br/>
openVALIDATION enables programming of validation rules using natural language, such as German or English and many more.
The rules recorded in natural language are readable not only by humans but also by the computer and therefore no longer need to be programmed by a software developer.

<br/><br/>

**The grammar**

The Grammar of openVALIDATION based on a natural language is both formal and natural. This distinguishes this grammar from other programming languages or DSL's. It allows the use of additional semantic or grammatical content. The additional content is only relevant for human readability. The machine, on the other hand, ignores this addition. Thus it is possible to express the rules in a grammatically correct way on the one hand and to give them a semantic context on the other. This all makes the rules easier to understand. Rules formulated with openVALIDATION are thus at the same time a formal, machine-processable specification, but also a documentation that is easy for humans to understand.

<p align="center">
<img src="https://blobscdn.gitbook.com/v0/b/gitbook-28427.appspot.com/o/assets%2F-LZu2slXWxv3PqRBZ9rs%2F-L_6dad7BzkwzHIkvLuu%2F-L_6n6-bhPwuuuHonLpy%2Fgrammar2.png?alt=media&token=b5588e1b-19df-4330-9575-43e33f39a0cd" width="80%"/>
</p>

<br/><br/>
For more details check out our [documentation and guides](https://docs.openvalidation.io)

<br/><br/><br/><br/>
## try
<br/><br/>
try it out directly in the browser on the [playground](https://playground.openvalidation.io/)


<br/><br/><br/><br/>

## contribute
<br/><br/>

We still have 2 zeros in the version 0.0.X of openVALIDATION, so there is still a lot to do. If you want to join us, you are more than welcome to participate! 

Check our [contribution guide](https://docs.openvalidation.io/contribution/contribution-guide).

Even if you're not a developer or don't fully understand the technical part of openVALIDATION yet, it doesn't matter. There are many different ways to join the project. 

Check our [contribution guide specially for beginners](https://docs.openvalidation.io/contribution/for-beginners)



<br/><br/>
Thank you to all the people and bots who already contributed to openVALIDATION!

<!-- generate new contributor list.. https://contributors-img.firebaseapp.com/ -->
<a href="https://github.com/openvalidation/openvalidation/graphs/contributors"><img src="https://contributors-img.firebaseapp.com/image?repo=openvalidation/openvalidation"/>
</a>

<br/><br/><br/><br/>

## contact
<br/><br/>
You can write an [E-Mail](mailto:validaria@openvalidation.io), mention our twitter account [@openVALIDATION](https://twitter.com/openVALIDATION) or message us at our instagram account [@openvalidation_](https://www.instagram.com/openvalidation_/).

<br/><br/><br/><br/>

## license
<br/><br/>
openVALIDATION was initially developed as part of a research project at [BROCKHAUS AG](http://brockhaus-ag.de) in Dortmund.

Only an Open Source solution has the ability to unfold its true potential. That's why we released it on GitHub as an open source project under the Apache 2.0 license.

See [LICENSE.txt](LICENSE.txt)
