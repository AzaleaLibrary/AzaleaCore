# AzaleaCore

### Maven Import

In **pom.xml**:

### (BROKEN)

```xml

  <repositories>
    <!-- repos -->
    <repository>
        <id>azalea-core-repo</id>
        <url>https://github.com/AzaleaLibrary/AzaleaCore/raw/public/</url>
    </repository>
  </repositories>
  
  <dependencies>
    <!-- deps -->
    <dependency>
        <groupId>AzaleaLibrary</groupId>
        <artifactId>AzaleaCore</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
  </dependencies>

```

### (WORKING)

```xml

  <repositories>
    <!-- repos -->
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
  </repositories>
  
  <dependencies>
    <!-- deps -->
    <dependency>
        <groupId>com.github.AzaleaLibrary</groupId>
        <artifactId>AzaleaCore</artifactId>
        <version>master-SNAPSHOT</version>
    </dependency>
  </dependencies>

```
