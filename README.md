# Chess Rest API

This project is a RESTful API for managing chess games. It is built using Spring Boot and provides endpoints for creating and retrieving chess games.

## Features

- Create a new chess game
- Retrieve all chess games
- Store and retrieve game details such as PGN, date, and result
- Automated Chess.com data fetching
- Stockfish game analysis

## Technologies Used

- Java 21
- Spring Boot 3.4.1
- PostgreSQL
- Gradle
- Python 3.11 (for chess analysis services)
- Stockfish chess engine
- Docker & Docker Compose

## Quick Start with Docker (Recommended)

The easiest way to run this project is using Docker. This will set up the entire stack including the database, REST API, and chess analysis services.

### Prerequisites
- Docker
- Docker Compose

### Setup

1. Clone the repository:
    ```sh
    git clone https://github.com/yourusername/chess-rest-api.git
    cd chess-rest-api
    ```

2. Run the initialization script:
    ```sh
    ./init.sh
    ```
   
   This script will:
   - Prompt you for your Chess.com username
   - Prompt you for PostgreSQL credentials
   - Create the necessary environment configuration
   - Build and start all services

3. The services will be available at:
   - REST API: http://localhost:8080
   - PostgreSQL: localhost:5432

### Docker Services

The Docker setup includes:
- **PostgreSQL Database**: Stores chess games and analysis
- **Spring Boot REST API**: Serves chess game data
- **Python Data Fetcher**: Pulls games from Chess.com API daily
- **Python Analyzer**: Analyzes games with Stockfish engine

### Docker Commands

```sh
# Start services
./init.sh

# Validate setup
./validate-setup.sh

# Health check
./health-check.sh

# View logs
docker-compose logs -f

# View specific service logs
docker-compose logs -f chess-rest-api
docker-compose logs -f chess-data-fetcher
docker-compose logs -f chess-analyzer

# Stop services
docker-compose down

# Rebuild after changes
docker-compose down && docker-compose up --build -d
```

### Configuration

The setup uses environment variables stored in a `.env` file. You can modify this file to change:
- Database credentials
- Chess.com username
- Service configurations

Example `.env` file structure:
```env
# Database Configuration
POSTGRES_DB=chessgames
POSTGRES_USER=your_username
POSTGRES_PASSWORD=your_password

# Spring Boot Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/chessgames
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password

# Python Services Configuration
DB_HOST=db
DB_USER=your_username
DB_PASSWORD=your_password
DB_NAME=chessgames
CHESS_USERNAME=your_chess_com_username
ENV_Test=prod
```

## Manual Setup (Alternative)

### Prerequisites

- Java 21
- PostgreSQL
- Gradle
- Python 3.11+
- Stockfish

### Setup

1. Clone the repository:
    ```sh
    git clone https://github.com/yourusername/chess-rest-api.git
    cd chess-rest-api
    ```

2. Set up PostgreSQL database and create a `chessgames` database

3. Create a `.env` file in the root directory based on `.env.example`

4. Update the `application.properties` file if needed

5. Build the project:
    ```sh
    ./gradlew build
    ```

6. Run the application:
    ```sh
    ./gradlew bootRun
    ```

7. Set up Python environment and install dependencies:
    ```sh
    cd scripts/Chess-Analysing-Pi
    pip install -r requirements.txt
    ```

## API Endpoints

- `GET /api/v1/games` - Retrieve all chess games
- `POST /api/v1/games` - Create a new chess game

## Architecture

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Next.js/      │    │  Spring Boot     │    │   PostgreSQL    │
│   Expo App      │◄──►│   REST API       │◄──►│   Database      │
│                 │    │  (Port 8080)     │    │  (Port 5432)    │
└─────────────────┘    └──────────────────┘    └─────────────────┘
                                │                        ▲
                                ▼                        │
                       ┌──────────────────┐              │
                       │  Python Services │──────────────┘
                       │                  │
                       │ • Data Fetcher   │
                       │ • Game Analyzer  │
                       │ • Stockfish      │
                       └──────────────────┘
                                │
                                ▼
                       ┌──────────────────┐
                       │   Chess.com API  │
                       └──────────────────┘
```

## Development

### Adding New Features

1. For REST API changes: Modify files in `src/main/java`
2. For Python services: Modify files in `scripts/Chess-Analysing-Pi`
3. Rebuild containers after changes: `docker-compose down && docker-compose up --build -d`

### Debugging

- Check container logs: `docker-compose logs -f [service-name]`
- Run health check: `./health-check.sh`
- Access database directly: `docker exec -it chess-postgres psql -U [username] -d chessgames`

## Intended Use

This API is intended to be used with the service [Chess-Analysing-Pi](https://github.com/maik123100/chess-analysing-pi), which provides advanced analysis and insights for chess games.
