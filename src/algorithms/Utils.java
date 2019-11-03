package algorithms;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
    protected static ArrayList<ColoredPoint> toColoredPoint(ArrayList<Point> p){
        return (ArrayList<ColoredPoint>) p.stream().map(ColoredPoint::new)
                .collect(Collectors.toList());
    }

    protected static void mark(ArrayList<ColoredPoint> points, ArrayList<Point> toMark, Colour c){
        points.stream().filter(e-> toMark.contains(e)).forEach(e->{e.setColor(c);});
    }

    protected static ColoredPoint greyNode(ArrayList<ColoredPoint> points){
        Stream stream = points.stream().filter(e -> e.getColor()==Colour.BLACK);
        return (ColoredPoint) stream.findFirst().get();
    }

    protected static ArrayList<ColoredPoint> neighbors
            (ArrayList<ColoredPoint> points, ColoredPoint p, int edgeThreshold){
        return (ArrayList<ColoredPoint>)points.stream()
                .filter(e -> !e.equals(p) && e.distance(p)<edgeThreshold)
                .collect(Collectors.toList());
    }

    protected static ArrayList<ColoredPoint> neighbors
            (ArrayList<ColoredPoint> points, ColoredPoint p, Colour c, int edgeThreshold){
        return (ArrayList<ColoredPoint>) neighbors(points, p, edgeThreshold).stream().filter(e->e.getColor()==c).collect(Collectors.toList());
    }

    private static ArrayList<ColoredPoint> cloud
            (ArrayList<ColoredPoint> points, ColoredPoint p, HashSet<ColoredPoint> result, Colour c,
             int edgeThreshold){
        return (ArrayList<ColoredPoint>) points.stream()
                .filter(e->!result.contains(e) && e.getColor()==c && p.distance(e)<edgeThreshold)
                .collect(Collectors.toList());
    }

    /* points de la même couleur c pour lesquels il existe
    un chemin dans cet ensemble qui mène au point en paramètre. */
    protected static ArrayList<ColoredPoint> cloud
    (ArrayList<ColoredPoint> points, ColoredPoint p, Colour c, int edgeThreshold){
        HashSet<ColoredPoint> result = new HashSet<>();
        HashSet<ColoredPoint> tmp = new HashSet<>();;

        tmp.addAll(neighbors(points, p, Colour.BLUE, edgeThreshold));
        do {
            result.addAll(tmp);
            tmp.addAll(points
                    .stream()
                    .map(e->cloud(points, e, result, Colour.BLUE, edgeThreshold))
                    .flatMap(e->e.stream())
                    .collect(Collectors.toSet()));
        }while(!tmp.equals(result));


        ArrayList<ColoredPoint> r = new ArrayList();
        r.addAll(result);
        return r;
    }

    protected static BlackBlueComponent blackBlueComponent
            (ArrayList<ColoredPoint> points, ColoredPoint p, int edgeThreshold){
        ArrayList<ColoredPoint> blackNeighbors = neighbors(points, p, Colour.BLACK, edgeThreshold);
        ArrayList<ColoredPoint> blueCloud = new ArrayList<>(); blueCloud.add(p);

        BlackBlueComponent blackBlueComponent = new BlackBlueComponent();
        blackBlueComponent.blackBlueComponent.addAll(blackNeighbors);
        blackBlueComponent.blackBlueComponent.addAll(blueCloud);

        blackBlueComponent.blackBlueComponent.addAll(blueCloud.stream()
                .map(e->neighbors(points, p, Colour.BLACK, edgeThreshold))
                .flatMap(e->e.stream())
                .collect(Collectors.toList()));

        return blackBlueComponent;
    }

    protected static boolean equalsBlackBlueComponent
            (ArrayList<ColoredPoint> bbc1, ArrayList<ColoredPoint> bbc2){
        return bbc1.stream().filter(e->e.getColor()==Colour.BLUE).anyMatch(bbc2::contains);
    }
}
