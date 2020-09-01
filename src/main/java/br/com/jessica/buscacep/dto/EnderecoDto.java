package br.com.jessica.buscacep.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import br.com.jessica.buscacep.entity.Endereco;

public class EnderecoDto {
	private Long id;
	private String cep;
	private String logradouro;
	private String complemento;
	private String bairro;
	private String localidade;
	private String uf;
	private String unidade;
	private String ibge;
	private String gia;
	private LocalDateTime dataHoraAtual = LocalDateTime.now();

	public EnderecoDto() {

	}

	public EnderecoDto(Endereco endereco) {
		this.id = endereco.getId();
		this.cep = endereco.getCep();
		this.logradouro = endereco.getLogradouro();
		this.complemento = endereco.getComplemento();
		this.bairro = endereco.getBairro();
		this.localidade = endereco.getLocalidade();
		this.uf = endereco.getUf();
		this.unidade = endereco.getUnidade();
		this.ibge = endereco.getIbge();
		this.gia = endereco.getGia();
		this.dataHoraAtual = endereco.getDataHoraAtual();
	}

	@Override
	public String toString() {
		return "{ \n" + (char) 34 + "cep" + (char) 34 + ": " + (char) 34 + cep + (char) 34 + ", \n" + (char) 34
				+ "logradouro" + (char) 34 + ": " + (char) 34 + logradouro + (char) 34 + ", \n" + (char) 34
				+ "complemento" + (char) 34 + ": " + (char) 34 + complemento + (char) 34 + ", \n" + (char) 34 + "bairro"
				+ (char) 34 + ": " + (char) 34 + bairro + (char) 34 + ", \n" + (char) 34 + "localidade" + (char) 34
				+ ": " + (char) 34 + localidade + (char) 34 + ", \n" + (char) 34 + "uf" + (char) 34 + ": " + (char) 34
				+ uf + (char) 34 + ", \n" + (char) 34 + "unidade" + (char) 34 + ": " + (char) 34 + unidade + (char) 34
				+ ", \n" + (char) 34 + "ibge" + (char) 34 + ": " + (char) 34 + ibge + (char) 34 + ", \n" + (char) 34
				+ "gia" + (char) 34 + ": " + (char) 34 + gia + (char) 34 + " \n" + '}';
	}

	// getters

	public String getCep() {
		return cep;
	}

	public Long getId() {
		return id;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public String getComplemento() {
		return complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public String getLocalidade() {
		return localidade;
	}

	public String getUf() {
		return uf;
	}

	public String getUnidade() {
		return unidade;
	}

	public String getIbge() {
		return ibge;
	}

	public String getGia() {
		return gia;
	}

	public LocalDateTime getDataHoraAtual() {
		return dataHoraAtual;
	}

	public static List<EnderecoDto> converter(List<Endereco> enderecos) {
		return enderecos.stream().map(EnderecoDto::new).collect(Collectors.toList());
	}

}
