# API Changes for Frontend Compatibility

## Summary

This document outlines all backend API changes made to ensure compatibility with the React Native frontend expectations. All changes maintain backward compatibility where possible while adding new endpoints and response formats.

---

## 1. Authentication Endpoints (/auth)

### Route Prefix Changes
- **Added dual mapping**: `@RequestMapping({"/api/auth", "/auth"})`
- Both `/api/auth/*` and `/auth/*` routes now work for all authentication endpoints
- **Reason**: Frontend expects `/auth` prefix for compatibility

### Response Format Changes
All authentication endpoints now return unified `AuthResponse` format:

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "role": "ROLE_USER",
    "avatarUrl": null
  }
}
```

### Updated Endpoints

#### POST /auth/register
**Before:**
- Returned: `User` entity
- Status: 200 OK

**After:**
- Returns: `AuthResponse` with token and user
- Status: **201 CREATED**
- **Breaking Change**: Now generates JWT token immediately on registration
- Note: Users are enabled immediately (`enabled = true`)

**Request:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "confirmPassword": "password123"
}
```

**Response:**
```json
{
  "token": "eyJ...",
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "role": "ROLE_USER",
    "avatarUrl": null
  }
}
```

---

#### POST /auth/verify
**Before:**
- Returned: `String` message
- Status: 200 OK

**After:**
- Returns: `AuthResponse` with token and user
- Status: 200 OK
- **Breaking Change**: Now returns JWT token after verification

**Request:**
```json
{
  "email": "john@example.com",
  "code": "123456"
}
```

**Response:**
```json
{
  "token": "eyJ...",
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "role": "ROLE_USER",
    "avatarUrl": null
  }
}
```

---

#### POST /auth/login
**Before:**
- Returned: `LoginResponse` with `{ name, token }`

**After:**
- Returns: `AuthResponse` with full user object
- **Breaking Change**: Response structure changed from `{ name, token }` to `{ token, user: {...} }`

**Request:**
```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJ...",
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "role": "ROLE_USER",
    "avatarUrl": null
  }
}
```

---

#### GET /auth/me ⭐ NEW
**Description**: Gets the current authenticated user's profile

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "role": "ROLE_USER",
  "avatarUrl": null
}
```

---

#### POST /auth/logout ⭐ NEW
**Description**: Logs out the current user (returns success confirmation)

**Response:**
```json
{
  "success": true,
  "message": "Logged out successfully"
}
```

**Note**: JWT is stateless, so logout is handled client-side by removing the token. This endpoint provides confirmation.

---

#### GET /auth/refresh-token
**Updated**: Now returns `AuthResponse` format instead of `LoginResponse`

**Response:**
```json
{
  "token": "eyJ... (new token)",
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "role": "ROLE_USER",
    "avatarUrl": null
  }
}
```

---

## 2. Movie Endpoints (/api/movies)

### New Frontend-Compatible Routes

#### GET /movies/search
**Description**: Search movies with frontend-compatible parameter names

**Query Parameters:**
- `q` (string, optional): Search query (frontend expected)
- `query` (string, optional): Alternative parameter name
- `page` (integer, default: 1): Page number

**Response:**
```json
{
  "page": 1,
  "results": [
    {
      "id": 550,
      "title": "Fight Club",
      "overview": "...",
      "poster_path": "/poster.jpg",
      "release_date": "1999-10-15",
      "vote_average": 8.4
    }
  ],
  "total_results": 100,
  "total_pages": 5
}
```

---

#### GET /movies/popular ⭐ NEW
**Description**: Get popular movies from TMDB

**Query Parameters:**
- `page` (integer, default: 1)

**Response:** Same format as search

---

#### GET /movies/now_playing ⭐ NEW
**Description**: Get movies currently playing in theaters

**Query Parameters:**
- `page` (integer, default: 1)

**Response:** Same format as search

**Note**: Frontend expects underscore format `now_playing`, not hyphen `now-playing`

---

#### GET /movies/upcoming ⭐ NEW
**Description**: Get upcoming movies

**Query Parameters:**
- `page` (integer, default: 1)

**Response:** Same format as search

---

### Legacy TMDB Endpoints (Backward Compatible)
The following endpoints are maintained for backward compatibility:
- `GET /movies/tmdb/search`
- `GET /movies/tmdb/popular`
- `GET /movies/tmdb/now-playing`
- `GET /movies/tmdb/upcoming`

---

## 3. User Favorites Endpoints ⭐ NEW

All endpoints require authentication via Bearer token.

### GET /users/me/favorites
**Description**: Get all favorite movies for the authenticated user

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "favorites": [
    {
      "id": 1,
      "title": "Movie Title",
      "descriptionShowtimes": "Short description",
      "descriptionMovie": "Full description",
      "duration": 120,
      "rating": "PG-13",
      "genreName": "Action",
      "genreId": 1,
      "imageUrl": "https://...",
      "trailerUrl": "https://...",
      "releaseDate": "2024-01-01",
      "status": "NOW_PLAYING",
      "director": "Director Name",
      "cast": ["Actor 1", "Actor 2"],
      "createdAt": "2024-01-01T00:00:00",
      "updatedAt": "2024-01-01T00:00:00"
    }
  ]
}
```

---

### POST /users/me/favorites
**Description**: Add a movie to user's favorites

**Headers:**
```
Authorization: Bearer <token>
```

**Request:**
```json
{
  "movieId": 1
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "message": "Movie added to favorites",
  "data": {
    "favorite": {
      "id": 1,
      "title": "Movie Title",
      ...
    }
  }
}
```

**Response (409 Conflict - Already favorited):**
```json
{
  "success": false,
  "message": "Movie already in favorites"
}
```

---

### DELETE /users/me/favorites/{movieId}
**Description**: Remove a movie from user's favorites

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Movie removed from favorites"
}
```

**Response (404 Not Found):**
```json
{
  "success": false,
  "message": "Favorite not found"
}
```

---

## 4. Device/Push Token Endpoints ⭐ NEW

For Expo push notification integration.

### POST /users/me/devices
**Description**: Register a push notification device token

**Headers:**
```
Authorization: Bearer <token>
```

**Request:**
```json
{
  "provider": "expo",
  "token": "ExponentPushToken[xxxxxxxxxxxxxxxxxxxxxx]",
  "platform": "ios"
}
```

**Validation:**
- `provider` must be exactly "expo"
- `platform` can be "ios", "android", or "web"

**Response (201 Created):**
```json
{
  "success": true,
  "message": "Device registered successfully",
  "data": {
    "device": {
      "id": 1,
      "provider": "expo",
      "token": "ExponentPushToken[...]",
      "platform": "ios"
    }
  }
}
```

**Response (200 OK - Already registered):**
```json
{
  "success": true,
  "message": "Device already registered",
  "data": {
    "device": {
      "id": 1,
      "provider": "expo",
      "token": "ExponentPushToken[...]",
      "platform": "ios"
    }
  }
}
```

---

### DELETE /users/me/devices/{deviceId}
**Description**: Remove a device token

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Device removed successfully"
}
```

**Response (403 Forbidden - Not owner):**
```json
{
  "success": false,
  "message": "Access denied"
}
```

---

## 5. Notifications Endpoint ⭐ NEW

### GET /users/me/notifications
**Description**: Get all notifications for the authenticated user

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "notifications": [
    {
      "id": 1,
      "title": "Booking Confirmed",
      "message": "Your booking for Movie Title has been confirmed",
      "type": "booking",
      "isRead": false,
      "createdAt": "2024-01-01T12:00:00",
      "data": "{\"bookingId\": 123}"
    }
  ]
}
```

---

## 6. Database Changes

### New Tables Created

#### `favorites` table
```sql
CREATE TABLE favorites (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  movie_id BIGINT NOT NULL,
  created_at TIMESTAMP NOT NULL,
  UNIQUE KEY unique_user_movie (user_id, movie_id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (movie_id) REFERENCES movies(id)
);
```

#### `devices` table
```sql
CREATE TABLE devices (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  provider VARCHAR(255) NOT NULL,
  token VARCHAR(500) NOT NULL,
  platform VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  UNIQUE KEY unique_user_token (user_id, token),
  FOREIGN KEY (user_id) REFERENCES users(id)
);
```

#### `notifications` table
```sql
CREATE TABLE notifications (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  title VARCHAR(255) NOT NULL,
  message VARCHAR(1000) NOT NULL,
  type VARCHAR(255) NOT NULL,
  is_read BOOLEAN NOT NULL DEFAULT FALSE,
  data TEXT,
  created_at TIMESTAMP NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id)
);
```

---

## 7. New DTOs Created

### `AuthResponse.java`
Unified authentication response for all auth endpoints.

### `AddFavoriteRequest.java`
Request DTO for adding favorites.

### `DeviceRequest.java`
Request DTO for registering push tokens with validation.

### `SuccessResponse.java`
Generic success response DTO with optional message and data.

---

## 8. Error Response Format

All errors should follow this standardized format:

```json
{
  "message": "Descriptive error message",
  "errors": {
    "field": "Specific field error (optional)"
  }
}
```

**Common HTTP Status Codes:**
- `400 Bad Request`: Invalid request data
- `401 Unauthorized`: Missing or invalid token
- `403 Forbidden`: User doesn't have permission
- `404 Not Found`: Resource not found
- `409 Conflict`: Resource already exists
- `500 Internal Server Error`: Server error

---

## 9. Migration Checklist

### Backend Changes ✅
- [x] Created `AuthResponse` DTO with token + user format
- [x] Updated `AuthController` with dual `/api/auth` and `/auth` mappings
- [x] Modified `register` to return `AuthResponse` (201 status)
- [x] Modified `verify` to return `AuthResponse`
- [x] Modified `login` to return `AuthResponse`
- [x] Added `GET /auth/me` endpoint
- [x] Added `POST /auth/logout` endpoint
- [x] Updated `AuthService` interface and implementation
- [x] Added `/movies/search` with `q` parameter support
- [x] Added `/movies/popular` endpoint
- [x] Added `/movies/now_playing` endpoint (with underscore)
- [x] Added `/movies/upcoming` endpoint
- [x] Created `Favorite` entity and repository
- [x] Created `Device` entity and repository
- [x] Created `Notification` entity and repository
- [x] Added favorites endpoints to `UserController`
- [x] Added device management endpoints to `UserController`
- [x] Added notifications endpoint to `UserController`
- [x] Backend compiled successfully

### Database Migrations Needed 🔄
- [ ] Run migration to create `favorites` table
- [ ] Run migration to create `devices` table
- [ ] Run migration to create `notifications` table

### Frontend Changes Needed (Your Frontend Team) 📱
- [ ] Update auth service to use new response format `{ token, user }`
- [ ] Change token storage key to `userToken`
- [ ] Update API calls to use `/auth` prefix (or keep `/api/auth`)
- [ ] Update movie search to use `/movies/search?q=query`
- [ ] Update movie listings to use `/movies/now_playing`, `/movies/popular`
- [ ] Implement favorites UI using new endpoints
- [ ] Implement push notification token registration
- [ ] Handle new error response formats

### Testing Needed 🧪
- [ ] Test all auth endpoints with new response format
- [ ] Test token refresh flow
- [ ] Test movie search with `q` parameter
- [ ] Test favorites CRUD operations
- [ ] Test device registration/removal
- [ ] Test notifications retrieval
- [ ] Test error responses

---

## 10. Breaking Changes Summary

⚠️ **Critical Breaking Changes:**

1. **AuthController responses changed:**
   - `POST /auth/register`: Now returns `AuthResponse` instead of `User`
   - `POST /auth/verify`: Now returns `AuthResponse` instead of `String`
   - `POST /auth/login`: Response structure changed from `{ name, token }` to `{ token, user: {...} }`
   - `GET /auth/refresh-token`: Now returns `AuthResponse`

2. **Registration flow changed:**
   - Users are now enabled immediately (`enabled = true`)
   - Token is generated on registration (no need to wait for verification)

3. **Movie ID endpoint:**
   - If frontend was using `/movies/{id}` for TMDB movies, clarify it's for local movies only
   - Use `/movies/enriched?tmdbId=X` for TMDB movie details

---

## 11. Backward Compatibility Notes

✅ **Maintained Compatibility:**

1. **Old TMDB endpoints still work:**
   - `/movies/tmdb/search`
   - `/movies/tmdb/popular`
   - `/movies/tmdb/now-playing`
   - `/movies/tmdb/upcoming`

2. **Both route prefixes work:**
   - `/api/auth/*` (legacy)
   - `/auth/*` (new)

3. **Existing user endpoints unchanged:**
   - `/api/user/me` still works
   - Admin endpoints unchanged

---

## 12. Next Steps

1. **Database Migration:**
   ```bash
   # Run these migrations in your database
   # See section 6 for SQL statements
   ```

2. **Update Application Configuration:**
   - Ensure JWT configuration is correct in `application.yml`
   - Verify CORS settings include your frontend URL

3. **Test Authentication Flow:**
   ```bash
   # Test registration
   POST /auth/register

   # Test login
   POST /auth/login

   # Test protected endpoint
   GET /auth/me
   ```

4. **Frontend Integration:**
   - Update API client to match new response formats
   - Test all endpoints with Postman/frontend
   - Update token storage logic

---

## 13. Example Postman Collection

See `docs/postman/Complete Cinerama API.postman_collection.json` for updated collection with all new endpoints.

---

## Contact & Support

For questions about these changes, contact the backend team or refer to:
- API Documentation: `/docs/API_CHANGES_FRONTEND_COMPATIBILITY.md`
- Entity Models: `/backend/src/main/java/com/cinerama/backend/entity/`
- DTOs: `/backend/src/main/java/com/cinerama/backend/dto/`

---

**Last Updated:** 2025-11-21  
**Backend Version:** 0.0.1-SNAPSHOT  
**Authors:** Cinerama Development Team
