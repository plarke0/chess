# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

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

## [Phase 2 Diagram V1][p2v1]
[p2v1]: https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoARiqfJCIK-P8gK0eh8KVEB-rgeWKkwes+h-DsXzQo8wHiVQSIwAgQkAJJoBiDnCQSRJgKSb6GLuNL7gyTJTspXI3v5d5LsKYoSm6MpymW7xKpgKrBhqbrarq+ohUYEACmgEDMFaaJhbyEU2XZPZ9tuvk2f6sUQGoAByBVRmiUYxnGhSgZhyCpjA6b4aMYw5qoebzNBRYlvUejriihJqGADb0SVC4CkmvrOl2G7uluKXqjAzkgNAKLgDA6k7NVIHVP6h3HSg4BNKZGntSgsYKdpPUlGAaZOAAjARI1jQWYyTdA9Q+NMl7QEgABeKC7HRTY+YKw78vUh5yCgz7xNOhmhaji6VMuAZrgGl6vpt7Z6aWgnuf+CCAVZGHrR2NQvMsIVfLBl5UfWkIwFc3Us6JOF4UpRHzCRqHfBRvPIVLWnLZ43h+P4XgoOgMRxIk6ua7TvhYKJgqgfUDTSBG-ERu0EbdD0cmqApwyy4h6AfR+TOls7SGYNTxubfUtMtKeGL66eHkLd5lN+aVaMwIyYDY+ePMu2gc7hQ6kX1AA4kyZMvtosryl7rt7RqxdoDlagwPlhXWjkK37ldnbdr2-bIyzJuSpDerQ3DHAvW98ZC8mvXYf1f1ZssgP5hNxZgwq3fxL38NLIjDEE2t9ys3Z2MU52KO3rHHAoNwx6Xkn8Ep2nMeE0K9TSCfTKGLvBfxeXl0s9TAdCQbGSqABPsPZNzZjAEYgtrrwhHl9UWg0wGNgYsrZi-gUTrn8NgcUGp+JohgFnJUGgja1VLA0LOVtbb2CVE7ZO3sha6Q9vUd+vtyounsmiXBOYQ6sLweHEkH8qSH3pHHJkidy7X1WveYUODc4v3kIXY0VCS6qjLvIiuHAGp5QKjAIq9cN7AIqq3D+hDXSL2Xv3aMr1OpuygX1dM-1RhT35EDWeU0F4URMaveBDcyobWbnnK8W5258PTgI5AOQ2FqAxKIxumdJHMBCcwCAAAzSsCA454M8RnJhW04AQD7PdTqAAeMJPJygGNobCf0WDQlcLUNgAoBhAHlMgd4kBIxljkJzAWBo4x2koEctIAsv1wjBECCCTY8RdQoDdJyLmyxkigDVFMyCqk2lKkakqSEFwYCdHARJJp8BR7fRgLhWBKyOmm26UqPpAyhkjOWGMiZiyjJkTGHMkACyJYzLGD0tZcwNlbLXoxFWAQOAAHY3BOBQE4GIEZghwC4gANngBOQwYSYBFAOX7XZptWgdDIRQxecsszfKVDsxMZSvz0OUUS1ZJKGlfkxbZZhGN0RhIxHAZFYTuFeV4TIfho5BEJ3PiI9Jt9iY52YNI4Asj36lzkZfJCld1G12Kjoju-tq76PboYrurioCw3hgPCxw9Kgi3HrY1pw0HEz0LHPUsENdX6t2ALDxqrMn1ElTyje6MOVKgxMSuYkSIpEwkeyo8KKlR70ZTVclcJ6ihsxpympdSxCMO1RanpVz6iDOGc641+zoFHLFnYr5lz+lZpuc6pWTFVaWBPg5TYWskAJDALWvsEAG0ACkIDihwRWfwry1Toq+gyySTRmQyR6D0yh8r0BZmwAgYAtaoDZIclANYGbpCkuZjG-05c50LqXSu6A67S2WUaQyuyAArbtaBWVdvFIm+aPCAm8qCfy+OwjlGBozsG7OUjyavyLso5Kii5WURToq6uGitFYFdc0vRVUtXNLqsYvVfdDXvTzaamxk9LW5mtSDW14NUOOvcctOD29mEepfV6gVPS-Wlu-aKiR4re1zDivKDdIHUqpLmH0yDNcYAogSRhSAxoK6gDAD4E0vGxAUZ8i3RDUdtUACEQxhL6RhoeEDPrWKLRa6e40bXOJmkJlAT7FqVqbPJ9VRTdqgfaMGGYMAjqrsrGaGs4YurKeQ6WI9UAwxIS0953Zumx7pkzMWwzwNQaeyrOaGAtZ6xWYYjRvl4NsBaBZb6jdax52LsoP5tYIUmNrV-ZKTLmMzoHsKzk6AMAe12fkKUh4jT6j3tvUqP+tS0D1NTb5l4W69mmuOVmAFiDVZeEXY25tU35SIGDLAYA2B52EDyAUNFBCBuNHNpba2ttjBux3WBM99K3Uue4HgNll2oBcsjvvaOq10Y3YiSKsrd99mLaMNoPQ9SkOtYpZ9vA3Xk10usmmobYXDmjdGACoAA
