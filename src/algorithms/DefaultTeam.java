package algorithms;

import java.awt.*;
import java.util.ArrayList;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultTeam {

  // sequential algorithm
  private ArrayList<Point> MIS(ArrayList<Point> points, int edgeThreshold){
    ArrayList<Point> I = new ArrayList<>();
    ArrayList<Point> V = (ArrayList)points.clone();

    while(!V.isEmpty()){
      Point v = V.remove(V.size()-1);
      I.add(v);
      for(int i=V.size()-1; i>=0; i--){
        Point n = V.get(i);
        if(n.distance(v)<edgeThreshold)
          V.remove(n);
      }
    }
    return I;
  }

  public ArrayList<Point> calculConnectedDominatingSet(ArrayList<Point> points, int edgeThreshold) {
    //REMOVE >>>>>
    ArrayList<Point> mis = MIS(points, edgeThreshold);
    ArrayList<Colored_Point> colored_points = Utils.toColoredPoint(points);
    Utils.mark(colored_points, (ArrayList)colored_points, Colour.GREY);
    Utils.mark(colored_points, mis, Colour.BLACK);

    Colored_Point grey;
    for(int i=5; i>1; i--){
      while((grey=Utils.greyNode(colored_points))!=null){
        grey.setColor(Colour.BLUE);
      }
    }
    /*for i ¼ 5; 4; 3; 2 do
      while there exists a grey node
    adjacent to at least i black
    nodes in different black-blue
    components
    do change its color from grey to
    blue;
    return all blue nodes.*/

    // WIP

    return (ArrayList)colored_points;
  }
  
  //FILE PRINTER
  private void saveToFile(String filename,ArrayList<Point> result){
    int index=0;
    try {
      while(true){
        BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(filename+Integer.toString(index)+".points")));
        try {
          input.close();
        } catch (IOException e) {
          System.err.println("I/O exception: unable to close "+filename+Integer.toString(index)+".points");
        }
        index++;
      }
    } catch (FileNotFoundException e) {
      printToFile(filename+Integer.toString(index)+".points",result);
    }
  }
  private void printToFile(String filename,ArrayList<Point> points){
    try {
      PrintStream output = new PrintStream(new FileOutputStream(filename));
      int x,y;
      for (Point p:points) output.println(Integer.toString((int)p.getX())+" "+Integer.toString((int)p.getY()));
      output.close();
    } catch (FileNotFoundException e) {
      System.err.println("I/O exception: unable to create "+filename);
    }
  }

  //FILE LOADER
  private ArrayList<Point> readFromFile(String filename) {
    String line;
    String[] coordinates;
    ArrayList<Point> points=new ArrayList<Point>();
    try {
      BufferedReader input = new BufferedReader(
          new InputStreamReader(new FileInputStream(filename))
          );
      try {
        while ((line=input.readLine())!=null) {
          coordinates=line.split("\\s+");
          points.add(new Point(Integer.parseInt(coordinates[0]),
                Integer.parseInt(coordinates[1])));
        }
      } catch (IOException e) {
        System.err.println("Exception: interrupted I/O.");
      } finally {
        try {
          input.close();
        } catch (IOException e) {
          System.err.println("I/O exception: unable to close "+filename);
        }
      }
    } catch (FileNotFoundException e) {
      System.err.println("Input file not found.");
    }
    return points;
  }
}
