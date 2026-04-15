# Testing: Calligraphy Exchange Activity Management System

## Overview
This is a full-stack system with 3 apps:
- **Backend**: Spring Boot 2.7 REST API (port 8080)
- **Admin Portal**: Vue2 + Element UI (port 8081)
- **User Portal**: Vue2 + Element UI (port 8082)

## Prerequisites
- MySQL running with database `calligraphy` initialized from `backend/src/main/resources/calligraphy.sql`
- DB credentials: root/root123

## Starting the Services
```bash
# 1. Initialize DB (if not done)
mysql -u root -proot123 < backend/src/main/resources/calligraphy.sql

# 2. Start backend (background shell)
cd backend && mvn spring-boot:run

# 3. Start admin frontend (background shell)
cd admin && npm install --legacy-peer-deps && npm run serve

# 4. Start user frontend (background shell)
cd front && npm install --legacy-peer-deps && npm run serve
```

Wait ~30 seconds for all services to compile and start. Verify backend with:
```bash
curl -s http://localhost:8080/api/front/home
```
Expected: JSON with `works`, `activities`, `videos` arrays.

## Test Accounts
| Role | Username | Password | Portal |
|------|----------|----------|--------|
| Super Admin | admin | 123456 | :8081 |
| Activity Admin | activity_admin | 123456 | :8081 |
| User | user1 | 123456 | :8082 |
| User | user2 | 123456 | :8082 |

## Key Test Flows

### Admin Portal (localhost:8081)
- **Login**: POST /api/admin/login, stores admin object in `localStorage('admin')` with JWT token
- **Dashboard**: Fetches stats from 4 `/page` endpoints (pageSize=1), reads `.total` for counts
- **Sidebar navigation**: 首页, 用户管理, 管理员管理, 书法作品管理, 活动管理, 报名管理, 视频管理, 投票管理, 投诉管理
- **Registration approval**: 报名管理 page shows 通过/拒绝 buttons only for status=待审核 records

### User Portal (localhost:8082)
- **Login**: POST /api/user/login, stores user object in `localStorage('user')` with JWT token
- **Home page**: GET /api/front/home returns {works, activities, videos}
- **Work detail**: GET /api/front/work/:id, shows like/dislike/comment/complaint buttons
- **Like toggle**: POST /api/like with {targetType, targetId, type} — type=1 like, type=2 dislike. Same type cancels, different type switches.
- **Comment**: POST /api/comment with {targetType, targetId, content}
- **Activity registration**: POST /api/registration with {activityId} on activity detail page
- **Auth-protected pages**: /profile, /my-works, /my-registrations, /my-complaints require login (meta.auth=true)

## Known Quirks
- `npm install` requires `--legacy-peer-deps` flag due to peer dependency conflicts
- Images show placeholder icons since no actual image files are uploaded in test data
- The `作品投票` menu item might not appear in the nav bar if the menu items overflow — scroll or widen the window
- Admin works table shows `like_count` from the database, but the user portal detail page fetches fresh data — counts may differ if cached

## Devin Secrets Needed
None — all credentials are local development defaults embedded in the codebase.
