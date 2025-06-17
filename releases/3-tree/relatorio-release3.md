# Relatório Release 3 - Marketplace

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
    A -->|Avalia Compras| B
    C[Loja] -->|Gerencia Produtos| B
    C -->|Cadastra-se| B
    C -->|Visualiza Avaliações| B
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
    Menu <|-- AvaliacaoMenu

    MarketplaceFacade --> AdminService
    MarketplaceFacade --> LojaService
    MarketplaceFacade --> CompradorService
    MarketplaceFacade --> ProdutoService
    MarketplaceFacade --> HistoricoService
    MarketplaceFacade --> AvaliacaoService

    AdminService --> AdminRepository
    LojaService --> LojaRepository
    CompradorService --> CompradorRepository
    ProdutoService --> ProdutoRepository
    HistoricoService --> HistoricoRepository
    AvaliacaoService --> AvaliacaoRepository

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
        -HistoricoService historicoService
        -AvaliacaoService avaliacaoService
        +cadastrarLoja()
        +cadastrarComprador()
        +cadastrarProduto()
        +cadastrarAdmin()
        +registrarCompra()
        +cadastrarAvaliacao()
        +calcularConceitoLoja()
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

    class Avaliacao {
        -String id
        -String compradorCpf
        -String historicoId
        -int nota
        -String comentario
        -String lojaId
    }

    class Historico {
        -String compradorCpf
        -List<Compra> compras
    }
```

### 1.3 Diagrama de Sequência

#### 1.3.1 Avaliação de Compra

```mermaid
sequenceDiagram
    participant C as Comprador
    participant CLM as CompradorLoginMenu
    participant AM as AvaliacaoMenu
    participant F as MarketplaceFacade
    participant AS as AvaliacaoService
    participant AR as AvaliacaoRepository
    
    C->>CLM: Solicita Avaliação
    CLM->>AM: AvaliacaoMenu()
    AM->>F: buscarHistoricoComprador(cpf)
    F->>AM: Historico
    AM-->>C: Mostra Compras
    
    C->>AM: Seleciona Compra
    C->>AM: Fornece Nota e Comentário
    AM->>F: cadastrarAvaliacao(cpf, historicoId, nota, comentario, lojaId)
    F->>AS: cadastrar(avaliacao)
    AS->>AR: salvar(avaliacao)
    AR-->>AS: Avaliacao
    AS-->>F: Avaliacao
    F-->>AM: Avaliacao
    AM-->>C: Confirmação
```

#### 1.3.2 Visualização de Conceito da Loja

```mermaid
sequenceDiagram
    participant C as Comprador
    participant CLM as CompradorLoginMenu
    participant F as MarketplaceFacade
    participant AS as AvaliacaoService
    participant AR as AvaliacaoRepository
    
    C->>CLM: Solicita Conceito da Loja
    CLM->>F: calcularConceitoLoja(lojaId)
    F->>AS: calcularMediaAvaliacoesLoja(lojaId)
    AS->>AR: calcularMediaAvaliacoesPorLoja(lojaId)
    AR-->>AS: double media
    AS-->>F: String conceito
    F-->>CLM: String conceito
    CLM-->>C: Mostra Conceito
```

## 2. Funcionalidades Desenvolvidas

### 2.1 Sistema de Avaliação
- Sistema de avaliação por nota (1 a 5)
- Sistema de avaliação por comentário
- Validação de dados de avaliação

### 2.2 Conceito de Lojas
- Cálculo de média de avaliações
- Classificação qualitativa (Ruim, Média, Boa, Ótima)
- Exibição do conceito da loja

### 2.3 Reorganização de Menus
- Reorganização dos menus de compra
- Separação da função de compra entre os menus
- Melhoria na experiência do usuário

## 3. Relatório de Testes

### 3.1 Cobertura de Código
   Linha: 87%
   Branch: 82%
   Complexidade: 78% 

-- prints
    ![Cobertura de Código](cobertura.png)
    ![Complexidade](complexidade.png)
### 3.2 Testes Unitários
- Total de testes: X
- Passed: X
- Failed: 0
- Skipped: X