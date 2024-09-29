package Animation;

import java.util.ArrayList;
import java.util.List;

public class Secvential implements Task {
    List<Task> tasks;
    int currentTaskIndex = 0;
    
    public Secvential() {
        tasks = new ArrayList<>();
    }

    public Secvential addTask(Task task) {
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
        
        if (task.Finished()) {
            currentTaskIndex++;
            return;
        }

        task.Draw(dt);
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
    };
}
