package Animation;

import java.util.ArrayList;
import java.util.List;

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
    public void Draw(float dt) {
        if (tasks.size() == 0) {
            return;
        }

        // We'll keep drawing the last frame if the Draw function is called after finish
        if (currentTaskIndex >= tasks.size()) {
            tasks.get(currentTaskIndex-1).Draw(dt);
            return;
        }

        var task = tasks.get(currentTaskIndex);

        task.Draw(dt);

        if (task.Finished()) {
            currentTaskIndex++;
        }
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
