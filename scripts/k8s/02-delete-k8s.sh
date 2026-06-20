#!/bin/bash

set -e

echo "🧹 Suppression des deployments et services..."

SERVICES=(
  discovery-service
  gateway-service
  config-service
  authent-service
  client-service
  company-service
  consultant-service
  prestation-service
  facture-service
  front-service
)

for svc in "${SERVICES[@]}"; do
  echo "🗑️ Delete deployment: $svc"
  kubectl delete deployment $svc --ignore-not-found=true

  echo "🗑️ Delete service: $svc"
  kubectl delete service $svc --ignore-not-found=true
done

echo "✅ Cleanup completed"