# VehicleDashboard

Backend for the **Epiroc EAE Full-Stack Developer Challenge**. Recreates a vehicle dashboard interface with gauges, indicators, and controls. The backend serves real-time dashboard state and emulates vehicle behavior server-side via a scheduled simulator.

## Submission links

- **Live app**: _TBD_
- **Backend API**: _TBD_
- **Read-only Postgres**: _TBD_
- **Frontend repo**: _TBD_

## Tech stack

- **Backend**: Spring Boot 4.0.6, Java 21, Maven
- **Database**: PostgreSQL (prod) / H2 in-memory (dev)
- **Real-time**: 1 s frontend polling; `@Scheduled` task mutates a single `vehicle_state` row server-side
- **Frontend** (separate repo): React + TypeScript + Vite

## API

Base path: `/api/state`

| Method | Path | Body | Description |
|---|---|---|---|
| GET | `/api/state` | — | Current dashboard state |
| PATCH | `/api/state/motor-speed` | `{ "motorSpeedSetting": 0..4 }` | Set motor speed (OFF / 1–4) |
| PATCH | `/api/state/charging-button` | — | Toggle charging |

Health check: `GET /actuator/health`.

## State model

A single `vehicle_state` row contains:

- User-controlled: `motor_speed_setting`, `charging_button`
- Simulator-derived: `motor_rpm`, `power_consumption`, `battery_percentage`, `battery_temp_celsius`, `motor_status`, `battery_low`
- Occasionally randomized: `parking_brake`, `check_engine`, `gear_ratio`

## Run locally

```bash
./mvnw spring-boot:run
```

Defaults to the `dev` profile (H2 in-memory, H2 console at <http://localhost:8080/h2-console>, seeded row on startup). CORS pre-allowed for `http://localhost:5173` and `http://localhost:3000`.

Hit the API:
```bash
curl http://localhost:8080/api/state
curl -X PATCH -H 'Content-Type: application/json' -d '{"motorSpeedSetting":3}' http://localhost:8080/api/state/motor-speed
curl -X PATCH http://localhost:8080/api/state/charging-button
```

## Run against Postgres (prod profile)

Set environment variables, then start with the `prod` profile:

```bash
export SPRING_PROFILES_ACTIVE=prod
export SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:5432/<db>
export SPRING_DATASOURCE_USERNAME=<user>
export SPRING_DATASOURCE_PASSWORD=<password>
export APP_CORS_ALLOWED_ORIGINS=https://<your-frontend>.vercel.app
./mvnw spring-boot:run
```

Schema is auto-created on first startup (`ddl-auto=update`); the singleton row is inserted via `@PostConstruct` before the web server accepts traffic.

## Deployment (free-tier: Render + Neon)

### 1. Postgres on Neon

1. Create a project at <https://neon.tech>. Copy the connection string for the main role.
2. In the Neon SQL Editor, create a read-only role for the public submission link:
   ```sql
   CREATE ROLE readonly WITH LOGIN PASSWORD '<pick-something>';
   GRANT CONNECT ON DATABASE <db> TO readonly;
   GRANT USAGE ON SCHEMA public TO readonly;
   GRANT SELECT ON ALL TABLES IN SCHEMA public TO readonly;
   ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT ON TABLES TO readonly;
   ```
3. Save the main role's URL for the backend env var and the `readonly` role's URL for the submission.

### 2. Backend on Render

1. Push this repo to GitHub.
2. On <https://render.com>, choose **New → Blueprint** and point at the repo. `render.yaml` provisions the service.
3. Fill in the env vars marked `sync: false`:
   - `SPRING_DATASOURCE_URL` — `jdbc:postgresql://<neon-host>/<db>?sslmode=require`
   - `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD` — Neon main role creds
   - `APP_CORS_ALLOWED_ORIGINS` — the Vercel URL once the frontend is live
4. Render's free tier idles after 15 min — first request after idle takes ~30 s to cold-start.

### 3. Frontend

Separate repo, deployed to Vercel. Point its API client at the Render URL.

## Notes

- The simulator runs every 1 s and drives derived values from user-controlled inputs (see `VehicleStateSimulator`).
- Polling over WebSocket is intentional — challenge doesn't require push.
- Single mutable row; no history table.
