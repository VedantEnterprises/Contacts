CREATE SCHEMA blink7_contacts DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

--
-- Contact
--
CREATE TABLE blink7_contacts.contact (
	id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    f_name VARCHAR(30) NOT NULL,
    l_name VARCHAR(30) NOT NULL,
    m_name VARCHAR(30),
    birthday DATE,
    sex ENUM('male', 'female'),
    nationality VARCHAR(50),
    marital_status VARCHAR(20),
    site VARCHAR(200),
    email VARCHAR (50),
    current_job VARCHAR(100),
    address_id INT UNSIGNED,
    avatar VARCHAR(50),
    delete_flag BOOL,
    delete_date TIMESTAMP,
    INDEX (id, address_id),
    PRIMARY KEY (id)
)
ENGINE=InnoDB 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_general_ci;

--
-- Address
--
create table blink7_contacts.address (
	id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    country VARCHAR(50),
    city VARCHAR(50),
    detail_address VARCHAR(100),
    zip INT,
    INDEX (id),
    PRIMARY KEY (id)
)
ENGINE=InnoDB 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_general_ci;

--
-- Phone
--
create table blink7_contacts.phone (
	id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    contact_id INT UNSIGNED NOT NULL,
    country_code INT(3) NOT NULL,
    operator_code INT(2) NOT NULL,
    phone_number INT(7) NOT NULL,
    phone_type ENUM('home', 'mobile') NOT NULL,
    comment_text VARCHAR(200),
    PRIMARY KEY (id),
    INDEX (contact_id),
    FOREIGN KEY (contact_id)
		REFERENCES blink7_contacts.contact(id)
        ON DELETE CASCADE
)
ENGINE=InnoDB 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_general_ci;

--
-- Attachment
--
create table blink7_contacts.attachment (
	id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    contact_id INT UNSIGNED NOT NULL,
    file_uuid VARCHAR(50) NOT NULL,
    file_name VARCHAR (255) NOT NULL,
    upload_date TIMESTAMP NOT NULL,
    comment_text VARCHAR(200),
    PRIMARY KEY (id),
    INDEX (contact_id),
    FOREIGN KEY (contact_id)
		REFERENCES blink7_contacts.contact(id)
        ON DELETE CASCADE
)
ENGINE=InnoDB 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_general_ci;
