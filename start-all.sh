# Starts the entire Ascent platform, including all log aggregation services

docker-compose -f docker-compose.yml \
	-f docker-compose.logging.yml \
	up -d
