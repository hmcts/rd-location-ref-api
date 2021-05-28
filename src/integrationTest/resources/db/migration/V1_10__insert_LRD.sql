insert into cluster(
    cluster_name,
    welsh_cluster_name,
    updated_time)
values(
    'Cluster A',
    'Clwstwr A',
    timezone('utc', now()))
;

insert into building_location_status(
    status,
    welsh_status,
    updated_time)
values(
    'Status A',
    'Statws A',
    timezone('utc', now()))
;

insert into region(
    description,
    welsh_description,
    updated_time)
values(
    'Description A',
    'Disgrifiad A',
    timezone('utc', now()))
;

insert into building_location_status(
    status,
    welsh_status,
    updated_time)
values(
    'Status A',
    'Statws A',
    timezone('utc', now()))
;

insert into
  building_location(
    region_id,
    building_location_status_id,
    cluster_id,
    epimms_id,
    building_location_name,
    area,
    court_finder_url,
    postcode,
    address)
select
  1,
  (
  select
    region_id
  from
    REGION
  where
    description = 'Description A'
  ) as region_id, (
    select
    building_location_status_id
  from
    BUILDING_LOCATION_STATUS
  where
    status = 'Status A'
  ) as building_location_status_id, (
   select
    cluster_id
  from
    CLUSTER
  where
    cluster_name = 'Cluster A'
  ) as cluster_id,
  '123456789' as epimms_id,
  'Building Location A',
  'Area A',
  'Court Finder URL',
  'WX67 2YZ',
  '1 Street, London',
   timezone('utc', now())
from
  REGION;
