create type invoice_status as enum ('pending', 'paid', 'cancelled');
create type rental_status as enum ('pending', 'active', 'completed', 'cancelled');
alter table invoices add column invoice_status invoice_status default 'pending';
alter table invoices add column rental_status rental_status default 'pending';