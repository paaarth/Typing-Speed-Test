# SpeedType — Typing Speed Test

A full-stack typing speed test: pick a topic and difficulty, type the paragraph,
watch your WPM and accuracy update live, then track your progress over time on
your profile page.

- **Frontend:** React 18 (JavaScript, Vite) + React Router + Recharts + Axios
- **Backend:** Spring Boot 3.5 + Spring Security (JWT) + Spring Data JPA + PostgreSQL

## Why Spring Boot 3.5, not 4.x?

Spring Boot 4.0 shipped in November 2025 with real breaking changes (renamed
starters, Jackson 3, stricter Security defaults). It's very new, so most
tutorials, Stack Overflow answers, and course material you'll find while
learning are still written for the 3.x line. This project targets **3.5.16**
(the final, fully-supported 3.x release) so the code matches what you'll find
when you look things up. Upgrading later is a well-documented path — see
Spring's official migration guide when you're ready.

## Features

- JWT-based register/login (BCrypt-hashed passwords, stateless auth), with a
  regular-user/admin role system
- Topics and paragraphs are fully admin-manageable (no more editing
  `DataSeeder.java` to add content) — 8 topics × 3 difficulties, 2 original
  paragraphs each, seeded automatically on first run as a starting point
- Live typing indicator: per-character correct/incorrect/current highlighting, live WPM/accuracy/time/error readouts
- Results auto-save to your profile when signed in; guests can still take tests
- Profile page: best/average WPM, average accuracy, best WPM per difficulty, a speed-over-time graph, and a filterable history table
- Admin panel (`/admin`) to create/edit/delete paragraphs and topics
- Candy-pastel "keycap" UI — see Design notes below

## Project Structure

```
typing-speed-test/
├── backend/    Spring Boot REST API (Maven)
└── frontend/   React app (Vite)
```

Backend package layout: `config` (security), `security` (JWT), `model` (JPA
entities), `repository`, `dto`, `service`, `controller`, `exception`, `seed`
(startup data).

Frontend layout: `api` (axios calls), `context` (auth state), `hooks` (the
typing engine), `components/{home,typing,profile,auth,layout,admin}`, `utils`.

## Getting Started

### Prerequisites
- JDK 17+
- Maven 3.8+
- Node.js 18+
- PostgreSQL 14+, installed and running locally

### 1. Database

The driver won't create the database for you, so create it once up front:

```bash
psql -U postgres -c "CREATE DATABASE speedtype;"
```

Then open `backend/src/main/resources/application.properties` and set
`spring.datasource.username` / `spring.datasource.password` to match your local
PostgreSQL setup (default URL assumes `localhost:5432`).

### 2. Backend

```bash
cd backend
mvn spring-boot:run
```

Runs on **http://localhost:8080**. Tables are created automatically on first
run (`spring.jpa.hibernate.ddl-auto=update`). On first startup only, two seeders run:
- 8 starter topics and 48 starter paragraphs (skipped if paragraphs already exist)
- **A default admin account** — see below

### 3. Log in as admin

The first backend startup creates a default admin account and prints it to the
console **once**:

```
username: admin
password: ChangeMe123!
```

Log in with these at `/login`, then go to `/admin` to manage paragraphs and
topics. **Change this password immediately** — there's no in-app "change
password" screen yet, so for now that means updating the row directly in the
database (or registering a different admin account and removing this one).

### 4. Frontend

```bash
cd frontend
npm install
npm run dev
```

Runs on **http://localhost:5173** and talks to the backend at `localhost:8080`
(see `src/api/client.js` if you need to change the API URL).

## API Overview

| Method | Endpoint                  | Auth? | Description                          |
|--------|----------------------------|-------|---------------------------------------|
| POST   | `/api/auth/register`       | No    | Create an account, returns a JWT + role |
| POST   | `/api/auth/login`          | No    | Log in, returns a JWT + role          |
| GET    | `/api/paragraphs/topics`   | No    | List topics (`{id, name, icon}`)      |
| GET    | `/api/paragraphs/random`   | No    | Random paragraph by `topic`+`difficulty` |
| POST   | `/api/results`             | Yes   | Save a completed test                 |
| GET    | `/api/results/history`     | Yes   | Your full test history                |
| GET    | `/api/results/stats`       | Yes   | Aggregated stats for your profile     |
| GET/POST/PUT/DELETE | `/api/admin/paragraphs` | **Admin** | Full paragraph CRUD              |
| GET/POST/PUT/DELETE | `/api/admin/topics`     | **Admin** | Full topic CRUD                  |
| GET    | `/api/admin/topics/icons`  | **Admin** | The fixed set of icon keys an admin can assign to a topic |

Authenticated requests send `Authorization: Bearer <token>`. Admin endpoints
additionally require that token to belong to a user with the `ADMIN` role.

## Design notes

The brief called for candy colors that stay easy on the eyes, so rather than a
generic pastel template, buttons/chips/toggles use a consistent "keycap" motif
(soft bevel + press-down animation) — a nod to the pastel mechanical-keyboard
aesthetic, since this is, after all, a typing app. Fonts: Baloo 2 for display,
Figtree for body text, Space Mono for the typing area and stats.

## A few implementation notes

- The JWT is stored in `localStorage` for simplicity. A production app handling
  sensitive data would typically prefer an httpOnly cookie to reduce XSS exposure.
- Accuracy reflects the *final* state of what you typed (backspacing to fix a
  mistake clears it), not a running log of every keystroke — simpler to reason
  about, and how most typing-test sites behave.
- `jwt.secret` in `application.properties` is a placeholder — move it to an
  environment variable before deploying anywhere real.

## Possible enhancements

- Leaderboard / global rankings
- Word-stream mode (continuous, not paragraph-bound)
- Multiplayer race mode — this one needs real-time infrastructure (WebSockets,
  room/session state, live broadcast) that nothing in this codebase has yet, so
  it's a meaningfully bigger addition than the others
- A real "change password" flow for the admin account, instead of editing the database
