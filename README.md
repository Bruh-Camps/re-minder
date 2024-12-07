Projeto de backend para uma aplicação responsável por enviar lembretes para que os usuários realizem a troca de itens pessoais.

Após lançar a aplicação Spring Boot:

1. Para acessar o banco de dados H2: http://localhost:8080/h2-console/login.jsp

Os endpoints estarão disponíveis no seguinte formato de URL:

- Endpoints de registro e autenticação: http://localhost:8080/api/auth/{endpoint}
- Endpoints de usuários administrativos: http://localhost:8080/api/admin/{endpoint}
- Endpoints de usuários comuns: http://localhost:8080/api/user/{endpoint}

---
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
Exemplo de requisição para registrar usário:

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

