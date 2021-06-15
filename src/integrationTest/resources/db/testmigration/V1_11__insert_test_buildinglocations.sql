insert into cluster(
    cluster_id,
    cluster_name,
    welsh_cluster_name,
    updated_time)
values(
    '01234',
    'Cluster B',
    'Clwstwr B',
    now())
;

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
    welsh_description,
    updated_time)
values(
    '891011',
    'Description B',
    'Disgrifiad B',
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
  '891011',
  '45678',
  '01234',
  'epimmsId1234',
  'Building Location C',
  'Area C',
  'Court Finder URL 3',
  'EC2A 2YZ',
  '3 Street, London',
  now())
