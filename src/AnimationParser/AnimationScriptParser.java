package AnimationParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import Animation.Task;

import java.util.List;

public class AnimationScriptParser {
    String fileContent;

    public AnimationScriptParser(String filePath) {
        try {
            var file = new File(filePath);
            var scanner = new Scanner(file);

            this.fileContent = scanner.useDelimiter("\\Z").next();
            scanner.close();
        } catch(FileNotFoundException e) {
            System.out.println("file '" + filePath + "' not found: " + e.toString());
        } catch (Exception e ) {
            System.out.println("some error happened: " + e.toString());
        }
    }

    public Task ParseFile() {
        var tokens = GetTokens();

        var w = new Parser().ParseSyncBlock(tokens);
        return w.task;
    }

    public void PrintFileContent() {
        System.out.println("fileContent: " + fileContent);
    }

    public List<Token> GetTokens() {
        return new Tokenizer().GetTokens(fileContent);
    }
}