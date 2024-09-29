package Examples;
import com.raylib.java.core.Color;

import Graph.Node;

public class DrawingUtils {
    static Color GetNodeColor(Node node) {
        var value = (int)(node.Value/1.25) + 50;
        int r = value;
        int g = value;
        int b = value;

        return new Color(r, g, b, 255); 
    }
}
