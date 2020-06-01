package algorithm;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.util.*;

public class PSORunner<T> {
    private List<Particle<T>> particles;
    private List<Set<Integer>> targetPaths;
    private Class testClass;
    private String testableMethod;
    private CFGGenerator generator;

    public PSORunner(List<Particle<T>> particles) {
        this.particles = particles;
    }

    public void setTestClass(String testClassName) throws ClassNotFoundException {
        this.testClass = Class.forName(testClassName);
        this.generator = new CFGGenerator(testClass.getName());
        this.targetPaths = generator.getCFGAsArray();
    }

    public List<Particle<T>> getParticles() {
        return particles;
    }

    public List<Set<Integer>> getTargetPaths() {
        return targetPaths;
    }

    public Class getTestClass() {
        return testClass;
    }

    public List<Object> getAllGetterValueMethodFrom(Class dataClass, Object instanceObject) {
        List<Object> objects = new ArrayList<>();
        Method[] methods = dataClass.getMethods();

        for(Method method: methods) {
            if (isGetter(method)) {
                try {
                    objects.add(method.invoke(instanceObject, null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }


        return objects;
    }

    public static boolean isGetter(Method method){
        if(!method.getName().startsWith("get"))
            return false;
        if(method.getParameterTypes().length != 0)
            return false;
        if(void.class.equals(method.getReturnType()))
            return false;
        if (method.getName().equals("getClass")) {
            return false;
        }
        return true;
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

    public Set getExecutionPath() {
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

    public void newTrace() {
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

    public void renderData(List <Particle> particles) {

    }

    public void runPSO(Method method, Object instanceObject, Object... params) {
        newTrace();
        try {
            method.invoke(instanceObject, params);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
    }
}
