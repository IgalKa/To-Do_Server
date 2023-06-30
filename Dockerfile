FROM openjdk:17

RUN mkdir /app

COPY . /app

WORKDIR /app

CMD java -jar Ex5.jar