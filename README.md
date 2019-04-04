# Elevator System

## Run project instructions

1. Clone project from github
 ``  git clone https://github.com/Ferdudas97/ElevatorSystem.git"
2. Go into project folder
3. If you want run tests 
 `` mvn test``
3. If you want run application 
``  mvn install exec:java``

## About algorithm
I decided to implement my own solution instead of FCFS. That solution is mix of my idea and FCFS, it uses  RequestQueue
but only for request which can't be assigned to any elevator. Request are assigned to elevators by priority, 
which is computed based on distance between pickup floor and elevator's current/target floor.
## Overview
### How Elevator System is scheduling requests?
1. ElevatorSystem gets pickup request
2. System iterate over collections of elevator and choose with the same direction or not moving, when no one fulfill predicate,
add request to RequestQueue and omit rest steps
3. Compute priority based on distance for each elevator
4. Pick the one with the biggest priority and send request

### How Elevator works?
1. Elevator get request
2. If it is first request set moving direction
3. If current target floor is lower/higher(depends on direction) than request pickup floor, update target level
4. Add request to set of requests
5. Move in direction of target floor, removing completed request by way
6. If all request are completed, stop and if Elevator system have any unassigned request pick it.

## Future improvments
- add max possible number of passengers
- consider number of floors on elevator road in priority computing
- improve pickuping request with the same floor as elevator`s current floor;
- refactor code to be more flexible (use Strategy pattern)
