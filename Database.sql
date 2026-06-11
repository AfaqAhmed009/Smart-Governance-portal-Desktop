CREATE DATABASE IF NOT EXISTS smart_governance_portal;
use smart_governance_portal;

CREATE TABLE IF NOT EXISTS cities (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS departments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL UNIQUE,
    is_system BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS city_departments (
    city_id INT,
    department_id INT,
    PRIMARY KEY (city_id, department_id),
    FOREIGN KEY (city_id) REFERENCES cities(id),
    FOREIGN KEY (department_id) REFERENCES departments(id)
);

CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,
    email VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL,
    dob DATE,
    type ENUM('global_admin','city_admin','department_admin', 'officer','citizen') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS city_admins (
    user_id INT PRIMARY KEY,
    city_id INT,
    hire_date DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (city_id) REFERENCES cities(id),
    UNIQUE (city_id) -- Ensure one city admin per city
);

CREATE TABLE IF NOT EXISTS department_admins (
    user_id INT PRIMARY KEY,
    department_id INT,
    city_id INT,
    hire_date DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (department_id) REFERENCES departments(id),
    FOREIGN KEY (city_id) REFERENCES cities(id),
    UNIQUE (department_id) -- Ensure one department admin per department
);

CREATE TABLE IF NOT EXISTS officers (
    user_id INT PRIMARY KEY,
    department_id INT,
    city_id INT,
    hire_date DATETIME,
    total_cases_solved INT DEFAULT 0,
    total_cases_pending INT DEFAULT 0,
    total_unsolved_cases INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (department_id) REFERENCES departments(id),
    FOREIGN KEY (city_id) REFERENCES cities(id)
);

CREATE TABLE IF NOT EXISTS citizens (
    user_id INT PRIMARY KEY,
    city_id INT,
    cnic VARCHAR(20) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (city_id) REFERENCES cities(id)
);



CREATE TABLE IF NOT EXISTS issues (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tracking_code VARCHAR(20) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR NOT NULL DEFAULT 'pending',
    severity VARCHAR NOT NULL DEFAULT 'low',
    priority VARCHAR NOT NULL DEFAULT 'P4', -- P1 (highest) to P4 (lowest)

    department_id INT,
    city_id INT,
    officer_id INT,
    citizen_id INT,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (officer_id) REFERENCES officers(user_id),
    FOREIGN KEY (citizen_id) REFERENCES citizens(user_id),
    FOREIGN KEY (department_id) REFERENCES departments(id),
    FOREIGN KEY (city_id) REFERENCES cities(id)
);

-- Here the policies will be pre-defined 

CREATE TABLE IF NOT EXISTS sla_policies (
    id INT PRIMARY KEY AUTO_INCREMENT,
    priority ENUM('P1','P2','P3','P4') NOT NULL UNIQUE,
    response_time_minutes INT NOT NULL,
    resolution_time_minutes INT NOT NULL,
    escalation_level VARCHAR(50), -- optional (e.g., officer → admin)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS sla_tracking (
    id INT PRIMARY KEY AUTO_INCREMENT,

    issue_id INT NOT NULL,
    officer_id INT NOT NULL,
    sla_policy_id INT NOT NULL,

    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    first_response_at TIMESTAMP NULL,
    resolved_at TIMESTAMP NULL,
    response_deadline TIMESTAMP NOT NULL,
    resolution_deadline TIMESTAMP NOT NULL,

    status ENUM('active','breached','met','resolved') DEFAULT 'active',

    is_response_breached BOOLEAN DEFAULT FALSE,
    is_resolution_breached BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (issue_id) REFERENCES issues(id),
    FOREIGN KEY (officer_id) REFERENCES officers(user_id),
    FOREIGN KEY (sla_policy_id) REFERENCES sla_policies(id)
);

CREATE TABLE IF NOT EXISTS feedbacks (
    id INT PRIMARY KEY AUTO_INCREMENT,
    issue_id INT,
    citizen_id INT,
    rating INT CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (issue_id) REFERENCES issues(id),
    FOREIGN KEY (citizen_id) REFERENCES citizens(user_id)
);

CREATE TABLE IF NOT EXISTS announcements (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    city_id INT,
    department_id INT,
    published_by INT,
    type VARCHAR(50) NOT NULL, -- e.g., 'global', 'city', 'department'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (city_id) REFERENCES cities(id),
    FOREIGN KEY (department_id) REFERENCES departments(id),
    FOREIGN KEY (published_by) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS polls (
    id INT PRIMARY KEY AUTO_INCREMENT,
    question VARCHAR(255) NOT NULL,
    city_id INT,
    department_id INT,
    type VARCHAR(50) NOT NULL, -- e.g., 'global', 'city', 'department'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (city_id) REFERENCES cities(id),
    FOREIGN KEY (department_id) REFERENCES departments(id)
);

CREATE TABLE IF NOT EXISTS poll_options (
    id INT PRIMARY KEY AUTO_INCREMENT,
    poll_id INT,
    option_text VARCHAR(255) NOT NULL,
    FOREIGN KEY (poll_id) REFERENCES polls(id)
);

CREATE TABLE IF NOT EXISTS poll_votes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    poll_id INT,
    citizen_id INT,
    option_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (poll_id) REFERENCES polls(id),
    FOREIGN KEY (citizen_id) REFERENCES citizens(user_id),
    FOREIGN KEY (option_id) REFERENCES poll_options(id),
    UNIQUE (poll_id, citizen_id) -- Ensure one vote per citizen per poll
);

CREATE TABLE IF NOT EXISTS notifications (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    message TEXT NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS discussions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    city_id INT,
    department_id INT,
    type VARCHAR(50) NOT NULL, -- e.g., 'global', 'city', 'department'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (city_id) REFERENCES cities(id),
    FOREIGN KEY (department_id) REFERENCES departments(id)
);

CREATE TABLE IF NOT EXISTS discussion_replies (
    id INT PRIMARY KEY AUTO_INCREMENT,
    discussion_id INT,
    author_id INT,
    parent_reply_id INT, -- For nested replies
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (discussion_id) REFERENCES discussions(id),
    FOREIGN KEY (author_id) REFERENCES users(id),
    FOREIGN KEY (parent_reply_id) REFERENCES discussion_replies(id)
);



CREATE TABLE IF NOT EXISTS suggestions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR NOT NULL DEFAULT 'submitted',
    city_id INT,
    citizen_id INT,
    department_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (citizen_id) REFERENCES citizens(user_id),
    FOREIGN KEY (city_id) REFERENCES cities(id),
    FOREIGN KEY (department_id) REFERENCES departments(id)
);



CREATE TABLE IF NOT EXISTS audit_logs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    user_role ENUM('global_admin','city_admin','department_admin', 'officer','citizen') NOT NULL,
    description TEXT,
    action VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);