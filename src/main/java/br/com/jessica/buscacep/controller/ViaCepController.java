package br.com.jessica.buscacep.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.jessica.buscacep.client.ViaCepClient;
import br.com.jessica.buscacep.dto.EnderecoDto;
import br.com.jessica.buscacep.entity.Endereco;
import br.com.jessica.buscacep.repository.EnderecoRepository;

@RestController
public class ViaCepController {

	@Autowired
	private ViaCepClient client;
	@Autowired
	private EnderecoRepository enderecoRepository;

	public void salvarEndereco(EnderecoDto enderecoDto) {
		Endereco end = new Endereco();
		end.setCep(enderecoDto.getCep());
		end.setLogradouro(enderecoDto.getLogradouro());
		end.setComplemento(enderecoDto.getComplemento());
		end.setBairro(enderecoDto.getBairro());
		end.setLocalidade(enderecoDto.getLocalidade());
		end.setUf(enderecoDto.getUf());
		end.setUnidade(enderecoDto.getUnidade());
		end.setIbge(enderecoDto.getIbge());
		end.setGia(enderecoDto.getGia());
		end.setDataHoraAtual(LocalDateTime.now());
		enderecoRepository.save(end);
	}

	@RequestMapping(value = "/busca", produces = "application/json")
	@ResponseBody()
	public String getConsultaPorCep(String cep) {
		String out = "";
		System.out.println(cep);
		if (cep == null) {
			out = "Nenhum CEP informado para consulta...";
		} else {
			if (cep.length() == 8) {
				boolean isClient = false;
				String cepConsulta = cep.substring(0, 5) + '-' + cep.substring(5, 8);
				List<EnderecoDto> bdlst = getConsulta(cepConsulta);
				if (bdlst.isEmpty()) {
					isClient = true;
				} else {
					EnderecoDto end = bdlst.get(0);
					long diferencaMin = Duration.between(end.getDataHoraAtual(), LocalDateTime.now()).toMinutes();
					System.out.println(diferencaMin);
					// Caso o tempo de cadastro for superior a 5 minutos, forçar buscar do client
					if (diferencaMin > 5) {
						isClient = true;
						/**
						 * Deletar registro com tempo de cadastro superior a 5 min, p/ incluir novamente
						 * atualizado
						 **/
						enderecoRepository.deleteById(end.getId());
					} else {
						out = end.toString();
					}
				}
				if (isClient) {
					EnderecoDto enderecoDto = client.jsonBuscaEnderecoPor(cep);
					if (enderecoDto.getCep() != null) {
						out = enderecoDto.toString();
						// Gravar Registro no Banco
						salvarEndereco(enderecoDto);
					} else {
						out = "CEP NÃO encontrado ou inválido...";
					}
				}
			} else {
				out = "CEP informado é inválido...";
			}
		}
		return out;
	}

	@RequestMapping(value = "/consulta", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody()
	public List<EnderecoDto> getConsulta(String cep) {
		System.out.println(cep);
		if (cep == null) {
			List<Endereco> enderecos = enderecoRepository.findAll();
			return EnderecoDto.converter(enderecos);
		} else {
			List<Endereco> enderecos = enderecoRepository.findByCep(cep);
			return EnderecoDto.converter(enderecos);
		}
	}

	@RequestMapping(value = "/consultaCep", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody()
	public String getConsultaCepByUf(String uf) {
		String out = "";
		if (uf == null) {
			// Caso parâmetro UF vazio consultar TODOS os CEP's agrupando por UF
			out = "[ \n";
			List<String> ufs = new ArrayList<String>();
			// Buscar TODOS os CEPS ordenando por UF
			List<Endereco> enderecos = enderecoRepository.findAllByOrderByUfAsc();
			for (Endereco e : enderecos) {
				if (!ufs.contains(e.getUf())) {
					out += "{ \n" + (char) 34 + "UF" + (char) 34 + ": " + (char) 34 + e.getUf() + (char) 34 + "\n} \n";
				}
				out += e.toString() + "\n";
				ufs.add(e.getUf());
			}
			out += "]";
		} else {
			// Retornar CEP's filtrando por UF e agrupando por Cidade
			out = "[ \n";
			List<String> cidades = new ArrayList<String>();

			// Buscar CEPS filtrando por UF
			List<Endereco> enderecos = enderecoRepository.findByUf(uf);

			// Ordenar retorno por Localidade
			Collections.sort(enderecos, new Comparator<Object>() {
				@Override
				public int compare(Object o1, Object o2) {
					Endereco a1 = (Endereco) o1;
					Endereco a2 = (Endereco) o2;
					return a1.getLocalidade().compareToIgnoreCase(a2.getLocalidade());
				}

			});
			for (Endereco e : enderecos) {
				if (!cidades.contains(e.getLocalidade())) {
					out += "{ \n" + (char) 34 + "LOCALIDADE" + (char) 34 + ": " + (char) 34 + e.getLocalidade()
							+ (char) 34 + "\n} \n";
				}
				out += e.toString() + "\n";
				cidades.add(e.getLocalidade());
			}
			out += "]";
		}
		return out;
	}

	@RequestMapping(value = "/consultaCidade", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody()
	public List<EnderecoDto> getConsultaCepByCidade(String cidade) {
		List<Endereco> enderecos = enderecoRepository.findByLocalidade(cidade);
		return EnderecoDto.converter(enderecos);
	}

	@RequestMapping(value = "/cepAleatorio", produces = "application/json")
	@ResponseBody()
	public EnderecoDto getCepValidoAleatorio() {

		String uf = "";
		String cepAleatorio = "";
		String retorno = "";
		EnderecoDto enderecoDto = new EnderecoDto();

		/**
		 * Conforme o site oficial dos correios os 5 primeiros números do CEP são
		 * divididos em: 1. Região do Estado ... 2. Sub-Região ... 3. Setor Região ...
		 * 4. Setor da região e adjacências ... 5. SubSetor (cidade) ... Cada algarismo
		 * pode estar entre 0 a 9 ... Já os 3 últimos números são o sufixo individual
		 * por localidade... - Faixa de Sufixos utilizada: 000 a 899 (exceto sufixos
		 * expeciais).
		 * 
		 */
		while (retorno.equals("")) {
			int numRegiao = getNumRegiao(uf);

			int subRegiao = getAleatorio(0, 9);

			int setorRegiao = getAleatorio(0, 9);

			int setorRegiaoAdj = getAleatorio(0, 9);

			int subSetor = getAleatorio(0, 9);

			int sufixo = getAleatorio(000, 899);
			String sufixoEndereco = String.format("%03d", new Object[] { sufixo });

			cepAleatorio = String.valueOf(numRegiao) + String.valueOf(subRegiao) + String.valueOf(setorRegiao)
					+ String.valueOf(setorRegiaoAdj) + String.valueOf(subSetor) + sufixoEndereco;

			enderecoDto = client.jsonBuscaEnderecoPor(cepAleatorio);

			if (enderecoDto.getCep() == null) {
				// Caso não encontre com Sufixo aleatório, tentar buscar com sufixo 000
				sufixoEndereco = "000";

				cepAleatorio = String.valueOf(numRegiao) + String.valueOf(subRegiao) + String.valueOf(setorRegiao)
						+ String.valueOf(setorRegiaoAdj) + String.valueOf(subSetor) + sufixoEndereco;

				enderecoDto = client.jsonBuscaEnderecoPor(cepAleatorio);

			}
			/**
			 * Caso NÃO encontre na busca forçar gerar outra possibilidade de CEP aleatório
			 * 
			 */
			if (enderecoDto.getCep() != null) {
				// Consultar cep no bd, caso não exista pode retornar true
				String cepConsulta = cepAleatorio.substring(0, 5) + '-' + cepAleatorio.substring(5, 8);
				List<EnderecoDto> bdlst = getConsulta(cepConsulta);

				if (bdlst.isEmpty()) {
					retorno = enderecoDto.toString();
				}
			}
		}
		return enderecoDto;
	}

	public static int getAleatorio(int minimo, int maximo) {
		Random random = new Random();
		return random.nextInt((maximo - minimo) + 1) + minimo;
	}

	public int getNumRegiao(String uf) {
		int numRegiao;
		switch (uf) {
		case "sp-g":
			numRegiao = 0;
			break;
		case "sp":
			numRegiao = 1;
			break;
		case "rj":
			numRegiao = 2;
			break;
		case "es":
			numRegiao = 2;
			break;
		case "mg":
			numRegiao = 3;
			break;
		case "ba":
			numRegiao = 4;
			break;
		case "se":
			numRegiao = 4;
			break;
		case "pe":
			numRegiao = 5;
			break;
		case "al":
			numRegiao = 5;
			break;
		case "pb":
			numRegiao = 5;
			break;
		case "rn":
			numRegiao = 5;
			break;
		case "ce":
			numRegiao = 6;
			break;
		case "pi":
			numRegiao = 6;
			break;
		case "ma":
			numRegiao = 6;
			break;
		case "pa":
			numRegiao = 6;
			break;
		case "am":
			numRegiao = 6;
			break;
		case "ac":
			numRegiao = 6;
			break;
		case "ap":
			numRegiao = 6;
			break;
		case "rr":
			numRegiao = 6;
			break;
		case "go":
			numRegiao = 7;
			break;
		case "to":
			numRegiao = 7;
			break;
		case "mt":
			numRegiao = 7;
			break;
		case "ms":
			numRegiao = 7;
			break;
		case "ro":
			numRegiao = 7;
			break;
		case "df":
			numRegiao = 7;
			break;
		case "pr":
			numRegiao = 8;
			break;
		case "sc":
			numRegiao = 8;
			break;
		case "rs":
			numRegiao = 9;
			break;
		default:
			numRegiao = getAleatorio(0, 9);
			break;
		}

		return numRegiao;
	}
}
