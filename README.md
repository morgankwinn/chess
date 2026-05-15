# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

<a href="https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwVygYKQH+uB5afJCIJqTsXzQo8wHiVQSIwAgQnihignCQSRJgKSb6GLuNL7rUjIsmyylcjeXl3kuwowGKEpujKcplu8SqYCqwYam6RgQGoMBoBAzBWmiQW8iFlnWT2fbbh5ln+lFaWqAAcllUZolGMZxoUWlJpUolpk4ACMBE5qoebzNBRYlvUPjTJe0BIAAXiguwwHRTbDvybXua6M5bu5FRLfSMCHnIKDPvE56Xte22qBUy4BmuAYnRtzooG1Omlo54oZKoAGYE9K3VM8elxcRpHfBRVH1iRqENq1ybIKmMC4fhoyEfp8xgzB5GXiDyEo2h9GMd4fj+F4KDoDEcSJITxOOb4WCiYKoH1A00gRvxEbtBG3Q9HJqgKcMwOIeg6HwpUT31LzSGfeZlAVKttn2FTDlCVTzlqK5pVbbe-IMkyh3HfBfNoHOwXLeUl0RU+t3yLK8qi-ziXqjdutIal6WZdl1o5DA4pCnlC7nVL93dr2-abX7EmlodGPSFVtVgPVOSNSgsYKQLbUdemPUI31A0FmMw3QKN416pNM1zQtDFnd9nb1Idr73Wrhs7RwKDcMel461e2gG-lRuXdITdMoY1d3Z2j0S3UlOnm9H1fSHNQvJpP1QCn0PYbDeFZqXuPMf4KLrv42Dihq-FojAADiSoaDT5Wlg0J8s+z9hKjz6N68n5TC8az9i195SrTZaJnzmBy-9z5KxJKrcoZ1NZgG1tbfW3t9wXTCqbe27cLYxVgQlVUGpw56ydgKF2MAcru09sYeBI4K5WRdBlQOqsZ7+hwUhSOaho6xxjtGBOzVk4VFTt1Xq-Is5DWLHnBUBd4hF1mksDe5cZ7WUHvIcBkCYC+QAWoDEncfaIPqMgk0CAlHn1oW-Ue9Qj45BUaoSeCBAKjwobpZYD8cwFgaOMexKAACS0gCxdXCMEQIIJNjxF1CgN0nI9jfGSKANUwTIKLG+C46q0SxhQjWC4FJMBOjz1DkvEoYAcJr1GHY8+jjnFKncZ47xvjlj+MCVEgyiSQThJAJEoig06kFLmPE2pSSYApJcGkjeng8YBA4AAdjcE4FATgYgRmCHALiAA2eAE5DAqJgEUZeYlfQL3pq0Do99H6iIxlmOJSoMk1AqO-WBRylQdJQGZWEksf7+12kslRGI4AvKVKAlWm0IHqx2r5GBn90DqIQcbJB4ozYvm0JbD+DsbZYJQRjPBGUsqELdswEhJhpGPMrtQkqwccWz0RXrJhNU6rovjoneMoEsmpjTnw3M+ZBEjRERRcRJdGxlz+b7QlsjzbAAUdy+oe10SvOOXMEF5CwUrmuismK4qxAEvfu8o8KAVEWKsfcxedCwJtLcR4+oXifHzS4eUDqq94YjD1aUw15STU4wGVvSwTdbKbBJkgBIYBnV9ggG6gAUhAT2Kz-ANLVGs7JtMtmNCaMyGSPQXFPzhYpUY2AEDAGdVAOAEBbJQDWC49xpyHkXKBcmkYqb02UCzTmvNJTpB3K-DIqhAArQNaBXkBvFOqlAhJlZuVrr8+uo4lFa1brAyV3dwUSjkcAGFGDbbYJLcighRCMVoC9ti3+xUg61x1VXEtpKWEUvYVSlqC9aXYXpRnfhTLCxCNLGNNlUBpoSPtYtblFC+VQvkT8xRAAzCFYra3jvUJo5Bcr5T5ukJgpKp8KwcCqii12x9MVkOWo2rseLt3D13bBuYB7yUNWPZwml3D1m1HTJasYmcb05zvfUPQ64UQ9pyJIzlqGQPofqComuw8B1dx2i2ztSoMTlozVW6AwHzrSsfBuWAkBcOGH6OKRwonK3ZugAY9+Ha22fP-JY8WWqbFgULdqs1ZGLXrzY46-GXh03us9bZ+UiBgywGANgVNhA8gFFWZfTZod6aM2ZqzdmpCaWGK1fUAzDbeVUJANwPAbz4tQAUO5r5fbeOKLiy5t0qg1Hsakz3Pu6JKy6Jy4KwdwqktmLy+XaVMgiuGB0QpjQP6hW7SSww9AuXJOaPq83RrfZiVIRazu8LX56jObwCl7AGqotwiM7UEYJnz05Is6MDeQA"> Sequence Diagrams </a>

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
