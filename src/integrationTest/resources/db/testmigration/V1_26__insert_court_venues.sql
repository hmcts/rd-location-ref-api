insert into
  building_location(
    building_location_id,
    region_id,
    building_location_status,
    cluster_id,
    epimms_id,
    building_location_name,
    area,
    court_finder_url,
    postcode,
    address,
    updated_time)
values(
  '22041999',
  '2',
  'Open',
  '01234',
  '123457',
  'Building Location 4',
  'Area D',
  'Court Finder URL 4',
  'EC2A 3AQ',
  '4 Street, London',
  now());

insert into court_venue (court_venue_id, epimms_id, site_name, created_time, updated_time, region_id, court_type_id, cluster_id, open_for_public, court_address, postcode, phone_number, closed_date, court_location_code, dx_address, welsh_site_name, welsh_court_address, court_status, court_open_date, court_name, venue_name, is_case_management_location, is_hearing_location, external_short_name)
values(13, '123461','Aberdeen Tribunal Hearing Centre 7',	'2021-07-05 09:50:46.269032','2021-07-05 09:50:46.269032','2','17', '2',true,'AB7, 54 HUNTLY STREET, ABERDEEN','AB11 1TY',null,null,null,null,null,null,'Open', null,'ABERDEEN TRIBUNAL HEARING CENTRE 7',null,null,null, 'Aberdeen Tribunal External');
commit;
