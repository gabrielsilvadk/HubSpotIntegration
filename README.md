# HubSpot Integration

Spring Boot application for HubSpot integration with contact management and webhook support.

## Prerequisites

- Java 17 or later
- Maven
- HubSpot Developer Account
- H2 Database (included)
- Fly.io CLI (for deployment)

## Local Development Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd hubspot-integration
```

### 2. Configure Environment Variables

1. Copy the `.env.example` file to `.env`:
```bash
cp .env.example .env
```

2. Update the `.env` file with your values:
```properties
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:h2:mem:hubspotdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
SPRING_DATASOURCE_USERNAME=sa
SPRING_DATASOURCE_PASSWORD=your_password_here

# HubSpot Configuration
HUBSPOT_CLIENT_ID=your_client_id_here
HUBSPOT_CLIENT_SECRET=your_client_secret_here
HUBSPOT_REDIRECT_URI=http://localhost:8080/api/auth/callback
```

### 3. Build the Application

1. Build using Maven:
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

### 4. HubSpot Authentication

1. Access the authorization URL:
```
http://localhost:8080/api/auth/authorize
```

2. Complete the HubSpot OAuth flow:
   - Log in to your HubSpot account
   - Authorize the application
   - You will be redirected back to the callback URL

3. Verify the token:
```bash
curl http://localhost:8080/api/auth/token
```

### 5. Testing the Application

1. Create a contact:
```bash
curl -X POST http://localhost:8080/api/contacts \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "phone": "+1234567890"
  }'
```

2. List all contacts:
```bash
curl http://localhost:8080/api/contacts
```

3. Get contact by email:
```bash
curl http://localhost:8080/api/contacts/test@example.com
```

## Deployment to Fly.io

### 1. Install Fly.io CLI
```bash
curl -L https://fly.io/install.sh | sh
```

### 2. Login to Fly.io
```bash
fly auth login
```

### 3. Configure Secrets

1. Set up HubSpot credentials:
```bash
fly secrets set HUBSPOT_CLIENT_ID=your_client_id
fly secrets set HUBSPOT_CLIENT_SECRET=your_client_secret
fly secrets set HUBSPOT_REDIRECT_URI=https://hubspot-integration.fly.dev/api/auth/callback
```

2. Set up database credentials:
```bash
fly secrets set SPRING_DATASOURCE_PASSWORD=your_secure_password
```

### 4. Deploy the Application

1. Launch the application:
```bash
fly launch
```

2. Deploy:
```bash
fly deploy
```

3. Monitor the deployment:
```bash
fly status
```

4. View logs:
```bash
fly logs
```

### 5. Post-Deployment Verification

1. Test the HubSpot authentication:
```
https://hubspot-integration.fly.dev/api/auth/authorize
```

2. Create a test contact:
```bash
curl -X POST https://hubspot-integration.fly.dev/api/contacts \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "phone": "+1234567890"
  }'
```

## Profiles

The application supports different profiles:

### Development Profile
- H2 Console enabled
- SQL logging enabled
- Detailed logging
- Auto database updates

To use development profile:
```bash
export SPRING_PROFILES_ACTIVE=dev
```

### Production Profile
- H2 Console disabled
- SQL logging disabled
- Minimal logging
- Database validation only

To use production profile:
```bash
export SPRING_PROFILES_ACTIVE=prod
```

## API Endpoints

### Contact Management
- `POST /api/contacts`: Create a new contact
- `GET /api/contacts`: List all contacts
- `GET /api/contacts/{email}`: Get contact by email

### Authentication
- `GET /api/auth/authorize`: Start HubSpot OAuth flow
- `GET /api/auth/callback`: HubSpot OAuth callback
- `GET /api/auth/token`: Verify current token

### Webhooks
- `POST /api/webhooks/hubspot`: Webhook endpoint for HubSpot events

## Database

The application uses H2 database:
- Development: In-memory database
- Production: File-based database (persisted in Fly.io volume)

### Development Database Access
- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:hubspotdb
- Username: sa
- Password: (from environment variables)

## Troubleshooting

### Common Issues

1. **Token Not Found**
   - Ensure you've completed the HubSpot authentication flow
   - Check the logs for any authentication errors
   - Verify the HubSpot credentials in your environment

2. **Database Connection Issues**
   - Verify the database URL and credentials
   - Check if the database file is writable
   - Ensure the database directory exists

3. **Deployment Issues**
   - Check the Fly.io logs for errors
   - Verify all required secrets are set
   - Ensure the application has proper permissions

### Logs

1. Local Development:
```bash
tail -f logs/application.log
```

2. Production (Fly.io):
```bash
fly logs
```