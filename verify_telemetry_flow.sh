#!/bin/bash

# Base URL
BASE_URL="http://localhost:8080/api/v1"

# Generate random suffix
SUFFIX=$(date +%s)
EMAIL="test${SUFFIX}@safecar.com"
LICENSE_PLATE="TST-${SUFFIX: -3}"
MAC_ADDRESS="AA:BB:CC:DD:EE:${SUFFIX: -2}"

# 1. Register User
echo "1. Registering/Logging in User..."
SIGNIN_RESPONSE=$(curl -s -X POST "$BASE_URL/authentication/sign-in" \
  -H "Content-Type: application/json" \
  -d "{\"email\": \"$EMAIL\", \"password\": \"password\"}")

if [[ $SIGNIN_RESPONSE != *"token"* ]]; then
  echo "User not found, registering..."
  curl -s -X POST "$BASE_URL/authentication/sign-up" \
    -H "Content-Type: application/json" \
    -d "{\"email\": \"$EMAIL\", \"password\": \"password\", \"confirmPassword\": \"password\", \"roles\": [\"ROLE_CLIENT\"]}"

  SIGNIN_RESPONSE=$(curl -s -X POST "$BASE_URL/authentication/sign-in" \
    -H "Content-Type: application/json" \
    -d "{\"email\": \"$EMAIL\", \"password\": \"password\"}")
fi
echo "Sign-in Response: $SIGNIN_RESPONSE"
TOKEN=$(echo $SIGNIN_RESPONSE | jq -r '.token')

if [ -z "$TOKEN" ] || [ "$TOKEN" == "null" ]; then
  echo "Failed to obtain token"
  exit 1
fi
echo "Token obtained."

# 1.2 Create PersonProfile
echo "1.2 Creating PersonProfile..."
PROFILE_RESPONSE=$(curl -s -X POST "$BASE_URL/person-profiles?userEmail=$EMAIL" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"fullName\": \"Test Driver $SUFFIX\", \"city\": \"Test City\", \"country\": \"Test Country\", \"phone\": \"123456789\", \"dni\": \"${SUFFIX: -8}\"}")
echo "Profile Response: $PROFILE_RESPONSE"

# Extract Profile ID (simple grep/sed, assuming JSON structure)
PROFILE_ID=$(echo $PROFILE_RESPONSE | grep -o '"profileId":[0-9]*' | grep -o '[0-9]*')
echo "Profile ID: $PROFILE_ID"

# 1.5 Create Driver (for Profile ID)
echo "1.5 Creating Driver..."
DRIVER_RESPONSE=$(curl -s -X POST "$BASE_URL/profiles/$PROFILE_ID/driver" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json")
echo "Driver Response: $DRIVER_RESPONSE"

# Extract Driver ID
DRIVER_ID=$(echo $DRIVER_RESPONSE | grep -o '"driverId":[0-9]*' | grep -o '[0-9]*')
echo "Driver ID: $DRIVER_ID"

# 2. Create Vehicle
echo "2. Creating Vehicle..."
VEHICLE_RESPONSE=$(curl -s -X POST "$BASE_URL/vehicles" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"driverId\": $DRIVER_ID, \"licensePlate\": \"$LICENSE_PLATE\", \"brand\": \"Toyota\", \"model\": \"Corolla\"}")
echo "Vehicle Response: $VEHICLE_RESPONSE"

# 3. Register Device (using License Plate)
echo "3. Registering Device..."
DEVICE_RESPONSE=$(curl -s -X POST "$BASE_URL/devices" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"macAddress\": \"$MAC_ADDRESS\", \"deviceType\": \"MOTOR\", \"licensePlate\": \"$LICENSE_PLATE\"}")
echo "Device Response: $DEVICE_RESPONSE"

# 4. Ingest Telemetry (using Mac Address)
echo "4. Ingesting Telemetry..."
TELEMETRY_RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/telemetry" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"macAddress\": \"$MAC_ADDRESS\", \"latitude\": 10.0, \"longitude\": 20.0, \"speed\": 60.0, \"temperature\": 25.0, \"fuelLevel\": 50.0}")
echo "Telemetry Response Body: $TELEMETRY_RESPONSE"

# Check if response contains error or is empty (success usually empty body for 201)
# But we need status code.
TELEMETRY_STATUS=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$BASE_URL/telemetry" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "macAddress": "'"$MAC_ADDRESS"'",
    "type": "SENSOR_DATA",
    "severity": "INFO",
    "timestamp": "2023-10-27T10:00:00Z",
    "speed": 60.5,
    "latitude": 40.7128,
    "longitude": -74.0060,
    "odometer": 15000.0,
    "engineTemperature": 90.0
  }')
echo "Telemetry Response Code: $TELEMETRY_STATUS"

if [ "$TELEMETRY_STATUS" == "201" ]; then
  echo "SUCCESS: Telemetry ingested successfully."
else
  echo "FAILURE: Telemetry ingestion failed."
fi
