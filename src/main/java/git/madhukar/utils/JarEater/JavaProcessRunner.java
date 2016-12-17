package git.madhukar.utils.JarEater;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class JavaProcessRunner {

	private List<String> javaCommand;

	public JavaProcessRunner(String input) {
		javaCommand = (Arrays.asList(input.split("\\s+")));

		System.out.println("Command line arguments in token format are: " + javaCommand);

	}

	public boolean run() {
		boolean isSuccess = false;
			BufferedReader in = null;

		try {

			ProcessBuilder ps = new ProcessBuilder(javaCommand);

			ps.redirectErrorStream(true);
			Process pr = ps.start();


				in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
				String line;
				while ((line = in.readLine()) != null) {
					System.out.println(line);
				}

				int wait = pr.waitFor();
				int exitStatus = pr.exitValue();

				isSuccess = wait == 0 && exitStatus == 0;
				in.close();

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isSuccess;
	}

}
