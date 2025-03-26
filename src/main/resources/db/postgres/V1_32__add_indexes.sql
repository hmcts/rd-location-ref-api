CREATE INDEX IF NOT EXISTS idx_building_location_epimms_id ON locrefdata.building_location(epimms_id);
CREATE INDEX IF NOT EXISTS idx_building_location_name ON locrefdata.building_location(UPPER(building_location_name));
CREATE INDEX IF NOT EXISTS idx_building_location_status ON locrefdata.building_location(UPPER(building_location_status));
CREATE INDEX IF NOT EXISTS idx_building_location_cluster_id ON locrefdata.building_location(cluster_id);
CREATE INDEX IF NOT EXISTS idx_building_location_region_id ON locrefdata.building_location(region_id);
CREATE INDEX IF NOT EXISTS idx_court_venue_epimms_id ON locrefdata.court_venue(epimms_id);
CREATE INDEX IF NOT EXISTS idx_upper_court_name ON locrefdata.court_venue(UPPER(court_name));
CREATE INDEX IF NOT EXISTS idx_upper_site_name ON locrefdata.court_venue(UPPER(site_name));
CREATE INDEX IF NOT EXISTS idx_upper_postcode ON locrefdata.court_venue(UPPER(postcode));
CREATE INDEX IF NOT EXISTS idx_upper_court_address ON locrefdata.court_venue(UPPER(court_address));
CREATE INDEX IF NOT EXISTS idx_court_type_status ON locrefdata.court_venue (court_type_id, court_status);
CREATE INDEX IF NOT EXISTS idx_epimms_court_type_status ON locrefdata.court_venue (epimms_id, court_type_id, court_status);
CREATE INDEX IF NOT EXISTS idx_region_status ON locrefdata.court_venue (region_id, court_status);
CREATE INDEX IF NOT EXISTS idx_upper_building_name ON locrefdata.building_location (UPPER(building_location_name));
CREATE INDEX IF NOT EXISTS idx_status_open ON locrefdata.building_location (building_location_status);
CREATE INDEX IF NOT EXISTS idx_cluster_id ON locrefdata.building_location (cluster_id);