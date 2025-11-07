# Spring Boot SSO POC - Office 365 Integration

A Spring Boot microservice application demonstrating Single Sign-On (SSO) authentication with Microsoft Office 365 using Azure AD OAuth2.

## Project Information

- **Project Name**: springboot-sso-poc
- **Package Name**: com.demo.sso
- **Java Version**: 17
- **Spring Boot Version**: 3.5.7
- **Build Tool**: Maven

## Features

- ✅ OAuth2/OpenID Connect authentication with Azure AD
- ✅ Sign in with Microsoft Office 365 credentials
- ✅ Secure REST API endpoints
- ✅ User profile information retrieval
- ✅ Robust attribute mapping (handles various Azure AD claim formats)
- ✅ Comprehensive logging with Logback
- ✅ Health check endpoints via Spring Boot Actuator
- ✅ Clean web interface with Thymeleaf templates

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Microsoft Azure account with Office 365
- Azure AD application registration

## Quick Start

### 1. Azure AD Setup

Register your application in Azure Portal:

1. Go to [Azure Portal](https://portal.azure.com/)
2. Navigate to **Azure Active Directory** > **App registrations**
3. Click **New registration**
4. Configure:
   - **Name**: `spring-boot-sso-poc`
   - **Supported account types**: "Accounts in this organizational directory only"
   - **Redirect URI**: Web - `http://localhost:8080/login/oauth2/code/azure`
5. Click **Register**

### 2. Get Credentials

#### Application (Client) ID and Tenant ID
1. On the app's **Overview** page, copy:
   - **Application (client) ID** → `AZURE_CLIENT_ID`
   - **Directory (tenant) ID** → `AZURE_TENANT_ID`

#### Client Secret
1. Go to **Certificates & secrets** > **Client secrets**
2. Click **+ New client secret**
3. Add description (e.g., "SpringBoot-SSO-Secret")
4. Select expiration period (e.g., 6 months, 12 months, 24 months)
5. Click **Add**
6. **CRITICAL**: Immediately copy the **Value** (not the Secret ID!)
   - ✅ Correct: Long random string like `AbC~123xyz.DEF456-ghi789_JKL012mno345PQR`
   - ❌ Wrong: GUID format like `e328c96c-148d-49a1-a76e-40979eed48a8`
   - **You can only see this once!** Save it immediately.

#### API Permissions
1. Go to **API permissions**
2. Click **Add a permission** > **Microsoft Graph** > **Delegated permissions**
3. Add these permissions:
   - `openid`
   - `profile`
   - `email`
   - `User.Read`
4. Click **Add permissions**
5. (Optional) Click **Grant admin consent** if you have admin privileges

### 3. Configure Environment Variables

Set the environment variables with your Azure AD credentials:

**Windows (PowerShell):**
```powershell
$env:AZURE_CLIENT_ID="your-application-client-id"
$env:AZURE_CLIENT_SECRET="your-client-secret-value"
$env:AZURE_TENANT_ID="your-directory-tenant-id"
```

**Windows (Command Prompt):**
```cmd
set AZURE_CLIENT_ID=your-application-client-id
set AZURE_CLIENT_SECRET=your-client-secret-value
set AZURE_TENANT_ID=your-directory-tenant-id
```

**Linux/Mac:**
```bash
export AZURE_CLIENT_ID="your-application-client-id"
export AZURE_CLIENT_SECRET="your-client-secret-value"
export AZURE_TENANT_ID="your-directory-tenant-id"
```

### 4. Build and Run

**Build the project:**
```bash
mvn clean install
```

**Run the application:**
```bash
mvn spring-boot:run
```

**Or use the convenience script:**
```bash
# Windows
setup-and-run.bat

# Linux/Mac
./setup-and-run.sh
```

The application will start on `http://localhost:8080`

### 5. Test the Application

1. Open your browser and navigate to `http://localhost:8080`
2. Click **"Sign in with Microsoft"**
3. Enter your Office 365 credentials
4. Authorize the application
5. You'll be redirected back and see your profile information!

## Application Endpoints

### Web Pages
- `http://localhost:8080/` - Public landing page
- `http://localhost:8080/login` - Login page
- `http://localhost:8080/home` - Authenticated home page (requires login)

### REST API Endpoints (requires authentication)
- **GET** `/api/user/me` - Get current user information (JSON)
- **GET** `/api/user/attributes` - Get all user attributes from Azure AD (JSON)

### Health Check (no authentication required)
- **GET** `/actuator/health` - Application health status

### Example API Usage

After logging in via browser, you can access the API:

```bash
# Get current user info
curl http://localhost:8080/api/user/me

# Response example:
{
  "username": "john.doe@company.com",
  "name": "John Doe",
  "email": "john.doe@company.com",
  "authenticated": true
}
```

## Project Structure

```
spring-boot-sso-poc/
├── src/
│   ├── main/
│   │   ├── java/com/demo/sso/
│   │   │   ├── SsoApplication.java              # Main application entry point
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java          # OAuth2 security configuration
│   │   │   └── controller/
│   │   │       ├── HomeController.java          # Web page controller
│   │   │       └── UserController.java          # REST API controller
│   │   └── resources/
│   │       ├── application.yml                  # Application & OAuth2 config
│   │       ├── logback.xml                      # Logging configuration
│   │       └── templates/                       # Thymeleaf HTML templates
│   │           ├── index.html                   # Landing page
│   │           ├── login.html                   # Login page
│   │           └── home.html                    # User profile page
│   └── test/java/                               # Test directory
├── docs/
│   └── REQUIREMENT.md                           # Project requirements
├── pom.xml                                      # Maven dependencies
├── README.md                                    # This file
├── .gitignore                                   # Git ignore rules
├── .env.example                                 # Environment variables template
├── setup-and-run.bat                            # Windows quick start script
└── setup-and-run.sh                             # Linux/Mac quick start script
```

## Key Components

### SecurityConfig.java
Configures Spring Security with:
- OAuth2 login with Azure AD
- Public access to landing page and health endpoints
- Authentication required for all other pages
- Custom logout handling

### HomeController.java
Web controller that handles:
- Public landing page (`/`)
- Login page (`/login`)
- Authenticated home page (`/home`) with user profile display
- Robust attribute mapping with fallback support

### UserController.java
REST API controller providing:
- `/api/user/me` - Current user information
- `/api/user/attributes` - All OAuth2 attributes
- Handles various Azure AD claim name variations

### application.yml
Configuration for:
- Azure AD OAuth2 client registration
- Token endpoints and scopes
- Logging levels and patterns
- Actuator endpoints

## Logging

Logs are written to multiple destinations:

- **Console output** - Real-time application logs
- **logs/application.log** - All application logs (rotated daily, max 10MB, 30 days retention)
- **logs/error.log** - Error-level logs only

Log levels:
- Application code (`com.demo.sso`): `DEBUG`
- Spring Security: `DEBUG`
- OAuth2 flows: `TRACE` (for troubleshooting)

## Common Issues & Solutions

### Issue 1: "Invalid redirect URI" Error

**Symptom:** Error message about redirect URI mismatch during login

**Solution:** Ensure the redirect URI in Azure AD exactly matches:
```
http://localhost:8080/login/oauth2/code/azure
```

### Issue 2: "401 Unauthorized" When Exchanging Code for Token

**Symptom:** Authentication fails after Microsoft login with 401 error in logs

**Solution:** You're using the **Secret ID** instead of the **Secret Value**
- The Secret ID is a GUID: `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx` ❌
- The Secret Value is a long random string: `AbC~123xyz...` ✅
- **Fix:** Create a new client secret and copy the **Value** immediately

### Issue 3: "Attribute value for 'preferred_username' cannot be null"

**Symptom:** Error after successful Microsoft login

**Solution:** This is already fixed in the application!
- The app uses fallback logic to try multiple attribute names
- Azure AD may return different claim names depending on configuration
- The app will work regardless of which attributes are returned

### Issue 4: Environment Variables Not Loaded

**Symptom:** Application fails to start or shows placeholder values

**Solution:**
1. Ensure environment variables are set **before** starting the application
2. Restart your terminal/IDE after setting environment variables
3. Verify variables are set: `echo %AZURE_CLIENT_ID%` (Windows) or `echo $AZURE_CLIENT_ID` (Linux/Mac)

### Issue 5: Build Failures

**Symptom:** Maven build fails

**Solution:**
1. Ensure Java 17 is installed: `java -version`
2. Ensure Maven is installed: `mvn -version`
3. Clear Maven cache: `mvn clean`
4. Rebuild: `mvn clean install`

## Security Best Practices

### Development
- ✅ Use environment variables (never hardcode credentials)
- ✅ Add `.env` to `.gitignore`
- ✅ Use the provided `.env.example` as a template
- ✅ Rotate client secrets regularly

### Production Considerations

For production deployment, implement these additional security measures:

1. **HTTPS Only**
   - Use proper SSL/TLS certificates
   - Never use HTTP in production

2. **Secret Management**
   - Use Azure Key Vault for storing secrets
   - Implement secret rotation
   - Use managed identities when possible

3. **Configuration**
   - Reduce logging levels (remove TRACE/DEBUG)
   - Configure proper CORS policies
   - Implement rate limiting
   - Enable CSRF protection (already configured)

4. **Monitoring**
   - Set up application monitoring
   - Configure alerts for authentication failures
   - Monitor token expiration and refresh

5. **Session Management**
   - Configure session timeout
   - Use Redis or similar for distributed sessions
   - Implement proper session invalidation

6. **Network Security**
   - Use firewall rules
   - Implement IP whitelisting if needed
   - Enable Azure AD conditional access policies

## Testing

### Manual Testing Checklist

- [ ] Navigate to landing page
- [ ] Click "Sign in with Microsoft"
- [ ] Authenticate with Office 365 credentials
- [ ] Verify user information is displayed
- [ ] Access `/api/user/me` endpoint
- [ ] Access `/api/user/attributes` endpoint
- [ ] Click "Logout" and verify redirect
- [ ] Check application logs for errors

### Automated Testing

To add unit and integration tests:

```bash
# Run all tests
mvn test

# Run with coverage
mvn test jacoco:report
```

## Dependencies

Key dependencies used:

- **Spring Boot 3.5.7** - Application framework
- **Spring Security OAuth2 Client** - OAuth2 authentication
- **Spring Boot Starter Web** - REST API support
- **Thymeleaf** - Template engine for web pages
- **Lombok** - Reduces boilerplate code
- **Logback** - Logging framework
- **Spring Boot Actuator** - Health checks and monitoring

See `pom.xml` for complete dependency list.

## Environment Variables Reference

| Variable | Description | Example |
|----------|-------------|---------|
| `AZURE_CLIENT_ID` | Application (client) ID from Azure AD | `7acbd773-c468-4d1d-a61b-a3a00130f68f` |
| `AZURE_CLIENT_SECRET` | Client secret value (not Secret ID!) | `AbC~123xyz.DEF456-ghi789_JKL` |
| `AZURE_TENANT_ID` | Directory (tenant) ID from Azure AD | `e20ec26a-5185-41cb-a510-40a82b67cde6` |

## Additional Resources

- [Spring Security OAuth2 Documentation](https://spring.io/projects/spring-security-oauth)
- [Azure AD Documentation](https://docs.microsoft.com/en-us/azure/active-directory/)
- [Microsoft Graph API](https://docs.microsoft.com/en-us/graph/)
- [OAuth 2.0 and OpenID Connect](https://oauth.net/2/)

## License

This is a proof-of-concept project for demonstration purposes.

## Support & Contribution

For issues, questions, or contributions:
1. Check this README for common solutions
2. Review the application logs in `logs/application.log`
3. Enable TRACE logging for detailed OAuth2 flow information
4. Check Azure AD app registration configuration

---

**Built with:** Java 17 | Spring Boot 3.5.7 | Maven | Azure AD OAuth2

**Last Updated:** November 2025
