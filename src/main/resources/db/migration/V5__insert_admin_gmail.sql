-- Migration V5: Insertar Administrador admin@gmail.com con contraseña admin1234 en la Base de Datos
INSERT INTO usuarios (
    nombre_completo,
    tipo_documento,
    rol_id,
    numero_documento,
    email,
    telefono,
    contra,
    enabled
) VALUES (
    'Administrador General',
    'DNI',
    1,
    '77777777',
    'admin@gmail.com',
    '999999999',
    '$2a$10$dxb2fRyWeqA3POOdRlm4OOJpPTalHG/TIGnyLXTDjSp9nOXsbWZUS',
    true
) ON DUPLICATE KEY UPDATE 
    email = 'admin@gmail.com',
    contra = '$2a$10$dxb2fRyWeqA3POOdRlm4OOJpPTalHG/TIGnyLXTDjSp9nOXsbWZUS';
