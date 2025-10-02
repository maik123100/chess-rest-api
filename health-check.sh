#!/bin/bash

echo "=== Chess REST API Health Check ==="
echo

# Function to check if a service is running
check_service() {
    local service=$1
    local container_name=$2
    
    if docker ps --filter "name=$container_name" --format "table {{.Names}}" | grep -q "$container_name"; then
        echo "✅ $service is running"
        return 0
    else
        echo "❌ $service is not running"
        return 1
    fi
}

# Function to check if a port is accessible
check_port() {
    local service=$1
    local port=$2
    
    if curl -s http://localhost:$port > /dev/null 2>&1; then
        echo "✅ $service is accessible on port $port"
        return 0
    else
        echo "❌ $service is not accessible on port $port"
        return 1
    fi
}

# Check if docker-compose is running
if ! docker-compose ps | grep -q "Up"; then
    echo "❌ No services are running. Start with: docker-compose up -d"
    exit 1
fi

echo "Checking Docker containers..."
check_service "PostgreSQL Database" "chess-postgres"
check_service "REST API" "chess-rest-api" 
check_service "Data Fetcher" "chess-data-fetcher"
check_service "Game Analyzer" "chess-analyzer"

echo
echo "Checking service accessibility..."
check_port "PostgreSQL" "5432"
sleep 2  # Give REST API a moment to start up
check_port "REST API" "8080"

echo
echo "=== Service Status ==="
docker-compose ps

echo
echo "=== Recent Logs ==="
echo "REST API logs:"
docker-compose logs --tail=5 app

echo
echo "Data Fetcher logs:"
docker-compose logs --tail=5 python-data-fetcher

echo
echo "Analyzer logs:"
docker-compose logs --tail=5 python-analyzer