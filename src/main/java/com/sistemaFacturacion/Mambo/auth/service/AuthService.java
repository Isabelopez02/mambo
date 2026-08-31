package com.sistemaFacturacion.Mambo.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sistemaFacturacion.Mambo.auth.dto.LoginRequest;
import com.sistemaFacturacion.Mambo.auth.dto.RegisterRequest;
import com.sistemaFacturacion.Mambo.auth.jwt.JwtService;
import com.sistemaFacturacion.Mambo.auth.response.AuthResponse;
import com.sistemaFacturacion.Mambo.entity.Repository.ClienteRepository;
import com.sistemaFacturacion.Mambo.entity.Repository.RolRepository;
import com.sistemaFacturacion.Mambo.entity.Repository.UsuarioRepository;
import com.sistemaFacturacion.Mambo.entity.model.Usuario;
import com.sistemaFacturacion.Mambo.entity.model.cliente;
import com.sistemaFacturacion.Mambo.entity.model.rol;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository userRepository;
    private final ClienteRepository clienteRepository;
    private final RolRepository roleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    // 🚀 Inicialización automática en Base de Datos: Insertar Administrador admin@gmail.com / admin1234
    @PostConstruct
    public void initAdminUser() {
        try {
            if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
                rol adminRole = roleRepository.findByNombre("ADMIN")
                        .orElseGet(() -> {
                            rol r = new rol();
                            r.setNombre("ADMIN");
                            return roleRepository.save(r);
                        });

                Usuario admin = new Usuario();
                admin.setEmail("admin@gmail.com");
                admin.setContra(passwordEncoder.encode("admin1234"));
                admin.setNombreCompleto("Administrador General");
                admin.setNumeroDocumento("77777777");
                admin.setTelefono("999999999");
                admin.setEnabled(true);
                admin.setRol(adminRole);

                userRepository.save(admin);
            }
        } catch (Exception e) {
            System.err.println("Nota inicialización usuario admin: " + e.getMessage());
        }
    }

    // LOGIN CON CORREO Y CONTRASEÑA EN BD
    public AuthResponse login(LoginRequest request) {
        String emailOrDoc = request.getEmail();
        
        // 🔹 1. Buscar Administrador en Usuario Repository por email o número de documento
        Usuario user = userRepository.findByEmail(emailOrDoc)
                .orElseGet(() -> userRepository.findByNumeroDocumento(emailOrDoc).orElse(null));

        if (user != null) {
            // Verificar contraseña enviada contra el hash BCrypt de la BD
            if (!passwordEncoder.matches(request.getPassword(), user.getContra())) {
                throw new RuntimeException("Contraseña incorrecta para " + emailOrDoc);
            }

            String token = jwtService.getToken(user);
            String userRol = user.getRol() != null ? user.getRol().getNombre() : "ADMIN";
            String userNombre = user.getNombreCompleto() != null ? user.getNombreCompleto() : user.getEmail();

            return AuthResponse.builder()
                    .token(token)
                    .numeroDocumento(user.getEmail())
                    .nombre(userNombre)
                    .rol(userRol)
                    .build();
        }

        // 🔹 2. Buscar en Cliente por email o número de documento
        cliente c = clienteRepository.findByEmail(emailOrDoc)
                .orElseGet(() -> clienteRepository.findByNumeroDocumento(emailOrDoc).orElse(null));

        if (c != null) {
            if (!passwordEncoder.matches(request.getPassword(), c.getContra())) {
                throw new RuntimeException("Contraseña incorrecta para " + emailOrDoc);
            }
            String token = jwtService.getTokenCliente(c);
            String clienteRol = c.getRol() != null ? c.getRol().getNombre() : "CLIENTE";
            String clienteNombre = c.getNombre() != null ? c.getNombre() : c.getEmail();

            return AuthResponse.builder()
                    .token(token)
                    .numeroDocumento(c.getEmail())
                    .nombre(clienteNombre)
                    .rol(clienteRol)
                    .build();
        }

        throw new UsernameNotFoundException("Usuario no registrado en la Base de Datos");
    }

    // REGISTRO DE NUEVO ADMINISTRADOR EN BD
    public AuthResponse register(RegisterRequest request) {
        rol adminRole = roleRepository.findByNombre("ADMIN")
                .orElseGet(() -> {
                    rol r = new rol();
                    r.setNombre("ADMIN");
                    return roleRepository.save(r);
                });

        Usuario newAdmin = new Usuario();
        newAdmin.setEmail(request.getEmail());
        newAdmin.setContra(passwordEncoder.encode(request.getPassword()));
        newAdmin.setNombreCompleto(request.getNombre() != null ? request.getNombre() : "Admin " + request.getEmail());
        newAdmin.setNumeroDocumento("DOC-" + System.currentTimeMillis());
        newAdmin.setTelefono("900000000");
        newAdmin.setEnabled(true);
        newAdmin.setRol(adminRole);

        userRepository.save(newAdmin);

        String token = jwtService.getToken(newAdmin);
        return AuthResponse.builder()
                .token(token)
                .numeroDocumento(newAdmin.getEmail())
                .nombre(newAdmin.getNombreCompleto())
                .rol(adminRole.getNombre())
                .build();
    }
}
