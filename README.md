
Como acessar o banco de dados H2:
http://localhost:8080/h2-console/login.jsp

Ao iniciar a aplicação Spring Boot, os endpoints padrão estarão disponíveis no seguinte formato de URL:
http://localhost:8080/{endpoint}

Como registrar usuário:

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

Como logar com o usuário:

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