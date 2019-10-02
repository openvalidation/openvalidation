$scriptPath =  Split-Path -Path $MyInvocation.MyCommand.Definition -Parent
$revision = invoke-expression -Command (Join-Path $scriptPath "./getRevision.ps1")
return $revision -Match "-SNAPSHOT"
