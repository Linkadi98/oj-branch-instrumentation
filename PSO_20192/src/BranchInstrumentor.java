import openjava.mop.*;
import openjava.ptree.*;

/**
 * Add trace vào file .java
 */
public class BranchInstrumentor extends OJClass {

	public static int branchCounter = 0;

	String className;

	java.io.PrintStream signatureFile;

	java.io.PrintStream targetFile;

	java.io.PrintStream pathFile;

	boolean isFirstTarget = true;

	static String traceInterfaceType = "java.util.Set";

	static String traceConcreteType = "java.util.HashSet";

	public String getClassName() {
		return className;
	}

	/**
	 * Inserts import statements (java.util.*) vào đầu file .java
	 */
	public void insertImports() {
		try {
			ParseTreeObject pt = getSourceCode();
			while (!(pt instanceof CompilationUnit)) {
				pt = pt.getParent();
			}
			CompilationUnit cu = (CompilationUnit) pt;

			String[] oldImports = cu.getDeclaredImports();
			String[] newImports = new String[oldImports.length + 2];
			System.arraycopy(oldImports, 0, newImports, 0, oldImports.length);
			newImports[oldImports.length] = "java.util.*;";
			newImports[oldImports.length + 1] = "demo.*;";
			cu.setDeclaredImports(newImports);
		} catch (CannotAlterException e) {
			System.err.println(e);
			System.exit(1);
		}
	}

	/**
	 * Adds field trace vào class đang được instrument
	 */
	public void insertTraceField() {
		try {
			OJModifier mod = OJModifier.forModifier(OJModifier.STATIC);
			FieldDeclaration fd = new FieldDeclaration(
					new ModifierList(ModifierList.STATIC),
					TypeName.forOJClass(OJClass.forName(traceInterfaceType)), "trace",
					new AllocationExpression(OJClass.forName(traceConcreteType),
							new ExpressionList()));
			OJField f = new OJField(getEnvironment(), this, fd);
			addField(f);
		} catch (OJClassNotFoundException e) {
			System.err.println(e);
			System.exit(1);
		} catch (CannotAlterException e) {
			System.err.println(e);
			System.exit(1);
		}
	}

	/**
	 * Adds method getTrace vào class đang được instrument
	 */
	public void insertTraceAccessor() {
		try {
			StatementList body = makeStatementList("return trace;");
			OJModifier mod = OJModifier.forModifier(OJModifier.PUBLIC);
			mod = mod.add(OJModifier.STATIC);
			OJMethod m = new OJMethod(this, mod, OJClass.forName(traceInterfaceType),
					"getTrace", new OJClass[0], new OJClass[0], body);
			addMethod(m);
		} catch (OJClassNotFoundException e) {
			System.err.println(e);
			System.exit(1);
		} catch (CannotAlterException e) {
			System.err.println(e);
			System.exit(1);
		} catch (MOPException e) {
			System.err.println(e);
			System.exit(1);
		}
	}

	/**
	 * Add method newTrace vào class đang được instrument
	 */
	public void insertTraceCreator() {
		try {
			StatementList body = makeStatementList("trace = new " + traceConcreteType + "();");
			OJModifier mod = OJModifier.forModifier(OJModifier.PUBLIC);
			mod = mod.add(OJModifier.STATIC);
			OJMethod m = new OJMethod(this, mod, OJClass.forName("void"), "newTrace",
					new OJClass[0], new OJClass[0], body);
			addMethod(m);
		} catch (OJClassNotFoundException e) {
			System.err.println(e);
			System.exit(1);
		} catch (CannotAlterException e) {
			System.err.println(e);
			System.exit(1);
		} catch (MOPException e) {
			System.err.println(e);
			System.exit(1);
		}
	}

	/**
	 * Tạo statement add branch id (Integer) vào trace.
	 */
	public Statement makeTraceStatement() {
		Statement traceBranch = null;
		try {
			branchCounter++;
			traceBranch = makeStatement("trace.add(new java.lang.Integer(" + branchCounter + "));");
			printPath(branchCounter);
		} catch (MOPException e) {
			System.err.println(e);
			System.exit(1);
		}
		return traceBranch;
	}

	/**
	 * Thăm cây phân tích của method
	 */
	public void insertBranchTraces(StatementList block) {
		try {
			block.accept(new BranchTraceVisitor(this));
		} catch (ParseTreeException e) {
			System.err.println(e);
			System.exit(1);
		}
	}

	/**
	 * In các control deps dẫn đến 1 target vào path file (.path)
	 */
	private void printPath(int tgt) {
		pathFile.print(tgt + ":");
		java.util.Iterator controlDep = BranchTraceVisitor.getControlDependences().iterator();
		while (controlDep.hasNext()) {
			Integer br = (Integer) controlDep.next();
			pathFile.print(" " + br);
		}
		pathFile.println();
	}

	/**
	 * Return fullname của method hoặc constructor.
	 */
	private String getSignature(OJMember mem) {
		String clName = mem.getDeclaringClass().toString();
		String signature = clName;
		signature += "." + mem.signature().toString();
		signature = signature.replaceAll("\\$", "\\\\\\$");
		clName = clName.replaceAll("\\$", "\\\\\\$");
		signature = signature.replaceFirst("\\.constructor\\s+", "." + clName);
		signature = signature.replaceFirst("\\.method\\s+", ".");
		signature = signature.replaceAll("class\\s+", "");
		signature = signature.replaceAll("\\\\\\$", "\\$");

		return signature;
	}

	/**
	 * In tất cả method/constructor name vào signature file (.sign)
	 */
	private void printSignature(OJMember mem) {
		if (mem.getModifiers().isPrivate() || mem.getModifiers().isProtected()) {
			return;
		}
		signatureFile.println(getSignature(mem));
	}

	/**
	 * Opens sigature, target và path files
	 */
	private void openOutputFiles() {
		try {
			signatureFile = new java.io.PrintStream(
					new java.io.FileOutputStream(Common.relativePath + className + ".sign"));
			pathFile = new java.io.PrintStream(new java.io.FileOutputStream(Common.relativePath + className + ".path"));

		} catch (java.io.FileNotFoundException e) {
			System.err.println("File not found: " + e);
			System.exit(1);
		}
	}

	/**
	 * Ghi vào sigature, target và path files( add instrumentation)
	 */
	public void translateDefinition() throws MOPException {
		if (className == null) {
			className = getSimpleName();
		}
		openOutputFiles();
		insertTraceField();
		OJConstructor[] constructors = getDeclaredConstructors();
		for (int i = 0; i < constructors.length; ++i) {
			printSignature(constructors[i]);
			insertBranchTraces(constructors[i].getBody());
		}
		OJMethod[] methods = getDeclaredMethods();
		for (int i = 0; i < methods.length; ++i) {
			printSignature(methods[i]);
			insertBranchTraces(methods[i].getBody());
		}
		insertTraceCreator();
		insertTraceAccessor();
	}

	/**
	 * Generates a metaobject from source code
	 * 
	 * @param oj_param0
	 * @param oj_param1
	 * @param oj_param2
	 */
	public BranchInstrumentor(Environment oj_param0, OJClass oj_param1,
                              ClassDeclaration oj_param2) {
		super(oj_param0, oj_param1, oj_param2);
	}

	/**
	 * Generates a metaobject from byte code
	 * 
	 * @param oj_param0
	 * @param oj_param1
	 */

	public BranchInstrumentor(Class oj_param0, MetaInfo oj_param1) {
		super(oj_param0, oj_param1);
	}

}
