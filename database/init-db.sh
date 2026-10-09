#!/usr/bin/env sh

set -e

if [ -z "$POSTGRES_USERNAME" ] || [ -z "$POSTGRES_PASSWORD" ]; then
  echo "ERROR: Missing environment variables. Set values for 'POSTGRES_USERNAME' and 'POSTGRES_PASSWORD'."
  exit 1
fi

echo "Creating dbrdlocationref database . . ."

psql -v ON_ERROR_STOP=1 --username postgres --dbname postgres \
  -v app_user="$POSTGRES_USERNAME" \
  -v app_password="$POSTGRES_PASSWORD" <<-EOSQL
  CREATE ROLE :"app_user" WITH PASSWORD :'app_password';
  CREATE DATABASE dbrdlocationref ENCODING = 'UTF-8' CONNECTION LIMIT = -1;
  GRANT ALL PRIVILEGES ON DATABASE dbrdlocationref TO :"app_user";
  ALTER ROLE :"app_user" WITH LOGIN;
EOSQL

echo "Done creating database dbrdlocationref."
