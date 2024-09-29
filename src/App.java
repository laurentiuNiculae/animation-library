import Animation.AnimationScriptParser;
import Examples.*;

public class App {
    public static void main2(String[] args) {
        var scriptParse = new AnimationScriptParser("/home/laur/dev/personal/java-learn/bunica/src/script");

        scriptParse.PrintFileContent();
    }

    public static void main(String[] args) {
        new Example4().RunExample();
    }
}
