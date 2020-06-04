package algorithm;

import com.google.gson.Gson;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.*;

public class PSORunner<T> {
    private final List<Particle<T>> particles;
    private List<Set<Integer>> targetPaths;
    private Class testClass;
    private String testableMethod;
    private final ArrayList testData = new ArrayList<String>();

    public PSORunner(List<Particle<T>> particles, String testClass) {
        this.particles = particles;

        try {
            setTestClass(testClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setTestClass(String testClassName) throws ClassNotFoundException {
        this.testClass = Class.forName(testClassName);
        CFGGenerator generator = new CFGGenerator(testClass.getName());
        this.targetPaths = generator.getCFGAsArray();
    }

    public void initParticle(int max) {

    }

    public void findTestDataFor(String methodName, T instanceTestClass,  Class<?>... parameterTypes) {
        Method method = getMethodInClass(methodName, parameterTypes);
        runPSO(method, instanceTestClass);
    }

    private List<Set<Integer>> getTargetPaths() {
        return targetPaths;
    }

    private List<Object> getAllGetterValueMethodFrom(Class dataClass, Object instanceObject) {
        List<Object> objects = new ArrayList<>();
        Method[] methods = dataClass.getMethods();

        for(Method method: methods) {
            if (isGetter(method)) {
                try {
                    objects.add(method.invoke(instanceObject, null));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }


        return objects;
    }

    private static boolean isGetter(Method method){
        if(!method.getName().startsWith("get"))
            return false;
        if(method.getParameterTypes().length != 0)
            return false;
        if(void.class.equals(method.getReturnType()))
            return false;
        return !method.getName().equals("getClass");
    }

    public Method getMethodInClass(String methodName, Class<?>... parameterTypes) {
        Method method = null;
        try {
            method = testClass.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            System.out.println("Can not find method " + methodName);
            e.printStackTrace();
        }

        return method;
    }

    private void run(Method method, Object instanceObject, Object... params) {
        method.setAccessible(true);
        try {
            method.invoke(instanceObject, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Set getExecutionPath() {
        Method getTraceMethod;
        Set executionPath = new HashSet();
        try {
            getTraceMethod = testClass.getDeclaredMethod("getTrace", null);
            Object path = getTraceMethod.invoke(null, null);
            if (path instanceof Set) {
                executionPath = (Set) path;
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return executionPath;
    }

    private void newTrace() {
        try {
            testClass.getDeclaredMethod("newTrace", null).invoke(null, null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void runPSO(Method method, T instanceObject) {
        List<Set<Integer>> targetPaths = getTargetPaths();
        for (Set<Integer> targetPath : targetPaths) {
            if (targetPath.size() == 0)
                continue;
            Object gBest = new Object();
            double uploadLevelMax = 0.0;
            System.out.println("Target path: " + targetPath);
            for (Particle<T> particle : particles) {
                newTrace();
                Class dataClass = particle.getData().getClass();
                List<Object> listObjects = getAllGetterValueMethodFrom(dataClass, particle.getData());
                run(method, instanceObject, listObjects.toArray(new Object[listObjects.size()]));
//                getExecutionPath();
                double uploadLevel = particle.calculateUploadLevel(getExecutionPath(), targetPath);
                particle.setUploadLevel(uploadLevel);

                Gson gson = new Gson();
                String stringData = gson.toJson(particle.getData());
                System.out.println("Particle: " + stringData + " - upload level: " + uploadLevel);
                if (particle.getUploadLevel() > uploadLevelMax) {
                    uploadLevelMax = particle.getUploadLevel();
                    gBest = particle.getData();
                    if (uploadLevelMax == 1.0) {
                        break;
                    }
                }
                System.out.println("G BEST: " + gBest + " --- Upload MAX : " + uploadLevelMax);
            }
            testData.add(new Gson().toJson(gBest));
        }
        System.out.println("TEST DATA: " + testData);
    }
}
