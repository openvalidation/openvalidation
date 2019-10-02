$scriptPath =  Split-Path -Path $MyInvocation.MyCommand.Definition -Parent
$revision = invoke-expression -Command (Join-Path $scriptPath "./getRevision.ps1")
if (invoke-expression -Command (Join-Path $scriptPath "./isSnapshot.ps1")) {
    invoke-expression -Command "$(Join-Path $scriptPath "./setRevision.ps1") $($revision -replace '-SNAPSHOT', '')"
}
