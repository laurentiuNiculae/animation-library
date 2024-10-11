package com.lniculae.Animation;

import java.util.ArrayList;
import java.util.List;

import com.lniculae.Animation.Context.AnimationContext;

public class Secvential implements Task {
    AnimationContext ctx;
    List<Task> tasks;
    int currentTaskIndex = 0;
    
    public Secvential(AnimationContext ctx) {
        this();
        this.ctx = ctx;
    }
    
    public Secvential() {
        tasks = new ArrayList<>();
    }

    public Secvential addTask(Task task) {
        task.SetAnimationCtx(ctx);
        tasks.add(task);

        return this;
    }

    @Override
    public float Draw(float dt) {
        if (tasks.size() == 0) {
            return 0;
        }

        // We'll keep drawing the last frame if the Draw function is called after finish
        if (currentTaskIndex >= tasks.size()) {
            tasks.get(currentTaskIndex-1).Draw(dt);
            return 0;
        }

        var task = tasks.get(currentTaskIndex);
        
        float drawTime = task.Draw(dt);
        float totalDrawTime = drawTime;

        if (task.Finished()) {
            currentTaskIndex++;
        }

        while ((dt - drawTime > 1e06)) {
            dt = dt - drawTime;

            currentTaskIndex++;

            if (currentTaskIndex >= tasks.size()) {
                break;
            }

            task = tasks.get(currentTaskIndex);
            drawTime = task.Draw(dt);
            totalDrawTime += drawTime;
        }

        return totalDrawTime;
    }

    @Override
    public boolean Finished() {
        return currentTaskIndex >= tasks.size();
    }

    @Override
    public void Reset() {
        currentTaskIndex = 0;
        for (var task : tasks) {
            task.Reset();
        }
    }

    @Override
    public void SetAnimationCtx(AnimationContext ctx) {
        this.ctx = ctx;
        tasks.forEach(t -> t.SetAnimationCtx(ctx));
    };
}
