alter table building_location drop column building_location_status_id;
alter table building_location add column building_location_status varchar(32);

drop table building_location_status;
drop table location_refdata_audit;
drop table location_refdata_exception;

alter table court_location rename to court_venue;
alter table court_location_category rename to court_type;
alter table court_location_category_service_assoc rename to court_type_service_assoc;

alter table court_venue rename column court_location_id to court_venue_id;
alter table court_venue rename column court_location_name to site_name;
alter table court_venue rename column court_location_category_id to court_type_id;
alter table court_venue rename column welsh_court_location_name to welsh_site_name;

alter table court_venue add column court_status varchar(32);
alter table court_venue add column court_open_date timestamp;
alter table court_venue add column court_name varchar(256);
alter table court_venue alter column dx_address type varchar(64);

alter table court_type rename column court_location_category_id to court_type_id;
alter table court_type rename column court_location_category to court_type;
alter table court_type rename column welsh_court_location_category to welsh_court_type;

alter table court_type_service_assoc rename column court_location_category_service_assoc_id to court_type_service_assoc_id;
alter table court_type_service_assoc rename column court_location_category_id to court_type_id;

alter table region add column api_enabled boolean;

update region set api_enabled = 'N' where description = 'National';
update region set api_enabled = 'Y' where description != 'National';
update building_location set building_location_status = 'Status A' where building_location_id = '22041995';
update building_location set building_location_status = 'Status B' where building_location_id = '22041996';
update building_location set building_location_status = 'Status B' where building_location_id = '22041997';
