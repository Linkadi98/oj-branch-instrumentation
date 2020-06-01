package algorithm;

import java.util.*;

public class Particle<T> {
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
        List<Integer> targetPathArray = new ArrayList<>(targetPath);
        List<Integer> executionPathArray = new ArrayList<>(executionPath);
        List<Integer> executionNodesInTargetPath = new ArrayList<>();
//        Iterator<Integer> it = executionPath.iterator();
        System.out.println("Execution path: " + executionPath);
        int targetPathSize = targetPath.size();
        for (int i = 0; i < targetPathSize; i++) {
            if (targetPathArray.get(i).equals(executionPathArray.get(i))) {
                executionNodesInTargetPath.add(executionPathArray.get(i));
            } else {
                break;
            }
        }

//        while (it.hasNext()) {
//            int node = it.next();
//            if (targetPath.contains(node)) {
//                executionNodesInTargetPath.add(node);
//            }
//        }
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
}
