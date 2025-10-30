# System Evidencie Odbornej Praxe (SEP)

This is a development setup guide for the System Evidencie Odbornej Praxe project. The project consists of three main components:
- Spring Boot Backend (Java)
- Vue.js Frontend
- MariaDB Database

## Prerequisites

### Windows
1. Install [Docker Desktop for Windows](https://www.docker.com/products/docker-desktop/)
2. Make sure WSL2 (Windows Subsystem for Linux) is installed and enabled
3. Install Git for Windows

### Linux
1. Install Docker and Docker Compose:
   ```bash
   # For Ubuntu/Debian
   sudo apt update
   sudo apt install docker.io docker-compose

   # For Fedora
   sudo dnf install docker docker-compose

   # Start and enable Docker service
   sudo systemctl start docker
   sudo systemctl enable docker

   # Add your user to docker group (optional, to run docker without sudo)
   sudo usermod -aG docker $USER
   ```

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/Edodian/System-Evidencie-Odbornej-Praxe.git
   cd System-Evidencie-Odbornej-Praxe
   ```

2. Environment setup is handled through Docker Compose, no need for local installations of Node.js, Java, or MariaDB.

## Running the Application

1. Open PowerShell or Command Prompt on Windows and Terminal on Linux
2. Navigate to the project directory
3. Run:
   ```bash
   docker compose up --build
   ```

The application will start with the following components:
- Frontend: http://localhost:5173
- Backend API: http://localhost:8081
- MariaDB: localhost:3306

## Development Access Points

- **Frontend Development Server**
  - URL: http://localhost:5173
  - Hot-reload enabled for development

- **Backend API**
  - URL: http://localhost:8081
  - Debug port: 5005 (for IDE debugging)

- **Database**
  - Host: localhost
  - Port: 3306
  - Database: sep
  - User: appuser
  - Password: apppassword

## Project Structure

```
├── Backend/                  # Spring Boot application
│   ├── src/                 # Source code
│   ├── pom.xml             # Maven dependencies
│   └── Dockerfile.dev      # Development Dockerfile
├── Frontend/                # Vue.js application
│   ├── src/                # Source code
│   ├── package.json        # npm dependencies
│   └── Dockerfile.dev      # Development Dockerfile
└── docker-compose.yml      # Docker Compose configuration
```

## Stopping the Application

To stop the application:
1. Press `Ctrl+C` in the terminal where docker-compose is running
2. Run:
   ```bash
   docker compose down
   ```

To completely reset the database and volumes:
```bash
docker compose down -v
```

## Troubleshooting

### Common Issues

1. **Port Conflicts**
   - If ports 5173, 8081, or 3306 are already in use, modify the port mappings in `docker-compose.yml`

2. **Database Connection Issues**
   - Ensure no local MariaDB/MySQL instance is running on port 3306
   - Wait for the MariaDB container to fully initialize before accessing the application

3. **Docker Issues**
   - On Windows, ensure Docker Desktop is running
   - On Linux, ensure the Docker daemon is running (`sudo systemctl status docker`)

### Logs

To view logs for specific services:
```bash
# All services
docker compose logs

# Specific service
docker compose logs backend
docker compose logs frontend
docker compose logs mariadb
```

## Contributing

1. Create a new branch for your feature
2. Make your changes
3. Test thoroughly
4. Submit a pull request

## Development Tips

- The project uses Docker volumes to persist database data and Maven/npm dependencies
- Hot-reload is enabled for both frontend and backend development
- The backend includes remote debugging support on port 5005