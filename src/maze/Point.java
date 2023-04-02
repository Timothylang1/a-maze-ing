package maze;

public class Point {
    int x;
    int y;

    // Unlike Kilt Graphics where points can have a decimal x and y,
    // this class will only have integer x and y
    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
