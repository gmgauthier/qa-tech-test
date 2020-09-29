To run the tests:

```bash
cd src/test/e2e
./run_tests.sh
```

Assumptions:
    Your environment already has:
        > Python 3 (3.6.x or better)
        > Docker

This will:

1. use docker to prepare the react app. I chose to do this, because it was the simplest way to do setup / teardown of the app, under the circumstances.

2. prepare your python environment, by running the pip installer (using the requirements file)

3. Execute the test using the pytest runner installed by the previous step.

### NOTES

* I initially tried to build this with javascript, but decided to redo it with python, because at the moment, I'm more confident in python.

* I have checked the calculations multiple times, and am convinved that these values are the correct responses: 10, 92, and 40. However, the app refuses to recognize those as valid responses. So, this challenge is set up to throw a failing test *by design*. It looks at the text of the response after clicking the submit button, and if it's the "fail" text, then the test fails on assertion. That is why this challenge looks as though it's failing.

* I briefly attempted to inspect the javascript, to see if I could fix this myself, but it looks as though the app is supposed to be connecting to some AWS backend when it starts up, and is supposed to use that to validate answers. That seems beyond the scope of this challenge. So, I'm letting the test fail.

* I used an explicit WebdriverWait twice. Once, to insure that the automation would wait long enough for the scroll. However, this wait was technically not necessary. Since the entire DOM is visible to the driver, I could have proceeded to collect the table data while the scroll was taking place. I waited anyway, to conform to the implicit rules of the challenge. The second webDriverWait was put before the response popup on the submit button. Here, a wait makes more sense, but still, the responsiveness of the app is such that a wait here is almost not needed.
