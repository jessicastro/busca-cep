package br.com.jessica.buscacep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import br.com.jessica.buscacep.controller.ViaCepController;
import br.com.jessica.buscacep.dto.EnderecoDto;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EntityScan(basePackages = {"br.com.jessica.buscacep.entity"})
public class BuscaCepApplication {

	@Autowired
	private ViaCepController controller;

	public static void main(String[] args) {
		SpringApplication.run(BuscaCepApplication.class, args);
	}

	@Scheduled(fixedDelay = 60000)
	public void searchCep() {
		for (int i = 1; i <= 5; i++) {
			EnderecoDto ceps = controller.getCepValidoAleatorio();
			controller.salvarEndereco(ceps);
			System.out.println(ceps);
		}
	}

}
