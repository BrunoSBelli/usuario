package com.raposo.usuario.business.DTO;


import com.raposo.usuario.infrastructure.entity.Endereco;
import com.raposo.usuario.infrastructure.entity.Telefone;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDTO {

    private String nome;
    private String email;
    private String senha;
    private List<EnderecoDTO> enderecos;
    private List<TelefoneDTO> telefones;

}
