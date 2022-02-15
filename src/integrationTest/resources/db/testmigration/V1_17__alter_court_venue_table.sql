ALTER TABLE court_venue ADD COLUMN welsh_venue_name varchar(256);
ALTER TABLE court_venue ADD COLUMN is_temporary_location varchar(1);
ALTER TABLE court_venue ADD COLUMN is_nightingale_court varchar(1);
ALTER TABLE court_venue ADD COLUMN location_type varchar(16);
ALTER TABLE court_venue ADD COLUMN parent_location varchar(16);

update court_venue
set welsh_venue_name = 'testVenue', is_temporary_location = 'N', is_nightingale_court = 'N', location_type = 'Court', parent_location = '366559'
where court_venue_id = 1 and epimms_id = '123456789'
