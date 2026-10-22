# 📬 User Management API — Postman Testing Guide

> **Base URL:** `http://localhost:8080`  
> **Content-Type:** `application/json` (required on all requests with a body)

---

## ⚙️ Postman Setup

1. Open Postman → click **"New"** → **"Collection"** → name it `User Management API`
2. For every request, set the header:
   - **Key:** `Content-Type`
   - **Value:** `application/json`
3. Keep requests in order — you'll need the `id` returned from **Create** for later tests.

---

## 📋 API Endpoints Overview

| # | Method | URL | Description | Success Code |
|---|--------|-----|-------------|-------------|
| 1 | `POST` | `/api/users` | Create a new user | `201 Created` |
| 2 | `GET` | `/api/users` | List all active users | `200 OK` |
| 3 | `GET` | `/api/users/{id}` | Get a single user by ID | `200 OK` |
| 4 | `PUT` | `/api/users/{id}` | Update name and email | `200 OK` |
| 5 | `DELETE` | `/api/users/{id}` | Deactivate (soft delete) a user | `204 No Content` |

---

## 1️⃣ Create User — `POST /api/users`

**URL:** `http://localhost:8080/api/users`  
**Method:** `POST`  
**Tab:** Body → raw → JSON

### Request Body
```json
{
  "name": "Alice Martin",
  "email": "alice@example.com",
  "password": "secret123",
  "role": "ADMIN"
}
```

> **Role values:** must be exactly `"ADMIN"` or `"USER"` (uppercase)  
> **Password:** minimum 8 characters

### ✅ Success Response — `201 Created`
```json
{
  "id": 1,
  "name": "Alice Martin",
  "email": "alice@example.com",
  "role": "ADMIN",
  "active": true,
  "createdAt": "2026-10-22T12:00:00.123456",
  "updatedAt": "2026-10-22T12:00:00.123456"
}
```

> 💡 **Save the `id` value** — you will need it for GET, PUT, and DELETE.

---

### Create a second user to have data for listing

```json
{
  "name": "Bob Smith",
  "email": "bob@example.com",
  "password": "password99",
  "role": "USER"
}
```

---

### ❌ Validation Error — `400 Bad Request`

Triggered when any field is missing or invalid.

**Example:** sending an empty name
```json
{
  "name": "",
  "email": "alice@example.com",
  "password": "secret123",
  "role": "ADMIN"
}
```

**Response `400`:**
```json
{
  "name": "Name is required"
}
```

---

### ❌ Duplicate Email — `409 Conflict`

Triggered when the email is already registered.

**Example:** send the same body again for `alice@example.com`

**Response `409`:**
```json
{
  "timestamp": "2026-10-22T12:01:00.000",
  "status": 409,
  "error": "Conflict",
  "message": "Email already in use: alice@example.com"
}
```

---

### ❌ Validation Errors Reference

| Field | Rule | Error Message |
|-------|------|--------------|
| `name` | Must not be blank | `Name is required` |
| `email` | Must not be blank | `Email is required` |
| `email` | Must be valid format | `Email must be valid` |
| `password` | Must not be blank | `Password is required` |
| `password` | Minimum 8 characters | `Password must be at least 8 characters` |
| `role` | Must not be null | `Role is required` |
| `role` | Must be `ADMIN` or `USER` | `400 Bad Request` (JSON parse error) |

---

## 2️⃣ List All Active Users — `GET /api/users`

**URL:** `http://localhost:8080/api/users`  
**Method:** `GET`  
**Body:** none

### ✅ Success Response — `200 OK`
```json
[
  {
    "id": 1,
    "name": "Alice Martin",
    "email": "alice@example.com",
    "role": "ADMIN",
    "active": true,
    "createdAt": "2026-10-22T12:00:00.123456",
    "updatedAt": "2026-10-22T12:00:00.123456"
  },
  {
    "id": 2,
    "name": "Bob Smith",
    "email": "bob@example.com",
    "role": "USER",
    "active": true,
    "createdAt": "2026-10-22T12:01:00.456789",
    "updatedAt": "2026-10-22T12:01:00.456789"
  }
]
```

> **Note:** Only users with `"active": true` are returned. Deactivated users are hidden.

---

## 3️⃣ Get User by ID — `GET /api/users/{id}`

**URL:** `http://localhost:8080/api/users/1`  *(replace `1` with your actual id)*  
**Method:** `GET`  
**Body:** none

### ✅ Success Response — `200 OK`
```json
{
  "id": 1,
  "name": "Alice Martin",
  "email": "alice@example.com",
  "role": "ADMIN",
  "active": true,
  "createdAt": "2026-10-22T12:00:00.123456",
  "updatedAt": "2026-10-22T12:00:00.123456"
}
```

---

### ❌ User Not Found — `404 Not Found`

**URL:** `http://localhost:8080/api/users/9999`

**Response `404`:**
```json
{
  "timestamp": "2026-10-22T12:05:00.000",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id: 9999"
}
```

---

## 4️⃣ Update User — `PUT /api/users/{id}`

**URL:** `http://localhost:8080/api/users/1`  *(replace `1` with your actual id)*  
**Method:** `PUT`  
**Tab:** Body → raw → JSON

> **Note:** Only `name` and `email` can be updated. Role and password are not changed here.

### Request Body
```json
{
  "name": "Alice Johnson",
  "email": "alice.johnson@example.com"
}
```

### ✅ Success Response — `200 OK`
```json
{
  "id": 1,
  "name": "Alice Johnson",
  "email": "alice.johnson@example.com",
  "role": "ADMIN",
  "active": true,
  "createdAt": "2026-10-22T12:00:00.123456",
  "updatedAt": "2026-10-22T12:10:00.789012"
}
```

> Notice `updatedAt` has changed but `createdAt` is the same.

---

### ❌ Validation Error — `400 Bad Request`

**Example:** invalid email format
```json
{
  "name": "Alice Johnson",
  "email": "not-an-email"
}
```

**Response `400`:**
```json
{
  "email": "Email must be valid"
}
```

---

### ❌ User Not Found — `404 Not Found`

**URL:** `http://localhost:8080/api/users/9999`

Same 404 error format as above.

---

## 5️⃣ Deactivate User — `DELETE /api/users/{id}`

**URL:** `http://localhost:8080/api/users/2`  *(replace `2` with your actual id)*  
**Method:** `DELETE`  
**Body:** none

> **⚠️ This is a soft delete** — the user is NOT removed from the database.  
> The `active` field is set to `false`. The user will no longer appear in the `GET /api/users` list.

### ✅ Success Response — `204 No Content`

The response has **no body**. Postman will show `204 No Content` in the status bar.

---

### ❌ User Not Found — `404 Not Found`

**URL:** `http://localhost:8080/api/users/9999`

Same 404 error format as above.

---

### Verify the soft delete worked

After deactivating user `2`, call:

```
GET http://localhost:8080/api/users
```

Bob Smith (`id: 2`) should **no longer appear** in the list.

Then call:

```
GET http://localhost:8080/api/users/2
```

This may still return the user (depending on your service implementation), or return 404.

---

## 🔄 Recommended Testing Order

Run these requests in sequence to test the full lifecycle:

```
Step 1: POST   /api/users           → Create Alice (ADMIN)       → save id=1
Step 2: POST   /api/users           → Create Bob (USER)           → save id=2
Step 3: GET    /api/users           → List both users             → expect 2 results
Step 4: GET    /api/users/1         → Get Alice by ID             → expect Alice's data
Step 5: PUT    /api/users/1         → Update Alice's name/email   → expect updated data
Step 6: GET    /api/users/1         → Verify Alice was updated    → expect new values
Step 7: DELETE /api/users/2         → Deactivate Bob              → expect 204
Step 8: GET    /api/users           → List active users           → expect only Alice
Step 9: GET    /api/users/9999      → Non-existent user           → expect 404
Step 10: POST  /api/users           → Duplicate Alice's email     → expect 409
```

---

## 📦 Response Fields Reference

| Field | Type | Description |
|-------|------|-------------|
| `id` | `Long` | Auto-generated unique identifier |
| `name` | `String` | Full name of the user |
| `email` | `String` | Unique email address |
| `role` | `String` | Either `"ADMIN"` or `"USER"` |
| `active` | `boolean` | `true` = active, `false` = deactivated |
| `createdAt` | `LocalDateTime` | Timestamp when user was created |
| `updatedAt` | `LocalDateTime` | Timestamp of last update |

> **Note:** The `password` field is **never returned** in any response for security.

---

## ⚡ Quick Copy — All Request Bodies

```json
// 1. Create ADMIN user
{ "name": "Alice Martin", "email": "alice@example.com", "password": "secret123", "role": "ADMIN" }

// 2. Create USER
{ "name": "Bob Smith", "email": "bob@example.com", "password": "password99", "role": "USER" }

// 3. Update user
{ "name": "Alice Johnson", "email": "alice.johnson@example.com" }
```
