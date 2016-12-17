package git.madhukar.utils.JarEater;
 

/**
 * @author makarapu
 * @since Dec 14, 2016
 *
 */
public class App {

	
	public static void main(String[] args) {

		try {

			JavaProcessRunner javaProcess = UserInputHandler.createCommandLineArgs();
 
			// Test initial run with all jars
			if (!javaProcess.run()) {
				throw new RuntimeException("Given java command didnt run with all jars included! ");
			}

			//Start cleaning unused jars
			UserInputHandler.getJarManager().cleanupJars(javaProcess);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	

}
