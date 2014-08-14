# jcalc
This is a simple expression calculator written in Java.

It supports following:
- operators +, -, *, /
- constants E, PI
- functions sin, cos, exp
- brackets () to control operator ordering

It can evaluale expression like this:
`$ ./calc.sh "11+(exp(2.010635+sin(PI/2)*3)+50)/2"
Result is 110,99998`

## How to build
Project uses [Gradle](http://gradle.org Gradle) to build.
With `gradle` in your PATH just run `gradle build` in project dir.
It'll build the project and run tests.
