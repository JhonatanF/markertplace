# Marketplace System

Um sistema de marketplace simplificado implementado em Java puro, seguindo o padrão de projeto Facade.

## Requisitos

- Java 11 ou superior
- Maven 3.6 ou superior

## Como Compilar e Executar

1. Clone o repositório:
```bash
git clone [URL_DO_REPOSITORIO]
cd marketplace
```

2. Compile o projeto:
```bash
mvn clean package
```

3. Execute o sistema:
```bash
java -cp target/marketplace-1.0-SNAPSHOT.jar com.marketplace.Main
```

## Funcionalidades Implementadas (Release 1)

- CRUD completo de Lojas
- CRUD completo de Compradores
- CRUD completo de Produtos
- Interface de linha de comando
- Persistência local em arquivos

## Estrutura do Projeto

- `model/`: Classes de modelo (Loja, Comprador, Produto)
- `service/`: Camada de serviços com lógica de negócio
- `repository/`: Camada de persistência
- `facade/`: Implementação do padrão Facade
- `Main.java`: Interface de linha de comando

## Testes

Para executar os testes e gerar o relatório de cobertura:

```bash
mvn clean test
```

O relatório de cobertura será gerado em `target/site/jacoco/index.html`
