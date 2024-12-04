use sakila;
select
  *
from
  actor;
select
  *
from
  film;
describe film;
select
  *
from
  film
where
  length = (
    select
      max(length)
    from
      film
  );
describe payment;
select
  now();
show character set;
CREATE TABLE person (
    person_id SMALLINT UNSIGNED,
    fname VARCHAR(20),
    lname VARCHAR(20),
    eye_color ENUM('BR', 'BL', 'GR'),
    birth_date DATE,
    street VARCHAR(30),
    city VARCHAR(20),
    state VARCHAR(20),
    country VARCHAR(20),
    postal_code VARCHAR(20),
    CONSTRAINT pk_person PRIMARY KEY (person_id)
  );
select
  *
from
  person;
describe sakila.customer;
select
  first_name,
  last_name
from
  customer
where
  last_name = 'zamai';
select
  *
from
  category;
select
  language_id,
  'COMMON' language_usage,
  language_id * 3.1215 lang_pi_value,
  upper(name) language
from
  language;
select
  version(),
  user(),
  database();
describe film_actor;
describe rental;
select
  c.first_name,
  c.last_name,
  date(rental_date) as rdate,
  c.customer_id
from
  rental r
  join customer c on c.customer_id = r.customer_id;

select c.first_name, c.last_name, count(*) as cnt
from customer c
  join rental r on c.customer_id = r.customer_id
group by c.first_name, c.last_name
having cnt >= 40
ORDER BY cnt DESC;

select distinct customer_id, rental_date, date(rental_date) rd
from rental
where date(rental_date) = '2005-07-05';

select *, date(rental_date) as rt from rental limit 10;

select last_name, first_name from customer
where lower(last_name) LIKE '_a%w%';

select last_name, first_name from customer
where lower(last_name) REGEXP '^.[a][a-z]*[w][a-z]*';