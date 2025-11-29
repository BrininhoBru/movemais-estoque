package com.movemais_estoque.movemais_estoque.config;

import com.movemais_estoque.movemais_estoque.dto.movimento_estoque.MovimentoEstoqueResponseDTO;
import com.movemais_estoque.movemais_estoque.model.MovimentoEstoque;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        Converter<MovimentoEstoque, MovimentoEstoqueResponseDTO> movimentoConverter =
                new Converter<>() {
                    @Override
                    public MovimentoEstoqueResponseDTO convert(MappingContext<MovimentoEstoque, MovimentoEstoqueResponseDTO> context) {
                        MovimentoEstoque source = context.getSource();
                        if (source == null) return null;

                        MovimentoEstoqueResponseDTO dto = new MovimentoEstoqueResponseDTO();
                        dto.setId(source.getId());
                        dto.setTipo(source.getTipo() != null ? source.getTipo().name() : null);
                        dto.setProduto(source.getProduto() != null ? source.getProduto().getNome() : null);
                        dto.setDeposito(source.getDeposito() != null ? source.getDeposito().getNome() : null);
                        dto.setQuantidade(source.getQuantidade());
                        dto.setDataHoraMovimento(source.getDataHoraMovimento());
                        dto.setObservacao(source.getObservacao());
                        dto.setUsuarioResponsavel(source.getUsuarioResponsavel() != null ? source.getUsuarioResponsavel().getNome() : null);
                        return dto;
                    }
                };

        mapper.createTypeMap(MovimentoEstoque.class, MovimentoEstoqueResponseDTO.class)
                .setConverter(movimentoConverter);

        return mapper;
    }
}