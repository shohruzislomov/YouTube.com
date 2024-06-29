INSERT INTO profile ( name, surname, email,password, status, visible, role)
VALUES ('Adminjon', 'Adminov', 'admin5@gmail.com','JRSIpuUOBrlNMJrx2URi', 'ACTIVE', true,
        'ROLE_ADMIN') ON CONFLICT (id) DO NOTHING;