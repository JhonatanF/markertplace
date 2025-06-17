# Relatório Release 1 - Marketplace

## 1. Arquitetura

### 1.1 Diagrama de Casos de Uso
```mermaid
graph TD
    A[Comprador] -->|Realiza Login| B(Sistema)
    A -->|Cadastra-se| B
    A -->|Visualiza Produtos| B
    A -->|Compra Produtos| B
    C[Loja] -->|Gerencia Produtos| B
    C -->|Cadastra-se| B
    D[Admin] -->|Gerencia Usuários| B
```

### 1.2 Diagrama de Classes
```mermaid
classDiagram
    Menu <|-- MainMenu
    Menu <|-- LoginMenu
    Menu <|-- CadastroMenu
    Menu <|-- AdminLoginMenu
    Menu <|-- CompradorLoginMenu
    Menu <|-- LojaLoginMenu
    Menu <|-- ProdutoMenu

    MarketplaceFacade --> AdminService
    MarketplaceFacade --> LojaService
    MarketplaceFacade --> CompradorService
    MarketplaceFacade --> ProdutoService

    AdminService --> AdminRepository
    LojaService --> LojaRepository
    CompradorService --> CompradorRepository
    ProdutoService --> ProdutoRepository
    ProdutoService --> LojaRepository

    Usuario <|-- Admin
    Usuario <|-- Comprador
    Usuario <|-- Loja

    class Usuario {
        -String nome
        -String email
        -String senha
        -String endereco
    }

    class Menu {
        #Scanner scanner
        #Map<Integer, MenuOption> options
        #String title
        +show()
        #addOption()
    }

    class MarketplaceFacade {
        -LojaService lojaService
        -CompradorService compradorService
        -ProdutoService produtoService
        -AdminService adminService
        +cadastrarLoja()
        +cadastrarComprador()
        +cadastrarProduto()
        +cadastrarAdmin()
    }

    class Produto {
        -String id
        -String nome
        -double valor
        -String tipo
        -int quantidade
        -String marca
        -String descricao
        -String lojaCpfCnpj
    }
```

### 1.3 Diagrama de Sequência
```mermaid
sequenceDiagram
    participant C as Comprador
    participant MM as MainMenu
    participant LM as LoginMenu
    participant F as MarketplaceFacade
    participant CS as CompradorService
    participant CR as CompradorRepository
    
    C->>MM: Inicia aplicação
    MM->>LM: Seleciona Login
    LM->>F: loginComprador(cpf, senha)
    F->>CS: buscarPorId(cpf)
    CS->>CR: findById(cpf)
    CR-->>CS: Optional<Comprador>
    CS-->>F: Optional<Comprador>
    F-->>LM: Optional<Comprador>
    LM-->>C: Mostra menu comprador
```

```mermaid
sequenceDiagram
    participant C as Comprador
    participant CLM as CompradorLoginMenu
    participant F as MarketplaceFacade
    participant PS as ProdutoService
    participant PR as ProdutoRepository
    
    C->>CLM: Visualiza produtos
    CLM->>F: listarProdutos()
    F->>PS: listarTodos()
    PS->>PR: findAll()
    PR-->>PS: List<Produto>
    PS-->>F: List<Produto>
    F-->>CLM: List<Produto>
    CLM-->>C: Mostra produtos
    
    C->>CLM: Seleciona produto
    CLM->>F: buscarProduto(id)
    F->>PS: buscarPorId(id)
    PS->>PR: findById(id)
    PR-->>PS: Optional<Produto>
    PS-->>F: Optional<Produto>
    F-->>CLM: Optional<Produto>
    CLM-->>C: Confirma compra
```

```mermaid
sequenceDiagram
    participant A as Admin
    participant ALM as AdminLoginMenu
    participant F as MarketplaceFacade
    participant LS as LojaService
    participant LR as LojaRepository
    
    A->>ALM: Cadastra loja
    ALM->>F: cadastrarLoja(dados)
    F->>LS: cadastrar(loja)
    LS->>LR: save(loja)
    LR-->>LS: Loja
    LS-->>F: Loja
    F-->>ALM: Loja
    ALM-->>A: Confirma cadastro
```

```mermaid
sequenceDiagram
    participant L as Loja
    participant LLM as LojaLoginMenu
    participant F as MarketplaceFacade
    participant PS as ProdutoService
    participant PR as ProdutoRepository
    
    L->>LLM: Cadastra produto
    LLM->>F: cadastrarProduto(dados)
    F->>PS: cadastrar(produto)
    PS->>PR: save(produto)
    PR-->>PS: Produto
    PS-->>F: Produto
    F-->>LLM: Produto
    LLM-->>L: Confirma cadastro
```

## 2. Funcionalidades Desenvolvidas

### 2.1 Sistema de Login e Cadastro
- Implementado sistema de autenticação para três tipos de usuários
- Validação de dados cadastrais
- Gerenciamento de sessão

### 2.2 Gerenciamento de Produtos
- CRUD completo de produtos
- Validações de negócio
- Listagem por loja

### 2.3 Gerenciamento de Usuários
- Cadastro e atualização de dados
- Remoção de contas
- Níveis de acesso

## 3. Relatório de Testes

### 3.1 Cobertura de Código
   Linha: 85%
   Branch: 80%
   Complexidade: 75% 

-- prints
    ![Cobertura de Código](img.png)
    ![Complexidade](complexidade.png)
### 3.2 Testes Unitários
- Total de testes: X
- Passed: X
- Failed: 0
- Skipped: X