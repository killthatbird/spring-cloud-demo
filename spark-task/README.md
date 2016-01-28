spark-task
==========

Spring Boot app for running Spark on YARN as a task

### Build with:

    ./mvnw clean install

### Start Hadoop

Follow instructions for the `SpringOne-2015-Edition` here [https://github.com/trisberg/hadoop-install#clone-this-repository-and-pick-a-branch-to-use](https://github.com/trisberg/hadoop-install#clone-this-repository-and-pick-a-branch-to-use)

If you use another Hadoop installation then you must copy the Spark Assembly jar to HDFS. This task assumes the file to be found at `hdfs:///app/spark/spark-assembly-1.5.0-hadoop2.6.0.jar`

### Compile your Spark app

There is an example app here - [https://github.com/trisberg/springone-2015/tree/master/spark/scala-hashtags](https://github.com/trisberg/springone-2015/tree/master/spark/scala-hashtags)

You can download it from here built as a jar - [https://github.com/trisberg/springone-2015/raw/master/batch-spark/app/spark-hashtags_2.10-0.1.0.jar](https://github.com/trisberg/springone-2015/raw/master/batch-spark/app/spark-hashtags_2.10-0.1.0.jar)

This example reads a file containing tweets. You can find sample data here - [https://github.com/trisberg/springone-2015/blob/master/batch-spark/data/](https://github.com/trisberg/springone-2015/blob/master/batch-spark/data/)

You can download this file and copy it to HDFS using:

    wget https://github.com/trisberg/springone-2015/raw/master/batch-spark/data/tweets.dat
    hadoop fs -mkdir /tmp/spark-in
    hadoop fs -chmod 777 /tmp/spark-in
    hadoop fs -copyFromLocal tweets.dat /tmp/spark-in/tweets.dat
    hadoop fs -chmod 777 /tmp/spark-in/tweets.dat

### Run local with:

    java -jar target/spark-task-0.0.1-SNAPSHOT.jar [--properties...] [args...]

You should provide the following properties:

 * --spark.app.class
 * --spark.app.jar

If your Spark app takes arguments you also need to pride them when running the Spark task.

Here is an example command line for the `Hashtag` sample Spark app mentioned above:

    java -jar target/spark-task-0.0.1-SNAPSHOT.jar --spark.app.class=Hashtags --spark.app.jar=/Users/trisberg/Downloads/spark-hashtags_2.10-0.1.0.jar hdfs://borneo:8020/tmp/spark-in hdfs://borneo:8020/tmp/spark-out

### Run in Spring Cloud Data Flow:

Register the module:

    dataflow:>module register --name spark --type task --coordinates com.springdeveloper.task:spark-task:0.0.1-SNAPSHOT

After the module is registered create the task:

    dataflow:>task create spark1 --definition "spark [--properties...] [--args=[args...]]"

You should provide the following properties:

 * --spark.app.class
 * --spark.app.jar

If your Spark app takes arguments you also need to provide them in the `--args` option

Here is an example _task create_ command for the `Hashtag` sample Spark app mentioned above:

    dataflow:>task create spark1 --definition "spark --spark.app.class=Hashtags --spark.app.jar=/Users/trisberg/Downloads/spark-hashtags_2.10-0.1.0.jar --args='hdfs://borneo:8020/tmp/spark-in,hdfs://borneo:8020/tmp/spark-out'"

