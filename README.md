# Netflix Conductor Client

1. Start the conductor server as per https://netflix.github.io/conductor/server/#installing

pull the project and `cd docker` then run the below docker compose command

* `docker-compose -p conductor-server up`

2. Run docker compose `docker-compose -f docker/docker-compose.yml -p conductor-client up` to run Postgres Db

3. Run the springboot application


# To run with Conductor:

Run the steps 1, 2 and 3. When the application runs it will set up the workflows and tasks in conductor. Before that they shouldn't be in it. 
After running step 3, you should find in your logs:
`#### Loading the tasks ####` 
`#### Loading the workflows ####`
Which should mean upsert these workflows. 

Then execute the following command. 
`curl -X POST http://localhost:8080/api/workflow/nig-test-workflow -H 'Content-Type: application/json' -d '{"lang": "SUCCESS","name": "Test"}'`

This will trigger your workflow on the decision tree where everything works like a charm. 

With FAILURE parameter the 2nd job will fail a few times until it works. Here I play with the rate limit and number of retries to see how this works.  

`curl -X POST http://localhost:8080/api/workflow/nig-test-workflow -H 'Content-Type: application/json' -d '{"lang": "FAILURE","name": "Test"}'` 
