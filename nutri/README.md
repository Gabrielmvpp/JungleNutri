# JungleNutri — Calculadora de Calorias

**Desenvolvido por**
Gabriel Monteiro Do Vale de Paula – 2310297
Mateus Levi Alencar – 2310315

Este é o nosso trabalho da AV3 de Programação Funcional em Clojure. Atendemos a tudo que o PDF do professor requisitou:

- Cadastro e consulta de usuário (altura, peso, idade, sexo)
- Registro de consumo de alimento (API USDA)
- Registro de atividade física (API Ninjas)
- Exibição de extrato e saldo de calorias por período
- Armazenamento em memória usando átomos
- Front‐end e back‐end 100% em Clojure (Hiccup para views, Garden para CSS e um JS mínimo para interações)

## Como rodar

1. Clone este repositório e entre na pasta do projeto:

   ```
   É na primeira pasta de nutri, nao há que fica dentro de src. E sim a que é pai de src.
   cd nutri
   ```

2. Instale as dependências:

   ```
   lein deps
   ```

3. Compile o ClojureScript para gerar `app.js`:

   ```
   lein cljsbuild once app
   ```

4. Inicie o servidor:

   ```
   lein run
   ```

5. Abra no navegador em `http://localhost:3000`

Pronto! O JungleNutri estará rodando na porta 3000. Aí é só cadastrar seu perfil, inserir alimentos ou exercícios, e consultar seu extrato e saldo de calorias.

Este projeto entrega todas as funcionalidades descritas no PDF de abertura, usando o poder do Clojure e das bibliotecas Ring, Compojure, Cheshire, clj-http e Garden.
