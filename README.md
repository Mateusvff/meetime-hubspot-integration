
# Case Técnico: Integração HubSpot 💚

Repositório destinado à entrega do Case Técnico: Integração HubSpot.

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
   https://github.com/Mateusvff/meetime-hubspot-integration.git
   cd hubspot
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



## 🧠 Referências

- [HubSpot Developer Docs](https://developers.hubspot.com/)
- [OAuth Quickstart HubSpot](https://developers.hubspot.com/docs/guides/apps/authentication/oauth-quickstart-guide)
---
Feito com 💚 para o desafio técnico da Meetime.
