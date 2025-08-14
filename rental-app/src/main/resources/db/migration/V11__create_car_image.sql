create table if not exists car_images (
    id serial primary key,
    car_id bigint not null,
    image_url varchar(255) not null default './images/default.png',
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);

alter table if exists car_images add constraint fk_car_images_car_id foreign key (car_id) references cars(id);