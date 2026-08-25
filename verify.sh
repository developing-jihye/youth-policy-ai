#!/usr/bin/env bash

set -Eeuo pipefail

current_step="initialization"

handle_error() {
  local exit_code=$?
  printf 'Verification failed during: %s\n' "$current_step" >&2
  exit "$exit_code"
}

trap handle_error ERR

script_dir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
cd "$script_dir"

current_step="checking Docker CLI"
if ! command -v docker >/dev/null 2>&1; then
  printf 'Verification failed: docker command was not found.\n' >&2
  exit 1
fi

current_step="checking Docker Engine"
docker info >/dev/null

current_step="checking Docker Compose"
docker compose version >/dev/null

current_step="checking .env"
if [[ ! -f .env ]]; then
  printf 'Verification failed: .env file was not found.\n' >&2
  exit 1
fi

current_step="validating Docker Compose configuration"
docker compose config --quiet

current_step="checking PostgreSQL container"
postgres_container_id="$(docker compose ps --all -q postgres)"
postgres_running="false"

if [[ -n "$postgres_container_id" ]]; then
  postgres_running="$(docker inspect --format '{{.State.Running}}' "$postgres_container_id")"
fi

if [[ -z "$postgres_container_id" || "$postgres_running" != "true" ]]; then
  current_step="starting PostgreSQL"
  docker compose up -d postgres
  postgres_container_id="$(docker compose ps -q postgres)"
fi

if [[ -z "$postgres_container_id" ]]; then
  printf 'Verification failed: PostgreSQL container was not found after startup.\n' >&2
  exit 1
fi

current_step="waiting for PostgreSQL healthcheck"
postgres_healthy="false"

for _ in $(seq 1 30); do
  health_status="$(docker inspect --format '{{if .State.Health}}{{.State.Health.Status}}{{else}}none{{end}}' "$postgres_container_id")"

  case "$health_status" in
    healthy)
      postgres_healthy="true"
      break
      ;;
    unhealthy)
      printf 'Verification failed: PostgreSQL container is unhealthy.\n' >&2
      exit 1
      ;;
    none)
      printf 'Verification failed: PostgreSQL container has no healthcheck.\n' >&2
      exit 1
      ;;
  esac

  sleep 2
done

if [[ "$postgres_healthy" != "true" ]]; then
  printf 'Verification failed: PostgreSQL did not become healthy within 60 seconds.\n' >&2
  docker compose ps postgres >&2
  exit 1
fi

current_step="resolving Windows project path"
windows_project_root="$(pwd -W)"

current_step="running Gradle clean build"
MSYS_NO_PATHCONV=1 docker run --rm \
  --env-file "${windows_project_root}/.env" \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/youth_policy \
  -e GRADLE_USER_HOME=/workspace/backend/.gradle \
  -v "${windows_project_root}:/workspace" \
  -w /workspace/backend \
  eclipse-temurin:17-jdk \
  ./gradlew --no-daemon --console=plain clean build

printf 'Verification completed successfully.\n'
