#!/bin/bash

echo "=== Chess REST API Docker Setup ==="
echo

# Check if .env file already exists
if [ -f ".env" ]; then
    echo "Found existing .env file. Using existing configuration."
    echo "To reconfigure, delete the .env file and run this script again."
    echo
else
    echo "Setting up environment variables..."
    echo

    # Prompt for Chess.com username
    read -p "Enter your Chess.com username: " CHESS_USERNAME

    # Prompt for PostgreSQL credentials
    read -p "Enter PostgreSQL username (default: chessgames): " DB_USER
    DB_USER=${DB_USER:-chessgames}

    read -s -p "Enter PostgreSQL password: " DB_PASSWORD
    echo

    # Create .env file
    cat > .env << EOF
# Database Configuration
POSTGRES_DB=chessgames
POSTGRES_USER=${DB_USER}
POSTGRES_PASSWORD=${DB_PASSWORD}

# Spring Boot Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/chessgames
SPRING_DATASOURCE_USERNAME=${DB_USER}
SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}

# Python Services Configuration
DB_HOST=db
DB_USER=${DB_USER}
DB_PASSWORD=${DB_PASSWORD}
DB_NAME=chessgames
CHESS_USERNAME=${CHESS_USERNAME}
ENV_Test=prod
EOF

    echo
    echo "Environment file created successfully!"
fi

echo "Building and starting services..."
echo

# Build and start the services
docker-compose up --build -d

echo
echo "=== Setup Complete ==="
echo
echo "Services starting up:"
echo "- PostgreSQL Database: localhost:5432"
echo "- Chess REST API: http://localhost:8080"
echo "- Python Chess Services: Running in background"
echo
echo "To view logs:"
echo "  docker-compose logs -f"
echo
echo "To stop services:"
echo "  docker-compose down"
echo
echo "To rebuild after changes:"
echo "  docker-compose down && docker-compose up --build -d"