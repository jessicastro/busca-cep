package br.com.jessica.buscacep.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import br.com.jessica.buscacep.config.FeignConfig;
import br.com.jessica.buscacep.dto.EnderecoDto;

@FeignClient(name = "viaCepClient", url = "https://viacep.com.br/ws/", configuration = FeignConfig.class)
public interface ViaCepClient {

	@GetMapping("{cep}/json")
	EnderecoDto jsonBuscaEnderecoPor(@PathVariable("cep") String cep);

	@GetMapping("{cep}/xml")
	EnderecoDto xmlBuscaEnderecoPor(@PathVariable("cep") String cep);

	@GetMapping("{cep}/piped")	
	EnderecoDto pipedBuscaEnderecoPor(@PathVariable("cep") String cep);

}