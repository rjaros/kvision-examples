CREATE TABLE users (
  id varchar(255),
  username varchar(255),
  password varchar(255),
  linkedid varchar(255),
  serializedprofile varchar(10000),
  PRIMARY KEY (id)
);

CREATE TABLE address (
    id serial NOT NULL,
    first_name varchar(255),
    last_name varchar(255),
    email varchar(255),
    phone varchar(255),
    postal_address varchar(255),
    favourite boolean NOT NULL DEFAULT false,
    created_at timestamp,
    user_id varchar(255),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE
);
