package br.com.igor.loja_api.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.igor.loja_api.modelo.Venda;

// a "gaveta das comandas": save, findById, findAll... já vêm prontos do JpaRepository
// Long = tipo do id da Venda
public interface VendaRepository extends JpaRepository<Venda, Long> {
}
