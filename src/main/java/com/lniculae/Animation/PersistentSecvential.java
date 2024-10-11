package com.lniculae.Animation;

import java.util.ArrayList;
import java.util.List;

import com.lniculae.Animation.Context.AnimationContext;

public class PersistentSecvential implements Task {
    AnimationContext ctx;
    List<Task> tasks;
    int currentTaskIndex = 0;
    
    public PersistentSecvential(AnimationContext ctx) {
        this();
        this.ctx = ctx;
    }

    public PersistentSecvential() {
        tasks = new ArrayList<>();
    }

    public PersistentSecvential addTask(Task task) {
        task.SetAnimationCtx(ctx);
        tasks.add(task);

        return this;
    }

    @Override
    public float Draw(float dt) {
        if (tasks.size() == 0) {
            return 0;
        }

        for (int i = 0; i < Math.min(currentTaskIndex, tasks.size()); i++) {
            tasks.get(i).Draw(dt);
        }

        if (currentTaskIndex >= tasks.size()) {
            return 0;
        }

        var task = tasks.get(currentTaskIndex);
        
        float drawTime = task.Draw(dt);
        float totalDrawTime = drawTime;

        while ((dt - drawTime > 1e-06)) {
            dt = dt - drawTime;

            currentTaskIndex++;

            if (currentTaskIndex >= tasks.size()) {
                break;
            }

            task = tasks.get(currentTaskIndex);
            drawTime = task.Draw(dt);
            totalDrawTime += drawTime;
        }

        if (task.Finished()) {
            currentTaskIndex++;
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
