# Crypto Wallet Application

A Spring Boot application for managing cryptocurrency wallets. This application allows users to create wallets, add crypto assets, and simulate wallet performance.

## Features

- User registration and authentication with JWT
- Wallet creation and management
- Adding and updating crypto assets in wallets
- Simulating wallet performance
- Integration with CoinCap API for cryptocurrency data

## Prerequisites

- Java 21
- Maven 3.6+
- IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)

## Running the Application

### Running in an IDE

1. Clone the repository
2. Open the project in your IDE
3. Run the `CryptoWalletApplication` class as a Java application
4. The application will start on port 8080 by default

### Running with Maven

1. Clone the repository
2. Navigate to the project root directory
3. Run the following command:
   ```
   ./mvnw spring-boot:run
   ```
   On Windows, use:
   ```
   mvnw.cmd spring-boot:run
   ```
4. The application will start on port 8080 by default

## Testing the Application

> **Note:** For testing purposes, the `validatePrice` method in the `CoinCapServiceImpl` class validates the price using only the integer part of the price since it is challenging to predict the current price of a crypto.

### Running Tests with Maven

To run all tests:
```
./mvnw test
```

On Windows:
```
mvnw.cmd test
```

### Running Specific Tests

To run a specific test class:
```
./mvnw test -Dtest=WalletServiceImplTest
```

On Windows:
```
mvnw.cmd test -Dtest=WalletServiceImplTest
```

## Accessing the Application

- The application provides a RESTful API
- The API endpoints are available at `http://localhost:8080/api/`
- Authentication is required for most endpoints
- H2 Database console is available at `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:exchange`
  - Username: `sa`
  - Password: (empty)

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/authenticate` - Authenticate and get JWT token

### Wallets
- `POST /api/wallets` - Create a new wallet
- `GET /api/wallets/{walletId}` - Get wallet by ID

### Assets
- `PATCH /api/assets/{walletId}` - Add or update an asset in a wallet

### Simulations
- `POST /api/simulations` - Simulate wallet performance
