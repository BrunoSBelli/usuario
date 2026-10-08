package com.raposo.usuario.business;

import com.raposo.usuario.business.DTO.UsuarioDTO;
import com.raposo.usuario.business.converter.UsuarioConverter;
import com.raposo.usuario.infrastructure.entity.Usuario;
import com.raposo.usuario.infrastructure.exceptions.ConflictException;
import com.raposo.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.raposo.usuario.infrastructure.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
         usuario = usuarioRepository.save(usuario);
         return usuarioConverter.paraUsuarioDTO(usuario);
    }

    public boolean verificaEmailExistente(String email){
        return usuarioRepository.existsByEmail(email);
    }

    public void emailExiste(String email){
        try {
            boolean existe = verificaEmailExistente(email);
            if(existe){
                throw new ConflictException("Email já cadastrado"+ email);
            }
        } catch (ConflictException e) {
            throw new ConflictException("Email já cadastrado"+ e.getCause());
        }
    }

    public Usuario buscarUsuarioPorEmail(String email){
        return usuarioRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("Email não cadastrado "+ email));
    }

    public void deletaUsuarioPorEmail(String email){
        usuarioRepository.deleteByEmail(email);
    }




}
