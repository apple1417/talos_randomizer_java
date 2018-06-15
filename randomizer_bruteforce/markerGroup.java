import java.util.ArrayList;
import java.util.function.Supplier;

public class markerGroup {
    private Supplier<Boolean> test;
    private ArrayList<String> markers;
    private String world;

    public markerGroup(Supplier<Boolean> test, ArrayList<String> markers, String world) {
        this.test = test;
        this.markers = markers;
        this.world = world;
    }

    public markerGroup(Supplier<Boolean> test, ArrayList<String> markers) {
        this(test, markers, null);
    }

    public boolean isOpen() {return test.get();}
    public String getWorld() {return world;}
    public ArrayList<String> getMarkers() {return markers;}
    public int getSize() {return markers.size();}

    public markerGroup clone() {
        return new markerGroup(this.test, new ArrayList<String>(markers), this.world);
    }
}
