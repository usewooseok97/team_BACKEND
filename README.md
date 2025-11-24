# WORKOUT_DB

## Overview
This project contains the Docker configuration for a MongoDB database used in the Workout project. <br>
본 프로젝트는 Docker Configuration을 통한 MongoDB 데이터베이스를 사용하였습니다.

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
- Do **not** commit `mongo_data/`, it contains local database files.<br>
- `mongo_data/`는 절대로 커밋하지 마십시오. 로컬 데이터베이스 파일들이 들어있습니다.
- Passwords and sensitive info are stored in docker-compose.yml for local development only.<br>
- 비밀번호나 중요한 정보들은 docker-compose.yml 파일에 들어있습니다 -> 로컬로 돌려보기 위해서.


