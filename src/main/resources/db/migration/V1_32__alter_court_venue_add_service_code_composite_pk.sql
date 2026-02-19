-- Add service_code column to court_venue table
ALTER TABLE court_venue ADD COLUMN IF NOT EXISTS service_code VARCHAR(16);

-- Drop the existing unique constraint on (epimms_id, court_type_id)
ALTER TABLE court_venue DROP CONSTRAINT IF EXISTS court_location_unique;

-- Add a new unique constraint with epimms_id and service_code
ALTER TABLE court_venue ADD CONSTRAINT court_location_unique UNIQUE (epimms_id, service_code);

-- Add foreign key constraint for service_code
ALTER TABLE court_venue ADD CONSTRAINT court_venue_service_code_fk FOREIGN KEY (service_code) REFERENCES SERVICE (service_code);

insert into court_venue (court_venue_id, epimms_id, site_name, created_time, updated_time, region_id, court_type_id, cluster_id, open_for_public, court_address, postcode, phone_number, closed_date, court_location_code, dx_address, welsh_site_name, welsh_court_address, court_status, court_open_date, court_name, is_case_management_location, is_hearing_location, is_temporary_location, location_type,service_code)
values(2, '815833','Aberdeen Tribunal Hearing Centre 11',	'2021-07-05 09:50:46.269032','2021-07-05 09:50:46.269032','7','1', '8',true,'AB10, 57 HUNTLY STREET, ABERDEEN','AB11 5QA',null,null,null,null,null,null,'Open', null,'ABERDEEN TRIBUNAL HEARING CENTRE 11', 'Y', 'Y', 'N', 'CTSC','AAA6');
