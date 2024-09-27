package AnimationScriptParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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

    public void PrintFileContent() {
        System.out.println("fileContent: " + fileContent);
    }
}

/* First tokenize the text

    - Identifier
    - OpenCurlyParen
    - CloseCurlyParen
    - OpenParen
    - CloseParen
    - Comma
    - Equals
    - Number

    Parsing:
    - Arguments = OpenParen
    - 
*/