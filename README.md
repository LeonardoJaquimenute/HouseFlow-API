# HouseFlow API
### API REST segura com Spring Boot, JWT e controle rigoroso de autorização

Backend desenvolvido com **Java + Spring Boot**, focado em **segurança, arquitetura limpa e validação de ownership em nível de serviço**.

A aplicação permite que usuários com role **PARENT** criem **CHILDREN** e gerenciem tarefas com regras de autorização baseadas no usuário autenticado.

---

##  Stack Tecnológica

- Java 21
- Spring Boot
- Spring Security
- JWT (Autenticação Stateless)
- JPA / Hibernate
- Jakarta Validation
- Maven

---

##  Arquitetura

Aplicação estruturada em camadas:

```
Controller → Service → Repository → Database
```

### Controller
- Exposição de endpoints REST
- `@PreAuthorize` para controle de acesso
- Extração do usuário autenticado via `@AuthenticationPrincipal`

### Service
- Regras de negócio centralizadas
- Validação de ownership do recurso
- Proteção contra acesso indevido entre usuários
- Lançamento de exceptions customizadas

### Repository
- Spring Data JPA
- Métodos derivados para consultas específicas
- Mapeamento correto de relacionamentos

---

##  Segurança

### Autenticação
- JWT stateless
- Filtro customizado
- Senhas criptografadas com BCrypt
- Configuração manual do `SecurityFilterChain`

### Autorização
- Controle baseado em roles (`PARENT`, `CHILD`)
- Validação no Service Layer (não apenas no Controller)
- Parent só pode:
  - Criar filhos para si
  - Criar tasks para seus filhos
  - Atualizar/deletar apenas tasks que criou

---

## Modelo de Domínio

### User
- id
- name
- email (único)
- password (BCrypt)
- role (Enum)
- parent (auto-relacionamento)
- children (OneToMany)

### Task
- id
- titulo
- description
- term (LocalDate)
- status (Enum)
- createdBy (User)
- assignedTo (User)

### Status da Task

```java
CRIADA
CONCLUIDA
APROVADA
ATRASADA
```

---

## Tratamento Global de Erros

Implementado com `@RestControllerAdvice`.

Mapeamentos:

- 400 → Validação / regras inválidas
- 401 → Credenciais inválidas
- 403 → Acesso negado / violação de ownership
- 404 → Recurso não encontrado
- 500 → Erro inesperado

---

## Principais Endpoints

### Auth
```
POST /auth/login
POST /auth/register
```

### Users
```
POST /users/my-children
GET  /users/my-children
GET  /users/me
```

### Tasks
```
POST   /tasks
GET    /tasks/my-tasks
GET    /tasks/child/{childId}
PATCH  /tasks/{taskId}/status
DELETE /tasks/{taskId}
```

---

##  Objetivo

Projeto desenvolvido para demonstrar:

- Implementação segura de APIs REST
- Autenticação JWT stateless
- Autorização com validação de ownership
- Arquitetura organizada e orientada a boas práticas
- Separação clara entre camadas

---

##  Autor

Leonardo Jaquimenute Gomes

Estudante de ADS

Futuro Backend Developer (Java / Spring)
