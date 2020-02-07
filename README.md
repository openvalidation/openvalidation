<p>&nbsp;<br/></p>
<p align="center"><a href="https://openvalidation.io" target="_blank" rel="noopener noreferrer"><img width="200" src="docs/ci/logo-v2.png" alt="Vue logo"></a></p>
<p align="center">
  <a href="https://dev.azure.com/validaria/openvalidation/_build/latest?definitionId=1&branchName=master"><img src="https://dev.azure.com/validaria/openvalidation/_apis/build/status/openVALIDATION/openVALIDATION%20master?branchName=master" alt="Build Status"></a>
  <a href="https://img.shields.io/azure-devops/tests/validaria/openvalidation/1?compact_message"><img src="https://img.shields.io/azure-devops/tests/validaria/openvalidation/1?compact_message" alt="Azure DevOps tests (compact)"></a> 
  <a href="https://search.maven.org/search?q=g:io.openvalidation"><img src="https://img.shields.io/maven-central/v/io.openvalidation/openvalidation" alt="Maven Central"></a> 
 <br/>
  <a href="https://twitter.com/openVALIDATION"><img src="https://img.shields.io/twitter/follow/openVALIDATION?style=social" alt="Follow us on Twitter"></a> 
 <br/><br/>
 <span>a natural language (no-code) compiler for validation rules</span>
 <br/>
 <a href="https://openvalidation.io" target="_blank">openvalidation.io</a>
</p>


<br/><br/><br/><br/><br/><br/><br/><br/>


![first screen](/docs/first-screen.png)

<br/><br/><br/><br/><br/><br/><br/><br/>


## install

install via npm as global cli command

```bash
npm i openvalidation -g
```

<br/><br/><br/><br/><br/><br/>
## use

after npm install:

```bash
openvalidation -r "users age should not be less than 18 years" -s "{age:0}" -c en
```

<br/><br/>

**-r** (--rule)

validation rule in a natural language

**-s** (--schema)

schema in JSON Schema or JSON Object format

**-c** (--culture)

culture code of the natural language. For example **de** for German or **en** for English

**-l** (--language)

the programming language of the generation output. 
**JavaScript** is a default language. Available: Java, JavaScript, CSharp, (Python and Rust are still in development)

more <a href="https://docs.openvalidation.io/openvalidation-cli" target="_blank">CLI Options...</a>


<br/><br/><br/><br/><br/><br/>
## understand

For more details check out our [documentation and guides](https://docs.openvalidation.io)

<br/><br/><br/><br/><br/><br/>
## samples

<table>
  <tr>
    <th>rule</th>
    <th>schema</th>
    <th>description</th>
  </tr>
  <tr>
    <td>the name <b>should</b> be Alex</td>
    <td>{name:''}</td>
    <td>simple rule. The rule itself is also the error message</td>    
  </tr>
  <tr>
    <td><b>if</b> the name <b>is not</b> Alex<br/> <b>then</b> the given name is not Alex</td>
    <td>{name:''}</td>
    <td>simple if/then rule. The text after <b>then</b> is the error message</td>    
  </tr>  
  <tr>
    <td>a name <b>must</b> be Alex, Peter or Helmut</td>
    <td>{name:''}</td>
    <td>condition with multiple(OR) values</td>    
  </tr>   
  <tr>
    <td>Berlin <b>as</b> capital city<br/><br/>
      the location <b>has</b> to be a capital city</td>
    <td>{location:''}</td>
    <td>domain specific expression as variable</td>    
  </tr>   
  <tr>
    <td>
      the age <b>ist smaller</b> than 18 years <b>as</b> underage
<br/><br/>
      the user <b>must not</b> be underage<br/>
      <b>and</b> his name <b>should</b> be Alex
    </td>
    <td>{age:0}</td>
    <td>preconditions as variable</td>    
  </tr>   
  <tr>
    <td>
    user's age - 18 years AS actual work experience
    </td>
    <td>{age:0}</td>
    <td>arithmetic</td>    
  </tr>
  <tr>
    <td>
      <b>age</b> and <b>smaller as</b>  operator younger
 
user <b>must not</b> be younger than 18 years
    </td>
    <td>{age:0}</td>
    <td>semantic/domain specific comparison operator</td>    
  </tr>  
  
  

  
</table>

<br/><br/><br/><br/><br/><br/>
## try

try it out directly in the browser on the [playground](https://playground.openvalidation.io/)


<br/><br/><br/><br/><br/><br/>

## contribute

Please refer to our [contribution guidelines](CONTRIBUTING.md).

Thank you to all the people and bots who already contributed to openVALIDATION!

<!-- generate new contributor list.. https://contributors-img.firebaseapp.com/ -->
<a href="https://github.com/openvalidation/openvalidation/graphs/contributors"><img src="https://contributors-img.firebaseapp.com/image?repo=openvalidation/openvalidation"/>
</a>

<br/><br/><br/><br/><br/><br/>

## contact

You can write an [E-Mail](mailto:validaria@openvalidation.io), mention our twitter account [@openVALIDATION](https://twitter.com/openVALIDATION) or message us at our instagram account [@openvalidation_](https://www.instagram.com/openvalidation_/).
