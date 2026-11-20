$ErrorActionPreference = 'Stop'
$base = 'http://127.0.0.1:8095/api/v1/auth'
$result = [ordered]@{ target = 'instância local isolada, porta 8095'; configuredTimeoutSeconds = 60; startedAtUtc = [DateTime]::UtcNow.ToString('o') }
try {
  $body = @{ email = 'admin@sige.demo'; password = $env:SIGE_CT11_PASSWORD } | ConvertTo-Json -Compress
  $login = Invoke-WebRequest "$base/login" -Method Post -Body $body -ContentType 'application/json' -SessionVariable session
  $result.loginStatus = [int]$login.StatusCode
  $result.cookieReceived = @($session.Cookies.GetCookies($base) | Where-Object Name -eq 'JSESSIONID').Count -eq 1
  $result.beforeWaitStatus = [int](Invoke-WebRequest "$base/me" -WebSession $session).StatusCode
  $watch = [System.Diagnostics.Stopwatch]::StartNew()
  Start-Sleep -Seconds 65
  $watch.Stop()
  $result.waitSeconds = [Math]::Round($watch.Elapsed.TotalSeconds, 2)
  $result.cookieStillSent = @($session.Cookies.GetCookies($base) | Where-Object Name -eq 'JSESSIONID').Count -eq 1
  $response = Invoke-WebRequest "$base/me" -WebSession $session -SkipHttpErrorCheck
  $result.afterWaitStatus = [int]$response.StatusCode
  $result.passed = $result.loginStatus -eq 200 -and $result.cookieReceived -and $result.beforeWaitStatus -eq 200 -and $result.waitSeconds -ge 65 -and $result.cookieStillSent -and $result.afterWaitStatus -eq 401
} finally {
  $result.finishedAtUtc = [DateTime]::UtcNow.ToString('o')
  $result | ConvertTo-Json | Set-Content '../docs/qa/execucao-ct11-expiracao-http-20261119.json' -Encoding utf8
}
$result | ConvertTo-Json
if (-not $result.passed) { exit 1 }
