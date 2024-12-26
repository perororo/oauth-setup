# OAuth Setup

This project demonstrates an OAuth2-based authentication and authorization setup using three distinct services:

1. **Authorization Service**
2. **OAuth Client**
3. **Resource Server**

---

## Overview of Services

### 1. Authorization Service
- **Purpose**: Acts as the OAuth2 Authorization Server.
- **Responsibilities**:
    - Issue access tokens to clients.
    - Validate authentication credentials.
- **Configuration**:
    - The service uses Spring Authorization Server.
    - Defines clients, scopes, and supported authorization grant types.

#### Key Files
- `AuthorizationServiceApplication.kt`: Main entry point for the service.
- `application.yaml`: Configuration file for client registration and authorization settings.

#### Notable Configuration
```yaml
spring:
  security:
    oauth2:
      authorizationserver:
        client:
          client-1:
            registration:
              client-id: client
              client-secret: '{bcrypt}$2a$10$jdJGhzsiIqYFpjJiYWMl/eKDOd8vdyQis2aynmFN0dgJ53XvpzzwC'
              client-authentication-methods: client_secret_basic
              authorization-grant-types: client_credentials, authorization_code, refresh_token
              redirect-uris: http://127.0.0.1:8082/login/oauth2/code/spring
              scopes:
                - user.read
                - user.write
                - openid
```

---

### 2. OAuth Client
- **Purpose**: Acts as the client application that interacts with the Authorization Service and the Resource Server.
- **Responsibilities**:
    - Authenticate with the Authorization Server.
    - Relay tokens to the Resource Server.
- **Configuration**:
    - Uses Spring Security to handle client registration.
    - Configures a gateway with token relay capabilities.

#### Key Files
- `ClientConfiguration.kt`: Configures a Spring Cloud Gateway for routing and token relay.
- `application.properties`: Contains client-specific OAuth2 settings.

#### Notable Configuration
```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          spring:
            provider: spring
            client-id: client
            client-secret: secret
            authorization-grant-type: authorization_code
            client-authentication-method: client_secret_basic
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
            scope: user.read, openid
        provider:
          spring:
            issuer-uri: http://localhost:8080
```

#### Gateway Configuration
The gateway routes requests to the resource server while relaying tokens:
```kotlin
fun gateway(rlb: RouteLocatorBuilder): RouteLocator? {
    return rlb
        .routes()
        .route { rs: PredicateSpec ->
            rs
                .path("/**") // Matches all paths
                .filters(GatewayFilterSpec::tokenRelay)
                .uri("http://localhost:8081/")
        }
        .build()
}
```

---

### 3. Resource Server
- **Purpose**: Serves protected resources that require OAuth2 tokens for access.
- **Responsibilities**:
    - Validate incoming tokens from clients.
    - Respond with resources to authenticated requests.
- **Configuration**:
    - Configured as a Spring Security Resource Server using JWT validation.

#### Key Files
- `HelloController.kt`: Contains endpoints for testing resource access.
- `application.yaml`: Specifies JWT settings and issuer URI.

#### Example Endpoints
- `/hello`: Protected resource that requires a valid token.
- `/`: Public resource accessible without authentication.

#### Notable Configuration
```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8080
```

---

## Testing the Services

### Individual Service Testing
You can test the services individually using the following commands:

#### Step 1: Obtain an Access Token
```bash
TOKEN=$(http -f POST :8080/oauth2/token grant_type=client_credentials scope='user.read' -a client:secret | jq -r ".access_token")
```

#### Step 2: Introspect the Token
```bash
http -f POST :8080/oauth2/introspect token=$TOKEN -a client:secret
```

---

## Running the Setup
1. **Start the Authorization Service**:
    - Navigate to the `authorization-service` directory.
    - Run the application using `./gradlew bootRun`.

2. **Start the Resource Server**:
    - Navigate to the `resource-server` directory.
    - Run the application using `./gradlew bootRun`.

3. **Start the OAuth Client**:
    - Navigate to the `oauth-client` directory.
    - Run the application using `./gradlew bootRun`.

4. **Test the Setup**:
    - Access public and protected resources via the gateway:
        - Public resource: `http://localhost:8082/`
        - Protected resource: `http://localhost:8082/hello` (requires OAuth2 token).

---