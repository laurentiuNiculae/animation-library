#  Simple Animation Rendering Library


## External Dependecies:
You will need to install [Raylib](https://github.com/CreedVI/Raylib-J/releases/tag/v0.5.2) and [Processing](https://processing.org/download) jars in your local repository. After you downloaded the library jar you can use this command:

```bash
$ mvn install:install-file -Dfile=./core/library/core.jar -DgroupId=org.processing -DartifactId=core -Dversion=4.0.0 -Dpackaging=jar
```

Same for the raylib jar.

## Build

Build a jar

```bash
$ mvn package
```

Then run it 

```bash
$ java -jar ./target/animation-renderer-lib-2.0.0.jar
```

