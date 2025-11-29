# MoveMais Estoque

API backend para gerenciamento de estoque com autenticação JWT, construída com Spring Boot 3.5.8.

## 🛠️ Stack

- **Java 25** | **Spring Boot 3.5.8** | **Spring Security** | **JWT (JJWT)**
- **Spring Data JPA** | **H2 Database** | **Flyway** | **ModelMapper**
- **SpringDoc OpenAPI** (Swagger) | **Lombok** | **Maven**

## 🚀 Como Executar

**Pré-requisitos**: Java 25+ e Maven 3.6+

```bash
git clone https://github.com/BrininhoBru/movemais-estoque.git
cd movemais-estoque
./mvnw clean install
./mvnw spring-boot:run
```

Aplicação disponível em: `http://localhost:8080`

**Banco de Dados**: H2 em memória (dev), credenciais padrão: `sa` / (sem senha)

## 🧪 Como Rodar os Testes

```bash
./mvnw test                                # Todos os testes
./mvnw test -Dgroups=integration          # Testes de integração
./mvnw test -Dgroups=service              # Testes de serviço
```

## 📚 Swagger UI

Acesse a documentação interativa em: `http://localhost:8080/swagger-ui.html`

**Para usar endpoints protegidos:**

1. Faça login em `/api/auth/login`
2. Copie o token JWT retornado
3. Clique em "Authorize" (cadeado)
4. Cole: `Bearer {seu_token}`

## 🗄️ Console H2

Acesse: `http://localhost:8080/h2-console`

- **URL**: `jdbc:h2:mem:devdb`
- **User**: `sa`
- **Senha**: (deixar vazio)

Tabelas: `USUARIO`, `DEPOSITO`, `PRODUTO`, `ESTOQUE`, `MOVIMENTO_ESTOQUE`

## 🏗️ Arquitetura

### Estrutura em Camadas

```
Controller → Service → Repository → Database (H2)
```

### Componentes Principais

| Camada | Responsabilidade |
|--------|-----------------|
| **Controller** | Recebe requisições, valida entrada, retorna respostas padronizadas |
| **Service** | Lógica de negócio, validações complexas, transações |
| **Repository** | Acesso a dados via Spring Data JPA |
| **DTO** | Comunicação com clientes (separação entre representação externa e interna) |
| **Model** | Entidades JPA, mapeamento de tabelas |
| **Security** | Filtro JWT, autenticação, autorização |
| **Config** | Configurações de beans (OpenAPI, ModelMapper, etc.) |

### Controladores Disponíveis

- `AuthController` - Login/Registro
- `ProdutoController` - CRUD de produtos
- `DepositoController` - CRUD de depósitos
- `EstoqueController` - Consultas de estoque
- `MovimentoEstoqueController` - Registrar movimentações

## 🎯 Principais Decisões Técnicas

1. **JWT para Autenticação** - Stateless, escalável horizontalmente, token com expiração configurável
2. **H2 Database** - Facilita desenvolvimento e testes sem dependência externa
3. **Flyway** - Versionamento automático do schema SQL
4. **ModelMapper** - Conversão automática Entity ↔ DTO
5. **Padrão de Resposta Consistente** - `ApiResponsePattern` com status, mensagem, dados e timestamp
6. **Validação em Dois Níveis** - HTTP (@Valid) e lógica de negócio (Service)
7. **Exception Handler Centralizado** - Tratamento consistente de erros
8. **Organização por Camada** - Facilita manutenção e testes

## 🔐 Segurança

- Autenticação: Username/Email + Senha (BCrypt)
- Autorização: JWT nos headers
- **Endpoints Públicos**: `/api/auth/login`, `/api/auth/register`, `/swagger-ui.html`, `/h2-console`
- **Endpoints Protegidos**: Requerem token (`Authorization: Bearer {token}`)

---
