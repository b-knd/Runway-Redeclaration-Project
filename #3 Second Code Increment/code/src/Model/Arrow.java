package Model;

import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class Arrow {
    private Line start;
    private Line length;
    private Line end;
    private Label label;
    private Polygon arrowHead;
    public Arrow(Line start, Line length, Line end,Label label,Polygon polygon){
        this.start = start;
        this.length = length;
        this.end = end;
        this.label = label;
        this.arrowHead = polygon;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public Line getStart() {
        return start;
    }

    public void setStart(Line start) {
        this.start = start;
    }

    public Line getLength() {
        return length;
    }

    public void setLength(Line length) {
        this.length = length;
    }

    public Line getEnd() {
        return end;
    }

    public void setEnd(Line end) {
        this.end = end;
    }

    public Polygon getArrowHead() {
        return arrowHead;
    }

    public void setArrowHead(Polygon arrowHead) {
        this.arrowHead = arrowHead;
    }
}
