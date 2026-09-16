# Portfolio

## Overview

A personal portfolio site for publishing websites and apps. The home page shows project cards (cover, tech stack, and a short intro). Each project has a detail page with a longer description, a gallery, and optional test accounts or APIs. The public site and the admin console are separate entry points. Visitors browse `/`. Admins sign in at `/admin/login` to add, edit, or remove work.

Content is stored in MySQL. Uploaded images are saved on disk (or a Docker volume) and served from `/uploads`.

## Features

- Home page project cards with a single cover image
- Project detail pages with gallery, description, live/code links, and demo test data
- Admin console for projects, site settings, and password change
- One cover image plus up to 20 detail images per project
- Session-based visitor count, visible only after admin login
- First admin account is created from `ADMIN_USERNAME` / `ADMIN_PASSWORD` when the database is empty
- Docker Compose stack (app on port 8081, MySQL on host port 3307)
- GitHub Actions deploy to a Hostinger VPS

## Tech Stack

- Java 21
- Spring Boot 3.4.5 (Web, Security, Data JPA, Validation)
- Thymeleaf
- MySQL 8.4
- Maven
- Docker and Docker Compose
- GitHub Actions (`hostinger/deploy-on-vps`)

## Screenshots
Move forward to my portfolio website to check screenshots and test data
https://portfolio.bowang.tech/

## Getting Started

Requirements: JDK 21, Maven, Docker Desktop (for MySQL), or a local MySQL 8 instance.

1. Clone the repository.

```bash
git clone https://github.com/bowang01/Portfolio.git
cd Portfolio
```

2. Start MySQL. Compose maps container `3306` to host `3307` so it does not clash with a local MySQL on `3306`.

```bash
docker compose up -d mysql
```

3. If you run the app on the host (not in Compose), point it at the mapped port. Default `application.properties` uses `127.0.0.1:3306` with `root` / `root`. For the Compose MySQL, set:

```bash
DB_URL=jdbc:mysql://127.0.0.1:3307/portfolio?useUnicode=true&characterEncoding=utf8&connectionCollation=utf8mb4_unicode_ci&serverTimezone=UTC
DB_USERNAME=portfolio
DB_PASSWORD=portfolio
```

4. Run the app.

```bash
mvn spring-boot:run
```

5. Open [http://localhost:8081](http://localhost:8081). Admin login is [http://localhost:8081/admin/login](http://localhost:8081/admin/login). The first account is `admin` / `admin123` unless you set `ADMIN_USERNAME` and `ADMIN_PASSWORD`. After that, change the password in the admin console. Environment variables no longer overwrite an existing account.

To run the full stack in Docker, create a `.env` file (do not commit it) and start both services:

```bash
APP_HOST=localhost
docker compose up -d --build
```

`APP_HOST` is required by Compose. The app also joins an external Docker network named `proxy`. Create it first if it does not exist:

```bash
docker network create proxy
```

## Testing

There is no automated test suite in this repository. After startup, check these paths by hand:

- `/` shows project cards and only cover images
- `/projects/{id}` shows the gallery, about text, and test data
- `/admin/login` signs in; `/admin` is blocked until login
- The home page has no public Log in link
- Upload a cover and several detail images, save once, then confirm the home page still shows only the cover
- Refresh should not increase Visitors. Sign in, then check `Visitors` in the footer
- Log out and confirm Visitors is hidden

## Deployment

The site is meant to run on a Hostinger VPS with Docker. Push to `main` or `master` (or run the workflow by hand) to deploy.

The Action only sends `docker-compose.yml` to Hostinger Docker Manager. A service with `build: .` does not rebuild the JAR unless you rebuild the image on the VPS:

```bash
cd /docker/portfolio
docker compose build --no-cache app
docker compose up -d
```

Public HTTP/HTTPS should be handled by a shared reverse proxy (for example a global nginx on the `proxy` network). Do not let another project occupy ports 80 and 443 as the only default site. The app listens on **8081**.

Uploads persist in the `portfolio_uploads` volume. MySQL data persists in `portfolio_mysql`.

## Live Demo

[https://portfolio.bowang.tech](https://portfolio.bowang.tech)

