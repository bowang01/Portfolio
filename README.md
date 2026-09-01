# Portfolio

A Spring Boot site that lists your projects on the home page. Each project has a cover, a short intro, and a detail page with test data. Adding, editing, or deleting work requires an admin login.

Projects and site settings are stored in **MySQL**, so they survive restarts.

Default admin account: `admin` / `admin123`. Change this before you deploy.

## Run locally

You need JDK 21, Maven, and Docker (for MySQL).

```bash
docker compose up -d mysql
mvn spring-boot:run
```

Open http://localhost:8080

- Home: project cards (name, tech stack, summary, view details)
- Project detail: intro, test accounts, and endpoints
- Admin: http://localhost:8080/admin/login

Uploaded covers are stored in `data/uploads/`. Project content lives in MySQL.

The first start creates the tables. The home page is empty until you log in and add a project. Those records stay in MySQL after you restart.

## Admin

- Create, edit, and delete projects
- Upload a cover image
- Maintain test accounts, endpoints, and notes
- Edit the site name, home copy, and footer

Visiting `/admin` while signed out sends you to the login page. After login you land on the home page.

## Docker (local or Hostinger Docker Manager)

```bash
docker compose up -d --build
```

This starts MySQL and the app. The site is at http://localhost:8080

On Hostinger VPS Docker Manager:

1. Open hPanel -> VPS -> Manage -> Docker Manager -> Compose
2. Choose Compose from URL and paste this GitHub repository URL, or paste `docker-compose.yml` with Compose manually
3. Set `ADMIN_PASSWORD`, `MYSQL_PASSWORD`, and `MYSQL_ROOT_PASSWORD` in the environment fields
4. Deploy, then open `http://YOUR_VPS_IP:8080`

MySQL data is kept in the `mysql_data` Docker volume. Uploaded images are kept in the `uploads` volume.

## Environment variables

| Variable | Meaning | Default |
| --- | --- | --- |
| `PORT` | App port | `8080` |
| `DB_URL` | JDBC URL | `jdbc:mysql://127.0.0.1:3307/portfolio?...` |
| `DB_USERNAME` | Database user | `portfolio` |
| `DB_PASSWORD` | Database password | `portfolio` |
| `MYSQL_PASSWORD` | MySQL user password in Compose | `portfolio` |
| `MYSQL_ROOT_PASSWORD` | MySQL root password in Compose | `portfolio_root` |
| `ADMIN_USERNAME` | Admin username | `admin` |
| `ADMIN_PASSWORD` | Admin password | `admin123` |
| `UPLOAD_DIR` | Cover upload directory | `./data/uploads` |
