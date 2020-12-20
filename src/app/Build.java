package app;

/*
    Gradle builds cannot access classes which extends
 */
public class Build {
    public static void main(String[] args) {
        Main.main(args);
    }
}
