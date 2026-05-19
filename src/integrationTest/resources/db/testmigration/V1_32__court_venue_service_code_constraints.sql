DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_schema = 'public'
          AND table_name = 'backup_court_venue'
    ) AND NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'court_venue'
          AND column_name = 'service_code'
    ) THEN
        ALTER TABLE court_venue ADD COLUMN IF NOT EXISTS service_code VARCHAR(16);
    END IF;
END $$;
