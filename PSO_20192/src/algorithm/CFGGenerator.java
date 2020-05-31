package algorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CFGGenerator {
    private String className;
    private String rootPath = System.getProperty("user.dir");
    private String projectDir = rootPath + "/PSO_20192/src";

    private final String OJ_POSTFIX = ".oj";
    private final String PATH_POSTFIX = ".path";

    private String OJ_FILE_NAME;
    private String PATH_FILE_NAME;

    private Map paths = new HashMap();

    public CFGGenerator(String className) {
        this.className = className;

        OJ_FILE_NAME = projectDir + "/" + className + OJ_POSTFIX;
        PATH_FILE_NAME = projectDir + "/" + className + PATH_POSTFIX;
    }

    public void generateNecessaryFilesToUseToAnalysis() {
        String[] sources = { OJ_FILE_NAME };
        openjava.ojc.Main.main(sources);
    }

    public List<Set> getCFGAsArray() {
        readPaths();
        return getBranchSetFromPaths();
    }

    private void readPaths() {
        try {
            String s;
            BufferedReader in = new BufferedReader(new FileReader(PATH_FILE_NAME));
            while ((s = in.readLine()) != null) {
                String r = s.substring(0, s.indexOf(":"));
                int tgt = Integer.parseInt(r);
                r = s.substring(s.indexOf(":") + 1);
                StringTokenizer tok = new StringTokenizer(r);
                ArrayList pathPoints = new ArrayList();
                while (tok.hasMoreTokens()) {
                    int n = Integer.parseInt(tok.nextToken());
                    pathPoints.add(n);

                    Collections.sort(pathPoints);
                }
                paths.put(tgt, pathPoints);
            }
            String[] list = new String[paths.size()];
            List arrayKey = new LinkedList();
            arrayKey.addAll(paths.keySet());
            List arrayPaths = new LinkedList();
            arrayPaths.addAll(paths.values());
            for (int i = 0; i < paths.size(); i++) {
                String temp = arrayPaths.get(i).toString().replace("[", "").replaceAll("]", "");
                if (temp.length() < 1) {
                    list[i] = " " + arrayKey.get(i).toString();
                } else {
                    list[i] = " " + arrayPaths.get(i).toString().replace("[", "").replaceAll("]", "") + ", "
                            + arrayKey.get(i).toString();
                }

            }

            for (int i = 0; i < arrayPaths.size() - 1; i++) {
                for (int j = i + 1; j < arrayPaths.size(); j++) {
                    if ((list[j]).contains(list[i] + ",")) {
                        paths.remove(Integer.parseInt(arrayKey.get(i).toString()));
                    } else if ((list[i]).contains(list[j] + ",")) {
                        paths.remove(Integer.parseInt(arrayKey.get(j).toString()));
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Wrong format file: " + PATH_FILE_NAME);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("IO error: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * biến đổi path từ dạng HashMap thành list
     *
     * @return
     */
    private List<Set> getBranchSetFromPaths() {
        List valuePaths = new LinkedList();
        valuePaths.addAll(paths.values());
        List keyPaths = new LinkedList();
        keyPaths.addAll(paths.keySet());

        List<Set> newSet = new LinkedList<>();
        for (int i = 0; i < valuePaths.size(); i++) {
            Set<Integer> temp = new HashSet<>();
            if (valuePaths.get(i).toString().equals("[]")) {
                temp.add((int) valuePaths.get(i));
            } else {
                List<Integer> integers = (List<Integer>) valuePaths.get(i);
                temp.addAll(integers);
                temp.add(Integer.parseInt(keyPaths.get(i).toString()));
            }
            newSet.add(temp);
        }
        return newSet;
    }
}
