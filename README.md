# Getting Started

### Application start

You need Docker and Docker compose to be installed.
Just run in the root folder:
- for docker compose v2
`docker compose up -d`
- for docker compose v1
`docker-compose up -d`

The application will use 8080 port.

To run tests
`mvn test`

### Rules
There is a _tick_ which lasts for 3 seconds. So every action requires at least 1 tick. During 1 tick:
 - drone will get at max 10% of its battery until 100%
 - drone will move at max 10km to reach its route target
 - moving drone will loose 1% of its battery for every passed km
 - drone will load or unload medications
 
### Minimal interaction
There are two drones and two medications to start from. To make it works:
 - load a medication into a drone
 - assign a rout to a drone
 - give start command
 
### Endpoints

#### Get all drones
GET http://localhost:8080/drones

#### Get all medications
GET http://localhost:8080/medications

#### Get drones journal
GET http://localhost:8080/event-logs

#### Load drone
POST http://localhost:8080/drones/{droneId}/load/{medicationId}

#### Create route
POST http://localhost:8080/drones/{droneId}/create_route/{distance}

#### Launch drone
POST http://localhost:8080/drones/{droneId}/start

#### Postman collection
For more endpoints please use Drones.postman_collection.json

### More features to implement
Next steps (skipped to save time):
 - more unit tests
 - complex routes, not just a distance
 - DronesServiceImpl may be refactored, looks too overloaded right now

### Initial task

Drones

[[TOC]]

:scroll: START

Introduction

There is a major new technology that is destined to be a disruptive force in the field of transportation: the drone. Just as the mobile phone allowed developing countries to leapfrog older technologies for personal communication, the drone has the potential to leapfrog traditional transportation infrastructure.

Useful drone functions include delivery of small items that are (urgently) needed in locations with difficult access.

Task description

We have a fleet of 10 drones. A drone is capable of carrying devices, other than cameras, and capable of delivering small loads. For our use case the load is medications.

A Drone has:

serial number (100 characters max);
model (Lightweight, Middleweight, Cruiserweight, Heavyweight);
weight limit (500gr max);
battery capacity (percentage);
state (IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING).


Each Medication has:

name (allowed only letters, numbers, ‘-‘, ‘_’);
weight;
code (allowed only upper case letters, underscore and numbers);
image (picture of the medication case).

Develop a service via REST API that allows clients to communicate with the drones (i.e. dispatch controller). The specific communicaiton with the drone is outside the scope of this task.

The service should allow:

registering a drone;
loading a drone with medication items;
checking loaded medication items for a given drone;
checking available drones for loading;
check drone battery level for a given drone;
Feel free to make assumptions for the design approach.
Requirements

While implementing your solution please take care of the following requirements:

Functional requirements

There is no need for UI;
Prevent the drone from being loaded with more weight that it can carry;
Prevent the drone from being in LOADING state if the battery level is below 25%;
Introduce a periodic task to check drones battery levels and create history/audit event log for this.
Non-functional requirements

Input/output data must be in JSON format;
Your project must be buildable and runnable;
Your project must have a README file with build/run/test instructions (use DB that can be run locally, e.g. in-memory, via container);
Any data required by the application to run (e.g. reference tables, dummy data) must be preloaded in the database.
JUnit tests are optional but advisable (if you have time);
Advice: Show us how you work through your commit history.
:scroll: END
