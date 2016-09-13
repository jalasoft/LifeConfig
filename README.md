# LifeConfig

##Maven

##Basics

*LifeConfig* is a configutation library that abstracts details of format and source of configuration and offers a dynamic implementation of provided interface as source of configuration. 

Basically what is needed to accomplish the usage of the *LifeConfig* is to provide in which **format** the **source** of configuration is and through which **interface** you will access it.

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
Then we can take advantage of the interface as an access point to the yaml configuration. The client application is accessing the configuration just via the interface without any knowledge of the underlying format or source of configuration. The following snippet prepares a dynamic implementation of the interface *DatabaseConfiguration*:
```java
MyConfiguration config = LifeConfig.pretending(MyConfiguration.class)
    .yaml()
    .fromFile("/home/lastovicka/config.yml")
    .live()
    .load();
```

First of all, we demanded that we want to use the interface *MyConfiguration* by starting with static factory method `pretending`. Next, we defined format (`yaml()`) and source of the configuration (`fromFile()`). The last step was invoking method `live()` meaning that we want to have the latest values of the configuration, even though the content of the file changes after initializing the configuration interface.
As a result, we obtained a dynamic implemntation of the interface that can be used as an ordinary interface (without any knowledge of the implementation):

```java
String url = confing.dbUrl();
int port = config.port();
String dbName = config.databaseName();
String user = config.user();
String pwe = config.getPassword();
```

As you might have noticed, the configuration interface provides a set of methods and contains a set annotations. Some of them will be described later.

##Formats

*LifeConfig* supports following formats of configuration:
* [Property file](https://en.wikipedia.org/wiki/.properties) - `javaProperties()` method 
* [Hocon](https://github.com/typesafehub/config/blob/master/HOCON.md) - `hocon()` method
* [Yaml](http://yaml.org/) - `yaml()` method
* custom format by providing an implementation of the interface ConfigFormat - `format(ConfigFormat)` method


##Sources

*LifeConfig* supports following sources of configuration:
* filesystem file - `fromFile(Path)` or `fromFile(String)` methods
* classpath resource - `fromClasspath(String)`, `fromClasspath(Classloader, String)` and `fromClasspath(Class, String)` methods
* custom source by providing an implementation of the interface ConfigSource - `from(ConfigSource)` method

##Converters

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
