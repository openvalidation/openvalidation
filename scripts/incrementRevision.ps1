$scriptPath =  Split-Path -Path $MyInvocation.MyCommand.Definition -Parent
$revision = invoke-expression -Command (Join-Path $scriptPath "./getRevision.ps1")
$version = $revision -replace "-SNAPSHOT", ""
$breaking, $major, $minor = $version.Split(".")
$new_version = "$($breaking).$($major).$([string]([int]$minor + 1))"
if (invoke-expression -Command (Join-Path $scriptPath "./isSnapshot.ps1")) {
    $new_revision = "$($new_version)-SNAPSHOT"
} else {
    $new_revision = "$($new_version)"
}
invoke-expression -Command "$(Join-Path $scriptPath "./setRevision.ps1") $new_revision"
