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
	}
}
