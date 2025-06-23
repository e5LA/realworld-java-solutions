DO $$
BEGIN
  FOR i IN 1..100 LOOP
    INSERT INTO timesheets (employee_id, hours, overtime, deductions, approved, processed)
    VALUES (
      1000 + i,                           -- employee_id (e.g., 1001, 1002, …)
      140 + (random() * 40)::int,         -- hours between 140–180
      (random() * 20)::int,               -- overtime between 0–20
      (random() * 100)::numeric(10,2),    -- deductions between 0–100
      (random() < 0.9),                   -- 90% chance approved
      FALSE                               -- processed = false
    );
  END LOOP;
END $$;