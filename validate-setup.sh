#!/bin/bash

echo "=== Chess REST API Docker Setup Validation ==="
echo

# Check if Docker is installed and running
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker first."
    exit 1
fi

if ! docker info &> /dev/null; then
    echo "❌ Docker daemon is not running. Please start Docker."
    exit 1
fi

echo "✅ Docker is installed and running"

# Check if docker-compose is available
if ! command -v docker-compose &> /dev/null; then
    echo "❌ docker-compose is not installed. Please install docker-compose."
    exit 1
fi

echo "✅ docker-compose is available"

# Check if .env file exists
if [ ! -f ".env" ]; then
    echo "⚠️  No .env file found. Please run ./init.sh first to configure the environment."
    exit 1
fi

echo "✅ Environment configuration found"

# Test Docker builds
echo
echo "Testing Docker builds..."

# Test Spring Boot build
echo "🔄 Testing Spring Boot application build..."
if docker build -t chess-rest-api-validate . > /dev/null 2>&1; then
    echo "✅ Spring Boot application builds successfully"
    docker rmi chess-rest-api-validate > /dev/null 2>&1
else
    echo "❌ Spring Boot application build failed"
    exit 1
fi

# Test Python services build
echo "🔄 Testing Python services build..."
if docker build -t chess-python-validate ./scripts/Chess-Analysing-Pi > /dev/null 2>&1; then
    echo "✅ Python services build successfully"
    docker rmi chess-python-validate > /dev/null 2>&1
else
    echo "❌ Python services build failed"
    exit 1
fi

echo
echo "🎉 All validations passed! Your Docker setup is ready."
echo
echo "To start the services, run:"
echo "  docker-compose up -d"
echo
echo "To view logs:"
echo "  docker-compose logs -f"