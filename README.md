# Uber for Bikes
I developed this project during one of my University courses called "Web and Cloud Computing". 
It includes a front-end that shows bikes on a Google Map, which can be "rented" and "unrented". 
The front-end communicates with a Java back-end via Websockets. The back-end manages a MongoDB database,
which holds information about the location and avaliability of the bikes, and does some simple user managenment.

# Architecture
![alt text](https://cloud.githubusercontent.com/assets/10426523/23740727/32103b36-04a5-11e7-88ce-970bf81f4cac.png?raw=true "Uber for Bikes Architecture")

The whole point of the course was to develop a distributed system using [Vagrant](https://www.vagrantup.com/) and [Docker](https://www.docker.com/). The front-end and back-end are included in a single Docker container and are integrated using [Glassfish](https://glassfish.java.net/). The MongoDB nodes are individual Docker container, which can be connected with each other into a network that uses sharding to replicate data. We used [Consul](https://www.consul.io/) to keep track of the container instances and to load balance "incoming traffic" over the front-end nodes. Newly created containers automatically connected to [Consul](https://www.consul.io/). 

# Contribution
This project was developed largely by me in cooperation with [Yannik](https://github.com/yyannikb) and Nadia.
