startup: build_image create_container start_containers

cleanup: clean_containers clean_image


start_containers:
	docker-compose start

stop_container:
	docker-compose stop

create_container:
	docker-compose up --no-start

clean_containers:
	docker-compose down

build_image:
	docker build -f Dockerfile -t kafka_try_img .

clean_image:
	docker image rm kafka_try_img