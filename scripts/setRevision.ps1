$scriptPath =  Split-Path -Path $MyInvocation.MyCommand.Definition -Parent
$pomPath = Join-Path $scriptPath "../pom.xml"
$pomXml = New-Object xml
$pomXml.PreserveWhitespace = $true
$pomXml.Load($pomPath)
$pomXml.project.properties.revision = $args[0]
$pomXml.Save($pomPath)
