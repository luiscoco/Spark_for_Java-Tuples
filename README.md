# Spark for Java: Tuples

## 1. Introduction

## 2. Sample 1

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

We run the application with the following command:

```
mvn compile exec:java "-Dexec.mainClass=com.virtualpairprogrammers.Main"
```

![image](https://github.com/luiscoco/Spark_for_Java-Tuples/assets/32194879/bbca2d1e-d02f-4716-ab99-3229da6e794f)

![image](https://github.com/luiscoco/Spark_for_Java-Tuples/assets/32194879/189f9623-add4-4ee0-8c13-10cb16782b82)
