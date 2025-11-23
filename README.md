# SafeCar Backend Platform

## Summary
SafeCar Backend Platform is a comprehensive IoT vehicle telemetry and workshop management platform. This documentation focuses on the execution flows to validate the system's functionality.

## Configuration and Setup

### Prerequisites
- Java 21+
- Maven 3.9+
- MySQL 8.0+

### Run the Application
```bash
./mvnw clean compile
./mvnw spring-boot:run
# Application available at: http://localhost:8080
```

## 🧪 End-to-End Execution Flows

### 📝 Notes
- **IDs**: Adjust IDs based on your database state.
- **Tokens**: Replace `$TOKEN` with actual JWT tokens.
- **Timestamps**: Use ISO-8601 format (e.g., `2025-11-12T10:00:00Z`).

---

### 🔐 FLOW 1: Initial Setup (IAM + Profiles + Devices)

#### 1.1. Register Users
```bash
# Register Driver
curl -X POST http://localhost:8080/api/v1/authentication/sign-up -H 'Content-Type: application/json' -d '{ "email": "driver1@safecar.com", "password": "Driver123!", "confirmPassword": "Driver123!", "roles": ["ROLE_DRIVER"] }'

# Register Mechanic
curl -X POST http://localhost:8080/api/v1/authentication/sign-up -H 'Content-Type: application/json' -d '{ "email": "mechanic1@safecar.com", "password": "Mechanic123!", "confirmPassword": "Mechanic123!", "roles": ["ROLE_MECHANIC"] }'

# Register Workshop Owner
curl -X POST http://localhost:8080/api/v1/authentication/sign-up -H 'Content-Type: application/json' -d '{ "email": "owner1@safecar.com", "password": "Owner123!", "confirmPassword": "Owner123!", "roles": ["ROLE_WORKSHOP"] }'
```

#### 1.2. Authenticate (Get Tokens)
```bash
# Login Driver
curl -X POST http://localhost:8080/api/v1/authentication/sign-in -H 'Content-Type: application/json' -d '{ "email": "driver1@safecar.com", "password": "Driver123!" }'

# Login Mechanic
curl -X POST http://localhost:8080/api/v1/authentication/sign-in -H 'Content-Type: application/json' -d '{ "email": "mechanic1@safecar.com", "password": "Mechanic123!" }'

# Login Owner
curl -X POST http://localhost:8080/api/v1/authentication/sign-in -H 'Content-Type: application/json' -d '{ "email": "owner1@safecar.com", "password": "Owner123!" }'
```

#### 1.3. Create Profiles
```bash
# Driver Profile
curl -X POST "http://localhost:8080/api/v1/person-profiles?userEmail=driver1@safecar.com" -H "Authorization: Bearer $DRIVER_TOKEN" -H 'Content-Type: application/json' -d '{ "fullName": "Juan Pérez", "city": "Lima", "phone": "987654321", "country": "Peru", "dni": "12345678" }'

# Mechanic Profile
curl -X POST "http://localhost:8080/api/v1/person-profiles?userEmail=mechanic1@safecar.com" -H "Authorization: Bearer $MECHANIC_TOKEN" -H 'Content-Type: application/json' -d '{ "fullName": "Carlos Rodríguez", "city": "Lima", "phone": "912345678", "country": "Peru", "dni": "87654321" }'

# Workshop Business Profile
curl -X POST "http://localhost:8080/api/v1/business-profiles?userEmail=owner1@safecar.com" -H "Authorization: Bearer $OWNER_TOKEN" -H 'Content-Type: application/json' -d '{ "businessName": "Taller El Buen Mecánico", "ruc": "20123456789", "businessAddress": "Av. Venezuela 789", "contactPhone": "987654321", "contactEmail": "contact@safecar.com" }'
```

#### 1.4. Get Profiles
```bash
# Get Person Profile by ID
curl -X GET "http://localhost:8080/api/v1/person-profiles/1" -H "Authorization: Bearer $DRIVER_TOKEN"

# Get Person Profile by Email
curl -X GET "http://localhost:8080/api/v1/person-profiles?userEmail=driver1@safecar.com" -H "Authorization: Bearer $DRIVER_TOKEN"

# Get Business Profile by Email
curl -X GET "http://localhost:8080/api/v1/business-profiles?email=owner1@safecar.com" -H "Authorization: Bearer $OWNER_TOKEN"
```

#### 1.5. Update Profiles
```bash
# Update Person Profile
curl -X PUT "http://localhost:8080/api/v1/person-profiles/1" -H "Authorization: Bearer $DRIVER_TOKEN" -H 'Content-Type: application/json' -d '{ "fullName": "Juan Pérez Actualizado", "city": "Lima", "phone": "987654321", "country": "Peru", "dni": "12345678" }'

# Update Business Profile
curl -X PUT "http://localhost:8080/api/v1/business-profiles/1" -H "Authorization: Bearer $OWNER_TOKEN" -H 'Content-Type: application/json' -d '{ "businessName": "Taller El Buen Mecánico S.A.C.", "ruc": "20123456789", "businessAddress": "Av. Venezuela 789", "contactPhone": "987654321", "contactEmail": "contact@safecar.com" }'
```

#### 1.6. Register Vehicle
```bash
curl -X POST http://localhost:8080/api/v1/vehicles -H "Authorization: Bearer $DRIVER_TOKEN" -H 'Content-Type: application/json' -d '{ "driverId": 1, "licensePlate": "ABC-123", "brand": "Toyota", "model": "Corolla 2020" }'
```

---

### ⚙️ FLOW 2: Workshop Configuration

#### 2.1. Update Workshop Description
```bash
curl -X PATCH http://localhost:8080/api/v1/workshops/1 -H "Authorization: Bearer $OWNER_TOKEN" -H 'Content-Type: application/json' -d '{ "workshopDescription": "Specialized in electronics and general mechanics." }'
```

#### 2.2. Configure Mechanic Specializations
```bash
# Update Specializations and Experience
# Valid Specializations: GENERAL, ENGINE, BRAKES, TRANSMISSION, ELECTRICAL, SUSPENSION, AIR_CONDITIONING, BODYWORK, DIAGNOSTICS
curl -X PATCH http://localhost:8080/api/v1/mechanics/1 -H "Authorization: Bearer $OWNER_TOKEN" -H 'Content-Type: application/json' -d '{ "specializations": ["ELECTRICAL", "DIAGNOSTICS"], "yearsOfExperience": 5 }'
```

#### 2.3. Assign Mechanic to Workshop
```bash
curl -X PATCH http://localhost:8080/api/v1/mechanics/1/workshop -H "Authorization: Bearer $OWNER_TOKEN" -H 'Content-Type: application/json' -d '{ "workshopId": 1 }'
```

---

### 📅 FLOW 3: Appointments Lifecycle

#### 3.1. Create Appointment
```bash
# Create Appointment
# Valid Service Types: OIL_CHANGE, BRAKE_SERVICE, TIRE_SERVICE, ENGINE_DIAGNOSTICS, TRANSMISSION_SERVICE, ELECTRICAL_REPAIR, AIR_CONDITIONING, SUSPENSION_REPAIR, GENERAL_MAINTENANCE, CUSTOM
curl -X POST http://localhost:8080/api/v1/workshops/1/appointments -H "Authorization: Bearer $OWNER_TOKEN" -H 'Content-Type: application/json' -d '{ "workshopId": 1, "vehicleId": 1, "driverId": 1, "startAt": "2025-12-16T10:00:00Z", "endAt": "2025-12-20T11:30:00Z", "serviceType": "GENERAL_MAINTENANCE", "description": "Maintenance 10k" }'
```

#### 3.2. Reschedule Appointment
```bash
curl -X PATCH http://localhost:8080/api/v1/workshops/1/appointments/1 -H "Authorization: Bearer $OWNER_TOKEN" -H 'Content-Type: application/json' -d '{ "startAt": "2026-01-17T14:00:00Z", "endAt": "2026-01-20T15:30:00Z" }'
```

#### 3.3. Manage Status
```bash
# Confirm
curl -X PATCH http://localhost:8080/api/v1/workshops/1/appointments/1/status -H "Authorization: Bearer $OWNER_TOKEN" -H 'Content-Type: application/json' -d '{ "status": "CONFIRMED" }'

# Start
curl -X PATCH http://localhost:8080/api/v1/workshops/1/appointments/1/status -H "Authorization: Bearer $OWNER_TOKEN" -H 'Content-Type: application/json' -d '{ "status": "IN_PROGRESS" }'

# Complete
curl -X PATCH http://localhost:8080/api/v1/workshops/1/appointments/1/status -H "Authorization: Bearer $OWNER_TOKEN" -H 'Content-Type: application/json' -d '{ "status": "COMPLETED" }'
```

#### 3.4. Assign Mechanic
```bash
curl -X PATCH http://localhost:8080/api/v1/workshops/1/appointments/1/mechanics/2 -H "Authorization: Bearer $OWNER_TOKEN"
```

---

### 📡 FLOW 4: IoT Telemetry Processing

#### 4.1. Ingest Speed & Location
```bash
curl -X POST http://localhost:8080/api/v1/telemetry -H "Authorization: Bearer $DRIVER_TOKEN" -H 'Content-Type: application/json' -d '{ "vehicleId": 1, "driverId": 1, "type": "SPEED", "severity": "INFO", "timestamp": "2025-11-15T10:30:00Z", "speed": 65.5, "latitude": -12.0464, "longitude": -77.0428, "odometer": 10523.8 }'
```

#### 4.2. Ingest Sensor Data (New)
```bash
# 1. MH MQ Sensor (Gas/Smoke)
# Sends: cabinGasType, cabinGasConcentration
curl -X POST http://localhost:8080/api/v1/telemetry -H "Authorization: Bearer $DRIVER_TOKEN" -H 'Content-Type: application/json' -d '{ "vehicleId": 1, "driverId": 1, "cabinGasType": "SMOKE", "cabinGasConcentration": 450.5 }'

# 2. DHT11 Sensor (Cabin Temp & Humidity)
# Sends: cabinTemperature, cabinHumidity
curl -X POST http://localhost:8080/api/v1/telemetry -H "Authorization: Bearer $DRIVER_TOKEN" -H 'Content-Type: application/json' -d '{ "vehicleId": 1, "driverId": 1, "cabinTemperature": 24.5, "cabinHumidity": 60.0 }'

# 3. LM35 Sensor (Engine Temperature)
# Sends: engineTemperature
curl -X POST http://localhost:8080/api/v1/telemetry -H "Authorization: Bearer $DRIVER_TOKEN" -H 'Content-Type: application/json' -d '{ "vehicleId": 1, "driverId": 1, "engineTemperature": 95.2 }'

# 4. ACS712 Sensor (Electrical Current)
# Sends: electricalCurrent
curl -X POST http://localhost:8080/api/v1/telemetry -H "Authorization: Bearer $DRIVER_TOKEN" -H 'Content-Type: application/json' -d '{ "vehicleId": 1, "driverId": 1, "electricalCurrent": 12.5 }'
```

#### 4.3. Ingest Critical Diagnostic (DTC)
```bash
curl -X POST http://localhost:8080/api/v1/telemetry -H "Authorization: Bearer $MECHANIC_TOKEN" -H 'Content-Type: application/json' -d '{ "vehicleId": 1, "driverId": 1, "type": "DTC", "severity": "CRITICAL", "dtcCode": "P0301", "dtcStandard": "OBD2", "latitude": -12.0464, "longitude": -77.0428, "odometer": 10532.1 }'
```

---

### 🧠 FLOW 5: Vehicle Insights (AI)

#### 5.1. Generate Insight
```bash
curl -X POST http://localhost:8080/api/v1/insights/generate/1 -H "Authorization: Bearer $MECHANIC_TOKEN"
```

#### 5.2. Get Vehicle Insights
```bash
curl -X GET http://localhost:8080/api/v1/insights/vehicle/1 -H "Authorization: Bearer $OWNER_TOKEN"
```

---

### 💳 FLOW 6: Payments & Subscriptions

#### 6.1. Create Checkout Session
```bash
curl -X POST http://localhost:8080/api/v1/payments/checkout-session -H "Authorization: Bearer $OWNER_TOKEN" -H "X-User-Id: owner1@safecar.com" -H 'Content-Type: application/json' -d '{ "planType": "BASIC" }'
```

#### 6.2. Simulate Webhook (Stripe)
```bash
# Use Stripe CLI or Postman to POST to /webhooks/stripe
# Payload must match Stripe signature structure.
```

---

### 🔗 FLOW 7: Integration (Alert -> Appointment)

#### 5.1. Ingest Critical Telemetry (Trigger)
```bash
curl -X POST http://localhost:8080/api/v1/telemetry -H "Authorization: Bearer $DRIVER_TOKEN" -H 'Content-Type: application/json' -d '{ "vehicleId": 1, "driverId": 1, "type": "DTC", "severity": "CRITICAL", "timestamp": "2025-11-15T16:00:00Z", "dtcCode": "P0420", "dtcStandard": "OBD2", "latitude": -12.0464, "longitude": -77.0428, "odometer": 10545.3 }'
```

#### 5.2. Mechanic Checks Alerts
```bash
curl -X GET "http://localhost:8080/api/v1/telemetry/alerts?severity=CRITICAL&from=2025-11-15T00:00:00Z&to=2025-11-15T23:59:59Z" -H "Authorization: Bearer $MECHANIC_TOKEN"
```

#### 5.3. Create Urgent Appointment
```bash
curl -X POST http://localhost:8080/api/v1/workshops/1/appointments -H "Authorization: Bearer $OWNER_TOKEN" -H 'Content-Type: application/json' -d '{ "workshopId": 1, "vehicleId": 1, "driverId": 1, "startAt": "2025-11-16T08:00:00Z", "endAt": "2025-11-16T10:00:00Z", "description": "URGENTE: Falla crítica P0420 - Catalizador" }'
```

#### 5.4. Assign Mechanic
```bash
curl -X PATCH http://localhost:8080/api/v1/workshops/1/appointments/3/mechanics/2 -H "Authorization: Bearer $OWNER_TOKEN"
```

#### 5.5. Confirm & Start
```bash
curl -X PATCH http://localhost:8080/api/v1/workshops/1/appointments/3/status -H "Authorization: Bearer $OWNER_TOKEN" -H 'Content-Type: application/json' -d '{"status": "CONFIRMED"}'
curl -X PATCH http://localhost:8080/api/v1/workshops/1/appointments/3/status -H "Authorization: Bearer $OWNER_TOKEN" -H 'Content-Type: application/json' -d '{"status": "IN_PROGRESS"}'
```

#### 5.6. Add Notes & Complete
```bash
curl -X POST http://localhost:8080/api/v1/workshops/1/appointments/3/notes -H "Authorization: Bearer $MECHANIC_TOKEN" -H 'Content-Type: application/json' -d '{ "content": "Catalizador reemplazado.", "author": "Carlos Rodríguez" }'
curl -X PATCH http://localhost:8080/api/v1/workshops/1/appointments/3/status -H "Authorization: Bearer $OWNER_TOKEN" -H 'Content-Type: application/json' -d '{"status": "COMPLETED"}'
```

#### 5.7. Verify Resolution (Normal Telemetry)
```bash
curl -X POST http://localhost:8080/api/v1/telemetry -H "Authorization: Bearer $DRIVER_TOKEN" -H 'Content-Type: application/json' -d '{ "vehicleId": 1, "driverId": 1, "type": "SPEED", "severity": "INFO", "timestamp": "2025-11-16T11:00:00Z", "speed": 60.0, "latitude": -12.0464, "longitude": -77.0428, "odometer": 10555.0 }'
```

---

### 🚫 FLOW 8: Subscription Limits

1. **Subscribe to BASIC** (Limit: 3 mechanics).
2. **Add 3 Mechanics** (Success).
3. **Attempt to Add 4th Mechanic**:
   ```bash
   curl -X POST "http://localhost:8080/api/v1/person-profiles?userEmail=mechanic4@safecar.com" -H "Authorization: Bearer $MECHANIC4_TOKEN" ...
   # Expected: 403 Forbidden (Limit Exceeded)
   ```

---

### 🌐 FLOW 9: Frontend Integration (Appointments with Mechanic Assignment)

This flow demonstrates the complete integration between frontend and backend for appointment management with mechanic assignment.

#### 9.1. List All Appointments
```bash
# Get all appointments for a workshop
curl -X GET "http://localhost:8080/api/v1/workshops/1/appointments" -H "Authorization: Bearer $OWNER_TOKEN"
```

#### 9.2. Get Appointment Details
```bash
# Get specific appointment with full details
curl -X GET "http://localhost:8080/api/v1/workshops/1/appointments/1" -H "Authorization: Bearer $OWNER_TOKEN"
```

#### 9.3. Get Available Mechanics (with Profile Info)
```bash
# Step 1: Get mechanics from workshop
curl -X GET "http://localhost:8080/api/v1/workshops/1/mechanics" -H "Authorization: Bearer $OWNER_TOKEN"

# Step 2: Get mechanic profile details (for each mechanic)
curl -X GET "http://localhost:8080/api/v1/person-profiles/1" -H "Authorization: Bearer $OWNER_TOKEN"
```

#### 9.4. Confirm Appointment
```bash
# Change status from PENDING to CONFIRMED
curl -X PATCH "http://localhost:8080/api/v1/workshops/1/appointments/1/status" \
  -H "Authorization: Bearer $OWNER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"status": "CONFIRMED"}'
```

#### 9.5. Assign Mechanic to Appointment
```bash
# Assign mechanic (ID 1) to appointment (ID 1)
curl -X PATCH "http://localhost:8080/api/v1/workshops/1/appointments/1/mechanics/1" \
  -H "Authorization: Bearer $OWNER_TOKEN"
```

#### 9.6. Progress Through Appointment Lifecycle
```bash
# Start service
curl -X PATCH "http://localhost:8080/api/v1/workshops/1/appointments/1/status" \
  -H "Authorization: Bearer $OWNER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"status": "IN_PROGRESS"}'

# Complete service
curl -X PATCH "http://localhost:8080/api/v1/workshops/1/appointments/1/status" \
  -H "Authorization: Bearer $OWNER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"status": "COMPLETED"}'

# Cancel appointment (if needed)
curl -X PATCH "http://localhost:8080/api/v1/workshops/1/appointments/1/status" \
  -H "Authorization: Bearer $OWNER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"status": "CANCELLED"}'
```

#### 9.7. Verify Complete Appointment (with Mechanic Info)
```bash
# Get complete appointment with assigned mechanic
APPOINTMENT=$(curl -s -X GET "http://localhost:8080/api/v1/workshops/1/appointments/1" \
  -H "Authorization: Bearer $OWNER_TOKEN")

# Extract mechanicId and get mechanic profile
MECHANIC_PROFILE=$(curl -s -X GET "http://localhost:8080/api/v1/person-profiles/1" \
  -H "Authorization: Bearer $OWNER_TOKEN")

echo "Appointment: $APPOINTMENT"
echo "Mechanic Profile: $MECHANIC_PROFILE"
```

---

## 📚 API Reference

### Profiles Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/v1/person-profiles/{id}` | Get person profile by ID | ✅ |
| GET | `/api/v1/person-profiles?userEmail={email}` | Get person profile by email | ✅ |
| POST | `/api/v1/person-profiles?userEmail={email}` | Create person profile | ✅ |
| PUT | `/api/v1/person-profiles/{id}` | Update person profile | ✅ |
| GET | `/api/v1/business-profiles?email={email}` | Get business profile by email | ✅ |
| POST | `/api/v1/business-profiles?userEmail={email}` | Create business profile | ✅ |
| PUT | `/api/v1/business-profiles/{id}` | Update business profile | ✅ |

### Workshop & Mechanics Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/v1/workshops/{id}` | Get workshop by ID | ✅ |
| GET | `/api/v1/workshops/by-business-profile/{profileId}` | Get workshop by business profile | ✅ |
| PATCH | `/api/v1/workshops/{id}` | Update workshop description | ✅ |
| GET | `/api/v1/workshops/{workshopId}/mechanics` | Get all mechanics from workshop | ✅ |
| PATCH | `/api/v1/mechanics/{id}` | Update mechanic specializations | ✅ |
| PATCH | `/api/v1/mechanics/{id}/workshop` | Assign mechanic to workshop | ✅ |

### Appointments Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/v1/workshops/{workshopId}/appointments` | Get all appointments | ✅ |
| GET | `/api/v1/workshops/{workshopId}/appointments/{id}` | Get appointment by ID | ✅ |
| POST | `/api/v1/workshops/{workshopId}/appointments` | Create appointment | ✅ |
| PATCH | `/api/v1/workshops/{workshopId}/appointments/{id}` | Reschedule appointment | ✅ |
| PATCH | `/api/v1/workshops/{workshopId}/appointments/{id}/status` | Update appointment status | ✅ |
| PATCH | `/api/v1/workshops/{workshopId}/appointments/{id}/mechanics/{mechanicId}` | Assign mechanic | ✅ |
| POST | `/api/v1/workshops/{workshopId}/appointments/{id}/notes` | Add note to appointment | ✅ |

### Vehicles & Telemetry Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/v1/vehicles` | Register vehicle | ✅ |
| GET | `/api/v1/vehicles` | Get all vehicles | ✅ |
| POST | `/api/v1/telemetry` | Ingest telemetry data | ✅ |
| GET | `/api/v1/telemetry/alerts` | Get telemetry alerts | ✅ |

### Authentication Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/v1/authentication/sign-up` | Register new user | ❌ |
| POST | `/api/v1/authentication/sign-in` | Authenticate user | ❌ |

---

## 🔄 Appointment Status Transitions

```
PENDING → CONFIRMED → IN_PROGRESS → COMPLETED
   ↓          ↓            ↓
CANCELLED  CANCELLED   CANCELLED
```

### Valid Status Values
- `PENDING`: Initial state when appointment is created
- `CONFIRMED`: Workshop has accepted the appointment
- `IN_PROGRESS`: Service work has started
- `COMPLETED`: Service work is finished
- `CANCELLED`: Appointment was cancelled

---

## 🔧 Service Types

Valid values for `serviceType` field:
- `OIL_CHANGE`
- `BRAKE_SERVICE`
- `TIRE_SERVICE`
- `ENGINE_DIAGNOSTICS`
- `TRANSMISSION_SERVICE`
- `ELECTRICAL_REPAIR`
- `AIR_CONDITIONING`
- `SUSPENSION_REPAIR`
- `GENERAL_MAINTENANCE`
- `CUSTOM` (requires `customServiceDescription`)

---

## 👨‍🔧 Mechanic Specializations

Valid values for `specializations` field:
- `GENERAL`
- `ENGINE`
- `BRAKES`
- `TRANSMISSION`
- `ELECTRICAL`
- `SUSPENSION`
- `AIR_CONDITIONING`
- `BODYWORK`
- `DIAGNOSTICS`

---

## 📖 Additional Documentation

For detailed frontend integration guide, see: [INTEGRATION_GUIDE.md](../INTEGRATION_GUIDE.md)
