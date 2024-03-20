# Spark for Java: Tuples

## 1. Introduction

**Apache Spark** is a powerful, **distributed computing system** that allows for big data processing and analytics across clusters of computers

It provides APIs in several programming languages, including **Java**

In Spark, particularly when working with its **Resilient Distributed Dataset (RDD)** and **Dataset APIs**, tuples are a common data structure used to group elements

Each element in a **tuple** can be of a different type, and tuples are immutable

**Tuples** in **Java** are not as straightforward as in **Scala** or **Python**, given Java's static type system and lack of built-in support for tuples

However, when working with Spark in Java, you can utilize the **scala.Tuple2**, **Tuple3**, ..., **Tuple22** classes provided by the Scala library for tuples with up to 22 elements

These classes are fully interoperable with Java

## 2. Sample 1: Spark for Java (Tuples)

This Java code uses Apache Spark, a powerful open-source processing framework for large data sets, to perform a simple map operation

Here's a brief overview of its components and what each part does:

**Package Declaration**: package com.virtualpairprogrammers; This line declares the package name, which is used to organize the code into a namespace that avoids name conflicts

**Imports**: The code imports necessary classes and interfaces from Java and Apache Spark libraries. For example, JavaRDD, JavaSparkContext, and SparkConf are Spark classes for resilient distributed datasets (RDDs), Spark context configuration, and context management, respectively

**Class Declaration**: The Main class encapsulates the application logic

**Main Method**: The main method is the entry point of the application. It performs the following tasks:

- Initializes a list of integers (inputData) with values

- Sets the logging level for Apache Spark to WARN using Log4j, reducing the verbosity of log messages

- Configures Spark with a SparkConf object, setting the application name to "startingSpark" and the master to "local[*]", which means Spark will run locally with as many worker threads as logical cores on your machine

- Initializes a JavaSparkContext (sc) with the Spark configuration. The context is used to interact with Spark's functionalities like creating RDDs

- Creates an RDD (originalIntegers) from the inputData list using the parallelize method. RDDs are Spark's core abstraction for working with distributed datasets

- Transforms the originalIntegers RDD into another RDD (sqrtRdd) using the map operation

  This transformation applies a function to each element of the original RDD, in this case, creating a tuple containing the original number and its square root

- Executes an action (foreach) to print the square root of each number. Actions in Spark trigger computations on the RDDs

- Finally, closes the Spark context (sc.close()), releasing the resources

In summary, this code demonstrates how to set up a basic Apache Spark application in Java, create an RDD from a collection, transform data within RDDs using map, and perform actions on RDDs

Run VSCode and create the following project structure

![image](https://github.com/luiscoco/Spark_for_Java-Tuples/assets/32194879/5607e80c-1e83-4f23-a95f-78111bed8e76)

Then we input the application source code

**Main.java**

```java
package com.virtualpairprogrammers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;

import scala.Tuple2;

public class Main {

    public static void main(String[] args) 
    {
        List<Integer> inputData = new ArrayList<>();
        inputData.add(35);
        inputData.add(12);
        inputData.add(90);
        inputData.add(20);
        
        Logger.getLogger("org.apache").setLevel(Level.WARN);
        
        SparkConf conf = new SparkConf().setAppName("startingSpark").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);
        
        JavaRDD<Integer> originalIntegers = sc.parallelize(inputData);
        
        JavaRDD<Tuple2<Integer, Double>> sqrtRdd = originalIntegers.map( value -> new Tuple2<>(value, Math.sqrt(value)) );
        
        // Action to print each element of the RDD
        sqrtRdd.foreach(new VoidFunction<Tuple2<Integer, Double>>() {
            @Override
            public void call(Tuple2<Integer, Double> t) throws Exception {
                System.out.println("The square root of " + t._1 + " is " + t._2);
            }
        });
        
        sc.close();
    }
}
```

**IntegerWithSquareRoot.java**

```java
package com.virtualpairprogrammers;

public class IntegerWithSquareRoot {

	private int originalNumber;
	private double squareRoot;
	
	public IntegerWithSquareRoot(int i) {
		this.originalNumber = i;
		this.squareRoot = Math.sqrt(originalNumber);
	}

}
```

**Util.java**

```java
package com.virtualpairprogrammers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A wrapper for an input file containing a list of what we think are "boring" words.
 * 
 * The list was generated by running a word count across all of VirtualPairProgrammer's subtitle files.
 * 
 * Words that appear in every single course must (we think) be "boring" - ie they don't have a relevance
 * to just one specific course.
 * 
 * This list of words is "small data" - ie it can be safely loaded into the driver's JVM - no need to 
 * distribute this data.
 */
public class Util 
{
	private static Set<String> borings = new HashSet<String>();
	private static Map<String, String> corrections = new HashMap<String, String>();
	
	static {
		InputStream is = Main.class.getResourceAsStream("/boringwords.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		br.lines().forEach(it -> borings.add(it));
		
		corrections.put("jav", "java");
		corrections.put("hybernate", "hibernate");
		corrections.put("tedius", "tedious");
		corrections.put("install", "install");
	}

	/**
	 * Returns true if we think the word is "boring" - ie it doesn't seem to be a keyword
	 * for a training course.
	 */
	public static boolean isBoring(String word)
	{
		return borings.contains(word);
	}

	/**
	 * Convenience method for more readable client code
	 */
	public static boolean isNotBoring(String word)
	{
		return !isBoring(word);
	}
	
	/**
	 * We discovered some typos in our subtitles (sorry!) - any corrections will appear here
	 */
	public static String correct(String word)
	{
		if (corrections.containsKey(word)) return corrections.get(word);
		return word;
	}
}
```

**pom.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<groupId>com.virtualpairprogrammers</groupId>
	<artifactId>learningSpark</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>jar</packaging>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
		<java.version>1.8</java.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>org.apache.spark</groupId>
			<artifactId>spark-core_2.10</artifactId>
			<version>2.0.0</version>
		</dependency>
		<dependency>
			<groupId>org.apache.spark</groupId>
			<artifactId>spark-sql_2.10</artifactId>
			<version>2.0.0</version>
		</dependency>
		<dependency>
			<groupId>org.apache.hadoop</groupId>
			<artifactId>hadoop-hdfs</artifactId>
			<version>2.2.0</version>
		</dependency>

	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>3.5.1</version>
				<configuration>
					<source>1.8</source>
					<target>1.8</target>
				</configuration>
			</plugin>
			<plugin>
				<artifactId>maven-assembly-plugin</artifactId>
				<configuration>
					<source>1.8</source>
					<target>1.8</target>
					<archive>
						<manifest>
							<mainClass>Main</mainClass>
						</manifest>
					</archive>
					<descriptorRefs>
						<descriptorRef>jar-with-dependencies</descriptorRef>
					</descriptorRefs>
				</configuration>
				 <executions>
				    <execution>
				      <id>make-assembly</id> <!-- this is used for inheritance merges -->
				      <phase>package</phase> <!-- bind to the packaging phase -->
				      <goals>
				        <goal>single</goal>
				      </goals>
				    </execution>
 				 </executions>
			</plugin>
		</plugins>
	</build>
</project>
```

**How to run the application**:

```
mvn compile exec:java "-Dexec.mainClass=com.virtualpairprogrammers.Main"
```

![image](https://github.com/luiscoco/Spark_for_Java-Tuples/assets/32194879/bbca2d1e-d02f-4716-ab99-3229da6e794f)

![image](https://github.com/luiscoco/Spark_for_Java-Tuples/assets/32194879/189f9623-add4-4ee0-8c13-10cb16782b82)

## 3. Sample 2: Spark for Java (Tuples)


