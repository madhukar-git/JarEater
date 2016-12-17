package git.madhukar.utils.JarEater;

import java.util.Scanner;

public class UserInputHandler {

	// ----------------------------- Helper Methods
	// ---------------------------------------//
	static JavaProcessRunner createCommandLineArgs() {

		String input = readConsoleInput("Enter the Java commandline argument \n"
				+ "e.g. java -DsysProp=V1 -cp a.jar;b.jar;c.jar  foo.bar.Main  arg1 arg2 \n" + "$ ");

		return new JavaProcessRunner(input);
	}

	static JarManager getJarManager() {
		String input = readConsoleInput("\n\nEnter the directory containing the jars which needs cleanup: \n" + "$ ");

		return new JarManager(input);

	}

	private static String readConsoleInput(String prompt) {
		System.out.print(prompt);
		return new Scanner(System.in).nextLine();
	}

}
