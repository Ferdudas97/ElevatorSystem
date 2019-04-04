# Elevator System

## Run project instructions

1. Clone project from github
 ``  git clone https://github.com/Ferdudas97/ElevatorSystem.git ``
2. Go into project folder
3. If you want to run tests 
 `` mvn test``
3. If you want to run application 
``  mvn install exec:java``

## Elevator System interface
- pickup(Integer currentFloor, Integer direction) - takes floor where request was created, and direction of request -1 means down, 1 means up, it represents functionality of buttons on elevator's entrance frame
- update(Integer id, Integer currentFloor, Integer direction) - updates state of specified elevator
- status() - returns state of each elevator
- step() - simulation step

## About algorithm
I decided to implement my own solution instead of FCFS. That solution is mix of my idea and FCFS, it uses  RequestQueue
but only for request which can't be assigned to any elevator. Requests are assigned to elevators by priority, 
which is computed accodring to distance between pickup floor and elevator's current/target floor.
## Overview
### How Elevator System is scheduling requests?
1. ElevatorSystem gets pickup request
2. System iterates over collections of elevators and chooses those with the same direction or not moving, when no one fulfills the predicate,adds requests to RequestQueue and omits other steps
3. Computes priority based on distance for each elevator
4. Picks the one with the biggest priority and send request

### How Elevator works?
1. Elevator gets request
2. If it is the first request - sets moving direction
3. If current target floor is lower/higher(depends on direction) than requested pickup floor, it updates target level
4. Adds request to set of requests
5. Moves in direction of target floor, removing completed request by way
6. If all request are completed Elevator stops and if Elevator system has any unassigned request - it picks it.

## Future improvments
- add max possible number of passengers
- consider number of floors on elevator's road in priority computing
- improve pickuping request with the same floor as elevator's current floor;
- refactor code to be more flexible (use Strategy pattern)
