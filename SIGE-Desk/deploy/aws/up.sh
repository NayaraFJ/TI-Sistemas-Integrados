#!/usr/bin/env bash
set -euo pipefail
script_dir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
project_dir="$(cd -- "$script_dir/../.." && pwd)"
cd -- "$project_dir"
if [[ ! -f "$script_dir/.env" ]]; then
    echo 'Execute init-env.sh com o domínio antes de iniciar os serviços.' >&2
    exit 1
fi
if grep -q 'SUBSTITUIR_ANTES_DE_INICIAR' "$script_dir/.env"; then
    echo 'Substitua as senhas de exemplo antes de iniciar os serviços.' >&2
    exit 1
fi
DOCKER_BUILDKIT=0 docker build -t sige-desk-api:local -f "$script_dir/Dockerfile.api" .
docker compose --env-file "$script_dir/.env" -f "$script_dir/compose.yaml" up -d --no-build --wait --wait-timeout 300
