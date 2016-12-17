package git.madhukar.utils.JarEater;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

/**
 * @author makarapu
 * @since Dec 14, 2016
 *
 */
public class App {

    private static String MAIN_PROGRAM = null;

    // Driver code
    public static void main(String[] args) {

        String jarDir = JOptionPane.showInputDialog("Directory containing all runtime jars: ");

        try {

            Set<String> jarSet = Arrays.stream(new File(jarDir).listFiles()).map(x -> x.getAbsolutePath()).collect(
                    Collectors.toSet());

            MAIN_PROGRAM = JOptionPane.showInputDialog("Name of Main-Class: ");

            validateRunWithAllJars(jarSet);

            analyzeJarDependency(jarSet);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void analyzeJarDependency(Set<String> currentJarSet) {
        Set<String> allJars = new HashSet<>(currentJarSet);

        for (String jarPath : allJars) {

            currentJarSet.remove(jarPath);

            if (!runProgram(currentJarSet)) {
                currentJarSet.add(jarPath); // Jar is needed. Undo remove
                System.out.println("This jar is needed: " + jarPath);
            }
            else {

                new File(jarPath).delete(); // Ate a jar!!
                System.out.println(" Eating a jar at  " + jarPath);
            }
        }

    }

    /**
     * @param jarSet
     */
    private static void validateRunWithAllJars(Set<String> jarSet) {
        // Initial run with all jars
        boolean test = runProgram(jarSet);
        if (!test) {
            System.out.println("Program didnt run even with all jars included! Exiting ");
            System.exit(-1);
        }
    }

    private static boolean runProgram(Set<String> jarSet) {
        boolean isSuccess = false;

        String jarSetString = "\"" + jarSet.stream().collect(Collectors.joining(File.pathSeparator)) + "\"";

        try {
            ProcessBuilder ps = new ProcessBuilder("java", "-cp", jarSetString,
                    MAIN_PROGRAM);

            System.out.println("Command line: " + ps.command());

            ps.redirectErrorStream(true);
            Process pr = ps.start();

            try (BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));) {

                while ((in.readLine()) != null) {
                }

                int wait = pr.waitFor();
                int exitStatus = pr.exitValue();

                isSuccess = wait == 0 && exitStatus == 0;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

}


