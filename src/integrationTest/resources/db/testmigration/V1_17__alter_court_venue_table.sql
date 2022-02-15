

update court_venue
set welsh_venue_name = 'testVenue', is_temporary_location = 'N', is_nightingale_court = 'N', location_type = 'Court', parent_location = '366559'
where court_venue_id = 1 and epimms_id = '123456789'
