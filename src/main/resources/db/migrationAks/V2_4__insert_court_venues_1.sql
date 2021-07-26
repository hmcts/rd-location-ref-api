

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
  '13',
  '2',
  'OPEN',
  '9',
  '219165',
  'Building Location B',
  'NORTH',
  'Court Finder URL 2',
  'SW19 2YZ',
  '2 Street, London',
  now())
;
insert into court_venue (court_venue_id, epimms_id, site_name, created_time, updated_time, region_id, court_type_id, cluster_id, open_for_public, court_address, postcode, phone_number, closed_date, court_location_code, dx_address, welsh_site_name, welsh_court_address, court_status, court_open_date, court_name)
values(2, '815833','Aberdeen Tribunal Hearing Centre 11',	'2021-07-05 09:50:46.269032','2021-07-05 09:50:46.269032','7','1', '8',true,'AB10, 57 HUNTLY STREET, ABERDEEN','AB11 5QA',null,null,null,null,null,null,'Open', null,'ABERDEEN TRIBUNAL HEARING CENTRE 11'),
(3, '815833','Aberdeen Tribunal Hearing Centre 112',	'2021-07-05 09:50:46.269032','2021-07-05 09:50:46.269032','7','1', '8',true,'AB10, 57 HUNTLY STREET, ABERDEEN','AB11 5QA',null,null,null,null,null,null,'Closed', null,'ABERDEEN TRIBUNAL HEARING CENTRE 11'),
(4, '219164','Aberdeen Tribunal Hearing Centre 11',	'2021-07-05 09:50:46.269032','2021-07-05 09:50:46.269032','7','1', '8',true,'AB10, 57 HUNTLY STREET, ABERDEEN','AB11 5QA',null,null,null,null,null,null,'Open', null,'ABERDEEN TRIBUNAL HEARING CENTRE 11'),
(5, '219164','Aberdeen Tribunal Hearing Centre 112',	'2021-07-05 09:50:46.269032','2021-07-05 09:50:46.269032','7','1', '8',true,'AB10, 57 HUNTLY STREET, ABERDEEN','AB11 5QA',null,null,null,null,null,null,'Closed', null,'ABERDEEN TRIBUNAL HEARING CENTRE 11'),
(6, '219165','Aberdeen Tribunal Hearing Centre 11',	'2021-07-05 09:50:46.269032','2021-07-05 09:50:46.269032','7','1', '8',true,'AB10, 57 HUNTLY STREET, ABERDEEN','AB11 5QA',null,null,null,null,null,null,'Closed', null,'ABERDEEN TRIBUNAL HEARING CENTRE 11');
