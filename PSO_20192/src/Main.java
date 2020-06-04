import algorithm.CFGGenerator;
import algorithm.DataStructment;
import algorithm.PSORunner;
import algorithm.Particle;
import com.google.gson.Gson;
import openjava.mop.Environment;
import openjava.mop.MetaInfo;
import openjava.mop.OJClass;
import org.omg.PortableServer.SERVANT_RETENTION_POLICY_ID;

import java.awt.dnd.DropTarget;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Array;
import java.util.*;
import java.util.logging.Logger;

public class Main {
    // class cần instrument code
    static String input = "Triangle";

    /**
     * hàm main
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException, ClassNotFoundException {

//		CFGGenerator generator = new CFGGenerator("Object");
//		generator.generateNecessaryFilesToUseToAnalysis();
////
//		Random random = new Random();
//		List<Particle<DataStructment>> particalInit = new LinkedList<>();
//
//		for (int i=0; i<1000; i++){
//
//			particalInit.add(new Particle<>(new DataStructment(random), 0));
//		}
//
//		PSORunner runner = new PSORunner(particalInit);
//		try {
//			runner.setTestClass(Class.forName(input));
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//
//		ArrayList testData = new ArrayList();
//
//		for (Object _targetPath: runner.getTargetPaths()) {
//			Set targetPath = (Set) _targetPath;
//			Object G_best = new Object();
//			double uploadLevelMax = 0.0;
//			for (Object _particle: runner.getParticles()) {
//				Particle<DataStructment> particle = (Particle<DataStructment>) _particle;
//				runner.newTrace();
//				Method instrumentMethod = runner.getMethodInClass("checkTriangle", int.class, int.class, int.class);
//				DataStructment data = particle.getData();
//				runner.run(instrumentMethod, null, data.getA(), data.getB(), data.getC());
//				System.out.println("TARGET PATH: " + targetPath);
//				System.out.println("Particle: " + particle.getData() + " - upload level: " +
//						particle.calculateUploadLevel(runner.getExecutionPath(), targetPath));
//				particle.setUploadLevel(particle.calculateUploadLevel(runner.getExecutionPath(), targetPath));
//				if(particle.getUploadLevel() > uploadLevelMax){
//					uploadLevelMax = particle.getUploadLevel();
//					G_best = particle.getData();
//				}
//			}
//
//			System.out.println("G_BEST --- " + targetPath + "---" + G_best);
//			testData.add(G_best);
//
//		}
//		System.out.println("TEST DATA: " + testData);
        // need to explicit Type of particle
        //init particle
//        Random random = new Random();
//        List<Particle<TriangleData>> particles = new ArrayList<>();
//
//        for (int i = 0; i < 1000; i++) {
//            Particle<TriangleData> particle = new Particle(new TriangleData(random), 0);
//
//            particles.add(particle);
//
//        }
//        PSORunner runner = new PSORunner(particles);
//        runner.setTestClass("Triangle");
//        Method method = runner.getMethodInClass("checkTriangle", int.class, int.class, int.class);
//
//        // pass instance object if invoking an instance method else instance method is null
//        runner.runPSO(method, null);

        Random random = new Random();
        List<Particle<FooData>> particles = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            Particle<FooData> particle = new Particle(new FooData(random), 0);
            particles.add(particle);
        }

        PSORunner runner = new PSORunner(particles, "Foo");
        runner.findTestDataFor("printSomething", new Foo(), int.class);

//        Random random = new Random();
//        List<Particle<ObjectData>> particles = new ArrayList<>();
//
//        for (int i = 0; i < 1000; i++) {
//            Particle<ObjectData> particle = new Particle(new ObjectData(), 0);
//            particles.add(particle);
//        }
//
//        PSORunner runner = new PSORunner(particles, "ObjectExample");
////        runner.setTestClass("ObjectExample");
////
////        Method method = runner.getMethodInClass("objectMethod", FooData.class);
////
////        runner.runPSO(method, new ObjectExample());
//
//        runner.findTestDataFor("objectMethod", new ObjectExample(), FooData.class);
    }
}
