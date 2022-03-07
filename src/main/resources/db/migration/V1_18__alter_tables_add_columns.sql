ALTER TABLE court_venue ADD COLUMN welsh_court_name VARCHAR(256);
ALTER TABLE court_venue ADD COLUMN uprn VARCHAR(16);
ALTER TABLE court_venue ADD COLUMN venue_ou_code VARCHAR(16);
ALTER TABLE court_venue ADD COLUMN mrd_building_location_id VARCHAR(16);
ALTER TABLE court_venue ADD COLUMN mrd_venue_id VARCHAR(16);
ALTER TABLE court_venue ADD COLUMN service_url VARCHAR(1024);
ALTER TABLE court_venue ADD COLUMN fact_url VARCHAR(1024);
ALTER TABLE court_venue ADD COLUMN mrd_created_time TIMESTAMP;
ALTER TABLE court_venue ADD COLUMN mrd_updated_time TIMESTAMP;
ALTER TABLE court_venue ADD COLUMN mrd_deleted_time TIMESTAMP