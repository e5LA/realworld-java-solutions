CREATE TABLE timesheets (
    id SERIAL PRIMARY KEY,
    employee_id INT NOT NULL,
    hours DECIMAL(5,2) NOT NULL,
    overtime DECIMAL(5,2) DEFAULT 0,
    deductions DECIMAL(10,2) DEFAULT 0,
    approved BOOLEAN DEFAULT FALSE,
    processed BOOLEAN DEFAULT FALSE
);

CREATE TABLE payments (
    id SERIAL PRIMARY KEY,
    employee_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    paid_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);