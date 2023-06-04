# 참고 :

https://www.youtube.com/watch?v=BVdQ3iuovg0

https://github.com/ali-bouali/spring-boot-3-jwt-security


## 설명 :

Java to Kotlin

## 예제 :

POST ] http://localhost:8080/api/v1/auth/register
```
{
    "firstname" : "hahahah",
    "lastname": "ho",
    "email" : "hahaha@hohoho.com",
    "pwd" : "0000",
    "role" : "ADMIN"
} 
```

POST ] http://localhost:8080/api/v1/auth/authenticate
```
{
    "email" : "hahaha@hohoho.com",
    "pwd" : "0000",
} 
```