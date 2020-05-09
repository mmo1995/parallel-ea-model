FROM python:3

RUN pip3 install flask
RUN pip3 install redis
RUN pip3 install kubernetes

ADD container_starter.py /container_starter.py
ADD config.yaml /config.yaml

ENTRYPOINT ["python3","-u", "/container_starter.py"]
