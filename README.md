# Web service for telegram bot
# Usage 
Download the docker-compose.yml file and enter required bot name and token
# HTTP Methods:
POST, PATCH, GET, DELETE
# URL 
`
http://localhost:8080/api/cities/
`

to update, get or remove a city by its id use:
`
http://localhost:8080/api/cities/{id}
`

POST and PATCH with JSON :
``` 
{
    "name" : "City name",
    "info" : "City Info",    
}
```
