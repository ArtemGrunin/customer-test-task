CREATE TABLE IF NOT EXISTS customers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    created BIGINT NOT NULL,
    updated BIGINT NOT NULL,
    full_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(14),
    is_active BOOLEAN NOT NULL
);

-- Rollback script:
-- DROP TABLE IF EXISTS customers;
