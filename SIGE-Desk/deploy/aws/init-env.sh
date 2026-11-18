#!/usr/bin/env bash
set -euo pipefail
umask 077

script_dir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
domain="${1:-}"
if [[ ! "$domain" =~ ^([A-Za-z0-9][A-Za-z0-9-]*\.)+[A-Za-z]{2,}$ ]]; then
    echo 'Informe somente o hostname HTTPS: bash deploy/aws/init-env.sh sige-desk.exemplo.com' >&2
    exit 1
fi
env_path="$script_dir/.env"
if [[ -e "$env_path" ]]; then
    echo "Configuração já existente em $env_path; nenhuma senha foi alterada."
    exit 0
fi
app_password="$(openssl rand -hex 18)"
{
    printf 'SIGE_DOMAIN=%s\n' "$domain"
    printf 'SIGE_SPRING_PROFILES=production,demo\n'
    printf 'SIGE_DB_PASSWORD=%s\n' "$(openssl rand -hex 24)"
    printf 'SIGE_DB_ROOT_PASSWORD=%s\n' "$(openssl rand -hex 24)"
    printf 'SIGE_ADMIN_PASSWORD=%s\n' "$app_password"
    printf 'SIGE_DEMO_PASSWORD=%s\n' "$app_password"
} > "$env_path"
chmod 600 "$env_path"
echo "Configuração criada em $env_path. As senhas não são exibidas nos logs."
