FROM postgres:13.2
ENV POSTGRES_USER=
ENV POSTGRES_PASSWORD=
ENV POSTGRES_DB=
ENV POSTGRES_INITDB_ARGS=
ENV TZ=
ENV PGTZ=
ENV LC_ALL=
ENV LC_CTYPE=
RUN apt-get update && apt-get install -y --no-install-recommends \
    locales \
    locales-all
COPY init.sql /docker-entrypoint-initdb.d/