alter table court_district_family_jurisdiction_assoc drop CONSTRAINT dfj_court_location_id_fk;
alter table court_district_family_jurisdiction_assoc drop CONSTRAINT dfj_court_district_family_jurisdiction_id_fk;

alter table court_district_civil_jurisdiction_assoc drop CONSTRAINT dcj_court_location_id_fk;
alter table court_district_civil_jurisdiction_assoc drop CONSTRAINT dcj_court_district_civil_jurisdiction_id_fk;

alter table court_venue ALTER COLUMN court_venue_id SET DATA TYPE bigint;

alter table building_location ALTER COLUMN building_location_id SET DATA TYPE bigint;

alter table district_civil_jurisdiction ALTER COLUMN district_civil_jurisdiction_id SET DATA TYPE bigint;

alter table court_district_civil_jurisdiction_assoc ALTER COLUMN court_district_civil_jurisdiction_assoc_id SET DATA TYPE bigint;

alter table court_district_civil_jurisdiction_assoc ALTER COLUMN district_civil_jurisdiction_id SET DATA TYPE bigint;

alter table court_district_civil_jurisdiction_assoc ALTER COLUMN court_location_id SET DATA TYPE bigint;

alter table district_family_jurisdiction ALTER COLUMN district_family_jurisdiction_id SET DATA TYPE bigint;

alter table court_district_family_jurisdiction_assoc ALTER COLUMN court_district_family_jurisdiction_assoc_id SET DATA TYPE bigint;

alter table court_district_family_jurisdiction_assoc ALTER COLUMN district_family_jurisdiction_id SET DATA TYPE bigint;

alter table court_district_family_jurisdiction_assoc ALTER COLUMN court_location_id SET DATA TYPE bigint;

create sequence court_venue_seq start 1 increment by 1;
create sequence building_location_seq start 1 increment by 1;

alter table building_location
ALTER COLUMN building_location_id SET DEFAULT nextval('building_location_seq');

alter table court_venue
ALTER COLUMN court_venue_id SET DEFAULT nextval('court_venue_seq');

alter table court_district_family_jurisdiction_assoc add CONSTRAINT dfj_court_location_id_fk
FOREIGN KEY (court_location_id)
REFERENCES court_venue (court_venue_id);

alter table court_district_family_jurisdiction_assoc add CONSTRAINT dfj_court_district_family_jurisdiction_id_fk
FOREIGN KEY (district_family_jurisdiction_id)
REFERENCES DISTRICT_FAMILY_JURISDICTION (district_family_jurisdiction_id);

alter table court_district_civil_jurisdiction_assoc add CONSTRAINT dcj_court_location_id_fk
FOREIGN KEY (court_location_id)
REFERENCES court_venue (court_venue_id);

alter table court_district_civil_jurisdiction_assoc add CONSTRAINT dcj_court_district_civil_jurisdiction_id_fk
FOREIGN KEY (district_civil_jurisdiction_id)
REFERENCES DISTRICT_CIVIL_JURISDICTION (district_civil_jurisdiction_id);

alter table court_venue drop CONSTRAINT court_location_code_uq;

alter table court_venue add CONSTRAINT court_location_uq UNIQUE (epimms_id,site_name,court_type_id)
