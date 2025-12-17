FROM ubuntu:latest
LABEL authors="ogsar"

ENTRYPOINT ["top", "-b"]