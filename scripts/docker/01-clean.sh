#!/bin/bash

set -e

docker system prune -f

echo "🧹 Cleaning dangling images..."

docker image prune -f

#docker compose down --volume

docker compose down

echo "✔ Clean completed"