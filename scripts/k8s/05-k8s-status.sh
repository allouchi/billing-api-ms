#!/bin/bash

echo "📦 Pods:"
kubectl get pods

echo "🌐 Services:"
kubectl get svc

echo "🐳 Docker:"
docker ps