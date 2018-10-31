create table voter (
  id integer identity primary key,
  email varchar(255) unique not null,
  name varchar(255) not null,
  password varchar(255) not null
);
