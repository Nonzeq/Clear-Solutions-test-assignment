# Clear-Solutions-test-assignment
##  How to run app

1) Install [Docker](https://www.docker.com/get-started/)
2) Clone current repository
3) Rename ".env.sample" to ".env" (Configure a ".env" file with necessary environment variables if your want)
5) Run ```mvn clean package```
6) Run ``` docker-compose up ``` command to start containers
7) Use default link to [Swagger](http://localhost:8081/api/v1/swagger) 
8) Use custom link http://localhost:{SPRING_LOCAL_PORT}/SWAGGER_PATH}