# WebSocket API
ViPLab WebSocket API.
This is used in the ViPLab frontend to get an interactive session with the ViPLab service.

## Documentation
Documentation is in the [docs](docs) directory.
Architectural Decision are documented as Markdown Architectural Decision Records in [docs/adr](docs/adr).

The schemata of the messages are defined in [schema](schema).

## Select ViPLab Backend Connector
There are two connectors you can choose from:
* ecs-connector
* amqp-connector

Activate the corresponding maven profile when packaging the application.
For example `mvn package -P amqp-connector -Dmaven.test.skip=true`.

## How to run
This project is build with maven and uses Open Liberty as Java EE server.
Build the docker image.
```
docker build -f Dockerfile.jvm -t websocket-api .
```
> The native build does currently not work, because we use reflection in our code.

To run this application, the `jwks.json` is required with all public keys.
Mount or copy this file into the container and use the `JWKS_FILE` environment variable to specifiy the location.

```
docker run --rm -it -p 8080:8080 -e JWKS_FILE=/config/jwks.json -v "${PWD}/jwks.json:/config/jwks.json" websocket-api
```

### Configuration

The configuration can be done using environment variables or a configuration file.
The configuration file must be located at `$PWD/config/application.properties`.
Environment variables names are following the conversion rules of [Eclipse MicroProfile](https://github.com/eclipse/microprofile-config/blob/master/spec/src/main/asciidoc/configsources.asciidoc#default-configsources).
Configuration properties:
| Name                                           | Type      | description                                                                                                                                                              |
|------------------------------------------------|-----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `viplab.validation.configuration.mustValidate` | `boolean` | If true all Computation configurations must be validated, else configurations without a validator are not validated                                                      |
| `viplab.jwt.jwks.file`                         | `Path`    | Path to the `jwks.json` with all public keys.                                                                                                                            |
| `mp.messaging.outgoing.computations.address`   | `String`  | Address of the computation exchange on the AMQP Broker                                                                                                                   |
| `amqp-*`                                       |           | Configuration of the AMQP Broker information, see [SmallRye Reactive Messaging AMQP connector](https://smallrye.io/smallrye-reactive-messaging/#_interacting_using_amqp) |

## Development
Generate test keys for development with [json-web-key-generator](https://github.com/Legion2/json-web-key-generator).
It's available as [docker image](https://hub.docker.com/repository/docker/legion2/json-web-key-generator) on docker hub.
New keys can be generated with the command:
```
docker run --rm legion2/json-web-key-generator jwk-generator -t RSA -s 2048 -S -p -i testkeyId
```
The private key should be stored in a file named `jwks.private.json` and the public key in a file named `jwks.json`.
The files must be stored in the `websocket-api-impl` directory.

You can generate test data by running:
```
mvn -pl websocket-api-impl -Dtest=de.uni_stuttgart.tik.viplab.websocket_api.GenerateJWTTest test
```
This will print the `authenticate` and `create-computation` json messages to the console.
These messages can send to the websocket.
