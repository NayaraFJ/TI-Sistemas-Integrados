$ErrorActionPreference = 'Stop'
$schema = 'sige_ct11_expiracao_20261119'
$mysql = 'C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe'
$env:MYSQL_PWD = $env:SIGE_CT11_DB_PASSWORD
$result = [ordered]@{ target = 'API local isolada 8095'; schema = $schema; startedAtUtc = [DateTime]::UtcNow.ToString('o') }
try {
  $database = & $mysql --user=root --host=127.0.0.1 --batch --skip-column-names $schema '--execute=SELECT DATABASE()'
  if ($LASTEXITCODE -ne 0 -or $database -ne $schema) { throw 'Schema isolado não confirmado' }
  $stored = @(& $mysql --user=root --host=127.0.0.1 --batch --skip-column-names $schema "--execute=SELECT password_hash FROM users WHERE email='admin@sige.demo'")
  if ($LASTEXITCODE -ne 0 -or $stored.Count -ne 1) { throw 'Conta sintética não encontrada ou ambígua' }
  $result.hashHasBcryptFormat = $stored[0] -match '^\$2[aby]\$\d{2}\$[./A-Za-z0-9]{53}$'
  $result.hashDiffersFromPassword = $stored[0] -cne $env:SIGE_CT11_PASSWORD
  $body = @{ email = 'admin@sige.demo'; password = $env:SIGE_CT11_PASSWORD } | ConvertTo-Json -Compress
  $login = Invoke-WebRequest 'http://127.0.0.1:8095/api/v1/auth/login' -Method Post -ContentType 'application/json' -Body $body -SessionVariable session
  $me = Invoke-WebRequest 'http://127.0.0.1:8095/api/v1/auth/me' -WebSession $session
  $users = Invoke-WebRequest 'http://127.0.0.1:8095/api/v1/users' -WebSession $session
  $result.httpStatuses = @([int]$login.StatusCode, [int]$me.StatusCode, [int]$users.StatusCode)
  $result.responsesOmitPasswordAndStoredHash = @(@($login.Content, $me.Content, $users.Content) | Where-Object { $_.Contains($env:SIGE_CT11_PASSWORD) -or $_.Contains($stored[0]) -or $_ -match '(?i)"(?:password|passwordHash|password_hash)"\s*:' }).Count -eq 0
  $result.passed = $result.hashHasBcryptFormat -and $result.hashDiffersFromPassword -and $result.responsesOmitPasswordAndStoredHash -and (($result.httpStatuses | Where-Object { $_ -ne 200 }).Count -eq 0)
} finally {
  $result.finishedAtUtc = [DateTime]::UtcNow.ToString('o')
  $result | ConvertTo-Json -Depth 4 | Set-Content '../docs/qa/execucao-ct11-senha-20261119.json' -Encoding utf8
  Remove-Item Env:MYSQL_PWD -ErrorAction SilentlyContinue
}
$result | ConvertTo-Json -Depth 4
if (-not $result.passed) { exit 1 }
