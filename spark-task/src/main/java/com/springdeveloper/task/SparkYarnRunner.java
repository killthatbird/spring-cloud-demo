package com.springdeveloper.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.spark.SparkConf;
import org.apache.spark.deploy.yarn.Client;
import org.apache.spark.deploy.yarn.ClientArguments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.util.StringUtils;

/**
 * @author Thomas Risberg
 */
public class SparkYarnRunner implements CommandLineRunner {

	@Autowired
	private Configuration hadoopConfiguration;

	@Value("${spark.assembly.jar:hdfs:///app/spark/spark-assembly-1.5.0-hadoop2.6.0.jar}")
	private String sparkAssemblyJar;

	@Value("${spring.application.name:spark-task}")
	private String appName;

	@Value("${spark.app.class}")
	private String appClass;

	@Value("${spark.app.jar}")
	private String appJar;

	private String resourceFiles;

	private String resourceArchives;

	@Value("${spark.executor.memory:1024M}")
	private String executorMemory;

	@Value("${spark.num.executors:2}")
	private int numExecutors;

	@Value("${args:#{null}}")
	private String[] sparkAppArgs;

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Running Spark App using:");
		System.out.println("jar:   " + appJar);
		System.out.println("class: " + appClass);

		if (sparkAppArgs == null) {
			List<String> argList = new ArrayList<>();
			for (String arg : args) {
				if (!arg.startsWith("-")) {
					argList.add(arg);
				}
			}
			sparkAppArgs = argList.toArray(new String[argList.size()]);
		}
		System.out.println("args:  " + Arrays.asList(sparkAppArgs));

		SparkConf sparkConf = new SparkConf();
		sparkConf.set("spark.yarn.jar", sparkAssemblyJar);

		List<String> submitArgs = new ArrayList<String>();
		if (StringUtils.hasText(appName)) {
			submitArgs.add("--name");
			submitArgs.add(appName);
		}
		submitArgs.add("--jar");
		submitArgs.add(appJar);
		submitArgs.add("--class");
		submitArgs.add(appClass);
		if (StringUtils.hasText(resourceFiles)) {
			submitArgs.add("--files");
			submitArgs.add(resourceFiles);
		}
		if (StringUtils.hasText(resourceArchives)) {
			submitArgs.add("--archives");
			submitArgs.add(resourceArchives);
		}
		submitArgs.add("--executor-memory");
		submitArgs.add(executorMemory);
		submitArgs.add("--num-executors");
		submitArgs.add("" + numExecutors);
		for (String arg : sparkAppArgs) {
			submitArgs.add("--arg");
			submitArgs.add(arg);
		}
		System.out.println("Submit args: " + Arrays.asList(submitArgs));
		ClientArguments clientArguments =
				new ClientArguments(submitArgs.toArray(new String[submitArgs.size()]), sparkConf);
		Client client = new Client(clientArguments, hadoopConfiguration, sparkConf);
		System.setProperty("SPARK_YARN_MODE", "true");
		client.run();

	}

}
