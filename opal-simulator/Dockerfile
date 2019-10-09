FROM python:2.7-slim

RUN apt-get update
RUN apt-get install -y libcurl4-openssl-dev
RUN pip install flask
RUN pip install redis

ADD ftext_d.txt /ftext_d.txt
ADD standard.vl /standard.vl
ADD gleam-simulator-read-from-redis.py /gleam-simulator-read-from-redis.py
ADD gleam-simulator-write-to-redis.py /gleam-simulator-write-to-redis.py
ADD commands.sh /commands.sh
ADD testOpalSimu_2 /testOpalSimu_2

RUN ["chmod", "+x", "testOpalSimu_2"]
RUN ["chmod", "+x", "commands.sh"]

ENTRYPOINT ["/commands.sh"]
