[xml]$pomXml = Get-Content (Join-Path (Split-Path -Path $MyInvocation.MyCommand.Definition -Parent) "../pom.xml" )
$revision = $pomXml.project.properties.revision
return $revision
