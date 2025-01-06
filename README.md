# Chess Rest API

This project is a RESTful API for managing chess games. It is built using Spring Boot and provides endpoints for creating and retrieving chess games.

## Features

- Create a new chess game
- Retrieve all chess games
- Store and retrieve game details such as PGN, date, and result

## Technologies Used

- Java 23
- Spring Boot 3.4.1
- PostgreSQL
- Gradle
- Dotenv for environment variable management

## Prerequisites

- Java 23
- PostgreSQL
- Gradle

## Setup

1. Clone the repository:
    ```sh
    git clone https://github.com/yourusername/chess-rest-api.git
    cd chess-rest-api
    ```

2. Create a `.env` file in the root directory with the following content:
    ```env
    DB_URL=jdbc:postgresql://localhost:5432/chessdb
    DB_USERNAME=yourusername
    DB_PASSWORD=yourpassword
    ```

3. Update the `application.properties` file in `src/main/resources` to use the environment variables:
    ```properties
    spring.datasource.url=${DB_URL}
    spring.datasource.username=${DB_USERNAME}
    spring.datasource.password=${DB_PASSWORD}
    ```

4. Build the project:
    ```sh
    ./gradlew build
    ```

5. Run the application:
    ```sh
    ./gradlew bootRun
    ```

## Endpoints

- `GET /api/games` - Retrieve all chess games
- `POST /api/games` - Create a new chess game

## Intended Use

This API is intended to be used with the service [Chess-Analysing-Pi](https://github.com/yourusername/chess-analysing-pi), which provides advanced analysis and insights for chess games.

## License

This project is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Please open an issue or submit a pull request.

## Contact

For any inquiries, please contact [yourname](mailto:youremail@example.com).