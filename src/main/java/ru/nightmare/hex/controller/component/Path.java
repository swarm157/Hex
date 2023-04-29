package ru.nightmare.hex.controller.component;

import ru.nightmare.hex.model.Point;

import java.util.List;

import static ru.nightmare.hex.controller.component.Util.isOutOfRange;

public class Path {
    List<Point> segments;

    int state;
    boolean reversed;

    public Path(List<Point> segments, boolean reversed) {
        this.segments = segments;
        this.reversed = reversed;
        if (reversed) {
            state = segments.size()-1;
        } else {
            state = 0;
        }
    }

    public Path(List<Point> segments) {
        this.segments = segments;
        reversed = false;
        state = 0;
    }

    public List<Point> getSegments() {
        return segments;
    }

    public void setSegments(List<Point> segments) {
        this.segments = segments;
    }

    public boolean isReversed() {
        return reversed;
    }

    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }
    public Point toBeginning() {
        if (reversed) {
            state = segments.size()-1;
        } else {
            state = 0;
        }
        if (isOutOfRange(state, 0, segments.size()-1))
            return null;
        else
            return segments.get(state);
    }
    public Point next() {
        if (reversed) {
            state -= 1;
        } else {
            state += 1;
        }
        if (isOutOfRange(state, 0, segments.size()-1))
            return null;
        else
            return segments.get(state);
    }
    public Point toEnd() {
        if (reversed) {
            state = 0;
        } else {
            state = segments.size()-1;
        }
        if (isOutOfRange(state, 0, segments.size()-1))
            return null;
        else
            return segments.get(state);
    }
    public boolean isFinale() {
        return isOutOfRange(state, 0, segments.size()-1);
    }
}
