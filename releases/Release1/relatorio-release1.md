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

## 3. Prints das Funcionalidades

### 3.1 Tela Inicial (View do Usuário)
![img.jpg](telaInicial.jpg)

### 3.2 Tela de Login (View do Usuário)
![img.jpg](telaLogin.jpg)

### 3.3 Cadastrar Comprador (View do Usuário)
![img.jpg](cadastrarComprador.jpg)

### 3.4 Login Comprador (View do Usuário)
![img.jpg](loginComprador.jpg)

### 3.5 Gerenciar Produtos (View do Admin)
![img.jpg](gerenciarProdutos.jpg)

### 3.6 Cadastrar Produto (View do Admin)
![img.jpg](cadastrarProdutos.jpg)

### 3.7 Listar Produtos (View do Admin)
![img.jpg](listarProdutos.jpg)

### 3.8 Buscar Produto (View do Admin)
![img.jpg](buscarProduto.jpg)

### 3.9 Atualizar Produto (View do Admin)
![img.jpg](atualizarProduto.jpg)

### 3.10 Remover Produto (View do Admin)
![img.jpg](removerProduto.jpg)

### 3.11 Gerenciar e Remover Comprador (View do Admin)
![img.jpg](removerComprador.jpg)

### 3.12 Atualizar dado do Comprador (View do Comprador)
![img.jpg](atualizarComprador.jpg)

## 4. Relatório de Testes

### 4.1 Cobertura de Código

Obs: menu e model são irrelevantes
Cobertura: incluindo diretórios irrelevantes/cobertura real

   Linha: 21%/94,33%
   Branch: 28%/90,45%
   Métodos: 32%/98,6%

-- prints
    ![Cobertura de Código](cobertura-testes-release1.png)
### 4.2 Testes Unitários
- Total de testes: 69
- Passed: 69
- Failed: 0
- Skipped: 0