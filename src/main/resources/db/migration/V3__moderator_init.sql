INSERT INTO profile ( name, surname, email,password, status, visible, role)
VALUES ('Moderator', 'Moderator', 'moderator@gmail.com','JRSIpuUOBrlNMJrx2URi', 'ACTIVE', true,
        'ROLE_MODERATOR') ON CONFLICT (id) DO NOTHING;
SELECT setval('profile_id_seq', max(id))
FROM profile;