package git.madhukar.utils.JarEater;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class JarManager {

	private List<File> jarList;

	public JarManager(String jarDir) {

		File[] jarArr = new File(jarDir).listFiles();

		if (jarArr == null || jarArr.length == 0) {
			System.err.println("Not a valid directory.");
			System.exit(1);
		}

		this.jarList = Arrays.asList(jarArr);
	}

	public void cleanupJars(JavaProcessRunner javaProcess) {

		for (File origFile : jarList) {

			String origFilePath = origFile.getAbsolutePath();

			File newName = new File(origFile.getAbsolutePath() + ".bak");

			if (!origFile.renameTo(newName)) {
				throw new RuntimeException("Unable rename jar file:" + origFilePath);
			}

			if (javaProcess.run()) {
				System.out.println(" Eating a jar at  " + origFilePath);

			} else {

				if (!newName.renameTo(origFile)) {
					throw new RuntimeException("Unable restore jar file:" + origFilePath);
				}
				System.out.println("This jar is needed: " + origFilePath);

			}
		}

	}
}
