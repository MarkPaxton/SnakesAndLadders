
Snakes and Ladders Scala Code Exercise
===================

This is a sample project demonstrating some Scala code. The original requirement is here: http://agilekatas.co.uk/katas/SnakesAndLadders-Kata and all features have been implemented.

It is built with the Play Framework using the HMRC MDTP Microservice reference architecture.

Usage
-------------

### Running the application
For demonstration purposes the application can be run with SBT, this presents an HTML Frontend using the HMRC play framework templates. It will look prettier if the Assets Frontend Service is running and providing assets, however it is not required and will function as intended without this service running.
```
sbt run
```
###Running the Tests
```
sbt test
```

### Notes

There was a time constraint for this task, therefore it was approached from the point of view of getting the core logic working with associated tests. As always more can be done...

* The UI is text based and basic. 
* Checking for URL manipulation is not done, the game can be manipulated this way (useful for varying the game options)
* There is no substantial testing of Controller methods/output
* scala.util.Random should be refactored out to be mockable to improve testability (for the above)
* Play 2.5 Dependency injection features can be used as per compiler warnings
* On MDTP use of the keystore sevice is favoured over session data
