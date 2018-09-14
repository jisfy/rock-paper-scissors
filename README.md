
## Disclaimer

This project and its source code reflects my personal coding style, which you may or may not like. Things like having
public and private methods out of order, which I do on purpose by reasons explained in Clean Code. The existence of
some classes, which may seem redundant or verbose, which is required by DDD, etc. 
Needless to say that when you join a team you always need to adopt their style and coding conventions. So please, 
read this code with that mindset. The style is simply personal preference and can always be adapted. 

## Design decisions

- I will try to adhere to the Domain Driven Design principles and create a core Domain Model which is infrastructure
agnostic. Please see [[1]](#references) and [[2]](#references) for more information

- I hope I did not misunderstand the problem. The current solution is purely sequential. It does not simulate two
players running on different threads, or anything like that. There is a driver application which will sequentially
simulate players making their moves.

- I have decided the make the different moves, Rock, Paper and Scissors explicit in the Domain Model. \
It would probably be easier to just use an enum type, and have the Game Rules coded in each Game implementation. 
If we wanted to implement some extended game version, like "Rock, Paper, Scissors, Well", we would then need to add a 
new value to the enum type, and have a new Game implementation with the new rules coded. The down side is that we 
are modifying the old Move enum, violating then the Open Closed Principle. Having a new nextMove type could have an impact
on the old classic game logic, and break it.\
On the other hand, having explicit value objects for the different moves, allows us to implement the game rules
in the moves themselves, and gets us rid of the enum type altogether. Sort of a replace conditional with polymorphism
if you will. [[3]](#references)\
Implementing an extended version of the game, like "Rock, Paper, Scissors, Well", requires adding a new class and 
probably subclassing the existing moves to adhere to the new rules too. With this version, we would create a new 
sub-interface of ClassicMove which adds support for the new Well nextMove, and probably extend the classic Paper, 
Rock and Scissors to implement the ExtendedClassicMove interface adding the handling for the Well nextMove.\
I had to simulate multiple-dispatch with single-dispatch here. I had to leave an unsafe casting to the (ClassicMove)
interface in the different ClassicMoves. At least, this allows me to leave Domain Model open for further extension
with different exotic moves.

- I have created explicit Value Object classes for the PlayerName and Iterations, which has several benefits [[4]](#references). 
One being the possibility to add validation logic to them, instead of having that logic scattered all over the place in 
other classes. The down side is having to create new classes and also their corresponding equals, toString and hashCode 
methods. In order to ease that pain a little I have used the library AutoValue which takes care of that.

- I have added a name to the Player Entity, even though it was not strictly necessary. Otherwise the GamePlay class
ended up with player one and player two. Its GamePlay.toString method could do very little but make up the player names,
and I thought a GamePlay class was not the right place to provide names for players. This led to adding the name 
attribute to the Player class, and then the PlayerName value object. 

- For now, the player names come from constants in the main application class. They could come from the properties
file too. However, the nice thing about the current design is that it lends itself well to accepting player names
from the command line.

- I am trying to leave the Domain Model classes as Spring agnostic as possible. Even though it would just be annotations,
if I can leave them out, the better. That is why I have to create Domain Model beans in the Spring @Configuration later
on.

- I am leaving the main application usage and error strings hard coded as constants. I could move them to the 
properties file for i18n, but I don't think this is important at this stage.

- I had an implementation of the Games.summarize method that relied on Stream.reduce and used GameSummary as an 
accumulator. Nevertheless it required a combiner method in the GameSummary which wasn't even used unless the reduce
was performed in parallel. The implementation was getting really convoluted and would require significant testing, 
so I decided to go for something simpler. Simply previously partitioning by whether the GamePlay result is DRAW.
Then grouping by PlayerName the non-draw results. It is not as elegant or 'efficient' as the other solution, but way 
simpler, and this is just to print out a game summary after all.

- I couldn't find anywhere in the Rock Paper Scissors game description anything about the correct behavior of the game
if one of the players simply can't perform a nextMove. If at all possible. So I am assuming that the correct behavior
is to expect the Players to always be able to perform a nextMove when required. Consequently, if our unreliable HTTP based
MoveStrategy is unable to work properly, that shouldn't affect the Game Play at all. This also means, that MoveStrategy
failures should not leak into the Domain Model classes. Not even with a non infrastructure related exception. All of
this makes me think I should have a fallback strategy for an unreliable MoveStrategy.

- I have added a CircuitBreaker pattern and made sure the timeouts are set in the RemoteRESTfulMoveStrategy 
implementation. Please see [[5]](#references) and [[6]](#references). You can see them in action by increasing the log 
level to DEBUG. You can do that with a command line property (see Enabling debugging in the running section), or simply
editing the src/resources/logback-spring.xml file, and changing the level of the "com.gig.rockpaperscissors" logger. 
If you then shut down the service or increase its latency significantly, you should see something like this coming out in the console \
"Accessing the remote api at http://localhost:8080/api/movestrategy/random/nextmove", "Falling back to Rock"

- I have created a different class "RemoteNextMoveResource" to represent the next move received from the service API
endpoint. I am not using com.gig.rockpaperscissors.infrastructure.rest.NextMoveResource on purpose, even though it 
might seem like code duplication. Notice that the service api should live in a different project from the command 
line tool. I am only combining both here in a single project for the sake of brevity. Using NextMoveResource 
from the RESTful client too would result in a cyclic dependency. That would make it difficult to segregate the projects
later on. Having RemoteNextMoveResource also enables the command line tool to be decoupled from the service API, 
which could change at a different speed.    

- I have added induced latency to the service API endpoints, in order to control easily the response time of the 
service. This will help me with the set up of time outs on the client side, etc. Check the "Running" section to see
how to use it.

- I could probably change the interface of Game.play to return a Stream instead of a Collection. This would help if the
game was to play for a long time, etc.

## Building

#### Requirements

- JDK 1.8
- Maven 3.5.3 or later 
 
In order to build the complete project, from the root project folder, type __mvn clean install__ in a command line shell.

## Running

#### Requirements

- JDK 1.8 (Make sure you are not using a later version for running). If you see JAXB errors coming up, you are using
a later version
- Maven 3.5.3 or later (optional)

##### 1) Using the command line tool

You can launch the execution of the Rock Paper Scissors Game like so:<br>
_java -Dspring.profiles.active=tool -jar target\rock-paper-scissors-0.0.1-SNAPSHOT.jar_<br>

The application will then play Rock Paper Scissors with the default strategy (FAIR) and default iterations (10).
Notice that you can alter any of these parameters from the command line:<br>
_java -Dspring.profiles.active=tool -jar target\rock-paper-scissors-0.0.1-SNAPSHOT.jar --iterations=20_<br>
_java -Dspring.profiles.active=tool -jar target\rock-paper-scissors-0.0.1-SNAPSHOT.jar --mode=UNFAIR --iterations=2_<br>

...If you want to use the _REMOTE_ mode, make sure the HTTP service is previously up 

###### Enabling debugging
_java -Dspring.profiles.active=tool -Dlogging.level.com.gig.rockpaperscissors=DEBUG -jar target\rock-paper-scissors-0.0.1-SNAPSHOT.jar_

##### 2) Starting up the Service:

Type _mvn spring-boot:run_ in the console. Or if you don't have Maven installed, simply <br>
_java -jar target\rock-paper-scissors-0.0.1-SNAPSHOT.jar_

You can make sure the service is working by going to your web browser and entering <br>
http://localhost:8080/api/movestrategy/random/nextmove

###### Inducing service latency
You can also simulate a slow response service using the property rockpapersicssors.api.induced.latency like so: <br>
_java -Drockpapersicssors.api.induced.latency=3000 -jar target\rock-paper-scissors-0.0.1-SNAPSHOT.jar_

 
## Pending

- Unit testing for the GameSummary.toString
- Unit testing the fallback with RemoteRESTfulMoveStrategy

## References

* [1]: Evans, Eric. (2003). Domain Driven Design. Addison Wesley
* [2]: Fowler, Martin (2003). In Anemic Model. https://martinfowler.com/bliki/AnemicDomainModel.html
* [3]: Fowler, Martin. Beck, Kent, and others (2002). Replace Conditional with Polymorphism.
    In "Refactoring, Improving the Design of Existing Code" (pp. 205-209). O'Reilly
* [4]: Johnsson, Dan Bergh (2008). The Power of Value. https://vimeo.com/13549100
* [5]: Nygard, Michael T. (2007). Stability Patterns, Circuit Breaker.
    In Release It! Design and Deploy Production Ready Software (pp. 115-118). The Pragmatic Programmers
* [6]: Nygard, Michael T. (2007). Stability Patterns, Use Timeouts.
    In Release It! Design and Deploy Production Ready Software (pp. 111-114). The Pragmatic Programmers
