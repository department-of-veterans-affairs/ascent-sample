# Starts the entire Ascent platform, including all log aggregation services

docker-compose -f docker-compose.yml \
	-f docker-compose.override.yml \
	up --build -d
