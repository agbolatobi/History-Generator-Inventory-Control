use TomKatDB;
create table theathes(
theathe_id int auto_increment primary key,
theathe_name varchar(200),
theathe_location varchar(200)
);

create table screens(
screen_id int auto_increment primary key,
seat_capacity int,
theathe_id int,
CONSTRAINT FK_ScreensTheathe FOREIGN KEY (theathe_id)
REFERENCES theathes(theathe_id)
);

create table movies(
movie_id int auto_increment primary key,
title varchar(200),
duration varchar(200),
movie_type varchar(200)
);

create table time_slots(
time_slot_id int auto_increment primary key,
start_time timestamp,
end_time timestamp,
screen_id int,
FOREIGN KEY (screen_id)
REFERENCES screens(screen_id)
);

create table time_slots_movies(
time_slot_id int,
movie_id int,
position_in_time_slot int,
FOREIGN KEY (time_slot_id)
REFERENCES time_slots(time_slot_id),
FOREIGN KEY (movie_id)
REFERENCES movies(movie_id)
);

create table attendances(
attendance_id int auto_increment primary key,
price int,
user_count int,
time_slot_id int,
FOREIGN KEY (time_slot_id)
REFERENCES time_slots(time_slot_id)
);