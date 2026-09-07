# Sistema de Suporte Tecnico - SENAI

Projeto desenvolvido como parte do curso livre de Desenvolvimento Back-end do SENAI.

Aplicacao web para abertura e gerenciamento de chamados de suporte tecnico (Informatica, Eletrica e Zeladoria), permitindo que qualquer pessoa registre um chamado e que tecnicos autenticados acompanhem, assumam, editem, concluam ou excluam solicitacoes atraves de um painel administrativo.

## Funcionalidades

### Area Publica
- Pagina inicial com apresentacao do sistema.
- Abertura de chamado (solicitacao) sem necessidade de login, informando NIF, nome do solicitante, sala, codigo de patrimonio, tipo e descricao do problema.
- Cadastro de novos tecnicos.
- Autenticacao (login) de tecnicos.

### Painel do Tecnico (Area Autenticada)
- Listagem geral de chamados com suporte a filtros por tipo de problema, status e nome do solicitante.
- Atribuicao de chamado pendente a um tecnico responsavel, com inclusao de observacoes.
- Edicao de dados cadastrais do chamado.
- Conclusao de chamados em andamento.
- Exclusao de chamados com etapa de confirmacao.

### Seguranca e Tratamento de Erros
- Tratamento global de excecoes (recursos nao encontrados, violacoes de regras de negocio) com redirecionamento para interface amigavel de erro.
- Armazenamento seguro de senhas utilizando hash BCrypt.
- Criacao automatica e idempotente das tabelas no banco de dados durante a inicializacao.

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.2.0
  - Spring Web (MVC)
  - Spring Data JPA (Hibernate)
  - Spring Security (Autenticacao baseada em sessao/formulario)
  - Thymeleaf + thymeleaf-extras-springsecurity6
  - Bean Validation (spring-boot-starter-validation)
- MySQL (Driver mysql-connector-j)
- Apache Maven (com Maven Wrapper)

## Arquitetura do Sistema

O projeto segue uma arquitetura em camadas baseada no padrao Spring MVC:

```
src/main/java/com/senai/suporte/suporte
|-- config/          # Configuracoes de seguranca, datasource e exception handling
|-- controller/      # Controladores MVC e manipulacao de requisicoes
|-- exception/       # Classes de excecao customizadas
|-- model/           # Entidades JPA (Solicitacao, Tecnico, PainelTecnico)
|-- repository/      # Interfaces de persistencia (Spring Data JPA)
`-- service/         # Camada de servicos e regras de negocio
```

### Entidades Principais

| Entidade | Tabela | Descricao |
|---|---|---|
| Solicitacao | solicitacao | Registro do chamado aberto, contendo tipo (INFORMATICA, ELETRICA, ZELADORIA) e status (PENDENTE, EM_ANDAMENTO, CONCLUIDO). |
| Tecnico | tecnicos | Entidade responsavel pelos dados e autenticacao dos tecnicos de suporte. |
| PainelTecnico | painel_tecnico | Vinculo entre um chamado assumido e o respectivo tecnico responsavel, com historico de observacoes. |

## Banco de Dados (MySQL)

A configuracao de conexao esta centralizada em `src/main/java/com/senai/suporte/suporte/config/DataConfiguration.java`.

- URL de Conexao: `jdbc:mysql://localhost:3306/suporte?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=America/Sao_Paulo`
- Usuario Padrao: `root`
- Variavel de Ambiente de Senha: `DB_PASSWORD` (valor padrao de fallback: `senai@126`)

O schema `suporte` e criado automaticamente se nao existir. A infraestrutura valida a existencia previa das tabelas (`solicitacao`, `tecnicos`, `painel_tecnico`) antes da emissao de instrucoes DDL pelo Hibernate.

*Nota de Seguranca: Por se tratar de um projeto didatico, as credenciais estao pre-configuradas. Em ambientes de producao, todas as credenciais devem ser injetadas exclusivamente via variaveis de ambiente e gerenciadores de segredos.*

## Pre-requisitos

- Java Development Kit (JDK) 21 instalado e configurado nas variaveis de ambiente.
- Instancia do MySQL Server ativa (porta padrao 3306).
- Git para controle de versao.

## Como Executar a Aplicacao

1. Clone o repositorio:
   ```bash
   git clone https://github.com/erickgabrielm/suporte.git
   cd suporte
   ```

2. Certifique-se de que o servico do MySQL esta ativo e acessivel.

3. (Opcional) Configure a senha do banco caso divirja do padrao:
   - Linux/macOS:
     ```bash
     export DB_PASSWORD=sua_senha_aqui
     ```
   - Windows (Command Prompt):
     ```cmd
     set DB_PASSWORD=sua_senha_aqui
     ```
   - Windows (PowerShell):
     ```powershell
     $env:DB_PASSWORD="sua_senha_aqui"
     ```

4. Inicie o servidor da aplicacao:
   - Linux/macOS:
     ```bash
     ./mvnw spring-boot:run
     ```
   - Windows:
     ```cmd
     mvnw.cmd spring-boot:run
     ```

5. Acesso web:
   Acesse `http://localhost:8081` no navegador.

## Rotas da Aplicacao

| Metodo HTTP | Rota | Nivel de Acesso | Descricao |
|---|---|---|---|
| GET | / | Publico | Pagina inicial de apresentacao |
| GET / POST | /solicitacao | Publico | Formulario e submissao de novo chamado |
| GET | /login | Publico | Formulario de autenticacao |
| GET / POST | /cadastro | Publico | Cadastro de novos tecnicos |
| GET | /painel | Autenticado | Painel com listagem e filtros de chamados |
| GET / POST | /painel/assumir/{id} | Autenticado | Atribuicao de chamado ao tecnico autenticado |
| POST | /painel/concluir/{id} | Autenticado | Atualizacao do status do chamado para concluido |
| GET / POST | /painel/editar/{id} | Autenticado | Formulario e persistencia de edicao do chamado |
| GET / POST | /painel/excluir/{id} | Autenticado | Confirmacao e remocao de registro de chamado |

## Execucao de Testes

Para rodar a suite de testes automatizados do projeto:

```bash
./mvnw test
```

## Propostas de Evolucao

- Externalizacao completa das credenciais de banco para `application.properties` ou variaveis de ambiente.
- Implementacao de controle de acesso baseado em papeis e permissoes (RBAC / Roles) no Spring Security.
- Ampliacao da cobertura de testes unitarios e de integracao na camada de servicos.
- Implementacao de paginacao e ordenacao dinamica na listagem de chamados.

## Contexto Academico

Projeto desenvolvido para consolidacao de competencias tecnicas no curso de Desenvolvimento Back-end do SENAI, com foco em desenvolvimento web em Java, persistencia com Spring Data JPA/Hibernate, seguranca com Spring Security e arquitetura em camadas no ecossistema Spring Boot.
