
# Case Técnico: Integração com HubSpot 💚

Repositório destinado à entrega do Case Técnico: Integração com HubSpot.

Este projeto é uma API REST desenvolvida em Java com Spring Boot, que realiza integração com a API do HubSpot utilizando o fluxo de autenticação OAuth 2.0.

A aplicação permite gerar a URL de autorização, tratar o callback com o código de autorização, criar contatos no CRM e escutar eventos de criação via Webhook.

## 🚀 Como Executar Localmente

### Pré-requisitos

- Possuir o Java 21+ instalado
- Possuir o [Docker](https://www.docker.com) instalado
- Ter uma conta criada na plataforma da [HubSpot](https://br.hubspot.com).

### Etapas

1. Clonar o repositório:
   ```bash
   git clone https://github.com/Mateusvff/meetime-hubspot-integration.git
   cd meetime-hubspot-integration
   ```

2. Configurar as variáveis de ambiente no arquivo `.env`:
   <br> <br>
   **Observação:**
   Para facilitar a execução da aplicação por parte dos avaliadores, este projeto já acompanha um arquivo `.env` com as credenciais de uma conta de desenvolvedor válidas para testes.
   <br> <br>
   **Importante:** Essas credenciais estão atreladas a uma conta de desenvolvedor do HubSpot criada especificamente para este desafio, com permissões adequadas para a criação de contatos e escuta de webhooks.

   ```env
   HUBSPOT_CLIENT_ID=xxx
   HUBSPOT_CLIENT_SECRET=xxx
   HUBSPOT_REDIRECT_URI=xxx
   DATASOURCE_URL=xxx
   POSTGRES_DB=xxx
   POSTGRES_USER=xxx
   POSTGRES_PASSWORD=xxx
   CLIENT_ID=xxx
   CLIENT_SECRET=xxx
   REDIRECT_URL=xxx
   SCOPE=xxx
   HUBSPOT_API_URL=xxx
   ```

3. Subir o ambiente com Docker:
   ```bash
   docker-compose up --build
   ```

4. Aplicação está configurada no endereço `http://localhost:8080`. Pronto para testar a aplicação 😊 

**Opcional:**
5. Configurar o **_LocalTunnel_** para recebimento do webhook localmente:
   <br><br>
   A aplicação precisa estar acessível publicamente para que os webhooks do HubSpot funcionem corretamente. Para isso, utilizei o [LocalTunnel](https://github.com/localtunnel/localtunnel) para criar uma URL pública temporária e validar o recebimento do webhook em ambiente de desenvolvimento.
 
   ```bash
    # Verifique se o Node.js está instalado
     node -v
     npx -v
   ```
   Caso o Node.js não esteja instalado, instale-o em: [Download Node.js](https://nodejs.org/)

   #### Comando para expor a aplicação:
   ```bash
   npx localtunnel --port 8080 --subdomain meetime-int-hubspot-v1
   ```

   Isso gerará uma URL do tipo:
   ```
   https://meetime-int-hubspot-v1.loca.lt
   ```
   **Importante:** Utilize essa URL para a configuração do webhook no HubSpot. Caso esteja utilizando as credenciais já configuradas no arquivo `.env`, não será preciso configurar nada no HubSpot.

<br>

## 📝 Documentação Swagger

Após a execução da aplicação, a documentação Swagger estará disponível nos endereços:
> http://localhost:8080/swagger-ui/index.html#/

> https://meetime-int-hubspot-v1.loca.lt/swagger-ui/index.html#/

<br>

## ⏩ Testando a aplicação
Após a execução da aplicação através do comando `docker-compose up --build` e a criação correta dos containeres, a aplicação estará pronta para ser testada.

### 1. Gerar a URL de autorização

```bash
   curl --location 'localhost:8080/oauth/authorize'
```

Ou, se estiver usando o localtunnel, conforme especificado no Passo 5:

```bash
   curl --location 'https://meetime-int-hubspot-v1.loca.lt/oauth/authorize'
```

Ao clicar na URL gerada, será solicito uma autenticação por parte do usuário.

### 2. Recebimento do Callback e troca pelo Token de Acesso
Após ter aceitado a vinculação com o aplicativo, a HubSpot irá enviar o código de autorização para o endpoint `/oauth/callback` através de um método **_GET_**.
A aplicação então irá realizar uma chamada para a HubSpot solicitando a troca do código de autorização pelo Token de Acesso e o gravará em uma tabela no banco de dados `TOKEN_INFORMATION`

Para visualizar o recebimento do callback, o log da aplicação/container pode ser consultado. <br>
Além disso, pode-se visualizar as informações do token através do comando:

```bash
  docker exec -it postgres_db psql -U nome_do_usuario -d nome_do_banco
  select * from token_information;
```

Caso esteja utilizando as credenciais definidas no arquivo `.env` pré-configurado:
```bash
  docker exec -it postgres_db psql -U postgres -d meetime
  select * from token_information;
```

### 3. Criação de um contato no CRM
Para testar a criação de um contato no CRM, pode-se realizar uma chamada **_POST_** no endpoint `api/contacts/create`:

```bash
  curl --location 'https://meetime-int-hubspot-v1.loca.lt/api/contacts/create' \
--header 'Content-Type: application/json' \
--data-raw '{
    "properties": {
        "email": "contact_name@gmail.com"
    }
}'
```

Para visualizar todas as informações que podem ser enviadas para criação do contato, consulte a documentação da API disponível no Swagger `http://localhost:8080/swagger-ui/index.html#/`.

### 4. Recebimento do Webhook
O recebimento do Webhook de forma local só estará disponível caso tenha realizado a configuração do Localtunnel após a configuração do ambiente (Passo 5). <br>
Caso sim, a aplicação receberá o webhook do tipo `contact.creation` e realizará o armazenamento das informações na tabela `CONTACT_CREATION_WEBOOK`.

Caso esteja utilizando as credenciais definidas no arquivo `.env` pré-configurado:
```bash
  docker exec -it postgres_db psql -U postgres -d meetime
  select * from contact_creation_webhook;
```
<br>

## ✅ Testes Unitários e Integração
O projeto conta com uma cobertura completa de testes unitários e de integração (39 casos de testes).

#### Execução:
```bash
   ./mvnw test
```
<br>

## 🧠 Referências

- [HubSpot Developer Docs](https://developers.hubspot.com/)
- [OAuth Quickstart HubSpot](https://developers.hubspot.com/docs/guides/apps/authentication/oauth-quickstart-guide)
---
Feito com 💚 para o desafio técnico da Meetime.
