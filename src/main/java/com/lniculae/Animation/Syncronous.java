package com.lniculae.Animation;

import java.util.ArrayList;
import java.util.List;

import com.lniculae.Animation.Context.AnimationContext;

public class Syncronous implements Task {
    AnimationContext ctx;
    List<Task> tasks;
    boolean allTasksDone;

    public Syncronous(AnimationContext ctx) {
        this();
        this.ctx = ctx;
    }

    public Syncronous() {
        tasks = new ArrayList<>();
        allTasksDone = false;
    }

    public Syncronous addTask(Task task) {
        task.SetAnimationCtx(ctx);
        tasks.add(task);

        return this;
    }

    @Override
    public float Draw(float dt) {
        allTasksDone = true;
        float maxDrawTime = 0;
        for (Task task : tasks) {
            float drawTime = task.Draw(dt);
            maxDrawTime = Math.max(maxDrawTime, drawTime);

            allTasksDone = allTasksDone && task.Finished();
        }

        return maxDrawTime;
    }

    @Override
    public boolean Finished() {
        return allTasksDone;
    }

    @Override
    public void Reset() {
        allTasksDone = false;
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
