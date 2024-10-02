package AnimationParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import Animation.Task;
import Helper.Result;

public class AnimationScriptParser {
    String fileContent;
    String filePath;

    public AnimationScriptParser(String filePath) {
        try {
            this.filePath = filePath;
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

    public Result<Task> ParseFile() {
        var task = new Parser().ParseTask(filePath, fileContent);

        return task;
    }

    public void PrintFileContent() {
        System.out.println("fileContent: " + fileContent);
    }
}