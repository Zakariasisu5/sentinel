$ErrorActionPreference = 'Stop'

Write-Host 'Preparing Sentinel local development environment...'

$javaVersion = (java -version 2>&1)[0]
$mavenVersion = (mvn -version 2>&1)[0]

Write-Host "Java: $javaVersion"
Write-Host "Maven: $mavenVersion"
Write-Host 'Environment check complete.'
