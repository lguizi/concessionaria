# Trabalho de Conclusão de Curso - Java (Avançatech)

Este repositório contém o código do meu TCC desenvolvido durante o curso de Java da Avançatech. O projeto consiste em um sistema para gerenciar uma concessionária via console.

### 📝 Sobre o Trabalho
O objetivo foi criar uma aplicação que coloque em prática os conceitos de Programação Orientada a Objetos (POO) e integração com serviços externos. Escolhi o tema de concessionária para simular operações reais de compra e venda de veículos.

### 🛠️ O que o código faz:
* **Integração com API:** O sistema se conecta com a API da Tabela Fipe para buscar marcas, modelos e anos reais de veículos.
* **Gestão de Estoque:** Permite cadastrar (comprar), listar, editar e remover (vender) carros da frota.
* **Controle Financeiro:** O sistema gerencia o saldo da loja, calcula lucro ou prejuízo nas vendas e mostra o valor total do patrimônio em pátio.
* **Lógica de Programação:** Uso de listas (ArrayList), leitura de dados com Scanner e manipulação de strings com Regex para tratar os dados da API.

### 💻 Tecnologias utilizadas:
- Java (JDK 11 ou superior)
- HttpClient (para consumo de API)
- Regex (para extração de dados JSON)
