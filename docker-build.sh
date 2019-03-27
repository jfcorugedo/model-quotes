docker build . -t model-quotes
echo
echo
echo "To run the docker container execute:"
echo "    $ docker run --network host model-quotes"
echo "In macOS, instead of the previous command, execute this one"
echo "    $ docker run --rm  -p 8080:8080 -e MONGO_HOST='host.docker.internal' -e CONSUL_HOST='host.docker.internal'  --name quotes model-quotes"