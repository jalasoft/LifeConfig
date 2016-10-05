# LifeConfig

##Overview

*LifeConfig* is a configuration library built on top of other low level configuration libraries like TypeSafe Config or Snake Yaml. It abstracts 
accessing configuration properties into regular java interface whose implementation is baked by LifeConfig, providing values from configuration 
files under the cover.

Here is a simple example. Let's consider we have a yaml file:

```yaml
config:
  db:
    url: "prg21.jalasoft.com"
    port: 5432
    dbName: my_database
    user: admin
    password: 12345
```

And following java interface:

```java
@KeyPrefix("config.db")
public interface DatabaseConfiguration {

  String url();
  
  int port();
  
  String dbName();
  
  String user();
  
  String getPassword();
}
```

Then we can ask LifeConfig to map the properties in the yaml file to methods of the interface.
The following snippet prepares the dynamic implementation of the interface *DatabaseConfiguration*:

```java
MyConfiguration config = LifeConfig.pretending(MyConfiguration.class)
    .yaml()
    .fromFile("/home/lastovicka/config.yml")
    .live()
    .load();
```


LifeConfig simply hides format and source of configuration into an instance of an interface, accessed by your application without any knowledge of such details.



##Maven

LifeConfig library is available in Maven central repository:
```xml
   <dependency>
        <groupId>com.github.jalasoft</groupId>
        <artifactId>LifeConfig</artifactId>
        <version>1.0.2</version>
   </dependency>
```

##Basics

###LifeConfig class

Class `LifeConfig` serves as a starting point of initialization that takes several steps. 
* First, we need to define which interface will be mapped to a configuration file, via factory method `pretending()`
* We must specify format of the configuration by invoking builder methods `yaml()`, `hocon()` or `javaProperties()`
* Then a source of configuration is next step, we can specifying it by invoking method `fromClasspath()`, `fromFile()`
* Additionally we might request receiving always fresh values in case that a source is changing over time by method `live()`
* Additionally we might register a converter of values in case that an interface method returns slightly different value than what
is seen in a configuration file, by method `addConverter()`
  
 

In the example above, we demanded that we want to use the interface `MyConfiguration` as a source of configuration values (factory method `pretending()`. 
Next, we defined format (`yaml()`) and source (`fromFile()`) of the configuration. The last step was invocation of method `live()` meaning that we want 
to receive the latest values from the configuration file, even though the content is varying after the initialization phase. 

As a result, we obtained a dynamic implementation of the interface that can be used as an ordinary interface (without any knowledge of the 
implementation):

```java
String url = confing.dbUrl();
int port = config.port();
String dbName = config.databaseName();
String user = config.user();
String pwe = config.getPassword();
```

###Supported formats and sources

*LifeConfig* on its own supports following formats of configuration:
* [Property file](https://en.wikipedia.org/wiki/.properties) - `javaProperties()` method 
* [Hocon](https://github.com/typesafehub/config/blob/master/HOCON.md) - `hocon()` method
* [Yaml](http://yaml.org/) - `yaml()` method
* custom format by providing an implementation of the interface ConfigFormat - `format(ConfigFormat)` method

***Note that for full usage of the last two formats you have to provide following libraries on classpath: ***

TODO

###Annotations

You might have noticed the annotation `@KeyPrefix` assigned to the configuration class in the example at the beginning. LifeConfig library comes with a set
of annotations that makes the adaption between configuration file and a configuration interface more smooth. 

#### `@KeyPrefix` is an annotation allowing prepend each key (configuration file)/method (configuration interface) with any custom string. This happens very often
 since usually we don't have out desired properties in the root of our configuration but it is sometimes hidden under one or more levels of configuration
 hierarchy.

```java
@KeyPrefix("config.db")
public interface DatabaseConfiguration {
    int port();
     ...
}
 ```

In this example each key that is associated with the interface mehod will have a prefix *config.db*. So the key that will be searched in the configuration file
will be `config.db.port`.

### `@Key` helps specify key. If there is no annotation associated with a method, then its name will be used as a key (or part of the key), otherwise a value
of the annotation is taken as a key instead.

```java
@KeyPrefix("config.db")
public interface DatabaseConfiguration {
    @Key("dbport")
    int port();
     ...
}
 ```
 
Here the resulting key to be searched in a configuration file will be `config.db.dbport`. 

### `@Converter` allows a value in a configuration file to be converted to a type returned by the method that is associated with the annotation. Let's have a 
converter called *UrlConverter* implementing *Converter* interface, that gathers a string value from a configuration file and parses it into an instance of the
class *java.net.URL*:

```java
@KeyPrefix("config.db")
public interface DatabaseConfiguration {
    
    @Key("dbport")
    @Converter(UrlConverter.class
    URL dbUrl();
     ...
}
 ```
 
 Here we expect that a value in the configuration file might be something like *host=myserver,port=5432, dbname=my_db* and the converter understands this syntax to
 create a new instance of *java.net.URL*.
 
### ```@IgnoreProperty``` You might encounter a situation when you do not want to have a method on a configuration interface to provide any value. For example you 
want to temporarily disable it or you do not own the configuration interface and do not have corresponding configuration for a method. For this purpose you can 
designate a method by an annotation `@IgnorePropety`:
                          
```java
public interface PersonInfo {
                          
    String getName();
    String getLastName();
                            
    @IgnoreProperty
    int getAge();                          
}
```
                          
In case that you invoke the method `getAge()` an exception `PropertyIgnoredException` will be thrown.
 

### Converters

### Nesting

### Default methods

### Collections

##Customization

###Formats



###Sources

*LifeConfig* supports following sources of configuration:
* filesystem file - `fromFile(Path)` or `fromFile(String)` methods
* classpath resource - `fromClasspath(String)`, `fromClasspath(Classloader, String)` and `fromClasspath(Class, String)` methods
* custom source by providing an implementation of the interface ConfigSource - `from(ConfigSource)` method


