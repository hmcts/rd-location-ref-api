FROM postgres:16

COPY init-db.sh /docker-entrypoint-initdb.d/

HEALTHCHECK --interval=10s --timeout=10s --retries=10 CMD psql -c 'select 1' -d dbrdlocationref -U dbrdlocationref

EXPOSE 5432
