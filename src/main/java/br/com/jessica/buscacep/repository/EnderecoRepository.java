package br.com.jessica.buscacep.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.jessica.buscacep.entity.Endereco;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

	List<Endereco> findByCep(String cep);
	
	List<Endereco> findByUf(String uf);
	
	List<Endereco> findByLocalidade(String cidade);

	List<Endereco> findAllByOrderByUfAsc();
}