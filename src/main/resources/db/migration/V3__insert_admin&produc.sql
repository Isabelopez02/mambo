INSERT INTO usuarios (
    nombre_completo,
    tipo_documento,
    rol_id,
    numero_documento,
    email,
    telefono,
    contra
) VALUES
(
    'Administrador General',
    'DNI',
    1,
    '00000001',
    'admin@gmail.com',
    '987654321',
    '$2a$10$dxb2fRyWeqA3POOdRlm4OOJpPTalHG/TIGnyLXTDjSp9nOXsbWZUS' /* admin1234 */
),
(
    'Vendedor Principal',
    'DNI',
    2,
    '00000002',
    'vendedor@mambo.com',
    '912345678',
    '$2a$10$dxb2fRyWeqA3POOdRlm4OOJpPTalHG/TIGnyLXTDjSp9nOXsbWZUS' /* vendedor */
);
