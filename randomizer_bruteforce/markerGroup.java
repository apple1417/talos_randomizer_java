package randomizer_bruteforce;

import java.util.ArrayList;
import java.util.function.Supplier;
import randomizer_bruteforce.Enums.World;

public class MarkerGroup {
    private Supplier<Boolean> test;
    private ArrayList<String> markers;
    private World groupWorld;

    public MarkerGroup(Supplier<Boolean> test, ArrayList<String> markers, World groupWorld) {
        this.test = test;
        this.markers = markers;
        this.groupWorld = groupWorld;
    }

    public MarkerGroup(Supplier<Boolean> test, ArrayList<String> markers) {
        this.test = test;
        this.markers = markers;
    }

    public boolean isOpen() {return test.get();}
    public World getWorld() {return groupWorld;}
    public ArrayList<String> getMarkers() {return markers;}
    public int getSize() {return markers.size();}

    public MarkerGroup clone() {
        return new MarkerGroup(this.test, new ArrayList<String>(markers), this.groupWorld);
    }
}
