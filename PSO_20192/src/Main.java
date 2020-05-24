

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class Main {
	// class cần instrument code
	static String input = "Triangle";
	// chứa các path
	static Map paths = new HashMap();

	static String pathsFile = "/Users/minhpham/Desktop/PSO_20192/src/Example/Triangle.path";
	static String ojFilePath = "/Users/minhpham/Desktop/PSO_20192/src/Example/Triangle.oj";

	/**
	 * hàm main
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		System.out.println(pathsFile);
		String[] srcfiles = { ojFilePath };
		// sử dụng thư viện openjava để phân tích code
		openjava.ojc.Main.main(srcfiles);
		Thread.sleep(5000);
		// đọc file path
		readPaths();
		// lấy ra các path có trong java code
		List<Set> listBranchTarget = getBranchSetFromPaths();
		System.out.println("All paths in class: " + input);
		System.out.println(listBranchTarget);

	}

	/**
	 * Đọc file .path
	 */
	public static void readPaths() {
		try {

			String s;
			BufferedReader in = new BufferedReader(new FileReader(pathsFile));
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
			System.err.println("Wrong format file: " + pathsFile);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("IO error: " + pathsFile);
			System.exit(1);
		}
	}

	/**
	 * biến đổi path từ dạng HashMap thành list
	 * 
	 * @return
	 */
	public static List<Set> getBranchSetFromPaths() {
		List valuePaths = new LinkedList();
		valuePaths.addAll(paths.values());
		List keyPaths = new LinkedList();
		keyPaths.addAll(paths.keySet());

		List<Set> newSet = new LinkedList<>();
		for (int i = 0; i < valuePaths.size(); i++) {
			Set<String> temp = new HashSet<>();
			if (valuePaths.get(i).toString().equals("[]")) {
				temp.add(keyPaths.get(i) + "");
			} else {
				temp.add((valuePaths.get(i) + ", " + keyPaths.get(i)).replace("[", "").replaceAll("]", ""));
			}
			newSet.add(temp);
		}
		return newSet;
	}
}
