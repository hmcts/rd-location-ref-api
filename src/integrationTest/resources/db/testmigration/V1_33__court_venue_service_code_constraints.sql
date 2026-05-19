DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'court_venue'
          AND column_name = 'service_code'
    ) AND NOT EXISTS (
        SELECT 1
        FROM court_venue
        WHERE service_code IS NULL
    ) AND NOT EXISTS (
        SELECT epimms_id, service_code
        FROM court_venue
        GROUP BY epimms_id, service_code
        HAVING COUNT(*) > 1
    ) AND NOT EXISTS (
        SELECT 1
        FROM court_venue cv
        LEFT JOIN service s
            ON s.service_code = cv.service_code
        WHERE cv.service_code IS NOT NULL
          AND s.service_code IS NULL
    ) THEN
ALTER TABLE court_venue ADD COLUMN IF NOT EXISTS service_code VARCHAR(16);
-- Drop the existing unique constraint on (epimms_id, court_type_id)
ALTER TABLE court_venue DROP CONSTRAINT IF EXISTS court_location_unique;
-- Add a new unique constraint with epimms_id and service_code
ALTER TABLE court_venue ADD CONSTRAINT court_location_unique UNIQUE (epimms_id, service_code);
-- Add foreign key constraint for service_code
ALTER TABLE court_venue ADD CONSTRAINT court_venue_service_code_fk FOREIGN KEY (service_code) REFERENCES SERVICE (service_code);
    END IF;
END $$;
