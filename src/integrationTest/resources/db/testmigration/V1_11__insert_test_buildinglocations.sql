insert into building_location_status(
    building_location_status_id,
    status,
    welsh_status,
    updated_time)
values(
    '45678',
    'Status B',
    'Statws B',
    now())
;

insert into region(
    region_id,
    description,
    updated_time)
values(
    '2',
    'London',
    now())
;

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
  '2',
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
  '22041997',
  '2',
  '45678',
  '01234',
  'epimmsId1234',
  'Building Location C',
  'Area C',
  'Court Finder URL 3',
  'EC2A 2YZ',
  '3 Street, London',
  now())
