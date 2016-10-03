# LifeConfig

##Overview

*LifeConfig* is a configuration library built on top of other low level configuration libraries like TypeSafe Config or Snake Yaml that abstracts 
accessing configuration properties into regular java interface whose implementation is baked by LifeConfig that provides values from configuration 
files underneath.

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

Then we can ask LifeConfig to map the properties in the yaml file to methods of the interface, as a dynamic implementation of the interface.
The following snippet prepares the dynamic implementation of the interface *DatabaseConfiguration*:

```java
MyConfiguration config = LifeConfig.pretending(MyConfiguration.class)
    .yaml()
    .fromFile("/home/lastovicka/config.yml")
    .live()
    .load();
```


LifeConfig simply abstracts details of format and source of configuration into an instance of an interface, accessed by your application without any knowledge.

##Maven

LifeConfig library is available in Maven central repository:
TODO

##Basics

Class `LifeConfig` serves as a starting point of initialization that takes several steps. 
* First, we need to define which interface will be mapped to a configuration file, via factory method `pretending()`
* We must specify format of the configuration by invoking builder methods `yaml()`, `hocon()` or `javaProperties()`
* Then a source of configuration is next step, we can specifying it by invoking method `fromClasspath()`, `fromFile()`
* Additionally we might request receiving always fresh values in case that a source is changing over time by method `live()`
* Additionally we might register a converter of values in case that an interface method returns slightly different value than what
is seen in a configuration file, by method `addConverter()`
  
 

In the example above, we demanded that we want to use the interface `MyConfiguration` as a source of configuration values. Next, we defined 
format (`yaml()`) and source of the configuration (`fromFile()`). The last step was invocation of method `live()` meaning that we want to see 
the latest values of the configuration, even though the content is varying.

As a result, we obtained a dynamic implementation of the interface that can be used as an ordinary interface (without any knowledge of the 
implementation):

```java
String url = confing.dbUrl();
int port = config.port();
String dbName = config.databaseName();
String user = config.user();
String pwe = config.getPassword();
```

##Annotations



##Customization

###Formats

*LifeConfig* supports following formats of configuration:
* [Property file](https://en.wikipedia.org/wiki/.properties) - `javaProperties()` method 
* [Hocon](https://github.com/typesafehub/config/blob/master/HOCON.md) - `hocon()` method
* [Yaml](http://yaml.org/) - `yaml()` method
* custom format by providing an implementation of the interface ConfigFormat - `format(ConfigFormat)` method


###Sources

*LifeConfig* supports following sources of configuration:
* filesystem file - `fromFile(Path)` or `fromFile(String)` methods
* classpath resource - `fromClasspath(String)`, `fromClasspath(Classloader, String)` and `fromClasspath(Class, String)` methods
* custom source by providing an implementation of the interface ConfigSource - `from(ConfigSource)` method

##Accessing properties

**@KeyPrefix** defines a prefix of all keys associated with values that can be obtained by invoking any of the methods on the interface. For instance *port* property is mapped to a property *config.db.port* where *db.port* is a prefix of a key *port*.

##Nesting

##Edge cases

##Ignoring properties

You might encounter a situation when you do not want to have a method on a configuration interface to provide any value. For example you want to temporariy disable it. For this purpose you can designate a method by an annotation `@IgnorePropety`:

```java
public interface PersonInfo {

  String getName();
  String getLastName();
  
  @IgnoreProperty
  int getAge();

}
```

Then in case that you invoke the method `getAge()` an exception `PropertyIgnoredException` will be thrown.
