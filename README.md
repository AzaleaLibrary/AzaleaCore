<div>
  <a href="https://github.com/AzaleaLibrary/AzaleaCore/actions/workflows/ci.yml">
    <img alt="Azalea Core CI" src="https://github.com/AzaleaLibrary/AzaleaCore/actions/workflows/ci.yml/badge.svg?branch=master" />
  </a>
  <a href="https://azalealibrary.net/#/releases/net/azalealibrary/core">
    <img src="https://azalealibrary.net/api/badge/latest/releases/net/azalealibrary/core?color=40c14a&name=Azalea%20Core&prefix=v" />
  </a>
</div>
 
# AzaleaCore

### How to

#### Import `AzaleaCore`

In **pom.xml**:

```xml
    <repositories>
        <!-- other repositories -->
        <repository>
            <id>azalea-repo-releases</id>
            <name>Azalea Repository</name>
            <url>https://azalealibrary.net/releases</url>
        </repository>
    </repositories>
    
    <dependencies>
        <!-- other dependencies -->
        <dependency>
            <groupId>net.azalealibrary</groupId>
            <artifactId>core</artifactId>
            <version>1.0</version>
        </dependency>
    </dependencies>
```

#### Register a minigame

> See a working example [here](https://github.com/AzaleaLibrary/ExampleMinigame).
