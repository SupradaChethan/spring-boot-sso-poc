@echo off
echo ============================================
echo Spring Boot SSO POC - Setup and Run
echo ============================================
echo.

REM Check if environment variables are set
if "%AZURE_CLIENT_ID%"=="" (
    echo ERROR: AZURE_CLIENT_ID environment variable is not set!
    echo Please set the required environment variables:
    echo.
    echo set AZURE_CLIENT_ID=your-client-id
    echo set AZURE_CLIENT_SECRET=your-client-secret
    echo set AZURE_TENANT_ID=your-tenant-id
    echo.
    pause
    exit /b 1
)

if "%AZURE_CLIENT_SECRET%"=="" (
    echo ERROR: AZURE_CLIENT_SECRET environment variable is not set!
    echo Please set the required environment variables:
    echo.
    echo set AZURE_CLIENT_SECRET=your-client-secret
    echo.
    pause
    exit /b 1
)

if "%AZURE_TENANT_ID%"=="" (
    echo ERROR: AZURE_TENANT_ID environment variable is not set!
    echo Please set the required environment variables:
    echo.
    echo set AZURE_TENANT_ID=your-tenant-id
    echo.
    pause
    exit /b 1
)

echo Environment variables are configured correctly!
echo.
echo Building the project...
call mvn clean install

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Build failed!
    pause
    exit /b 1
)

echo.
echo ============================================
echo Build successful! Starting the application...
echo ============================================
echo.
echo The application will be available at:
echo http://localhost:8080
echo.
echo Press Ctrl+C to stop the application
echo.

call mvn spring-boot:run
