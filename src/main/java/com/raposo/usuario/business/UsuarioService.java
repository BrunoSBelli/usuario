package com.raposo.usuario.business;

import com.raposo.usuario.business.DTO.EnderecoDTO;
import com.raposo.usuario.business.DTO.TelefoneDTO;
import com.raposo.usuario.business.DTO.UsuarioDTO;
import com.raposo.usuario.business.converter.UsuarioConverter;
import com.raposo.usuario.infrastructure.entity.Endereco;
import com.raposo.usuario.infrastructure.entity.Telefone;
import com.raposo.usuario.infrastructure.entity.Usuario;
import com.raposo.usuario.infrastructure.exceptions.ConflictException;
import com.raposo.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.raposo.usuario.infrastructure.repository.EnderecoRepository;
import com.raposo.usuario.infrastructure.repository.TelefoneRepository;
import com.raposo.usuario.infrastructure.repository.UsuarioRepository;
import com.raposo.usuario.infrastructure.security.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private EnderecoRepository enderecoRepository;
    private TelefoneRepository telefoneRepository;


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

    public UsuarioDTO buscarUsuarioPorEmail(String email){
        try {

            return usuarioConverter.paraUsuarioDTO(usuarioRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("Email não cadastrado "+ email)));

        }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("Email não encotrado"+ email);
        }

    }

    public void deletaUsuarioPorEmail(String email){
        usuarioRepository.deleteByEmail(email);
    }


    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto){
        //Aqui buscamos email atraves do token, tirando obrigatoriedade de email
        String email = jwtUtil.extractUsername(token.substring(7));

        //verifica se foi altarado senha, caso sim ele encripta
        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null);


        //Aqui buscamos dados do Usuario no  banco
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("Email não localizado! " + email));

        //mesclou os dados que recebemos da requisicao DTO com os dados do BD
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);


        //salvou os dados do usuario convertido e depois retornou com UsuarioDTO
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));

    }

    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO){

        Endereco entity = enderecoRepository.findById(idEndereco).orElseThrow(()-> new ResourceNotFoundException("Id não encontrado! " + idEndereco));

        Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO, entity);


        return  usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }

    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO telefoneDTO){

        Telefone entity = telefoneRepository.findById(idTelefone).orElseThrow(() -> new ResourceNotFoundException("Telefone não encontrado! "+ idTelefone));

        Telefone telefone = usuarioConverter.updateTelefone(telefoneDTO, entity);

        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));

    }


}
