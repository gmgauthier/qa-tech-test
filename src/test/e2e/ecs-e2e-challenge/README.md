# Cucumber-JVM Implementation of the ECS partitioning challenge

To run the tests:

```bash
cd src/test/e2e/ecs-e2e-challenge
./run_tests.sh
```

The shell script does the following things:

* moves to the root of the react project
* builds a new docker container
* starts the container (and capture the container id)
* moves back to the cucumber-jvm root
* runs the self-contained gradle clean and build tasks
* runs the self-contained gradle cucumber task
* shuts down the docker container

If you prefer to manually run the React App on your own, then simply start it up using yarn in a separate terminal, and then do this:

```bash
cd src/test/e2e/ecs-e2e-challenge
./gradlew clean build cucumber
```

## Minimum Requirements

* Java 8 (openjdk, mac java, or oracle all work)
* Gradle 6+ (builtin to the project)
* Docker 19 (what I tested with)

### Notes for non-java users

* You need not install gradle locally. It's bundled into the project. The `gradlew` script in the root of the java project will handle everything related to package dependencies for you.
* `build.gradle` includes a list of all the required libraries. If you're using the `./gradlew` command to run the tests (which is also in the runner shell script), it will automatically get all the dependencies listed from the maven repositories online.

## Project Notes

In this iteration of the challenge, I decided to add the cucumber layer, since folks were keen to see that. I only implemented one scenario (which I'll explain in a moment), but I also implemented a "Background" stage.

The background stage insures that the webdriver fires up properly, and that the browser is directed toward the react app url. The background stage will throw a test failure (NOT an exception) if the browser tab title is not "React App" after navigation. This insures that basic navigation is successful, and nothing more. Contents of the page will be tested later.

If the testing requirements were broader, I might also have broken out the initialization of the driver object into a separate class as well, in order to handle things like browser specification at runtime, or parallel testing, or virtual environment, implementations.

The scenario I implemented intentionally avoids all use of specific detail (such as doing tabular data comparisons or partition algorithm checks). This is because, under normal conditions, I would have expected this data to change (in other words, having it given by a backend api). In other words, in the ideal situation, you shouldn't be able to predict the correct solutions to each of the three rows.

There is only one scenario, because I count clicking on the "Render The Challenge" button as part of the functionality of the GIVEN step. The WHEN step relies on functional responses (i.e. exceptions) for feedback, and the THEN step finally does an explicit pass/fail check.

You could potentially split this into two scenarios: one to test the "render" button behavior at the top, and one to test the "submit" at the bottom. But, I would discourage this, because this is to confuse the kind of "behavior" that Cucumber is designed for. In the two-scenario approach, you're testing the discrete functional behavior of particular compoents, on input. In the one-scenario approach, you're testing the transactional behavior of the application, in relation to the user. The former approach is much better handled by Javascript unit or integration tests, where component inputs and outputs are the focus. The latter approach is better handled by Cucumber end-to-end tests, because the focus is on the *user journey*, not the individual application components.

I used two different variants of Gherkin assertion in this test. First, in the Background, I used normative ("should") language for the assertion. Then, in the test itself, I used state ("is") language. This was by design to show that I am aware of the difference. But there is an argument to be made that the state language is more appropriate (if you think of gherkin scenarios as state tables).

I took the opportunity in the rewrite, to modularize the test a bit more than the python one, as well. You'll notice that I've moved a few new things out into "helper" methods.

As before, I have chosen not to break out the locator strings into separate static variables, or "page objects", because the test suite is just too small to warrant it, and because I've implemented a few additional optimizations to the element search strings to minimize the brittleness. For example, the submit button in python looks like this: `"//*[@id='challenge']/div/div/div[2]/div/div[2]/button"` but in the java project looks like this: `"//*[text()='Submit Answers']"`, completely eliminating all explicit DOM hierarchy references.

A JUNIT runner could be written for this project, but since I was focusing on Cucumber, I decided to let gradle handle the test execution through the Cucumber plugin.

FINALLY, as before, you'll notice that **the test FAILS**, ultimately. This, again, is because the app itself seems to be failing to communicate with a backend which was supposed to validate the solution responses. The test is explicitly looking for the "Congratulations" message that should be present in the popup, but because the app is apparently broken, it only produces the "not quite right" response. I experimented with sending clear keystrokes before sending the numbers, but that didn't help, either. On the upside, the Junit5 matchers provide a much cleaner failure message, than the python does.

_PS: If need be, I could potentially provide a version of this in Ruby, or .Net. I'd just need a bit more time to realize them (perhaps a week?)._

## Conclusion

If you get stuck or have trouble, PLEASE feel free to email or call:

* gmgauthier@protonmail.com
* +44 (0)79 100 94267

Regards,
Greg.
