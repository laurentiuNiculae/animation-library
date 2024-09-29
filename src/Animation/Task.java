package Animation;

public interface Task {
    public void Draw(float dt);
    public boolean Finished();
    public void Reset();
}
