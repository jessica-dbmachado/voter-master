create table token (
  id integer identity primary key,
  voter_id integer unique not null,
  token varchar(255) not null,
  expire_date datetime not null
);
