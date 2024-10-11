package com.lniculae.AnimationParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.lniculae.Animation.Task;
import com.lniculae.Helper.Result;

public class AnimationScriptParser {
    Parser parser;

    public AnimationScriptParser() {
        parser = new Parser();
    }

    public Result<Task> ParseFile(String filePath) {
        try {
            var file = new File(filePath);
            var scanner = new Scanner(file);

            String fileContent = scanner.useDelimiter("\\Z").next();
            scanner.close();
            
            var task = parser.ParseTask(filePath, fileContent);

            return task;
        } catch(FileNotFoundException e) {
            return new Result<>(new Error(
                "file '" + filePath + "' not found: " + e.toString()
            ));
        } catch (Exception e ) {
            return new Result<>(new Error(
                "some error happened: " + e.toString()
            ));
        }
    }

    public Result<Task> ParseString(String scriptContent) {
        var task = parser.ParseTask("script", scriptContent);

        return task;
    }
}