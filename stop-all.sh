# Stops the entire Ascent platform, including all log aggregation services

docker-compose -f docker-compose.yml \
	down -v
