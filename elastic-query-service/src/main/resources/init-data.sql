CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


INSERT INTO public.users(
    id, username, firstname, lastname)
VALUES ('edb1ccd0-5afd-4925-b4a5-de813461b9aa', 'app_user', 'Standard', 'User');
INSERT INTO public.users(
    id, username, firstname, lastname)
VALUES ('14a7ac37-339f-40fa-ab96-f3f1091e35e1', 'app_admin', 'Admin', 'User');
INSERT INTO public.users(
    id, username, firstname, lastname)
VALUES ('49ca2a06-2b6a-43dd-8227-1ca0dedb0eb8', 'app_super_user', 'Super', 'User');


insert into documents(id, document_id)
values ('c1df7d01-4bd7-40b6-86da-7e2ffabf37f7', 1);
insert into documents(id, document_id)
values ('f2b2d644-3a08-4acb-ae07-20569f6f2a01', 2);
insert into documents(id, document_id)
values ('90573d2b-9a5d-409e-bbb6-b94189709a19', 3);

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(),'edb1ccd0-5afd-4925-b4a5-de813461b9aa', 'c1df7d01-4bd7-40b6-86da-7e2ffabf37f7', 'READ');

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(),'14a7ac37-339f-40fa-ab96-f3f1091e35e1', 'c1df7d01-4bd7-40b6-86da-7e2ffabf37f7', 'READ');

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(),'14a7ac37-339f-40fa-ab96-f3f1091e35e1', 'f2b2d644-3a08-4acb-ae07-20569f6f2a01', 'READ');

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(), '14a7ac37-339f-40fa-ab96-f3f1091e35e1', '90573d2b-9a5d-409e-bbb6-b94189709a19', 'READ');

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(), '49ca2a06-2b6a-43dd-8227-1ca0dedb0eb8', 'c1df7d01-4bd7-40b6-86da-7e2ffabf37f7', 'READ');


