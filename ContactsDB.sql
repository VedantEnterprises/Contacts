--
-- Contact
--
create table contact (
	id int not null auto_increment,
    f_name varchar(30) not null,
    l_name varchar(30) not null,
    m_name varchar(30),
    birthday datetime,
    sex char(1), -- 'M' for male and 'F' for female
    nationality varchar(50),
    marital_status varchar(20),
    site varchar(200),
    email varchar (50),
    current_job varchar(100),
    address_id int,
    avatar varchar(120),
    index (id, address_id),
    primary key (id)
) engine=InnoDB;

--
-- Address
--
create table address (
	id int not null auto_increment,
    country varchar(50),
    city varchar(50),
    detail_address varchar(100),
    zip int,
    index (id),
    primary key (id)
) engine=InnoDB;

--
-- Phone
--
create table phone (
	id int not null auto_increment,
    contact_id int not null,
    country_code int not null,
    operator_code int not null,
    phone_number int not null,
    phone_type char(1) not null, -- 'H' for home and 'M' for mobile
    comment_text varchar(200),
    primary key(id),
    index (contact_id),
    foreign key (contact_id)
		references contact(id)
        on delete cascade
) engine=InnoDB;

--
-- Attachment
--
create table attachment (
	id int not null auto_increment,
    contact_id int not null,
    file_name varchar (100) not null,
    upload_date datetime not null,
    comment_text varchar(200),
    full_file_name varchar(120) not null,
    primary key(id),
    index (contact_id),
    foreign key (contact_id)
		references contact(id)
        on delete cascade
) engine=InnoDB;