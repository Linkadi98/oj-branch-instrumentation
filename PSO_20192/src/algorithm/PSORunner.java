package algorithm;

import sun.tools.java.ClassType;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

public class PSORunner<T> {
    List<Particle> particles;
    Set targetPath;
    Class testClass;

    public PSORunner(List<Particle> particles, Set targetPath) {
        this.particles = particles;
        this.targetPath = targetPath;
    }

    public void setTestClass(Class testClass) {
        this.testClass = testClass;
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

    public void runPSO(Set targetPath) {

    }
}
