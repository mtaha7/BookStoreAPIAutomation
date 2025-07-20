FROM bellsoft/liberica-openjdk-alpine:21

WORKDIR /home/api-docker

ADD target/docker-resources ./
ADD runner.sh runner.sh

ENTRYPOINT sh runner.sh