#!/bin/bash

echo "============================================"
echo "Spring Boot SSO POC - Setup and Run"
echo "============================================"
echo ""

# Check if environment variables are set
if [ -z "$AZURE_CLIENT_ID" ]; then
    echo "ERROR: AZURE_CLIENT_ID environment variable is not set!"
    echo "Please set the required environment variables:"
    echo ""
    echo "export AZURE_CLIENT_ID=your-client-id"
    echo "export AZURE_CLIENT_SECRET=your-client-secret"
    echo "export AZURE_TENANT_ID=your-tenant-id"
    echo ""
    exit 1
fi

if [ -z "$AZURE_CLIENT_SECRET" ]; then
    echo "ERROR: AZURE_CLIENT_SECRET environment variable is not set!"
    echo "Please set the required environment variables:"
    echo ""
    echo "export AZURE_CLIENT_SECRET=your-client-secret"
    echo ""
    exit 1
fi

if [ -z "$AZURE_TENANT_ID" ]; then
    echo "ERROR: AZURE_TENANT_ID environment variable is not set!"
    echo "Please set the required environment variables:"
    echo ""
    echo "export AZURE_TENANT_ID=your-tenant-id"
    echo ""
    exit 1
fi

echo "Environment variables are configured correctly!"
echo ""
echo "Building the project..."
mvn clean install

if [ $? -ne 0 ]; then
    echo ""
    echo "ERROR: Build failed!"
    exit 1
fi

echo ""
echo "============================================"
echo "Build successful! Starting the application..."
echo "============================================"
echo ""
echo "The application will be available at:"
echo "http://localhost:8080"
echo ""
echo "Press Ctrl+C to stop the application"
echo ""

mvn spring-boot:run
