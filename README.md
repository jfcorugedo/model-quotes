# model-quotes
My first app with Micronautfw. A fancy generator of quotes I've ever heard.

It uses micornaut framework (@micronautfw) with some modules: MongoDB as a persisten storage, Consult as a service discovery and all the configuration needed to generate a docker container with a native image.

It uses a fully reactive aproach, using reactive-streams and mongo-reactive-driver.

# Dependencies

You need these tools installed on your local machine:
* JDK 1.8+
* Maven 3.5+
* Docker (if you want to create a native image)
* Consul

## MongoDB
This application needs a mongodb instance running on your machine (or in another place if you create a MONGO_HOME OS variable).

## Consul
This application uses Consul as a service discovery. You need an instance of consul running on your maching (or in another place if you create a CONSUL_HOME variable).

If you have docker, it is quite easy to have it running in no time, just execute this command:

```sh
docker run --rm -p 8500:8500 --name consul consul:1.2.4
```

# Usage

Make sure you have an instance of mongo running on your machine:

```sh
$ mongod
```
Put some quotes in mongo to test the application:

```sh
$ mongo
> use model-quotes
> db.quotes.insert({_id:1, text:"Don't leave for tomorrow what you can do today"} );
```
Start a Consul node as well:

```sh
docker run --rm -p 8500:8500 --name consul consul:1.2.4
```

To run the application just load it into your favourite IDE and configure an startup profile using the class ```model.quotes.Application```.

If you want to use the shell, just build the project using maven and execute the resulting uber-jar:

```sh
$ mvn clean install
```

```sh
$ java -jar model-quotes-0.1.jar
```

After seeing these traces, you can start testing the app:

    INFO  io.micronaut.runtime.Micronaut - Startup completed in 1437ms. Server Running: http://localhost:8080
    INFO  i.m.d.registration.AutoRegistration - Registered service [model-quotes] with Consul
    
Now you can test the endpoint:

    time curl localhost:8080/quotes/1
    real	0m0.031s
    user	0m0.009s
    sys	0m0.009s

You can also open the Consul dashboard (http://localhost:8500) to see your service there.

# Native image

To build and run a native image you need docker in your computer.

Run the script `docker-build.sh` and wait until docker creates the image.

Finally, start the image, making sure though you have mongo and consul running on your computer:

macOS:
```sh
$ docker run --rm  -p 8080:8080 -e MONGO_HOST='host.docker.internal' -e CONSUL_HOST='host.docker.internal'  --name quotes model-quotes
```

Linux:
```sh
$ docker run --rm  --network host --name quotes model-quotes
```

You will see somethig like that:

    INFO  io.micronaut.runtime.Micronaut - Startup completed in 31ms. Server Running: http://07575142c3de:8080
    INFO  i.m.d.registration.AutoRegistration - Registered service [model-quotes] with Consul
    
As you can see, the container starts in around 30ms.

Then you can test the app:

    time curl localhost:8080/quotes/1
    real	0m0.031s
    user	0m0.009s
    sys	0m0.009s
    
