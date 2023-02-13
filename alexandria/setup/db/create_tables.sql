create table version(
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(35),
                        version VARCHAR(35)
);

create table icd10(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    version BIGINT NOT NULL,
    code VARCHAR(25) NOT NULL,
    description VARCHAR(25) NOT NULL,
    FOREIGN KEY (version) references version(id)
);

