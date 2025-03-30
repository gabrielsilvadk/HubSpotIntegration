# HubSpot Integration

Spring Boot application for HubSpot integration with contact management and webhook support.

## Prerequisites

- Java 17 or later
- Maven
- HubSpot Developer Account
- H2 Database (included)

## Configuration

### Environment Variables

1. Copy the `.env.example` file to `.env`:
```bash
cp .env.example .env
```

2. Update the `.env` file with your values:
- Database credentials
- HubSpot API credentials
- Other configuration values

### Profiles

The application supports different profiles:

- `dev`: Development environment (default)
- `prod`: Production environment

To set the active profile:
```bash
export SPRING_PROFILES_ACTIVE=dev  # or prod
```

### Development Profile

The development profile includes:
- H2 Console enabled
- SQL logging enabled
- Detailed logging
- Auto database updates

### Production Profile

The production profile includes:
- H2 Console disabled
- SQL logging disabled
- Minimal logging
- Database validation only

## Running the Application

1. Build the application:
```bash
./mvnw clean package
```

2. Run the application:
```bash
java -jar target/hubspot-integration-0.0.1-SNAPSHOT.jar
```

Or with Maven:
```bash
./mvnw spring-boot:run
```

## Security Considerations

1. Never commit the `.env` file
2. Use strong passwords in production
3. Disable H2 Console in production
4. Keep HubSpot credentials secure
5. Use HTTPS in production

## API Endpoints

- `POST /api/contacts`: Create a new contact
- `GET /api/contacts`: List all contacts
- `GET /api/contacts/{email}`: Get contact by email
- `GET /api/auth/authorize`: Start HubSpot OAuth flow
- `GET /api/auth/callback`: HubSpot OAuth callback
- `POST /api/webhooks/hubspot`: Webhook endpoint for HubSpot events

## Database

The application uses H2 database:
- Development: In-memory database
- Production: File-based database

Access H2 Console (development only):
- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:hubspotdb
- Username: sa
- Password: (from environment variables) 