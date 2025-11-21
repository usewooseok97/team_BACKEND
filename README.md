# WORKOUT_DB

## Overview
This project contains the Docker configuration for a MongoDB database used in the Workout project.

- **MongoDB** 7.0
- **Docker Compose** for container management
- Database: `WORKOUT_DB`
- Collections: `users`, `exercises`, `workout_plans`, `logs`, `admin_logs`

## How to run
git clone https://github.com/usewooseok97/team_BACKEND.git <br>
cd WORKOUT_DB <br>
docker compose up -d <br>
MongoDB URI: `mongodb://admin:admin1234@localhost:27017/?authSource=admin` <br>

## Notes
- Do **not** commit `mongo_data/`, it contains local database files.
- Passwords and sensitive info are stored in docker-compose.yml for local development only.


