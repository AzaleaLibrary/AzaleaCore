<div>
  <a href="https://github.com/AzaleaLibrary/AzaleaCore/actions/workflows/ci.yml">
    <img alt="Azalea Core CI" src="https://github.com/AzaleaLibrary/AzaleaCore/actions/workflows/ci.yml/badge.svg?branch=master" />
  </a>
  <a href="https://azalealibrary.net/#/releases/azalea-library/azalea-core">
    <img src="https://azalealibrary.net/api/badge/latest/releases/azalea-library/azalea-core?color=40c14a&name=Azalea%20Core&prefix=v" />
  </a>
</div>
 
# AzaleaCore

### How to

#### Import `AzaleaCore`

1. Package *AzaleaCore* by running `mvn package`.

2. Create a folder called `lib` in the root of your project and place the jar file in it.

3. In **pom.xml**:

```xml
  <dependencies>
    <!-- other dependencies -->
    <dependency>
        <groupId>azalea-library</groupId>
        <artifactId>azalea-core</artifactId>
        <version>1.0</version>
        <scope>system</scope>
        <systemPath>${project.basedir}\lib\<filename>.jar</systemPath>
    </dependency>
  </dependencies>
```

#### Register a minigame

> See a working example [here](https://github.com/AzaleaLibrary/ExampleMinigame).
