package git.madhukar.utils.JarEater;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * @author makarapu
 * @since Dec 14, 2016
 *
 */
public class App {

	private static List<String> commandLineOfTargetApplication;

	public static void main(String[] args) {

		try {

			Set<File> jarSet = createCommandLineArgs();

			validateRunWithAllJars();

			analyzeJarDependency(jarSet);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static Set<File> createCommandLineArgs() {
		File jarDir = getParentJarDir();

		Set<File> jarSet = Arrays.stream(jarDir.listFiles())
				.collect(Collectors.toSet());

		String MAIN_PROGRAM = JOptionPane.showInputDialog("Commandline argument: e.g. java -DsysProp=V1 -cp a.jar;b.jar;c.jar  foo.bar.Main  arg1 arg2");
		
		String[] cmdArr = MAIN_PROGRAM.split("\\s+");
		commandLineOfTargetApplication=(Arrays.asList(cmdArr));

		System.out.println("Target Command line arguments: " + commandLineOfTargetApplication);

		return jarSet;

	}

	private static File getParentJarDir() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("/my/shared"));
		chooser.setDialogTitle("Directory containing jars which needs cleaning :");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
			System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
		} else {
			System.out.println("No Selection ");
			System.exit(-1);
		}

		File jarDir = chooser.getSelectedFile();
		return jarDir;
	}

	private static void analyzeJarDependency(Set<File> jarFileSet) {

		for (File origFile : jarFileSet) {

			String origFilePath = origFile.getAbsolutePath();

			File newName = new File(origFile.getAbsolutePath() + ".bak");  
			
			if (!origFile.renameTo(newName)) {
				throw new RuntimeException("Unable rename jar file:" + origFilePath);
			}

			if (runTargetApplication()) {
				System.out.println(" Eating a jar at  " + origFilePath);

			} else {

				if (!newName.renameTo(origFile)) {
					throw new RuntimeException("Unable restore jar file:" + origFilePath);
				}
				System.out.println("This jar is needed: " + origFilePath);

			}
		}

	}

	/**
	 * @param jarSet
	 */
	private static void validateRunWithAllJars() {
		// Initial run with all jars
		if (!runTargetApplication()) {
			throw new RuntimeException("Program didnt run with all jars included! ");
		}
	}

	private static boolean runTargetApplication() {
		boolean isSuccess = false;

		try {

			ProcessBuilder ps = new ProcessBuilder(commandLineOfTargetApplication);

			ps.redirectErrorStream(true);
			Process pr = ps.start();

			try (BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));) {

				String line;
				while ((line=in.readLine()) != null) {
					System.out.println(line);
				}

				int wait = pr.waitFor();
				int exitStatus = pr.exitValue();

				isSuccess = wait == 0 && exitStatus == 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isSuccess;
	}

}
