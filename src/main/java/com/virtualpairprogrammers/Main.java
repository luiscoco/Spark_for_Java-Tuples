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
