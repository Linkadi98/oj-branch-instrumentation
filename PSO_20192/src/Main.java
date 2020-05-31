import algorithm.CFGGenerator;
import algorithm.PSORunner;
import algorithm.Particle;
import openjava.mop.Environment;
import openjava.mop.MetaInfo;
import openjava.mop.OJClass;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;

public class Main {
	// class cần instrument code
	static String input = "Foo";
	// chứa các path
	static Map paths = new HashMap();

	static String pathsFile = "/Users/minhpham/Desktop/oj-branch-instrumentation/PSO_20192/src/Foo.path";
	static String ojFilePath = "/Users/minhpham/Desktop/oj-branch-instrumentation/PSO_20192/src/Foo.oj";

	/**
	 * hàm main
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {

		CFGGenerator generator = new CFGGenerator("Foo");

		generator.generateNecessaryFilesToUseToAnalysis();

		Thread.sleep(3);

		System.out.println(generator.getCFGAsArray());

		Random random = new Random();

		List<Particle> particleList = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			Particle particle = new Particle(random.nextInt(20), 0,0,0);
			particleList.add(particle);
		}

		int globalData = random.nextInt(20);

		for (Set<Integer> targetPath: generator.getCFGAsArray()) {
			System.out.println("- Target path: " + targetPath);
			for (Particle particle: particleList) {
//				Foo foo = new Foo();
//				Foo.newTrace();
//				foo.printSomething((int) particle.getData());
//
//				particle.calculateUploadLevel(, targetPath);
//				Foo.newTrace();
////				foo.printSomething(globalData);
//				particle.calculateUploadLevel(Foo.getTrace(), targetPath);

				PSORunner runner = new PSORunner(particleList, targetPath);

				try {
					Class fooClass = Class.forName("Foo");
					runner.setTestClass(fooClass);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

				Method testMethod = runner.getMethodInClass("printSomething", int.class);
				runner.run(testMethod, new Foo(), particle.getData());
			}
		}

		for (Particle particle: particleList) {
			System.out.println("Particle has data: " + particle.getData() + " - upload level: " + particle.getUploadLevel());
		}
	}
}
