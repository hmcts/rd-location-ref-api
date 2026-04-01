-- Restore court_venue data from backup created in V1_32
SET search_path TO locrefdata;

-- Ensure backup table matches current column set
ALTER TABLE court_venue_backup ADD COLUMN IF NOT EXISTS service_code VARCHAR(16);

-- Temporarily drop FKs that reference court_venue to allow restore
ALTER TABLE court_district_family_jurisdiction_assoc DROP CONSTRAINT IF EXISTS dfj_court_location_id_fk;
ALTER TABLE court_district_civil_jurisdiction_assoc DROP CONSTRAINT IF EXISTS dcj_court_location_id_fk;

TRUNCATE TABLE court_venue;

INSERT INTO court_venue
SELECT * FROM court_venue_backup;

-- Reset sequence to the current max id
SELECT setval(
  'court_venue_seq',
  COALESCE(MAX(court_venue_id), 1),
  COALESCE(MAX(court_venue_id), 0) > 0
) FROM court_venue;

-- Recreate FKs
ALTER TABLE court_district_family_jurisdiction_assoc ADD CONSTRAINT dfj_court_location_id_fk
FOREIGN KEY (court_location_id) REFERENCES court_venue (court_venue_id);
ALTER TABLE court_district_civil_jurisdiction_assoc ADD CONSTRAINT dcj_court_location_id_fk
FOREIGN KEY (court_location_id) REFERENCES court_venue (court_venue_id);
