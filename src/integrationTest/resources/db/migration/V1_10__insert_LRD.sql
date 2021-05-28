insert into cluster(
    cluster_id,
    cluster_name,
    welsh_cluster_name,
    updated_time)
values(
    '0123',
    'Cluster A',
    'Clwstwr A',
    now())
;

insert into building_location_status(
    building_location_status_id,
    status,
    welsh_status,
    updated_time)
values(
    '4567',
    'Status A',
    'Statws A',
    now())
;

insert into region(
    region_id,
    description,
    welsh_description,
    updated_time)
values(
    '8910',
    'Description A',
    'Disgrifiad A',
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
  '22041995',
  '8910',
  '4567',
  '0123',
  '123456789',
  'Building Location A',
  'Area A',
  'Court Finder URL',
  'WX67 2YZ',
  '1 Street, London',
  now())
;
