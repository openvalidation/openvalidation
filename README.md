<p>&nbsp;<br/></p>
<p align="center"><a href="https://openvalidation.io" target="_blank" rel="noopener noreferrer"><img width="200" src="docs/ci/logo-v2.png" alt="Vue logo"></a><br/><br/></p>
<p align="center">
  <a href="https://dev.azure.com/validaria/openvalidation/_build/latest?definitionId=1&branchName=master"><img src="https://dev.azure.com/validaria/openvalidation/_apis/build/status/openVALIDATION/openVALIDATION%20master?branchName=master" alt="Build Status"></a>
  <a href="https://img.shields.io/azure-devops/tests/validaria/openvalidation/1?compact_message"><img src="https://img.shields.io/azure-devops/tests/validaria/openvalidation/1?compact_message" alt="Azure DevOps tests (compact)"></a> 
  <a href="https://search.maven.org/search?q=g:io.openvalidation"><img src="https://img.shields.io/maven-central/v/io.openvalidation/openvalidation" alt="Maven Central"></a> 
 <br/>
  <a href="https://twitter.com/openVALIDATION"><img src="https://img.shields.io/twitter/follow/openVALIDATION?style=social" alt="Follow us on Twitter"></a> 
 <br/><br/>
 <span>a natural language (no-code) compiler for validation rules</span>
 <br/>
 <a href="https://openvalidation.io" target="_blank">openvalidation.io</a> | <a href="https://playground.openvalidation.io" target="_blank">playground.openvalidation.io</a> | <a href="https://docs.openvalidation.io" target="_blank">docs.openvalidation.io</a>
</p>


<br/><br/><br/><br/><br/><br/><br/><br/>


![first screen](/docs/first-screen.png)

<br/><br/><br/><br/><br/><br/><br/><br/>


## install
<br/><br/>
install via npm as global cli command

```bash
npm i openvalidation -g
```

<br/><br/><br/><br/><br/><br/>
## use
<br/><br/>
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
<br/><br/>
For more details check out our [documentation and guides](https://docs.openvalidation.io)

<br/><br/><br/><br/><br/><br/>
## samples
<br/><br/>
<table cellpadding="25">
  <tr>
    <th>rule</th>
    <th>schema</th>
    <th>description</th>
  </tr>
  <tr>
    <td>
      the <i>name</i> <b>should</b> be <i>Alex</i>
    </td>
    <td>{name:''}</td>
    <td>simple rule. The rule itself is also the error message</td>    
  </tr>
  <tr>
    <td><b>if</b> the <i>name</i> <b>is not</b> <i>Alex</i><br/> <b>then</b> <font style="color:#f00!important">the given name is not Alex</font></td>
    <td>{name:''}</td>
    <td>simple if/then rule. The text after <b>then</b> is the error message</td>    
  </tr>  
  <tr>
    <td>a <i>name</i> <b>must</b> be <i>Alex</i>, <i>Peter</i> or <i>Helmut</i></td>
    <td>{name:''}</td>
    <td>condition with multiple(OR) values</td>    
  </tr>   
  <tr>
    <td><i>Berlin</i> <b>as</b> <i>capital city</i><br/><br/>
      the <i>location</i> <b>has</b> to be a <i>capital city</i></td>
    <td>{location:''}</td>
    <td>domain specific expression as variable</td>    
  </tr>   
  <tr>
    <td>
      the <i>age</i> <b>is smaller</b> than <i>18</i> years <b>as</b> <i>underage</i>
<br/><br/>
      the user <b>must not</b> be <i>underage</i><br/>
      <b>and</b> his <i>name</i> <b>should</b> be <i>Alex</i>
    </td>
    <td>{age:0, name:''}</td>
    <td>preconditions as variable</td>    
  </tr>   
  <tr>
    <td>
      user's <i>age</i> <b>-</b> <i>18</i> years <b>as</b> <i>actual work experience</i>
    </td>
    <td>{age:0}</td>
    <td>arithmetic</td>    
  </tr>
  <tr>
    <td>
      <i>age</i> and <b>smaller as operator</b> <i>younger</i>
      <br/><br/>
      user <b>must not</b> be <i>younger</i> than <i>18</i> years
    </td>
    <td>{age:0}</td>
    <td>semantic, domain specific comparison operator</td>    
  </tr>   
  <tr>
    <td>
      <b>first</b> item <b>from</b> <i>names</i> <b>as</b> <i>Boss</i>
<br/><br/>    
      the <i>Boss</i> <b>should</b> be <i>Alex</i>
    </td>
    <td>{names:['Alex','Peter','Helmut']}</td>
    <td>first item from list</td>    
  </tr>     
  <tr>
    <td>
      <b>first</b> number <b>from</b> <i>numbers</i> <b>with</b> a value <b>bigger</b> than 3 <b>as</b> <i>magic number</i>
<br/><br/>    
      the <i>magic number</i> <b>has</b> to be <i>4</i>
    </td>
    <td>{numbers:[1,2,3,4,5,6,7]}</td>
    <td>filtering the list</td>    
  </tr>  
  
</table>

<br/><br/><br/><br/><br/><br/>
## try
<br/><br/>
try it out directly in the browser on the [playground](https://playground.openvalidation.io/)


<br/><br/><br/><br/><br/><br/>

## contribute
<br/><br/>
Please refer to our [contribution guidelines](CONTRIBUTING.md).

Thank you to all the people and bots who already contributed to openVALIDATION!

<!-- generate new contributor list.. https://contributors-img.firebaseapp.com/ -->
<a href="https://github.com/openvalidation/openvalidation/graphs/contributors"><img src="https://contributors-img.firebaseapp.com/image?repo=openvalidation/openvalidation"/>
</a>

<br/><br/><br/><br/><br/><br/>

## contact
<br/><br/>
You can write an [E-Mail](mailto:validaria@openvalidation.io), mention our twitter account [@openVALIDATION](https://twitter.com/openVALIDATION) or message us at our instagram account [@openvalidation_](https://www.instagram.com/openvalidation_/).

<br/><br/><br/><br/><br/><br/>

## license
<br/><br/>
openVALIDATION is released unter the Apache 2.0 license. See [LICENSE.txt](LICENSE.txt)
