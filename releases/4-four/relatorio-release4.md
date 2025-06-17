# Relatório Release 4 - Marketplace

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
    A -->|Filtra Produtos| B
    A -->|Recebe Recomendações| B
    C[Loja] -->|Gerencia Produtos| B
    C -->|Cadastra-se| B
    C -->|Visualiza Avaliações| B
    C -->|Gerencia Promoções| B
    D[Admin] -->|Gerencia Usuários| B
    D -->|Monitora Transações| B
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
    Menu <|-- RecomendacaoMenu
    Menu <|-- PromocaoMenu

    MarketplaceFacade --> AdminService
    MarketplaceFacade --> LojaService
    MarketplaceFacade --> CompradorService
    MarketplaceFacade --> ProdutoService
    MarketplaceFacade --> HistoricoService
    MarketplaceFacade --> AvaliacaoService
    MarketplaceFacade --> RecomendacaoService
    MarketplaceFacade --> PromocaoService

    AdminService --> AdminRepository
    LojaService --> LojaRepository
    CompradorService --> CompradorRepository
    ProdutoService --> ProdutoRepository
    HistoricoService --> HistoricoRepository
    AvaliacaoService --> AvaliacaoRepository
    RecomendacaoService --> RecomendacaoRepository
    PromocaoService --> PromocaoRepository

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
        -RecomendacaoService recomendacaoService
        -PromocaoService promocaoService
        +cadastrarLoja()
        +cadastrarComprador()
        +cadastrarProduto()
        +cadastrarAdmin()
        +registrarCompra()
        +cadastrarAvaliacao()
        +calcularConceitoLoja()
        +gerarRecomendacoes()
        +criarPromocao()
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
        -List<String> categorias
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

    class Promocao {
        -String id
        -String lojaId
        -double percentualDesconto
        -Date dataInicio
        -Date dataFim
        -List<String> produtosIds
    }

    class Recomendacao {
        -String compradorCpf
        -List<String> produtosRecomendados
        -Date dataGeracao
    }
```

### 1.3 Diagrama de Sequência

#### 1.3.1 Sistema de Recomendação

```mermaid
sequenceDiagram
    participant C as Comprador
    participant CLM as CompradorLoginMenu
    participant RM as RecomendacaoMenu
    participant F as MarketplaceFacade
    participant RS as RecomendacaoService
    participant HS as HistoricoService
    
    C->>CLM: Solicita Recomendações
    CLM->>RM: RecomendacaoMenu()
    RM->>F: gerarRecomendacoes(cpf)
    F->>RS: gerarRecomendacoes(cpf)
    RS->>HS: buscarHistoricoComprador(cpf)
    HS-->>RS: Historico
    RS-->>F: List<Produto>
    F-->>RM: List<Produto>
    RM-->>C: Mostra Recomendações
```

#### 1.3.2 Gerenciamento de Promoções

```mermaid
sequenceDiagram
    participant L as Loja
    participant LLM as LojaLoginMenu
    participant PM as PromocaoMenu
    participant F as MarketplaceFacade
    participant PS as PromocaoService
    participant PR as PromocaoRepository
    
    L->>LLM: Solicita Criar Promoção
    LLM->>PM: PromocaoMenu()
    L->>PM: Fornece Dados da Promoção
    PM->>F: criarPromocao(lojaId, percentual, dataInicio, dataFim, produtosIds)
    F->>PS: cadastrar(promocao)
    PS->>PR: salvar(promocao)
    PR-->>PS: Promocao
    PS-->>F: Promocao
    F-->>PM: Promocao
    PM-->>L: Confirmação
```

## 2. Funcionalidades Desenvolvidas

### 2.1 Sistema de Recomendação
- Análise de histórico de compras
- Recomendação baseada em produtos similares
- Recomendação baseada em preferências de categoria
- Personalização por perfil de usuário

### 2.2 Gerenciamento de Promoções
- Criação de promoções com período definido
- Aplicação de descontos percentuais
- Promoções por categoria ou produto específico
- Visualização de promoções ativas

### 2.3 Filtros Avançados de Busca
- Filtro por faixa de preço
- Filtro por categoria
- Filtro por avaliação da loja
- Ordenação por relevância

### 2.4 Monitoramento de Transações
- Dashboard para administradores
- Relatórios de vendas por período
- Análise de desempenho de lojas
- Detecção de padrões de compra

## 3. Relatório de Testes

### 3.1 Cobertura de Código
   Linha: 89%
   Branch: 85%
   Complexidade: 82% 

-- prints
    ![Cobertura de Código](img.png)
    ![Complexidade](complexidade-r4.png)
### 3.2 Testes Unitários
- Total de testes: 65
- Passed: 65
- Failed: 0
- Skipped: X