package algorithm;

import sun.tools.java.ClassType;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class PSORunner {
    private List<Particle> particles;
    private List<Set> targetPaths;
    private Class testClass;
    private String testableMethod;
    private CFGGenerator generator;

    public PSORunner(List<Particle> particles, List<Set> targetPaths) {
        this.particles = particles;
        this.targetPaths = targetPaths;
    }

    public void setTestClass(Class testClass) {
        this.testClass = testClass;
        this.generator = new CFGGenerator(testClass.getName());
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

    public void run(Method method, Object instanceObject, Object... params) {
        method.setAccessible(true);
        try {
            method.invoke(instanceObject, params);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Set getExecutionPath() {
        Method getTraceMethod = null;
        Set executionPath = new HashSet();
        try {
            getTraceMethod = testClass.getDeclaredMethod("getTrace", null);
            Object path = getTraceMethod.invoke(null, null);
            if (path instanceof Set) {
                executionPath = (Set) path;
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return executionPath;
    }

    private void newTrace() {
        try {
            testClass.getDeclaredMethod("newTrace", null).invoke(null, null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void runPSO(Set targetPath) {

    }


}
