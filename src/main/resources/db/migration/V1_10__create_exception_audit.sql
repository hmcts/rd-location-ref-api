create table location_refdata_audit (
    job_id bigint,
    authenticated_user_id varchar(36),
    job_start_time timestamp not null,
    file_name varchar(128) not null,
    job_end_time timestamp,
    status varchar(32),
    comments varchar(512),
    constraint location_refdata_audit_pk primary key (job_id)
);

create table location_refdata_exception (
   id bigint,
   job_id bigint not null,
   excel_row_id varchar(32),
   key varchar(256),
   field_in_error varchar(256),
   error_description varchar(512),
   updated_timestamp timestamp,
   constraint location_refdata_exception_pk primary key (id)
);

alter table location_refdata_exception add constraint job_id_fk1 foreign key (job_id)
references location_refdata_audit (job_id);

create sequence JOB_ID_SEQ;
create sequence EXCEPTION_ID_SEQ;

ALTER TABLE location_refdata_audit ALTER COLUMN job_id
SET DEFAULT nextval('JOB_ID_SEQ');

ALTER TABLE location_refdata_exception ALTER COLUMN id
SET DEFAULT nextval('EXCEPTION_ID_SEQ');
