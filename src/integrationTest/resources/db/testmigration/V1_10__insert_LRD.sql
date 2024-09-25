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
    updated_time)
values(
    '1',
    'National',
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
  '1',
  '4567',
  '01234',
  '123456788',
  'Building Location A',
  'Area A',
  'Court Finder URL',
  'WX67 2YZ',
  '1 Street, London',
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
  '1',
  '4567',
  '01234',
  '123456787',
  'Building Location A',
  'Area A',
  'Court Finder URL',
  'WX67 2YZ',
  '1 Street, London',
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
  '22042002',
  '2',
  '45678',
  '01234',
  '123458',
  'Building Location C',
  'Area C',
  'Court Finder URL 3',
  'EC2A 2YZ',
  '3 Street, London',
  now())


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
  '22042003',
  '2',
  '45678',
  '01234',
  '123459',
  'Building Location C',
  'Area C',
  'Court Finder URL 3',
  'EC2A 2YZ',
  '3 Street, London',
  now())


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
    '22042004',
    '2',
    '45678',
    '01234',
    '123460',
    'Building Location C',
    'Area C',
    'Court Finder URL 3',
    'EC2A 2YZ',
    '3 Street, London',
    now())

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
    '22042005',
    '2',
    '45678',
    '01234',
    '123461',
    'Building Location C',
    'Area C',
    'Court Finder URL 3',
    'EC2A 2YZ',
    '3 Street, London',
    now())
