# busca-cep
API Rest middleware para busca de CEP utilizando SPRING

** ESPECIFICAÇÕES **

- Desenvolver rotina que periodicamente busque na API aberta CEP’s aleatórios. (A cada minuto, buscar 5 CEP’s aleatórios);
>> Criado na classe principal (application - BuscaCepApplication.java) função que executará rotina de busca por 5 CEP's aleatórios (na api aberta viaCep) a cada 60 segundos.

- Alterar as execuções de consultas nos tipos de retorno (XML, PIPED e JSON);
>> Na classe ViaCepClient.java foi criado 3 tipos de retorno referente a busca por CEP, em: JSON, XML e PIPED. Durante as execuções de consultas poderá ser alterado o tipo de retorno dos dados.

- Os retornos devem ser persistidos em banco de dados (preferencialmente Oracle). Guardar a data e hora que a consulta foi realizada;
>> Após consulta dos CEP's aleatórios o retorno será armazanado no banco de dados (na tabela criada "ENDERECO"). Utilizado PostgreSQL.

- Caso o CEP montado aleatoriamente já esteja persistido no banco de dados, deverá ser gerado uma nova combinação.
>> Colocado validação na função getCepValidoAleatorio (na classe de controller) para que retorne apenas um CEP que ainda não exista na tabela do banco de dados, evitando assim inclua dados repetidos.

- Criar endPoint para busca do CEP (apenas os dígitos) que deverá verificar se o CEP já está cadastrado no banco e, caso não esteja ou o tempo cadastrado seja superior a 5 minutos, deverá buscar na API aberta, persistir os dados no banco, e retornar a RESPONSE com os dados.
>> Criado o endPoint "busca" (dentro da classe controller), nele deverá ser passado o parâmetro cep (apenas os números) conforme exemplo: "http://localhost:8080/busca?cep=14407484", ao executar será validado se cep informado existe no banco de dados (caso exista e o tempo de cadastro é inferior a 5 minutos) a RESPONSE será retornada com dados do banco. Caso não exista ou tempo de cadastro é superior a 5 minutos será retornado os dados da API aberta viacep e registro será atualizado no banco de dados.

- Criar endPoint para busca de todos os CEP’s, agrupando por estado (UF);
>> Criado endPoint "consultaCep" (dentro da classe controller) exemplo "http://localhost:8080/consultaCep", SEM passar nenhum parâmetro, assim será retornado todos os CEP's cadastrados no banco de dados (ordenando/agrupando) por UF. 

- Criar endPoint para busca por estado (UF), agrupando por cidade;
>> Criado endPoint "consultaCep" (dentro da classe controller) exemplo "http://localhost:8080/consultaCep?uf=SP", ao passar a UF no parâmetro (conforme indicado no exemplo) será retornado os CEP's cadastrados no banco de dados filtrando pelo estado e (ordenando/agrupando) por UF.

- Criar endpoint para busca por cidade;
>> Criado endPoint "consultaCidade" (dentro da classe controller) exemplo "http://localhost:8080/consultaCidade?cidade=Franca", ao passar a Cidade(localidade) no parâmetro (conforme indicado no exemplo) será retornado todos os CEP's cadastrados no banco de dados filtrando pela cidade.

- A Response padrão da API deve ser em JSON;
>> TODOS os endPoints estão com o parâmetro: (produces = "application/json") na anotação @RequestMapping, como forma de garantir que o retorno seja em JSON.

**CONFIGURAÇÕES**

Spring: Versão: 2.3.3.RELEASE
Java: Versão: 11
PostgreSql: Versão: 12.4.1 (Win x64)
Obs.: Arquivo com configurações do banco (application.properties) 
      >>DataBase: jdbc:postgresql://localhost:5432/bancocep 
IMPORTANTE: Antes de executar criar base de dados "bancocep" pois o hibernate não cria o data base automático, as tabelas (e caso tenha triggers/procedures/indexes) essas sim são criadas automaticamente (de acordo com o que o projeto possuir) ao executar a application.
     >>Username: postgres
     >>password: a (configurado durante instalação do postgresql)
