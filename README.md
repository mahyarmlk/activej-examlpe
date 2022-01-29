# ActiveJ Example

This example shows how to connect to Database, Redis, Fetch your data from DB, transform it to JSON and send it to the client

1. To start the application first you need to run all prerequisites by doing

```
docker-compose up -d
```
This will run postgres & redis in docker containers

2. Run `WebApp::main`
3. Access `http://localhost:9090/years` to see the results