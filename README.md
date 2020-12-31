# EasyMySQL
Easy to use MySQL lib using HikariCP as backend

# How to get

**Maven**
```xml
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
```
```xml
        <dependency>
            <groupId>com.github.kyngs</groupId>
            <artifactId>EasyMySQL</artifactId>
            <version>1.2-SNAPSHOT</version> <!--Insert version here, see releases -->
        </dependency>
```

# How to use
**Creating**
```java
        MySQLBuilder builder = new MySQLBuilder();
        builder.setDatabase("database");
        builder.setPort(3306);
        builder.setUsername("root");
        builder.setHost("localhost");
        builder.setPoolName("Super Pool");
        builder.setPassword("123");
        MySQL mySQL = builder.build();
```

**Using**
```java
        mySQL.async().schedule(connection -> connection.prepareStatement("CREATE SCHEMA async").execute()); //Creating new schema async, the job gets put into queue waiting for worker to process it. (Async). See MySQLBuilder#setThreadCount
        mySQL.sync().schedule(connection -> connection.prepareStatement("CREATE SCHEMA sync").execute()); //Creating new schema sync, job gets executed immediately. 
```
Closing ResultSets and PreparedStatements is handled automatically, so you only need to do the important things.
