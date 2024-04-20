Sure, here's a markdown file with instructions on how to navigate to the Swagger page and generate a bearer token to test the APIs:

---

# Build Code and Deployment

## 1. Build Code and generate JAR file

- Open your CMD.
  - Command: `mvn clean packcage`


## 2. Run the docker containers for the JAR and database

- Open your CMD.
  - Command: `docker compose up -d`
  - 
# Accessing Swagger UI and Generating Bearer Token

## 1. Navigate to Swagger UI

- Open your web browser and enter the URL of your application followed by `/swagger-ui/index.html`.
    - Example: `http://localhost:8080/swagger-ui/index.html`

## 2. Explore Available APIs

- Once on the Swagger UI page, you'll see a list of available APIs categorized by controllers.
- Click on each API to expand it and view the available endpoints and operations.

## 3. Generate Bearer Token

- Locate the authentication endpoint in the Swagger UI.
- Enter the required authentication credentials (user, password) and click the "Try it out" button.
- After successful authentication, the response will include a bearer token.
- Copy the bearer token from the response.

## 4. Test APIs with Bearer Token

- For each API endpoint that requires authentication, click on it to expand and view its operations.
- Click on the operation you want to test (e.g., GET, POST) and then click the "Try it out" button.
- If the operation requires authentication, paste the bearer token into the "Authorization" field with the prefix "Bearer ".
    - Example: `Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c`

## 5. Execute API Requests

- After providing the necessary parameters, click the "Execute" button to send the request.
- View the response below the request parameters to see the result of the API operation.

---

These steps should guide you through accessing the Swagger UI, generating a bearer token, and testing the APIs with authentication. Adjust the instructions as needed based on your application's specific authentication mechanism and Swagger configuration.