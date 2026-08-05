-- User Table
CREATE TABLE IF NOT EXISTS tb_user (
    user_id VARCHAR(50) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    user_name VARCHAR(100),
    email VARCHAR(100),
    role VARCHAR(20) DEFAULT 'USER',
    use_yn CHAR(1) DEFAULT 'Y',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- User Refresh Token Table
CREATE TABLE IF NOT EXISTS tb_user_token (
    user_id VARCHAR(50) PRIMARY KEY REFERENCES tb_user(user_id) ON DELETE CASCADE,
    refresh_token TEXT NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
