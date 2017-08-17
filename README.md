DNA Android Tools
=================

## What is this?

This library encapsulates solutions for some problems I constantly run across
in my work on the Android platform.

Since I hate copying code around multiple times, this library was created.

I publicize them for reference and educational purposes.
Feel free to copy if it's of any use for you.

## What is this not?

This is not a library which tries to beat any specific problem until it is guaranteed
to function in any circumstance.

In my years of experience I learned the hard way, that depending too much on external
code ("dependicitis") can all of a sudden turn into a coffin nail for a project.

It also tends to blow up resource consumption and sophisticated solutions
(like e.g. ORM libraries) become huge obstacles when trying to understand
a project as a newcomer. (In other words: increase the learning curve gradient.)

So don't expect, this code will help you perfectly in your special situation.

Copy a class, if you like it, grab some ideas, but don't rely on this as a
dependency, because I won't guarantee, that updates won't break your code...

## Usage

If you still want to use this library directly, you can do so in the following way:

In your project's `build.gradle` file:

```gradle
allprojects {
    repositories {
        // Should be available here:
        jcenter()
        // OR here:
        maven {
            url 'https://dl.bintray.com/berhart/maven/'
        }
    }
}

```


In your module's `build.gradle` file:

```gradle
dependencies {
    ...
    compile 'com.netzarchitekten:tools:8.1.0'
    ...
}

```

## Copyright

2015 - 2017 Die Netzarchitekten e.U., Benjamin Erhart
