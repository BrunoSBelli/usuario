package com.raposo.usuario.business;

import com.raposo.usuario.business.DTO.UsuarioDTO;
import com.raposo.usuario.business.converter.UsuarioConverter;
import com.raposo.usuario.infrastructure.entity.Usuario;
import com.raposo.usuario.infrastructure.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
         usuario = usuarioRepository.save(usuario);
         return usuarioConverter.paraUsuarioDTO(usuario);
    }



}
