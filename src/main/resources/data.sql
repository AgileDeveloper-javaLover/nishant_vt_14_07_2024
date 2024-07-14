INSERT INTO users (id, name,email,password) VALUES (1, 'Rohan','rohan@gmail.com','123456');
INSERT INTO users (id, name,email,password) VALUES (2, 'Mohan','mohan@gmail.com','123456');


INSERT INTO permissions (id, name) VALUES (1, 'AllowRead');
INSERT INTO permissions (id, name) VALUES (2, 'AllowWrite');
INSERT INTO permissions (id, name) VALUES (3, 'AllowUpdate');


INSERT INTO roles (id, name) VALUES (1, 'Role1');
INSERT INTO roles (id, name) VALUES (2, 'Role2');
INSERT INTO roles (id, name) VALUES (3, 'Role3');



INSERT INTO role_permission (id, role_id,permission_id) VALUES (1, 1,1);
INSERT INTO role_permission (id, role_id,permission_id) VALUES (3, 2,2);
INSERT INTO role_permission (id, role_id,permission_id) VALUES (4, 2,3);
INSERT INTO role_permission (id, role_id,permission_id) VALUES (5, 3,1);
INSERT INTO role_permission (id, role_id,permission_id) VALUES (6, 3,2);
INSERT INTO role_permission (id, role_id,permission_id) VALUES (7, 3,3);



INSERT INTO user_role (id, role_id,user_id) VALUES (1, 1,1);
INSERT INTO user_role (id, role_id,user_id) VALUES (3, 2,2);

