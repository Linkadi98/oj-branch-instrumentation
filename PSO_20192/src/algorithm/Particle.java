package algorithm;

import sun.tools.java.ClassType;

import java.util.*;

public class Particle<T extends Comparable<T>> {
    /**
     * data có thể coi là position của particle
     */
    private T data;

    /**
     * uploadLevel cho chúng ta biết data của particle này sẽ bao phủ đến đâu target path
     * @discussion
     * Khi chúng ta sẽ dùng data của một particle để tìm ra execution path với 1 target path, thì particle nào có data bao phủ tốt nhất (nhiều
     * nhất số lượng node trong target path) sẽ được chon làm particle có data tốt nhất.
     */
    private double uploadLevel; // fitness value
    /**
     * globalData is the best data of all particles
     * @discussion
     *
     */
    private T globalData;

    public Particle(T data, double initUploadLevel) {
        this.data = data;
        this.uploadLevel = initUploadLevel;
    }

    public T getData() {
        return data;
    }

    public double calculateUploadLevel(Set executionPath, Set targetPath) {
        List<Integer> executionNodesInTargetPath = new ArrayList<>();
        Iterator<Integer> it = executionPath.iterator();
        System.out.println("Execution path: " + executionPath);
        while (it.hasNext()) {
            int node = it.next();
            if (targetPath.contains(node)) {
                executionNodesInTargetPath.add(node);
            }
        }
        return (double) executionNodesInTargetPath.size() / targetPath.size();
    }

    private void compareGlobalAndPersonal() {

    }

    public void setUploadLevel(double uploadLevel) {
        this.uploadLevel = uploadLevel;
    }

    public double getUploadLevel() {
        return uploadLevel;
    }

    public void findBestDataOfAllParticles(List<T> listGlobalData) {
        T max = listGlobalData.get(0);
        for (T element: listGlobalData) {
            if (element.compareTo(max) > 0) {
                max = element;
            }
        }

        this.globalData = max;
    }
}
