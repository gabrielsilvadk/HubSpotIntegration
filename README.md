# HubSpot Integration

Spring Boot application for HubSpot integration with contact management and webhook support.

## Prerequisites

- Java 17 or later
- Maven
- HubSpot Developer Account
- H2 Database (included)
- Fly.io CLI (for deployment)

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

## Deployment to Fly.io

1. Install Fly.io CLI:
```bash
curl -L https://fly.io/install.sh | sh
```

2. Login to Fly.io:
```bash
fly auth login
```

3. Set up secrets for HubSpot credentials:
```bash
fly secrets set HUBSPOT_CLIENT_ID=your_client_id
fly secrets set HUBSPOT_CLIENT_SECRET=your_client_secret
fly secrets set HUBSPOT_REDIRECT_URI=https://your-app-name.fly.dev/api/auth/callback
```

4. Deploy the application:
```bash
fly deploy
```

5. Monitor the deployment:
```bash
fly status
```

6. View logs:
```bash
fly logs
```

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
- Production: File-based database (persisted in Fly.io volume)

Access H2 Console (development only):
- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:hubspotdb
- Username: sa
- Password: (from environment variables) 