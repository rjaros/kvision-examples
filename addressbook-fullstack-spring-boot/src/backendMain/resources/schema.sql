CREATE TABLE IF NOT EXISTS users (
  id serial NOT NULL,
  username varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  name varchar(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE(username)
);

CREATE TABLE IF NOT EXISTS address (
    id serial NOT NULL,
    first_name varchar(255),
    last_name varchar(255),
    email varchar(255),
    phone varchar(255),
    postal_address varchar(255),
    favourite boolean NOT NULL DEFAULT false,
    created_at timestamp with time zone,
    user_id int NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE
);
