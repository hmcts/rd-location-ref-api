UPDATE court_venue
SET welsh_court_name = 'Llys Sirol Caerdydd',
    uprn = '100070123456',
    venue_ou_code = 'VOU123456',
    mrd_building_location_id = 'MRD-BLD-385',
    mrd_venue_id = 'MRD-123456789',
    service_url = 'https://service.example.gov.uk',
    fact_url = 'https://fact.example.gov.uk',
    court_status_code = 'OPEN',
    open_date = '2021-07-05',
    parent_id = null,
    district_registry_venue_id = null,
    appeal_centre_venue_id = null,
    contact_email = 'contact@example.gov.uk',
    breathing_space_email = 'breathing.space@example.gov.uk',
    is_district_registry = 'N',
    is_appeal_centre = 'N'
WHERE court_venue_id = 15
  AND epimms_id = '123463';

INSERT INTO court_venue_name (mrd_venue_id, court_name_type, language_code, name_desc)
VALUES
    ('MRD-123456789', 'SITE', 'EN', 'Aberdeen Tribunal Hearing Centre 7'),
    ('MRD-123456789', 'COURT', 'EN', 'ABERDEEN TRIBUNAL HEARING CENTRE 23'),
    ('MRD-123456789', 'COURT', 'CY', 'Llys Sirol Caerdydd')
ON CONFLICT (mrd_venue_id, court_name_type, language_code) DO NOTHING;

INSERT INTO address (mrd_venue_id, address_type, language_code, address, post_code, uprn)
VALUES
    ('MRD-123456789', 'MAILING', 'EN', 'AB7, 54 HUNTLY STREET, ABERDEEN', 'AB11 1TY', '100070123456')
ON CONFLICT (mrd_venue_id, address_type, language_code) DO NOTHING;

INSERT INTO contact_details (mrd_venue_id, contact_method_code, contact_type_code, contact_value)
VALUES
    ('MRD-123456789', 'EMAIL', 'CONTACT_SERVICE', 'contact@example.gov.uk'),
    ('MRD-123456789', 'EMAIL', 'BREATHING_SPACE', 'breathing.space@example.gov.uk')
ON CONFLICT (mrd_venue_id, contact_method_code, contact_type_code) DO NOTHING;

INSERT INTO court_use_mapping (mrd_venue_id, use_type_code)
VALUES
    ('MRD-123456789', 'CASE_MANAGEMENT'),
    ('MRD-123456789', 'HEARING')
ON CONFLICT (mrd_venue_id, use_type_code) DO NOTHING;

INSERT INTO reference_codes (mrd_venue_id, reference_code_type, reference_code)
VALUES
    ('MRD-123456789', 'VENUE_OU_CODE', 'VOU123456')
ON CONFLICT (mrd_venue_id, reference_code_type, reference_code) DO NOTHING;

INSERT INTO court_venue_url (mrd_venue_id, url_type, url)
VALUES
    ('MRD-123456789', 'SERVICE', 'https://service.example.gov.uk'),
    ('MRD-123456789', 'FACT', 'https://fact.example.gov.uk')
ON CONFLICT (mrd_venue_id, url_type) DO NOTHING;
