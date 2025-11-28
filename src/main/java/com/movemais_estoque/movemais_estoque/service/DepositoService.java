package com.movemais_estoque.movemais_estoque.service;

import com.movemais_estoque.movemais_estoque.model.Deposito;
import com.movemais_estoque.movemais_estoque.repository.DepositoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepositoService {
    private final DepositoRepository depositoRepository;

    public Deposito criar(Deposito deposito) {
        if (depositoRepository.existsByCodigo(deposito.getCodigo())) {
            throw new IllegalArgumentException("Código do depósito já cadastrado.");
        }
        return depositoRepository.save(deposito);
    }

    public Deposito buscarPorId(Long id) {
        return depositoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Depósito não encontrado."));
    }

    public List<Deposito> listar() {
        return depositoRepository.findAll();
    }

    public Deposito atualizar(Long id, Deposito dados) {
        Deposito dep = buscarPorId(id);

        if (!dep.getCodigo().equals(dados.getCodigo()) && depositoRepository.existsByCodigo(dados.getCodigo())) {
            throw new IllegalArgumentException("Código já está em uso.");
        }

        dep.setCodigo(dados.getCodigo());
        dep.setNome(dados.getNome());
        dep.setEndereco(dados.getEndereco());

        return depositoRepository.save(dep);
    }

    public void deletar(Long id) {
        depositoRepository.deleteById(id);
    }
}
