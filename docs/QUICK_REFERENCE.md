# Quick Reference Guide - Backend API Changes

## For Frontend Developers 👨‍💻

### Authentication Changes

**All auth endpoints now return:**
```json
{
  "token": "eyJhbGc...",
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "role": "ROLE_USER",
    "avatarUrl": null
  }
}
```

**New Endpoints:**
- `GET /auth/me` - Get current user profile
- `POST /auth/logout` - Logout confirmation

### Movie Endpoints

**Use these for frontend:**
```
GET /movies/search?q=inception&page=1
GET /movies/popular?page=1
GET /movies/now_playing?page=1
GET /movies/upcoming?page=1
```

### Favorites

**Get favorites:**
```
GET /users/me/favorites
Authorization: Bearer {token}
```

**Add favorite:**
```
POST /users/me/favorites
Authorization: Bearer {token}
Body: { "movieId": 1 }
```

**Remove favorite:**
```
DELETE /users/me/favorites/{movieId}
Authorization: Bearer {token}
```

### Push Notifications

**Register device:**
```
POST /users/me/devices
Authorization: Bearer {token}
Body: {
  "provider": "expo",
  "token": "ExponentPushToken[...]",
  "platform": "ios"
}
```

**Remove device:**
```
DELETE /users/me/devices/{deviceId}
Authorization: Bearer {token}
```

### Notifications

**Get notifications:**
```
GET /users/me/notifications
Authorization: Bearer {token}
```

---

## For Backend Developers 🔧

### New Entities Created
- `Favorite` - User movie favorites
- `Device` - Push notification tokens
- `Notification` - User notifications

### New Repositories Created
- `FavoriteRepository`
- `DeviceRepository`
- `NotificationRepository`

### New DTOs Created
- `AuthResponse` - Unified auth response
- `AddFavoriteRequest` - Add favorite request
- `DeviceRequest` - Device registration request
- `SuccessResponse` - Generic success response

### Service Changes
- `AuthService` - All methods now return `AuthResponse`
- `AuthServiceImpl` - Updated implementation with `buildAuthResponse()` helper

### Controller Changes
- `AuthController` - Dual route mapping + new endpoints
- `MovieController` - Added frontend-compatible aliases
- `UserController` - Added favorites, devices, notifications endpoints

---

## Database Migration Required ⚠️

**Run this SQL script:**
```bash
backend/src/main/resources/db/migration/V1__Create_Favorites_Devices_Notifications_Tables.sql
```

**Or manually run:**
```sql
-- See migration file for complete SQL
CREATE TABLE favorites (...)
CREATE TABLE devices (...)
CREATE TABLE notifications (...)
```

---

## Testing Checklist ✅

### Authentication
- [ ] POST /auth/register returns token + user (201)
- [ ] POST /auth/login returns token + user
- [ ] POST /auth/verify returns token + user
- [ ] GET /auth/me returns user data
- [ ] POST /auth/logout returns success

### Movies
- [ ] GET /movies/search?q=test returns results
- [ ] GET /movies/popular returns paginated results
- [ ] GET /movies/now_playing returns results (underscore format)

### Favorites
- [ ] GET /users/me/favorites returns array
- [ ] POST /users/me/favorites adds favorite (201)
- [ ] DELETE /users/me/favorites/{id} removes favorite

### Devices
- [ ] POST /users/me/devices registers token (201)
- [ ] DELETE /users/me/devices/{id} removes device

### Notifications
- [ ] GET /users/me/notifications returns notifications

---

## Common Issues & Solutions 🔍

### Issue: "User not found" after login
**Solution:** Check JWT token extraction in `JwtAuthenticationFilter`

### Issue: "Movie already in favorites" (409)
**Solution:** This is expected - check if favorite exists before adding

### Issue: CORS errors
**Solution:** Ensure frontend URL is in `cors.allowed-origins` in `application.yml`

### Issue: 401 Unauthorized
**Solution:** 
1. Check token is sent in header: `Authorization: Bearer {token}`
2. Verify token hasn't expired
3. Check JWT secret in configuration

---

## Performance Considerations 📊

### Database Indexes
All new tables have appropriate indexes:
- `favorites`: indexed on user_id, movie_id
- `devices`: indexed on user_id, token
- `notifications`: indexed on user_id, is_read, created_at

### Query Optimization
- Favorites use EAGER fetch for movies to avoid N+1 queries
- Notifications ordered by `created_at DESC` with index

---

## Security Notes 🔒

### Authentication
- All user-specific endpoints require valid JWT token
- Device ownership verified before deletion
- Expo provider validation ensures only "expo" tokens accepted

### Data Privacy
- Password never included in responses
- User can only access their own favorites/devices/notifications
- Admin endpoints still protected with `@PreAuthorize("hasRole('ADMIN')")`

---

## Next Steps 🚀

1. **Run Database Migration**
2. **Test All Endpoints** with Postman
3. **Update Frontend** API client
4. **Test Integration** end-to-end
5. **Deploy to Staging**

---

## Documentation Links 📚

- **Full Documentation:** `/docs/API_CHANGES_FRONTEND_COMPATIBILITY.md`
- **Migration Script:** `/backend/src/main/resources/db/migration/V1__...sql`
- **Postman Collection:** `/docs/postman/Complete Cinerama API.postman_collection.json`

---

**Questions?** Contact the backend team or check the detailed documentation.
