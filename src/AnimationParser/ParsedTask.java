package AnimationParser;

import Animation.Task;

public class ParsedTask {
    public Task task;
    public int parsedTokens;

    public ParsedTask(Task task, int parsedTokens) {
        this.task = task;
        this.parsedTokens = parsedTokens;
    }
}
