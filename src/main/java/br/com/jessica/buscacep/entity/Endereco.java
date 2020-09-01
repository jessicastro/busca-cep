package br.com.jessica.buscacep.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Endereco")
public class Endereco {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getLocalidade() {
		return localidade;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public String getIbge() {
		return ibge;
	}

	public void setIbge(String ibge) {
		this.ibge = ibge;
	}

	public String getGia() {
		return gia;
	}

	public void setGia(String gia) {
		this.gia = gia;
	}

	public LocalDateTime getDataHoraAtual() {
		return dataHoraAtual;
	}

	public void setDataHoraAtual(LocalDateTime dataHoraAtual) {
		this.dataHoraAtual = dataHoraAtual;
	}

}
