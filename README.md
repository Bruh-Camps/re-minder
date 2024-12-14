# Trabalho Prático - Engenharia de Software II
## Membros do grupo
- Antônio Isaac Silva Lima (Eng. Software 2)
- Bruno Esteves Campoi (Eng. Software 2  |  Teste de Software)
- Marcelo Lommez Rodrigues de Jesus (Eng. Software 2)

---
## Explicação do Sistema

Diversos itens utilizados no dia-a-dia possuem recomendações de troca com certa frequência, definidas por especialistas das mais diversas áreas. Entretanto, diante do grande número de objetos, torna-se difícil o gerenciamento de todas essas trocas dentro do prazo adequado, já que para isso é necessário decorar datas e prazos para cada objeto. Alguns exemplos comuns são:

- Escova de dentes
- Travesseiro
- Colchão
- Óleo do motor do carro
- Vela do filtro de água
- Esponja de banho
- Esponja de louças
- Etc.

Pensando nesse problema, o sistema proposto busca ajudar o usuário a se lembrar dessas trocas por meio de avisos recebidos por email. Para isso, basta que o usuário registre os itens para os quais gostaria de ser lembrado da necessidade de troca.


Resumo:  Projeto de backend de uma aplicação responsável por enviar lembretes para que os usuários realizem a troca de itens pessoais.

---
## Explicação das tecnologias Utilizadas

Para o desenvolvimento deste projeto, foi utilizada a linguagem Java na versão 17, em conjunto com o framework Spring Boot, ideal para a construção de APIs REST devido à sua simplicidade e robustez. Como sistema de gerenciamento de banco de dados (SGBD), optou-se pelo H2, uma solução leve e embutida, que atende perfeitamente aos objetivos do projeto. Para gerenciar a persistência de dados e realizar o mapeamento objeto-relacional (ORM), foi empregado o framework Jakarta Persistence, garantindo uma integração eficiente entre a aplicação e o banco de dados.

Além disso, foram implementados testes abrangentes para validar a funcionalidade e a segurança do sistema. Esses testes incluem verificações de autenticação, autorização e envio de notificações, garantindo que os endpoints e serviços funcionem corretamente. Foram utilizados frameworks como Mockito e JUnit para a criação de testes unitários e de integração, permitindo identificar e corrigir possíveis problemas antes da implantação.

---
## Como Usar?

Após lançar a aplicação Spring Boot:

1. Para acessar o banco de dados H2: http://localhost:8080/h2-console/login.jsp

Os endpoints estarão disponíveis no seguinte formato de URL:

- Endpoints de registro e autenticação: http://localhost:8080/api/auth/{endpoint}
- Endpoints de usuários administrativos: http://localhost:8080/api/admin/{endpoint}
- Endpoints de usuários comuns: http://localhost:8080/api/user/{endpoint}


Exemplo de requisição para registrar usuário:

``` curl
curl -i -X POST \
   -H "Content-Type:application/json" \
   -d \
'{
    "name": "test",
    "username": "test",
    "email": "test@example.com",
    "password": "1234test"
}' \
 'http://localhost:8080/api/auth/signup'
```

Exemplo de requisição para autenticar um usuário já cadastrado:
    Obs.: Depois de autenticado, o endpoint retornará um token_de_autenticacao <auth_token> 

``` curl
curl -i -X POST \
   -H "Content-Type:application/json" \
   -d \
'{
    "usernameOrEmail": "test",
    "password": "1234test"
}' \
 'http://localhost:8080/api/auth/signin'
``` 
Exemplo de requisição para associar um novo item ao usuário atualmente autenticado:

``` curl
curl -i -X POST \
   -H "Content-Type:application/json" \
   -H "Authorization:Bearer <auth_token>" \
   -d \
'{
    "name": "Escova de dentes",
    "dateLastChange": "01/01/2024",
    "changeDaysInterval": 90
}' \
 'http://localhost:8080/api/user/item'
```

Exemplo de requisição para retornar todos os itens associados ao usuário atualmente autenticado:

``` curl
curl -i -X GET \
   -H "Content-Type:application/json" \
   -H "Authorization:Bearer <auth_token>" \
 'http://localhost:8080/api/user/items'
```

--- 

## Próximos avanços

Lista completa de requisições implementadas e ainda a implementar:

POST
- OK - Cadastro de usuário:`api/auth/signup`
- OK - Autenticação de usuário: `api/auth/signin`
- OK - Criação de item: `api/user/item`

GET
- OK - Obtém a lista de itens do usuário atual `api/user/items`
- Pendente - Obtém a lista de itens com próxima data de troca de 10 dias ou menos

PUT
- Pendente - Altera número de dias para a troca de determinado item

DELETE
- Pendente - Exclui determinado item de um usuário
- Pendente - Exclui determinado usuário

---
