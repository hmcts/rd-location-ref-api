insert into
  building_location(
    building_location_id,
    region_id,
    building_location_status_id,
    cluster_id,
    epimms_id,
    building_location_name,
    area,
    court_finder_url,
    postcode,
    address,
    updated_time)
values(
  '22041996',
  '891011',
  '45678',
  '01234',
  '123456',
  'Building Location B',
  'Area B',
  'Court Finder URL 2',
  'SW19 2YZ',
  '2 Street, London',
  now())
;

insert into court_venue (court_venue_id, epimms_id, site_name, created_time, updated_time, region_id, court_type_id, cluster_id, open_for_public, court_address, postcode, phone_number, closed_date, court_location_code, dx_address, welsh_site_name, welsh_court_address, court_status, court_open_date, court_name)
values(1, '123456','Aberdeen Tribunal Hearing Centre 11',	'2021-07-05 09:50:46.269032','2021-07-05 09:50:46.269032','1','23', '2',true,'AB10, 57 HUNTLY STREET, ABERDEEN','AB11 5QA',null,null,null,null,null,null,'Open', null,'ABERDEEN TRIBUNAL HEARING CENTRE 11');
