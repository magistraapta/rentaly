create table refresh_tokens (
    id bigserial primary key,
    token varchar(255) not null,
    expiry_date timestamp not null,
    user_id bigint not null
);

alter table refresh_tokens add constraint fk_refresh_tokens_user_id foreign key (user_id) references users(id);

alter table refresh_tokens add constraint uk_refresh_tokens_token unique (token);
