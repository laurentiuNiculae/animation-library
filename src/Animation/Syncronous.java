package Animation;

import java.util.ArrayList;
import java.util.List;

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
    public void Draw(float dt) {
        allTasksDone = true;
        for (var task : tasks) {
            task.Draw(dt);
            allTasksDone = allTasksDone && task.Finished();
        }
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
