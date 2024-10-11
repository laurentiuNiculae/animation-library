package com.lniculae.AnimationParser;

import com.lniculae.Animation.Task;

public class ParsedTask {
    public Task task;
    public int parsedTokens;

    public ParsedTask(Task task, int parsedTokens) {
        this.task = task;
        this.parsedTokens = parsedTokens;
    }
}
