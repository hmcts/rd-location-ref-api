-- Restore court_venue data from backup created in V1_32
SET search_path TO locrefdata;

-- Ensure backup table matches current column set
ALTER TABLE court_venue_backup_2026 ADD COLUMN IF NOT EXISTS service_code VARCHAR(16);


-- Use DELETE rather than TRUNCATE because court_venue is FK-referenced.
DELETE FROM court_venue;

INSERT INTO court_venue
SELECT * FROM court_venue_backup_2026;

-- Reset sequence to the current max id
SELECT setval(
         'court_venue_seq',
         COALESCE(MAX(court_venue_id), 1),
         COALESCE(MAX(court_venue_id), 0) > 0
       ) FROM court_venue;
