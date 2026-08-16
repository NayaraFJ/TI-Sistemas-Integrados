param(
  [string]$SourceRoot = (Join-Path $PSScriptRoot '..\\..\\Trabalho'),
  [string]$OutputFile = (Join-Path $PSScriptRoot '..\\conteudo.tex')
)

$ErrorActionPreference = 'Stop'

function Escape-LaTeX([string]$text) {
  $result = $text
  $result = $result.Replace('\', '\textbackslash{}')
  $result = $result.Replace('&', '\&').Replace('%', '\%').Replace('$', '\$')
  $result = $result.Replace('#', '\#').Replace('_', '\_').Replace('{', '\{').Replace('}', '\}')
  $result = $result.Replace('~', '\textasciitilde{}').Replace('^', '\textasciicircum{}')
  return $result
}

function Format-Inline([string]$text) {
  $value = Escape-LaTeX $text
  $value = [regex]::Replace($value, '\*\*(.+?)\*\*', '\textbf{$1}')
  $value = [regex]::Replace($value, '(?<!\*)\*(?!\*)(.+?)(?<!\*)\*(?!\*)', '\textit{$1}')
  $value = [regex]::Replace($value, '\[([^\]]+)\]\((https?://[^\s)]+)\)', '\href{$2}{$1}')
  return $value
}

function Format-Heading([string]$text) {
  $withoutSourceNumber = $text -replace '^\d+(?:\.\d+)*\.?\s*', ''
  return Format-Inline $withoutSourceNumber
}

function Get-Columns([int]$count) {
  if ($count -le 1) { return '|p{0.92\textwidth}|' }
  $width = [math]::Floor((88 / $count))
  return ('|' + (1..$count | ForEach-Object { "p{0.$width\textwidth}|" }) -join '')
}

$documents = @(
  @{ Path = 'Etapa 01\\Etapa 01.md'; Label = 'ETAPA 01 --- TEMA E INTRODUÇÃO' },
  @{ Path = 'Etapa 02\\Artigo - Etapa 02.md'; Label = 'ETAPA 02 --- PROBLEMA E OBJETIVOS' },
  @{ Path = 'Etapa 03\\Artigo - Etapa 03.md'; Label = 'ETAPA 03 --- REFERENCIAL TEÓRICO' },
  @{ Path = 'Etapa 04\\Artigo - Etapa 04.md'; Label = 'ETAPA 04 --- METODOLOGIA' },
  @{ Path = 'Etapa 02\\BPMN - Processo To Be.md'; Label = 'APÊNDICE A --- PROCESSO TO BE' },
  @{ Path = 'Etapa 02\\Protótipo - Etapa 02.md'; Label = 'APÊNDICE B --- ESBOÇO DO PROTÓTIPO' },
  @{ Path = 'Etapa 03\\Requisitos - Aplicativo.md'; Label = 'APÊNDICE C --- REQUISITOS PRELIMINARES' }
)

$out = [System.Collections.Generic.List[string]]::new()
$out.Add('% Gerado por scripts/sync-trabalho.ps1; não editar manualmente.')
$out.Add('\setcounter{secnumdepth}{3}')

foreach ($document in $documents) {
  $file = Join-Path $SourceRoot $document.Path
  if (-not (Test-Path -LiteralPath $file)) { throw "Fonte ausente: $file" }
  $out.Add("\section{$($document.Label)}")
  $out.Add("\label{sec:$([regex]::Replace($document.Label.ToLowerInvariant(), '[^a-z0-9]+', '-').Trim('-'))}")
  $lines = Get-Content -LiteralPath $file
  $inCode = $false
  $inList = $false
  $inQuote = $false
  $index = 0

  while ($index -lt $lines.Count) {
    $line = $lines[$index].Trim()
    if ($line -match '^```') {
      if ($inList) { $out.Add('\end{itemize}'); $inList = $false }
      if ($inQuote) { $out.Add('\end{quote}'); $inQuote = $false }
      if ($inCode) { $out.Add('\end{verbatim}') } else { $out.Add('\begin{verbatim}') }
      $inCode = -not $inCode; $index++; continue
    }
    if ($inCode) { $out.Add($lines[$index]); $index++; continue }
    if ([string]::IsNullOrWhiteSpace($line)) {
      if ($inList) { $out.Add('\end{itemize}'); $inList = $false }
      if ($inQuote) { $out.Add('\end{quote}'); $inQuote = $false }
      $index++; continue
    }
    if ($line -match '^#\s+') { $index++; continue }
    if ($line -match '^##\s+') {
      if ($inList) { $out.Add('\end{itemize}'); $inList = $false }
      $out.Add("\subsection{$(Format-Heading ($line -replace '^##\s+', ''))}"); $index++; continue
    }
    if ($line -match '^###\s+') {
      if ($inList) { $out.Add('\end{itemize}'); $inList = $false }
      $out.Add("\subsubsection{$(Format-Heading ($line -replace '^###\s+', ''))}"); $index++; continue
    }
    if ($line -match '^####\s+') {
      if ($inList) { $out.Add('\end{itemize}'); $inList = $false }
      $out.Add("\paragraph{$(Format-Heading ($line -replace '^####\s+', ''))}"); $index++; continue
    }
    if ($line -match '^\|') {
      if ($inList) { $out.Add('\end{itemize}'); $inList = $false }
      $table = [System.Collections.Generic.List[string]]::new()
      while ($index -lt $lines.Count -and $lines[$index].Trim() -match '^\|') { $table.Add($lines[$index].Trim()); $index++ }
      $rows = $table | Where-Object { $_ -notmatch '^\|\s*[-: ]+(\|\s*[-: ]+)+\|?$' }
      if ($rows.Count -gt 0) {
        $firstCells = ($rows[0].Trim('|') -split '\|').Count
        $out.Add("\begin{longtable}{$(Get-Columns $firstCells)}")
        $out.Add('\hline')
        for ($rowIndex = 0; $rowIndex -lt $rows.Count; $rowIndex++) {
          $cells = @($rows[$rowIndex].Trim('|') -split '\|' | ForEach-Object { (Format-Inline $_.Trim()) })
          $out.Add(($cells -join ' & ') + ' \\ \hline')
        }
        $out.Add('\end{longtable}')
      }
      continue
    }
    if ($line -match '^>\s?(.*)$') {
      if (-not $inQuote) { $out.Add('\begin{quote}\small'); $inQuote = $true }
      $out.Add((Format-Inline $Matches[1])); $index++; continue
    }
    if ($line -match '^[-*]\s+(.*)$') {
      if (-not $inList) { $out.Add('\begin{itemize}[leftmargin=1.2cm]'); $inList = $true }
      $item = $Matches[1] -replace '^\[[ xX]\]\s*', ''
      $out.Add("\item $(Format-Inline $item)"); $index++; continue
    }
    if ($inList) { $out.Add('\end{itemize}'); $inList = $false }
    if ($inQuote) { $out.Add('\end{quote}'); $inQuote = $false }
    $out.Add((Format-Inline $line))
    $out.Add('')
    $index++
  }
  if ($inCode) { $out.Add('\end{verbatim}') }
  if ($inList) { $out.Add('\end{itemize}') }
  if ($inQuote) { $out.Add('\end{quote}') }
  if ($document.Path -eq 'Etapa 02\\BPMN - Processo To Be.md') {
    $out.Add('\begin{figure}[H]')
    $out.Add('\centering')
    $out.Add('\includegraphics[width=\textwidth]{figuras/bpmn-processo-to-be.png}')
    $out.Add('\caption{Processo To Be para gestão de demandas de tráfego pago}')
    $out.Add('\label{fig:bpmn-processo-to-be}')
    $out.Add('\end{figure}')
  }
  $out.Add('\clearpage')
}

$out | Set-Content -LiteralPath $OutputFile -Encoding utf8
Write-Host "Conteúdo sincronizado em $OutputFile"
