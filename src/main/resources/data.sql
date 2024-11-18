INSERT INTO ROLES (ID, NAME) VALUES
    (1, 'ADMIN'),
    (2, 'NORMAL_USER');

INSERT INTO USERS (ID, NAME, EMAIL, LOGIN, USER_PASS) VALUES
    (1, 'admin', 'admin@example.com', 'admin', 'admin'),
    (2, 'Maria Oliveira', 'maria.oliveira@example.com', 'user2', 'pass123'),
    (3, 'Carlos Pereira', 'carlos.pereira@example.com', 'user3', 'pass123'),
    (4, 'Ana Costa', 'ana.costa@example.com', 'user4', 'pass123');

INSERT INTO USER_ROLES (USER_ID, ROLE_ID) VALUES
    (1, 1),
    (2, 2),
    (3, 2),
    (4, 2);

INSERT INTO ITEMS (NAME, DATE_LAST_CHANGE, DATE_NEXT_CHANGE, USER_ID) VALUES
    ('Óleo de Motor', '2024-12-01', '2024-12-01', 1),
    ('Escova de dentes', '2024-12-01', '2024-12-01', null),
    ('Travesseiro', '2024-12-01', '2024-12-01', 1),
    ('Vela do filtro de água', '2024-12-30', '2024-12-31', null);

