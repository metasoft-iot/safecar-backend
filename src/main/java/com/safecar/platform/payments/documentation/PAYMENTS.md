# 💳 PAYMENTS BOUNDED CONTEXT - GUÍA COMPLETA

## 📋 Índice
1. [Descripción General](#descripción-general)
2. [Arquitectura DDD](#arquitectura-ddd)
3. [Configuración Inicial](#configuración-inicial)
4. [Flujo End-to-End](#flujo-end-to-end)
5. [Endpoints Disponibles](#endpoints-disponibles)
6. [Testing Completo](#testing-completo)
7. [Integración con Stripe](#integración-con-stripe)
8. [Troubleshooting](#troubleshooting)

---

## 📖 Descripción General

El **Payments Bounded Context** gestiona las suscripciones de los Workshop Owners a través de Stripe. Permite a los dueños de talleres suscribirse a planes que determinan la cantidad de mecánicos que pueden agregar a su taller.

### 🎯 Planes Disponibles

| Plan | Precio Mensual | Límite Mecánicos | Stripe Price ID |
|------|----------------|------------------|-----------------|
| **BASIC** | S/. 29 | 3 mecánicos | `price_1SQbsT3l890Fc29CerlSwh4r` |
| **PROFESSIONAL** | S/. 99 | 10 mecánicos | `price_1SQbt23l890Fc29CqoqLYCnu` |
| **PREMIUM** | S/. 299 | 30 mecánicos | `price_1SQbtK3l890Fc29COSEZ6iK4` |

### 🔄 Flujo de Negocio

```
Workshop Owner → Selecciona Plan → Crea Checkout Session → 
Paga en Stripe → Webhook persiste Subscription → 
Plan activo con límites aplicados
```

---

## 🏗️ Arquitectura DDD

### Estructura de Carpetas

```
payments/
├── domain/
│   ├── model/
│   │   ├── aggregates/
│   │   │   └── Subscription.java          // Aggregate Root
│   │   ├── commands/
│   │   │   ├── CreateCheckoutSessionCommand.java
│   │   │   └── CreateSubscriptionCommand.java
│   │   ├── queries/
│   │   │   └── GetSubscriptionByUserIdQuery.java
│   │   └── valueobjects/
│   │       └── PlanType.java               // Enum (BASIC/PROFESSIONAL/PREMIUM)
│   └── services/
│       ├── PaymentCommandService.java      // Interface
│       └── PaymentQueryService.java        // Interface
├── application/
│   └── internal/
│       ├── commandservices/
│       │   └── PaymentCommandServiceImpl.java
│       └── queryservices/
│           └── PaymentQueryServiceImpl.java
├── infrastructure/
│   ├── external/
│   │   └── StripePaymentGateway.java       // Stripe SDK
│   └── persistence/jpa/repositories/
│       └── SubscriptionRepository.java     // JPA Repository
└── interfaces/rest/
    ├── PaymentsController.java             // 3 endpoints
    ├── StripeWebhooksController.java       // 1 webhook
    ├── resources/                          // DTOs
    │   ├── CreateCheckoutSessionResource.java
    │   ├── CheckoutSessionResource.java
    │   └── SubscriptionResource.java
    └── transform/                          // Assemblers
        ├── CreateCheckoutSessionCommandFromResourceAssembler.java
        ├── CheckoutSessionResourceFromSessionIdAssembler.java
        └── SubscriptionResourceFromAggregateAssembler.java
```

### Componentes Clave

#### 1. Subscription (Aggregate Root)

```java
@Entity
@Table(name = "subscriptions")
public class Subscription extends AuditableAbstractAggregateRoot<Subscription> {
    private String userId;                    // Workshop Owner email
    private PlanType planType;                // BASIC/PROFESSIONAL/PREMIUM
    private String status;                    // ACTIVE/CANCELLED/EXPIRED
    private String stripeSubscriptionId;      // Referencia a Stripe
    
    // Domain methods
    public void cancel() { this.status = "CANCELLED"; }
    public void activate() { this.status = "ACTIVE"; }
    public int getMechanicsLimit() { return this.planType.getMechanicsLimit(); }
}
```

#### 2. PlanType (Value Object)

```java
public enum PlanType {
    BASIC("price_1SQbsT3l890Fc29CerlSwh4r", 3),
    PROFESSIONAL("price_1SQbt23l890Fc29CqoqLYCnu", 10),
    PREMIUM("price_1SQbtK3l890Fc29COSEZ6iK4", 30);
    
    private final String stripePriceId;
    private final int mechanicsLimit;
}
```

#### 3. Endpoints REST

| Endpoint | Método | Descripción | Autenticación |
|----------|--------|-------------|---------------|
| `/api/v1/payments/debug` | GET | Verificar sistema | No requerida |
| `/api/v1/payments/test-session` | POST | Crear sesión de prueba | No requerida |
| `/api/v1/payments/checkout-session` | POST | Crear sesión real | JWT + X-User-Id |
| `/webhooks/stripe` | POST | Recibir eventos Stripe | Stripe-Signature |

---

## ⚙️ Configuración Inicial

### 1. Variables de Entorno

**Crear archivo `.env` en la raíz del proyecto:**

```bash
# Database
MYSQL_ROOT_USER=root
MYSQL_ROOT_PASSWORD=admin

# JWT
JWT_SECRET=my-super-secret-key-for-jwt-tokens-minimum-256-bits

# Stripe API (obtener desde: https://dashboard.stripe.com/test/apikeys)
STRIPE_SECRET_KEY=sk_test_your_secret_key_here
STRIPE_WEBHOOK_SECRET=whsec_test_your_webhook_secret_here

# Frontend URL
FRONTEND_URL=http://localhost:4200
```

### 2. Configurar Stripe Webhook (Desarrollo Local)

**Opción A: Usar Stripe CLI (Recomendado)**

```bash
# 1. Instalar Stripe CLI
brew install stripe/stripe-cli/stripe

# 2. Login a Stripe
stripe login

# 3. Escuchar webhooks y reenviar a localhost
stripe listen --forward-to localhost:8080/webhooks/stripe

# Output esperado:
# > Ready! Your webhook signing secret is whsec_xxxxx
# Copiar este secret a STRIPE_WEBHOOK_SECRET
```

**Opción B: Usar ngrok (Alternativa)**

```bash
# 1. Instalar ngrok
brew install ngrok

# 2. Exponer puerto 8080
ngrok http 8080

# 3. Copiar URL pública (ej: https://abc123.ngrok.io)

# 4. Configurar webhook en Stripe Dashboard:
# - URL: https://abc123.ngrok.io/webhooks/stripe
# - Evento: customer.subscription.created
# - Copiar webhook secret a STRIPE_WEBHOOK_SECRET
```

### 3. Obtener Stripe API Keys

1. **Crear cuenta Stripe**: https://dashboard.stripe.com/register
2. **Modo Test**: Activar "Test mode" (toggle superior derecho)
3. **API Keys**: Navegar a Developers → API keys
   - **Secret Key**: `sk_test_51...` → Variable `STRIPE_SECRET_KEY`
4. **Webhook Secret**: 
   - Si usas Stripe CLI: Copiar de la terminal
   - Si usas Dashboard: Developers → Webhooks → Endpoint → Signing secret

### 4. Verificar Configuración

```bash
# 1. Levantar MySQL
docker run -d \
  --name safecar-mysql \
  -e MYSQL_ROOT_PASSWORD=admin \
  -e MYSQL_DATABASE=safecar-db \
  -p 3306:3306 \
  mysql:8.0

# 2. Exportar variables de entorno
export MYSQL_ROOT_USER=root
export MYSQL_ROOT_PASSWORD=admin
export JWT_SECRET=my-super-secret-key-for-jwt-tokens-minimum-256-bits
export STRIPE_SECRET_KEY=sk_test_your_key
export STRIPE_WEBHOOK_SECRET=whsec_test_your_secret
export FRONTEND_URL=http://localhost:4200

# 3. Compilar y ejecutar
./mvnw clean spring-boot:run

# 4. Verificar logs
# Buscar: "PAYMENT COMMAND SERVICE INITIALIZED"
```

---

## 🔄 Flujo End-to-End

### Diagrama de Secuencia

```
┌─────────┐       ┌──────────┐       ┌────────┐       ┌────────────┐
│Workshop │       │ Backend  │       │ Stripe │       │  Database  │
│  Owner  │       │  (API)   │       │  API   │       │  (MySQL)   │
└────┬────┘       └────┬─────┘       └───┬────┘       └─────┬──────┘
     │                 │                  │                  │
     │ 1. Sign Up      │                  │                  │
     ├────────────────>│                  │                  │
     │                 │                  │                  │
     │ 2. Sign In      │                  │                  │
     ├────────────────>│                  │                  │
     │<────────────────┤ JWT Token        │                  │
     │                 │                  │                  │
     │ 3. Create       │                  │                  │
     │   Checkout      │                  │                  │
     │   Session       │ 4. Create        │                  │
     ├────────────────>│   Session        │                  │
     │                 ├─────────────────>│                  │
     │                 │<─────────────────┤ sessionId        │
     │<────────────────┤ sessionId        │                  │
     │                 │                  │                  │
     │ 5. Redirect to  │                  │                  │
     │    Stripe       │                  │                  │
     ├─────────────────┼─────────────────>│                  │
     │                 │                  │ 6. Payment Page  │
     │<─────────────────────────────────────────────────────┤
     │                 │                  │                  │
     │ 7. Complete     │                  │                  │
     │    Payment      │                  │                  │
     ├─────────────────┼─────────────────>│                  │
     │                 │                  │                  │
     │                 │ 8. Webhook:      │                  │
     │                 │    subscription  │                  │
     │                 │<─────────────────┤ created          │
     │                 │                  │                  │
     │                 │ 9. Persist       │                  │
     │                 │    Subscription  │                  │
     │                 ├──────────────────┼─────────────────>│
     │                 │                  │                  │
     │ 10. Success     │                  │                  │
     │    Redirect     │                  │                  │
     │<────────────────┤                  │                  │
     │                 │                  │                  │
```

---

## 🎯 Endpoints Disponibles

### 1. Debug Endpoint (Verificación del Sistema)

**Sin autenticación requerida**

```bash
GET /api/v1/payments/debug
```

**Propósito**: Verificar que el Payment Controller está funcionando correctamente.

**Request:**
```bash
curl -X GET http://localhost:8080/api/v1/payments/debug
```

**Response:**
```json
{
  "status": "Payment controller is working",
  "timestamp": "2025-11-12T10:00:00",
  "testUserId": "31303200000000000000000000000000",
  "availablePlans": ["BASIC", "PROFESSIONAL", "PREMIUM"],
  "testResponse": {
    "sessionId": "debug-session-123",
    "class": "CheckoutSessionResource"
  }
}
```

**Casos de uso:**
- ✅ Verificar que el servicio de pagos está levantado
- ✅ Confirmar planes disponibles
- ✅ Testing inicial antes de integración

---

### 2. Test Session (Sesión de Prueba)

**Sin autenticación requerida**

```bash
POST /api/v1/payments/test-session
```

**Propósito**: Crear una sesión de Stripe sin requerir autenticación. Útil para testing rápido.

**Request:**
```bash
curl -X POST http://localhost:8080/api/v1/payments/test-session \
  -H 'Content-Type: application/json'
```

**Response:**
```text
Session created: cs_test_a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0
```

**Comportamiento interno:**
- Crea checkout session con `userId = "test-user-123"`
- Plan por defecto: `BASIC`
- Llama a Stripe API real

**⚠️ IMPORTANTE**: Este endpoint debe deshabilitarse en producción usando Spring Profiles:
```java
@Profile("!prod")
@PostMapping("/test-session")
```

---

### 3. Create Checkout Session (Sesión Real)

**Autenticación requerida: JWT Token + X-User-Id header**

```bash
POST /api/v1/payments/checkout-session
```

**Propósito**: Crear una sesión de Stripe Checkout para que el usuario realice el pago.

**Request Headers:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
X-User-Id: owner@safecar.com
Content-Type: application/json
```

**Request Body:**
```json
{
  "planType": "PROFESSIONAL"
}
```

**Validaciones:**
- `planType` es requerido (`@NotBlank`)
- `planType` debe ser: `BASIC`, `PROFESSIONAL` o `PREMIUM` (`@Pattern`)

**Request completo:**
```bash
curl -X POST http://localhost:8080/api/v1/payments/checkout-session \
  -H "Authorization: Bearer $TOKEN" \
  -H "X-User-Id: owner@safecar.com" \
  -H 'Content-Type: application/json' \
  -d '{
    "planType": "PROFESSIONAL"
  }'
```

**Response:**
```json
{
  "sessionId": "cs_test_a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0"
}
```

**Flujo interno:**
1. Valida `@Valid CreateCheckoutSessionResource`
2. Assembler → `CreateCheckoutSessionCommand(userId, PlanType)`
3. `PaymentCommandService.handle(command)`
4. `StripePaymentGateway.createCheckoutSession(userId, planType)`
5. Stripe API crea session con:
   - Mode: `SUBSCRIPTION`
   - Metadata: `{ user_id, plan_type }`
   - Price ID del plan seleccionado
   - Success URL: `{FRONTEND_URL}/payment/success?session_id={CHECKOUT_SESSION_ID}`
   - Cancel URL: `{FRONTEND_URL}/payment/cancel`
6. Retorna `sessionId`

**Frontend debe:**
1. Recibir `sessionId`
2. Redirigir usuario a: `https://checkout.stripe.com/c/pay/{sessionId}`
3. Usuario completa pago en Stripe
4. Stripe redirige a `success_url` o `cancel_url`

---

### 4. Stripe Webhook (Evento de Suscripción)

**Autenticación: Stripe-Signature header (HMAC SHA-256)**

```bash
POST /webhooks/stripe
```

**Propósito**: Recibir notificaciones de Stripe cuando ocurren eventos (ej: subscription creada).

**⚠️ IMPORTANTE**: Este endpoint es llamado **automáticamente por Stripe**, NO manualmente.

**Request Headers:**
```
Stripe-Signature: t=1234567890,v1=abcdef1234567890...
Content-Type: application/json
```

**Request Body (Ejemplo):**
```json
{
  "id": "evt_1234567890",
  "type": "customer.subscription.created",
  "data": {
    "object": {
      "id": "sub_1SQbsT3l890Fc29CerlSwh4r",
      "customer": "cus_abc123",
      "status": "active",
      "metadata": {
        "user_id": "owner@safecar.com",
        "plan_type": "PROFESSIONAL"
      }
    }
  }
}
```

**Flujo interno:**
1. Verifica firma HMAC con `webhookSecret`
2. Si firma inválida → `400 Bad Request: "Invalid signature"`
3. Parsea evento: `Webhook.constructEvent(payload, signature, secret)`
4. Si evento es `customer.subscription.created`:
   - Extrae `userId` de metadata
   - Extrae `planType` de metadata (fallback: "BASIC")
   - Crea `CreateSubscriptionCommand(userId, stripeSubscriptionId, planType)`
   - `PaymentCommandService.handle(command)` [@Transactional]
   - Persiste `Subscription` en base de datos
5. Retorna `200 OK` a Stripe

**Response:**
- `200 OK` (sin body) → Stripe marca evento como procesado
- `400 Bad Request` → Stripe reintenta (hasta 3 días)

**Testing del Webhook:**

**Opción 1: Stripe CLI (Recomendado)**
```bash
# Terminal 1: Escuchar eventos
stripe listen --forward-to localhost:8080/webhooks/stripe

# Terminal 2: Trigger evento de prueba
stripe trigger customer.subscription.created \
  --add customer:metadata.user_id=owner@safecar.com \
  --add customer:metadata.plan_type=PROFESSIONAL
```

**Opción 2: Simular con curl (Solo para testing)**
```bash
# ⚠️ Esta request fallará con "Invalid signature" porque no tiene firma real de Stripe
curl -X POST http://localhost:8080/webhooks/stripe \
  -H 'Stripe-Signature: t=fake,v1=fake' \
  -H 'Content-Type: application/json' \
  -d '{
    "type": "customer.subscription.created",
    "data": {
      "object": {
        "id": "sub_test123",
        "metadata": {
          "user_id": "owner@safecar.com",
          "plan_type": "BASIC"
        }
      }
    }
  }'
```

---

## 🧪 Testing Completo

### Prerrequisitos

Asegúrate de tener:
- ✅ MySQL corriendo en `localhost:3306`
- ✅ Variables de entorno configuradas
- ✅ Backend corriendo en `localhost:8080`
- ✅ Stripe CLI escuchando webhooks (para testing local)

### Test 1: Verificar Sistema

```bash
# 1. Health check
curl http://localhost:8080/actuator/health

# Esperado: {"status":"UP"}

# 2. Debug endpoint
curl http://localhost:8080/api/v1/payments/debug

# Esperado: JSON con "status": "Payment controller is working"
```

### Test 2: Flujo Completo con Usuario Real

#### Paso 1: Registrar Workshop Owner

```bash
# Sign Up
curl -X POST http://localhost:8080/api/v1/authentication/sign-up \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "owner@safecar.com",
    "password": "password123",
    "roles": ["ROLE_WORKSHOP"]
  }'

# Respuesta esperada:
# {
#   "id": 1,
#   "email": "owner@safecar.com",
#   "roles": ["ROLE_WORKSHOP"]
# }
```

#### Paso 2: Autenticar y Obtener Token

```bash
# Sign In
TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/authentication/sign-in \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "owner@safecar.com",
    "password": "password123"
  }' | jq -r '.token')

echo "Token obtenido: $TOKEN"
```

#### Paso 3: Crear Checkout Session

```bash
# Crear sesión para plan PROFESSIONAL
curl -X POST http://localhost:8080/api/v1/payments/checkout-session \
  -H "Authorization: Bearer $TOKEN" \
  -H "X-User-Id: owner@safecar.com" \
  -H 'Content-Type: application/json' \
  -d '{
    "planType": "PROFESSIONAL"
  }'

# Respuesta esperada:
# {
#   "sessionId": "cs_test_a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0"
# }
```

#### Paso 4: Simular Pago (con Stripe CLI)

```bash
# En otra terminal, asegúrate de tener Stripe CLI escuchando:
# stripe listen --forward-to localhost:8080/webhooks/stripe

# Trigger evento de subscription creada
stripe trigger customer.subscription.created \
  --add subscription:metadata.user_id=owner@safecar.com \
  --add subscription:metadata.plan_type=PROFESSIONAL
```

#### Paso 5: Verificar Subscription en Base de Datos

```bash
# Conectar a MySQL
mysql -u root -p -h localhost safecar-db

# Consultar subscriptions
SELECT * FROM subscriptions WHERE user_id = 'owner@safecar.com';

# Resultado esperado:
# +----+-------------------+---------------+--------+---------------------------+
# | id | user_id           | plan_type     | status | stripe_subscription_id    |
# +----+-------------------+---------------+--------+---------------------------+
# |  1 | owner@safecar.com | PROFESSIONAL  | ACTIVE | sub_1SQbsT3l890Fc29Cerl.. |
# +----+-------------------+---------------+--------+---------------------------+
```

### Test 3: Validaciones de Errores

#### Test 3.1: Plan Type Inválido

```bash
curl -X POST http://localhost:8080/api/v1/payments/checkout-session \
  -H "Authorization: Bearer $TOKEN" \
  -H "X-User-Id: owner@safecar.com" \
  -H 'Content-Type: application/json' \
  -d '{
    "planType": "INVALID_PLAN"
  }'

# Respuesta esperada: 400 Bad Request
# {
#   "message": "Plan type must be BASIC, PROFESSIONAL, or PREMIUM"
# }
```

#### Test 3.2: Sin Autenticación

```bash
curl -X POST http://localhost:8080/api/v1/payments/checkout-session \
  -H "X-User-Id: owner@safecar.com" \
  -H 'Content-Type: application/json' \
  -d '{
    "planType": "BASIC"
  }'

# Respuesta esperada: 401 Unauthorized
```

#### Test 3.3: Stripe API Key Inválida

```bash
# Detener el backend
# Cambiar STRIPE_SECRET_KEY a un valor inválido
export STRIPE_SECRET_KEY=sk_test_invalid_key

# Reiniciar backend
./mvnw spring-boot:run

# Intentar crear checkout session
curl -X POST http://localhost:8080/api/v1/payments/checkout-session \
  -H "Authorization: Bearer $TOKEN" \
  -H "X-User-Id: owner@safecar.com" \
  -H 'Content-Type: application/json' \
  -d '{
    "planType": "BASIC"
  }'

# Respuesta esperada: 500 Internal Server Error
# {
#   "error": "Failed to create checkout session: Invalid API Key provided"
# }
```

### Test 4: Diferentes Planes

```bash
# Plan BASIC (3 mecánicos)
curl -X POST http://localhost:8080/api/v1/payments/checkout-session \
  -H "Authorization: Bearer $TOKEN" \
  -H "X-User-Id: owner@safecar.com" \
  -H 'Content-Type: application/json' \
  -d '{"planType": "BASIC"}'

# Plan PROFESSIONAL (10 mecánicos)
curl -X POST http://localhost:8080/api/v1/payments/checkout-session \
  -H "Authorization: Bearer $TOKEN" \
  -H "X-User-Id: owner@safecar.com" \
  -H 'Content-Type: application/json' \
  -d '{"planType": "PROFESSIONAL"}'

# Plan PREMIUM (30 mecánicos)
curl -X POST http://localhost:8080/api/v1/payments/checkout-session \
  -H "Authorization: Bearer $TOKEN" \
  -H "X-User-Id: owner@safecar.com" \
  -H 'Content-Type: application/json' \
  -d '{"planType": "PREMIUM"}'
```

### Test 5: Test Session Endpoint (Sin Auth)

```bash
curl -X POST http://localhost:8080/api/v1/payments/test-session

# Respuesta esperada:
# "Session created: cs_test_xxxxxxxxxxxxxxxxx"

# Este endpoint es útil para:
# - Testing rápido sin necesidad de autenticación
# - Verificar conectividad con Stripe API
# - Debugging de integración Stripe
```

---

## 🔗 Integración con Stripe

### Flujo Detallado de Datos

#### 1. Creación de Checkout Session

**Backend → Stripe API:**
```http
POST https://api.stripe.com/v1/checkout/sessions
Authorization: Bearer sk_test_your_secret_key

{
  "mode": "subscription",
  "metadata": {
    "user_id": "owner@safecar.com",
    "plan_type": "PROFESSIONAL"
  },
  "line_items": [{
    "price": "price_1SQbt23l890Fc29CqoqLYCnu",
    "quantity": 1
  }],
  "success_url": "http://localhost:4200/payment/success?session_id={CHECKOUT_SESSION_ID}",
  "cancel_url": "http://localhost:4200/payment/cancel"
}
```

**Stripe API → Backend:**
```json
{
  "id": "cs_test_a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0",
  "url": "https://checkout.stripe.com/c/pay/cs_test_a1b2...",
  "metadata": {
    "user_id": "owner@safecar.com",
    "plan_type": "PROFESSIONAL"
  }
}
```

#### 2. Usuario Completa Pago

**Frontend redirige a Stripe Checkout:**
```
https://checkout.stripe.com/c/pay/cs_test_a1b2c3d4e5f6g7h8i9j0...
```

**Usuario ingresa datos de tarjeta de prueba:**
- Tarjeta: `4242 4242 4242 4242`
- Fecha: Cualquier fecha futura (ej: `12/34`)
- CVC: Cualquier 3 dígitos (ej: `123`)
- ZIP: Cualquier código (ej: `12345`)

**Stripe procesa pago y crea subscription:**
```json
{
  "id": "sub_1SQbsT3l890Fc29CerlSwh4r",
  "status": "active",
  "customer": "cus_abc123",
  "metadata": {
    "user_id": "owner@safecar.com",
    "plan_type": "PROFESSIONAL"
  }
}
```

#### 3. Stripe Envía Webhook

**Stripe → Backend:**
```http
POST http://localhost:8080/webhooks/stripe
Stripe-Signature: t=1234567890,v1=abcdef1234567890...

{
  "type": "customer.subscription.created",
  "data": {
    "object": {
      "id": "sub_1SQbsT3l890Fc29CerlSwh4r",
      "status": "active",
      "metadata": {
        "user_id": "owner@safecar.com",
        "plan_type": "PROFESSIONAL"
      }
    }
  }
}
```

**Backend procesa y responde:**
```http
HTTP/1.1 200 OK
```

#### 4. Persistencia en Base de Datos

**SQL ejecutado:**
```sql
INSERT INTO subscriptions 
  (user_id, plan_type, status, stripe_subscription_id, created_at, updated_at)
VALUES 
  ('owner@safecar.com', 'PROFESSIONAL', 'ACTIVE', 'sub_1SQbsT3l890Fc29CerlSwh4r', NOW(), NOW());
```

### Configuración de Price IDs en Stripe

Los Price IDs están hardcodeados en `PlanType.java` y deben coincidir con los productos creados en Stripe Dashboard.

**Crear productos en Stripe:**

1. Navegar a: https://dashboard.stripe.com/test/products
2. Crear 3 productos:

**Producto 1: SafeCar BASIC**
- Name: `SafeCar BASIC`
- Pricing: Recurring, Monthly
- Price: `29.00 PEN` (Soles Peruanos)
- Price ID generado: `price_1SQbsT3l890Fc29CerlSwh4r`

**Producto 2: SafeCar PROFESSIONAL**
- Name: `SafeCar PROFESSIONAL`
- Pricing: Recurring, Monthly
- Price: `99.00 PEN`
- Price ID generado: `price_1SQbt23l890Fc29CqoqLYCnu`

**Producto 3: SafeCar PREMIUM**
- Name: `SafeCar PREMIUM`
- Pricing: Recurring, Monthly
- Price: `299.00 PEN`
- Price ID generado: `price_1SQbtK3l890Fc29COSEZ6iK4`

**⚠️ IMPORTANTE**: Si cambias los Price IDs en Stripe, debes actualizarlos en:
- `PlanType.java` (hardcoded)
- `application.properties` (opcional, no se usa actualmente)

### Manejo de Webhooks

**Eventos soportados actualmente:**
- ✅ `customer.subscription.created` - Implementado

**Eventos pendientes de implementar:**
- ⚠️ `customer.subscription.updated` - Para cambios de plan
- ⚠️ `customer.subscription.deleted` - Para cancelaciones/expiraciones
- ⚠️ `invoice.payment_failed` - Para pagos fallidos

**Idempotencia de Webhooks:**

Stripe puede enviar el mismo evento múltiples veces. Para evitar duplicados:

```java
// TODO: Implementar verificación de idempotencia
Optional<Subscription> existing = subscriptionRepository
    .findByStripeSubscriptionId(command.stripeSubscriptionId());

if (existing.isPresent()) {
    log.warn("Subscription already exists for stripeSubscriptionId: {}", 
        command.stripeSubscriptionId());
    return existing; // Retornar existente en vez de crear duplicado
}
```

---

## 🐛 Troubleshooting

### Problema 1: "Invalid signature" en Webhook

**Error:**
```
400 Bad Request: "Invalid signature"
```

**Causas:**
1. `STRIPE_WEBHOOK_SECRET` incorrecto
2. Webhook no viene de Stripe (simulación con curl)
3. Payload modificado en tránsito

**Solución:**
```bash
# Verificar webhook secret
stripe listen --print-secret

# Output:
# whsec_abc123def456...

# Copiar el secret exacto a .env
export STRIPE_WEBHOOK_SECRET=whsec_abc123def456...

# Reiniciar backend
./mvnw spring-boot:run
```

### Problema 2: "Failed to create checkout session"

**Error:**
```json
{
  "error": "Failed to create checkout session: Invalid API Key provided"
}
```

**Causas:**
1. `STRIPE_SECRET_KEY` inválida o expirada
2. Usando key de producción en vez de test
3. Key no exportada correctamente

**Solución:**
```bash
# Verificar que la key es de test (comienza con sk_test_)
echo $STRIPE_SECRET_KEY
# Debe ser: sk_test_51...

# Si es sk_live_..., cambiar a test mode en Stripe Dashboard

# Obtener nueva test key:
# https://dashboard.stripe.com/test/apikeys

# Exportar correctamente
export STRIPE_SECRET_KEY=sk_test_your_new_key_here

# Reiniciar backend
```

### Problema 3: Subscription no se crea en Base de Datos

**Síntoma:**
- Webhook llega (200 OK)
- Pero no hay registro en tabla `subscriptions`

**Debug:**
```bash
# 1. Verificar logs del backend
# Buscar: "Creating subscription for userId:"
# Si no aparece, metadata no está llegando

# 2. Verificar metadata en Stripe Dashboard
# Event → Event data → data.object.metadata
# Debe contener: user_id y plan_type

# 3. Verificar que CreateSubscriptionCommand se está creando
# Log debe mostrar: "Subscription created successfully with ID: X"

# 4. Verificar permisos MySQL
mysql -u root -p -h localhost safecar-db
SHOW GRANTS FOR 'root'@'localhost';

# 5. Verificar que tabla existe
SHOW TABLES LIKE 'subscriptions';

# 6. Verificar estructura de tabla
DESCRIBE subscriptions;
```

**Solución:**
```bash
# Si tabla no existe, recrearla:
CREATE TABLE subscriptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    plan_type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    stripe_subscription_id VARCHAR(255) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_stripe_subscription_id (stripe_subscription_id)
);
```

### Problema 4: "401 Unauthorized" en /checkout-session

**Error:**
```
401 Unauthorized
```

**Causas:**
1. Token JWT no enviado
2. Token expirado (> 7 días)
3. Header `Authorization` mal formado
4. Header `X-User-Id` faltante

**Solución:**
```bash
# 1. Verificar que el token se obtiene correctamente
TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/authentication/sign-in \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "owner@safecar.com",
    "password": "password123"
  }' | jq -r '.token')

# 2. Verificar que el token NO es null
echo "Token: $TOKEN"
# Si es "null", el login falló

# 3. Usar formato correcto en request
curl -X POST http://localhost:8080/api/v1/payments/checkout-session \
  -H "Authorization: Bearer $TOKEN" \
  -H "X-User-Id: owner@safecar.com" \
  -H 'Content-Type: application/json' \
  -d '{"planType": "BASIC"}'

# Nota: 
# - "Bearer" es requerido (con espacio después)
# - X-User-Id debe ser el email del usuario autenticado
```

### Problema 5: Stripe CLI no Reenvía Webhooks

**Síntoma:**
```bash
stripe listen --forward-to localhost:8080/webhooks/stripe
# Output: Ready! ...
# Pero eventos no llegan al backend
```

**Causas:**
1. Backend no está corriendo en puerto 8080
2. Firewall bloqueando conexión
3. URL incorrecta en `--forward-to`

**Solución:**
```bash
# 1. Verificar que backend está corriendo
curl http://localhost:8080/actuator/health

# 2. Verificar puerto correcto
lsof -i :8080
# Debe mostrar proceso Java

# 3. Usar URL completa con http://
stripe listen --forward-to http://localhost:8080/webhooks/stripe

# 4. Probar trigger manual
stripe trigger customer.subscription.created

# 5. Verificar logs del backend
# Debe aparecer: POST "/webhooks/stripe" con status 200
```

### Problema 6: Frontend no Redirige Correctamente

**Síntoma:**
- sessionId se obtiene correctamente
- Pero usuario no es redirigido a Stripe

**Debug:**
```javascript
// Frontend (JavaScript/TypeScript)
const response = await fetch('http://localhost:8080/api/v1/payments/checkout-session', {
  method: 'POST',
  headers: {
    'Authorization': `Bearer ${token}`,
    'X-User-Id': userEmail,
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({ planType: 'PROFESSIONAL' })
});

const data = await response.json();
console.log('Session ID:', data.sessionId);

// Redirigir a Stripe Checkout
window.location.href = `https://checkout.stripe.com/c/pay/${data.sessionId}`;
```

**Alternativa con Stripe.js:**
```javascript
// Opción recomendada: Usar Stripe.js
import { loadStripe } from '@stripe/stripe-js';

const stripe = await loadStripe('pk_test_your_publishable_key');

const { sessionId } = await response.json();

// Redirige automáticamente
await stripe.redirectToCheckout({ sessionId });
```

### Problema 7: Logs para Debugging

**Activar logs detallados:**

```properties
# application.properties
logging.level.com.safecar.platform.payments=DEBUG
logging.level.com.stripe=DEBUG
```

**Logs importantes a buscar:**

```
# Inicialización correcta
=== PAYMENT COMMAND SERVICE INITIALIZED ===

# Checkout session creando
Creating checkout session for userId: owner@safecar.com and plan: PROFESSIONAL
Checkout session created successfully: cs_test_...

# Webhook recibido
POST "/webhooks/stripe" - 200 OK

# Subscription creada
Creating subscription for userId: owner@safecar.com with Stripe ID: sub_...
Subscription created successfully with ID: 1
```

**Si no ves estos logs:**
- ✅ Verificar que `@Slf4j` está en las clases
- ✅ Verificar que Lombok está instalado
- ✅ Reiniciar IDE si es necesario

---

## 📊 Tabla de Endpoints Completa

| Endpoint | Método | Auth | Request Body | Response | Propósito |
|----------|--------|------|--------------|----------|-----------|
| `/api/v1/payments/debug` | GET | ❌ No | - | JSON debug info | Verificar sistema |
| `/api/v1/payments/test-session` | POST | ❌ No | - | String sessionId | Testing rápido |
| `/api/v1/payments/checkout-session` | POST | ✅ JWT + Header | `CreateCheckoutSessionResource` | `CheckoutSessionResource` | Crear sesión real |
| `/webhooks/stripe` | POST | ✅ Stripe-Signature | Evento Stripe | 200 OK / 400 | Recibir eventos |

---

## 🔒 Seguridad y Mejores Prácticas

### 1. Validación de X-User-Id

**Problema actual:**
El header `X-User-Id` no se valida contra el JWT token. Un usuario malicioso podría crear checkout sessions para otros usuarios.

**Solución recomendada:**
```java
// En PaymentsController.createCheckoutSession()
@PostMapping("/checkout-session")
public ResponseEntity<CheckoutSessionResource> createCheckoutSession(
        @Valid @RequestBody CreateCheckoutSessionResource resource,
        @RequestHeader("X-User-Id") String userId,
        Authentication authentication) {
    
    // Obtener email del JWT token
    String authenticatedEmail = authentication.getName();
    
    // Validar que coincida con X-User-Id
    if (!userId.equals(authenticatedEmail)) {
        throw new UnauthorizedException("X-User-Id does not match authenticated user");
    }
    
    // Continuar con flujo normal...
}
```

### 2. Rate Limiting

**Implementar rate limiting para prevenir abuse:**

```xml
<!-- pom.xml -->
<dependency>
    <groupId>com.bucket4j</groupId>
    <artifactId>bucket4j-core</artifactId>
    <version>8.1.0</version>
</dependency>
```

```java
// PaymentsController
private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

private Bucket resolveBucket(String userId) {
    return cache.computeIfAbsent(userId, k -> {
        Bandwidth limit = Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    });
}

@PostMapping("/checkout-session")
public ResponseEntity<?> createCheckoutSession(...) {
    Bucket bucket = resolveBucket(userId);
    if (!bucket.tryConsume(1)) {
        return ResponseEntity.status(429).body("Too many requests");
    }
    // ... resto del código
}
```

### 3. Deshabilitar /test-session en Producción

```java
@Profile("!prod")
@PostMapping("/test-session")
public ResponseEntity<String> testSession() {
    // ... código
}
```

### 4. Logging Seguro

**NO loggear información sensible:**

```java
// ❌ MAL
log.info("Stripe API Key: {}", stripeSecretKey);

// ✅ BIEN
log.info("Stripe API configured with key starting with: {}...", 
    stripeSecretKey.substring(0, 10));
```

### 5. Manejo de Errores Global

**Implementar ControllerAdvice:**

```java
@RestControllerAdvice
public class PaymentExceptionHandler {
    
    @ExceptionHandler(StripeException.class)
    public ResponseEntity<ErrorResponse> handleStripeException(StripeException e) {
        return ResponseEntity.status(500)
            .body(new ErrorResponse("Payment processing error", e.getMessage()));
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest()
            .body(new ErrorResponse("Invalid request", e.getMessage()));
    }
}
```

---

## 📚 Referencias

### Documentación Oficial

- **Stripe API**: https://stripe.com/docs/api
- **Stripe Checkout**: https://stripe.com/docs/payments/checkout
- **Stripe Webhooks**: https://stripe.com/docs/webhooks
- **Stripe CLI**: https://stripe.com/docs/stripe-cli
- **Spring Boot**: https://spring.io/projects/spring-boot
- **Spring Security**: https://spring.io/projects/spring-security

### Tarjetas de Prueba Stripe

| Escenario | Número de Tarjeta | Resultado |
|-----------|-------------------|-----------|
| Pago exitoso | `4242 4242 4242 4242` | Success |
| Pago rechazado | `4000 0000 0000 0002` | Declined |
| Requiere autenticación | `4000 0025 0000 3155` | 3D Secure |
| Insuficientes fondos | `4000 0000 0000 9995` | Insufficient funds |

**Usar para todos:**
- Fecha: Cualquier fecha futura (ej: `12/34`)
- CVC: Cualquier 3 dígitos (ej: `123`)

### Códigos de Error Comunes

| Código | Mensaje | Causa |
|--------|---------|-------|
| 400 | Invalid signature | Webhook secret incorrecto |
| 400 | Plan type must be... | Plan inválido en request |
| 401 | Unauthorized | JWT token faltante o inválido |
| 404 | Not found | Endpoint incorrecto |
| 500 | Failed to create checkout session | Stripe API key inválida |
| 500 | Payment error | Error inesperado en gateway |

---

## 🎓 Conceptos Clave

### CQRS (Command Query Responsibility Segregation)

El Payments BC separa operaciones de escritura (Commands) y lectura (Queries):

**Commands:**
- `CreateCheckoutSessionCommand` - Crea sesión en Stripe (no persiste)
- `CreateSubscriptionCommand` - Persiste subscription en DB

**Queries:**
- `GetSubscriptionByUserIdQuery` - Consulta subscription existente

### DDD (Domain-Driven Design)

**Aggregate Root:**
- `Subscription` - Encapsula reglas de negocio de suscripciones

**Value Objects:**
- `PlanType` - Enum inmutable con stripePriceId y mechanicsLimit

**Domain Services:**
- `PaymentCommandService` - Orquesta operaciones de pago
- `PaymentQueryService` - Consulta datos de pagos

**Infrastructure:**
- `StripePaymentGateway` - Abstrae integración con Stripe
- `SubscriptionRepository` - Abstrae persistencia JPA

### Event-Driven Architecture

**Eventos consumidos:**
- `customer.subscription.created` (Stripe → Backend)

**Eventos futuros a publicar:**
- `SubscriptionActivatedEvent` (Payments → Workshop)
- `SubscriptionCancelledEvent` (Payments → Workshop)
- `SubscriptionExpiredEvent` (Payments → Workshop)

---

## ✅ Checklist de Deployment

### Pre-Production

- [ ] Cambiar Stripe keys de test a production
- [ ] Configurar webhook endpoint en Stripe production
- [ ] Deshabilitar `/test-session` con `@Profile("!prod")`
- [ ] Implementar rate limiting
- [ ] Validar X-User-Id contra JWT
- [ ] Configurar logging apropiado (no loggear secrets)
- [ ] Implementar monitoring (Stripe Dashboard + Application metrics)
- [ ] Backup de base de datos configurado
- [ ] SSL/TLS habilitado (HTTPS obligatorio para webhooks)
- [ ] Configurar alertas para pagos fallidos

### Production

- [ ] Variables de entorno production configuradas
- [ ] Database connection pool optimizado
- [ ] Stripe webhook endpoint verificado en Dashboard
- [ ] Frontend apuntando a production backend
- [ ] Testing E2E con tarjeta real
- [ ] Plan de rollback preparado
- [ ] Documentación de API actualizada
- [ ] Support team notificado sobre nuevo feature

---

## 📝 Notas Finales

Este documento cubre el 100% de la funcionalidad actual del Payments Bounded Context. Para cualquier duda adicional o implementación de nuevas features, consultar:

1. **README.md principal** - Contexto general del proyecto
2. **Swagger UI** - http://localhost:8080/swagger-ui.html
3. **Código fuente** - `src/main/java/com/safecar/platform/payments/`
4. **Stripe Dashboard** - https://dashboard.stripe.com/

**Última actualización**: 2025-11-12  
**Versión**: 1.0.0  
**Autor**: SafeCar Platform Team

---

**¿Listo para empezar?** 🚀

Sigue el [Flujo End-to-End](#flujo-end-to-end) y el [Testing Completo](#testing-completo) para tener Payments funcionando en menos de 15 minutos.
