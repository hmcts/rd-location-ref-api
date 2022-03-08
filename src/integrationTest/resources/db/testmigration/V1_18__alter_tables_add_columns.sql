ALTER TABLE court_venue ADD COLUMN welsh_court_name VARCHAR(256);
ALTER TABLE court_venue ADD COLUMN uprn VARCHAR(16);
ALTER TABLE court_venue ADD COLUMN venue_ou_code VARCHAR(16);
ALTER TABLE court_venue ADD COLUMN mrd_building_location_id VARCHAR(16);
ALTER TABLE court_venue ADD COLUMN mrd_venue_id VARCHAR(16);
ALTER TABLE court_venue ADD COLUMN service_url VARCHAR(1024);
ALTER TABLE court_venue ADD COLUMN fact_url VARCHAR(1024);
ALTER TABLE court_venue ADD COLUMN mrd_created_time TIMESTAMP;
ALTER TABLE court_venue ADD COLUMN mrd_updated_time TIMESTAMP;
ALTER TABLE court_venue ADD COLUMN mrd_deleted_time TIMESTAMP;


ALTER TABLE building_location ADD COLUMN welsh_building_location_name VARCHAR(256);
ALTER TABLE building_location ADD COLUMN welsh_address VARCHAR(512);
ALTER TABLE building_location ADD COLUMN uprn VARCHAR(16);
ALTER TABLE building_location ADD COLUMN latitude DECIMAL(10,8);
ALTER TABLE building_location ADD COLUMN longitude DECIMAL(11,8);
ALTER TABLE building_location ADD COLUMN mrd_building_location_id VARCHAR(16);
ALTER TABLE building_location ADD COLUMN mrd_created_time TIMESTAMP;
ALTER TABLE building_location ADD COLUMN mrd_updated_time TIMESTAMP;
ALTER TABLE building_location ADD COLUMN mrd_deleted_time TIMESTAMP;

ALTER TABLE court_type ADD COLUMN mrd_created_time TIMESTAMP;
ALTER TABLE court_type ADD COLUMN mrd_updated_time TIMESTAMP;
ALTER TABLE court_type ADD COLUMN mrd_deleted_time TIMESTAMP;

ALTER TABLE region ADD COLUMN mrd_created_time TIMESTAMP;
ALTER TABLE region ADD COLUMN mrd_updated_time TIMESTAMP;
ALTER TABLE region ADD COLUMN mrd_deleted_time TIMESTAMP;

ALTER TABLE cluster ADD COLUMN mrd_created_time TIMESTAMP;
ALTER TABLE cluster ADD COLUMN mrd_updated_time TIMESTAMP;
ALTER TABLE cluster ADD COLUMN mrd_deleted_time TIMESTAMP;

ALTER TABLE court_type_service_assoc ADD COLUMN mrd_created_time TIMESTAMP;
ALTER TABLE court_type_service_assoc ADD COLUMN mrd_updated_time TIMESTAMP;
ALTER TABLE court_type_service_assoc ADD COLUMN mrd_deleted_time TIMESTAMP;

ALTER TABLE district_civil_jurisdiction ADD COLUMN mrd_created_time TIMESTAMP;
ALTER TABLE district_civil_jurisdiction ADD COLUMN mrd_updated_time TIMESTAMP;
ALTER TABLE district_civil_jurisdiction ADD COLUMN mrd_deleted_time TIMESTAMP;

ALTER TABLE district_family_jurisdiction ADD COLUMN mrd_created_time TIMESTAMP;
ALTER TABLE district_family_jurisdiction ADD COLUMN mrd_updated_time TIMESTAMP;
ALTER TABLE district_family_jurisdiction ADD COLUMN mrd_deleted_time TIMESTAMP;

update court_venue
set welsh_court_name = 'welshCourtName1', uprn = '1234', venue_ou_code = '87675', mrd_building_location_id = '8686',
mrd_venue_id = '765', service_url = 'https://serviceurl.com', fact_url = 'https://facturl.com'
where court_venue_id = 1 and epimms_id = '123456789'

