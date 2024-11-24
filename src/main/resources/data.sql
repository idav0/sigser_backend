INSERT INTO user_types (id, name) SELECT 1, 'SUPERADMIN' WHERE NOT EXISTS (SELECT 1 FROM user_types WHERE id = 1);
INSERT INTO user_types (id, name) SELECT 2, 'ADMIN' WHERE NOT EXISTS (SELECT 1 FROM user_types WHERE id = 2);
INSERT INTO user_types (id, name) SELECT 3, 'TECHNICIAN' WHERE NOT EXISTS (SELECT 1 FROM user_types WHERE id = 3);
INSERT INTO user_types (id, name) SELECT 4, 'CLIENT' WHERE NOT EXISTS (SELECT 1 FROM user_types WHERE id = 4);

INSERT INTO repair_statuses (id, name) SELECT 1, 'RECEIVED' WHERE NOT EXISTS (SELECT 1 FROM repair_statuses WHERE id = 1);
INSERT INTO repair_statuses (id, name) SELECT 2, 'DIAGNOSIS' WHERE NOT EXISTS (SELECT 1 FROM repair_statuses WHERE id = 2);
INSERT INTO repair_statuses (id, name) SELECT 3, 'QUOTATION' WHERE NOT EXISTS (SELECT 1 FROM repair_statuses WHERE id = 3);
INSERT INTO repair_statuses (id, name) SELECT 4, 'WAITING_FOR_CUSTOMER_APPROVAL' WHERE NOT EXISTS (SELECT 1 FROM repair_statuses WHERE id = 4);
INSERT INTO repair_statuses (id, name) SELECT 5, 'WAITING_FOR_PARTS' WHERE NOT EXISTS (SELECT 1 FROM repair_statuses WHERE id = 5);
INSERT INTO repair_statuses (id, name) SELECT 6, 'REPAIRING' WHERE NOT EXISTS (SELECT 1 FROM repair_statuses WHERE id = 6);
INSERT INTO repair_statuses (id, name) SELECT 7, 'READY_FOR_COLLECTION' WHERE NOT EXISTS (SELECT 1 FROM repair_statuses WHERE id = 7);
INSERT INTO repair_statuses (id, name) SELECT 8, 'COLLECTED' WHERE NOT EXISTS (SELECT 1 FROM repair_statuses WHERE id = 8);

INSERT INTO device_types (id, name) SELECT 1, 'LAPTOP' WHERE NOT EXISTS (SELECT 1 FROM device_types WHERE id = 1);
INSERT INTO device_types (id, name) SELECT 2, 'DESKTOP' WHERE NOT EXISTS (SELECT 1 FROM device_types WHERE id = 2);
INSERT INTO device_types (id, name) SELECT 3, 'TABLET' WHERE NOT EXISTS (SELECT 1 FROM device_types WHERE id = 3);
INSERT INTO device_types (id, name) SELECT 4, 'SMARTPHONE' WHERE NOT EXISTS (SELECT 1 FROM device_types WHERE id = 4);
INSERT INTO device_types (id, name) SELECT 5, 'SMARTWATCH' WHERE NOT EXISTS (SELECT 1 FROM device_types WHERE id = 5);
INSERT INTO device_types (id, name) SELECT 6, 'VIDEOGAME_CONSOLE' WHERE NOT EXISTS (SELECT 1 FROM device_types WHERE id = 6);
INSERT INTO device_types (id, name) SELECT 7, 'SPEAKER' WHERE NOT EXISTS (SELECT 1 FROM device_types WHERE id = 7);

INSERT INTO users (id, email, name, lastname, phone, password, user_type_id) SELECT 1, 'superadmin', 'SUPER', 'ADMIN', '0000000000', '$2a$10$ofdE1nUvF0GJh7kkISNBdeYM2WNX/uGWF30UgLXxl.PGo4n0gvZU.', 1 WHERE NOT EXISTS (SELECT 1 FROM users WHERE id = 1);


