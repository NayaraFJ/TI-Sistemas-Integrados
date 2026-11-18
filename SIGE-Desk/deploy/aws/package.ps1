[CmdletBinding()]
param([switch]$UseExistingBuild)

$ErrorActionPreference = 'Stop'
$projectRoot = (Resolve-Path -LiteralPath (Join-Path $PSScriptRoot '../..')).Path

function Invoke-Checked {
    param([string]$Executable, [string[]]$Arguments)
    & $Executable @Arguments
    if ($LASTEXITCODE -ne 0) {
        throw "Falha em $Executable (código $LASTEXITCODE)."
    }
}

if (-not $UseExistingBuild) {
    Push-Location (Join-Path $projectRoot 'Frontend')
    try {
        Invoke-Checked -Executable 'npx.cmd' -Arguments @('--yes', 'pnpm@10.24.0', 'install', '--frozen-lockfile')
        Invoke-Checked -Executable 'node' -Arguments @('node_modules/typescript/bin/tsc', '-b')
        Invoke-Checked -Executable 'node' -Arguments @('node_modules/vite/bin/vite.js', 'build')
    } finally {
        Pop-Location
    }
    Push-Location (Join-Path $projectRoot 'Backend')
    try {
        Invoke-Checked -Executable '.\mvnw.cmd' -Arguments @('-B', '-ntp', 'verify')
    } finally {
        Pop-Location
    }
}

$jarRelative = 'Backend/target/sige-desk-api-0.1.0-SNAPSHOT.jar'
$jarPath = Join-Path $projectRoot $jarRelative
$frontendDist = Join-Path $projectRoot 'Frontend/dist'
if (-not (Test-Path -LiteralPath $jarPath) -or -not (Test-Path -LiteralPath (Join-Path $frontendDist 'index.html'))) {
    throw 'JAR ou frontend ausente. Execute o script sem -UseExistingBuild.'
}
$jarArchive = [IO.Compression.ZipFile]::OpenRead($jarPath)
try {
    if ($null -eq $jarArchive.GetEntry('BOOT-INF/classes/application-production.yml')) {
        throw 'O JAR não contém o perfil production. Reconstrua o backend antes de publicar.'
    }
} finally {
    $jarArchive.Dispose()
}

$releaseId = (Get-Date -Format 'yyyyMMdd-HHmmss') + '-' + [guid]::NewGuid().ToString('N').Substring(0, 8)
$artifactsRoot = Join-Path $PSScriptRoot 'artifacts'
$stageRoot = Join-Path $artifactsRoot $releaseId
$null = New-Item -ItemType Directory -Path (Join-Path $stageRoot 'Backend/target') -Force
$null = New-Item -ItemType Directory -Path (Join-Path $stageRoot 'Frontend') -Force
$null = New-Item -ItemType Directory -Path (Join-Path $stageRoot 'deploy/aws') -Force
Copy-Item -LiteralPath $jarPath -Destination (Join-Path $stageRoot $jarRelative)
Copy-Item -LiteralPath $frontendDist -Destination (Join-Path $stageRoot 'Frontend/dist') -Recurse
Copy-Item -LiteralPath (Join-Path $projectRoot '.dockerignore') -Destination $stageRoot
foreach ($deployFile in @('Dockerfile.api', 'compose.yaml', 'Caddyfile', '.env.example', 'init-env.sh', 'up.sh')) {
    Copy-Item -LiteralPath (Join-Path $PSScriptRoot $deployFile) -Destination (Join-Path $stageRoot 'deploy/aws')
}
$manifest = [ordered]@{
    releaseId = $releaseId
    builtAt = (Get-Date).ToUniversalTime().ToString('o')
    backendSha256 = (Get-FileHash -LiteralPath $jarPath -Algorithm SHA256).Hash.ToLowerInvariant()
}
$manifest | ConvertTo-Json | Set-Content -LiteralPath (Join-Path $stageRoot 'release.json') -Encoding utf8
$archivePath = Join-Path $artifactsRoot "sige-desk-$releaseId.tar.gz"
Invoke-Checked -Executable 'tar' -Arguments @('-czf', $archivePath, '-C', $stageRoot, '.')
[PSCustomObject]@{
    Archive = $archivePath
    Bytes = (Get-Item -LiteralPath $archivePath).Length
    Sha256 = (Get-FileHash -LiteralPath $archivePath -Algorithm SHA256).Hash.ToLowerInvariant()
    IncludesSecrets = $false
} | ConvertTo-Json -Compress
