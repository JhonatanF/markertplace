# Relatório Release 2 - Marketplace

## 1. Arquitetura

### 1.1 Diagrama de Casos de Uso
```mermaid
graph TD
    A[Comprador] -->|Realiza Login| B(Sistema)
    A -->|Cadastra-se| B
    A -->|Visualiza Produtos| B
    A -->|Gerencia Carrinho| B
    A -->|Compra Produtos| B
    A -->|Visualiza Historico| B
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
    Menu <|-- CompraMenu

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

#### 1.3.1 Compra Com Carrinho

```mermaid
sequenceDiagram
    participant C as Comprador
    participant CLM as CompradorLoginMenu
    participant CM as CompraMenu
    participant F as MarketplaceFacade
    participant PS as ProdutoService
    participant PR as ProdutoRepository
    
    C->>CLM: Realizar Compra
    CLM->>CM: CompraMenu()
    CM->>F: listarProdutoss()
    F->>PS: listarTodos()
    PS->>PR: findAll()
    PR-->>PS: List<Produto>
    PS-->>F: List<Produto>
    F-->>CLM: List<Produto>
    CLM-->>C: Mostra produtos
    
    loop AddToCart = True
        C->>CLM: Seleciona Produto
        CLM->>CM: selecionaProduto()
        CM->>F: buscarProduto(id)
        F->>PS: buscarPorId(id)
        PS->>PR: findById(id)
        PR-->>PS: Optional<Produto>
        PS-->>F: Optional<Produto>
        F-->>CLM: Optional<Produto>
        CLM-->>C: Adiciona ao Carrinho
    end
    
    C->>CLM: Finaliza a Compra
    CLM->>CM: finalizarCompra()
    CM-->>CLM: esvaziaCarrinho()
    CLM-->>C: Compra Finalizada
    
```

#### 1.3.1 Solicitar Histórico

```mermaid
sequenceDiagram
participant C as Comprador
participant CLM as CompradorLoginMenu
participant F as MarketplaceFacade
participant PS as HistoricoService
participant PR as HistoricoRepository

    C->>CLM: Solicita Historico
    CLM->>F: listarProdutos()
    F->>PS: buscarHistoricoComprador()
    PS->>PR: buscarPorComprador()
    PR-->>PS: List<Produto>
    PS-->>F: List<Produto>
    F-->>CLM: List<Produto>
    CLM-->>C: Mostra Historico
```

## 2. Funcionalidades Desenvolvidas

### 2.1 Carrinho de Compras
- CRUD do carrinho de compras
- Possibilidade de aplicar cupons

### 2.2 Vendas de Produtos
- Gerenciamento do carrinho de compras (adicionar, remover)
- Possibilidade de realizar a compra dos produtos selecionados

### 2.3 Histórico de Compras
- Histórico individual de produtos
- Histórico individual de lojas dos produtos comprados

### 2.4 Permanência
- Permanencia individual do carrinho
- Permanencia de login de usuários no sistema por tempo limitado

## 3. Prints com as funcionalidades

### 3.1 Adicionar Produto ao Carrinho
![img.jpg](adicionarProd.jpg)

### 3.2 Visualizar Produtos no Carrinho
![img.jpg](visualizarProd.jpg)

### 3.3 Remover Produto do Carrinho
![img.jpg](removerProd.jpg)

### 3.4 Finalizar Compra
![img.jpg](finalizarCompra.jpg)

### 3.5 Histórico de Compras
![img.jpg](historico.jpg)

## 4. Relatório de Testes

### 4.1 Cobertura de Código

Obs: menu e model são irrelevantes
#### Cobertura: incluindo diretórios irrelevantes/cobertura real

- Linha: 33%/83,62%
- Métodos: 32%/85%

-- prints
![Cobertura de Código](cobertura-testes-release2.png)
### 4.2 Testes Unitários
- Total de testes: 84
- Passed: 84
- Failed: 0
- Skipped: 0