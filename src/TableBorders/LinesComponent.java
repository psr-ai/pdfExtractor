package TableBorders;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import dataTypes.TableLine;


public class LinesComponent extends JComponent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static int i=0;
    public static void incrementi(){
    	i++;
    }

private static class Line{
    final int x1; 
    final int y1;
    final int x2;
    final int y2;   
    final Color color;

    public Line(int x1, int y1, int x2, int y2, Color color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
    }               
}

private final LinkedList<Line> lines = new LinkedList<Line>();

public void addLine(int x1, int x2, int x3, int x4) {
    addLine(x1, x2, x3, x4, Color.black);
}

public void addLine(int x1, int x2, int x3, int x4, Color color) {
    lines.add(new Line(x1,x2,x3,x4, color));
    repaint();
}

public void clearLines() {
    lines.clear();
    repaint();
}

@Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    for (Line line : lines) {
        g.setColor(line.color);
        g.drawLine(line.x1, line.y1, line.x2, line.y2);
    }
}

public static void main(String[] args) throws IOException {
	ExtractPaths test = new ExtractPaths();
	
	final ArrayList<TableLine> tableLines = new ArrayList<TableLine>(test.extractLinesOnPage("E:\\Analytics Practice\\testing pdf\\h8.pdf", 0));
    JFrame testFrame = new JFrame();
    testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    final LinesComponent comp = new LinesComponent();
    comp.setPreferredSize(new Dimension(800, 800));
    testFrame.getContentPane().add(comp, BorderLayout.CENTER);
    JPanel buttonsPanel = new JPanel();
    JButton newLineButton = new JButton("New Line");
    JButton clearButton = new JButton("Clear");
    buttonsPanel.add(newLineButton);
    buttonsPanel.add(clearButton);
    testFrame.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
    for (TableLine tableLine : tableLines){
    	int x1 = (int) tableLine.getFromCoord().getX();
        int x2 = (int) tableLine.getToCoord().getX();
        int y1 = (int) tableLine.getFromCoord().getY();
        int y2 = (int) tableLine.getToCoord().getY();
        comp.addLine(x1, y1, x2, y2);
    }
    
    newLineButton.addActionListener(new ActionListener() {    	
        @Override
        public void actionPerformed(ActionEvent e) {
        	Random rand = new Random();
        	int x1 = (int) tableLines.get(i).getFromCoord().getX();
            int x2 = (int) tableLines.get(i).getFromCoord().getX();
            int y1 = (int) tableLines.get(i).getFromCoord().getY();
            int y2 = (int) tableLines.get(i).getToCoord().getY();
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            Color randomColor = new Color(r, g, b);
            System.out.println(tableLines.get(i).getFromCoord().getX() + " " + tableLines.get(i).getFromCoord().getX() + " " + tableLines.get(i).getFromCoord().getY() + " " + tableLines.get(i).getToCoord().getY());
            incrementi();
            comp.addLine(x1, y1, x2, y2, randomColor);           
        	
        }
    });
    clearButton.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            comp.clearLines();
        }
    });
    testFrame.pack();
    testFrame.setVisible(true);
}
public void showLines(ArrayList<TableLine> tableLines) throws IOException {
	JFrame testFrame = new JFrame();
    testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    final LinesComponent comp = new LinesComponent();
    comp.setPreferredSize(new Dimension(800, 800));
    testFrame.getContentPane().add(comp, BorderLayout.CENTER);
    JPanel buttonsPanel = new JPanel();
    JButton newLineButton = new JButton("New Line");
    JButton clearButton = new JButton("Clear");
    buttonsPanel.add(newLineButton);
    buttonsPanel.add(clearButton);
    testFrame.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
    for (TableLine tableLine : tableLines){
    	int x1 = (int) tableLine.getFromCoord().getX();
        int x2 = (int) tableLine.getToCoord().getX();
        int y1 = (int) tableLine.getFromCoord().getY();
        int y2 = (int) tableLine.getToCoord().getY();
        comp.addLine(x1, y1, x2, y2);
    }
    clearButton.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            comp.clearLines();
        }
    });
    testFrame.pack();
    testFrame.setVisible(true);
}
public void showLine(TableLine tableLine) throws IOException {
	JFrame testFrame = new JFrame();
    testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    final LinesComponent comp = new LinesComponent();
    comp.setPreferredSize(new Dimension(800, 800));
    testFrame.getContentPane().add(comp, BorderLayout.CENTER);
    JPanel buttonsPanel = new JPanel();
    JButton newLineButton = new JButton("New Line");
    JButton clearButton = new JButton("Clear");
    buttonsPanel.add(newLineButton);
    buttonsPanel.add(clearButton);
    testFrame.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
    
    int x1 = (int) tableLine.getFromCoord().getX();
    int x2 = (int) tableLine.getToCoord().getX();
    int y1 = (int) tableLine.getFromCoord().getY();
    int y2 = (int) tableLine.getToCoord().getY();
    comp.addLine(x1, y1, x2, y2);
    
    clearButton.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            comp.clearLines();
        }
    });
    testFrame.pack();
    testFrame.setVisible(true);
}
public void showLinesDynamically(ArrayList<TableLine> tableLines){
    JFrame testFrame = new JFrame();
    testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    final LinesComponent comp = new LinesComponent();
    comp.setPreferredSize(new Dimension(800, 800));
    testFrame.getContentPane().add(comp, BorderLayout.CENTER);
    JPanel buttonsPanel = new JPanel();
    JButton newLineButton = new JButton("New Line");
    JButton clearButton = new JButton("Clear");
    buttonsPanel.add(newLineButton);
    buttonsPanel.add(clearButton);
    testFrame.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
//    for (TableLine tableLine : tableLines){
//    	int x1 = (int) tableLine.getFromCoord().getX();
//        int x2 = (int) tableLine.getToCoord().getX();
//        int y1 = (int) tableLine.getFromCoord().getY();
//        int y2 = (int) tableLine.getToCoord().getY();
//        comp.addLine(x1, y1, x2, y2);
//    }
    
    newLineButton.addActionListener(new ActionListener() {    	
        @Override
        public void actionPerformed(ActionEvent e) {
        	Random rand = new Random();
        	int x1 = (int) tableLines.get(i).getFromCoord().getX();
            int x2 = (int) tableLines.get(i).getFromCoord().getX();
            int y1 = (int) tableLines.get(i).getFromCoord().getY();
            int y2 = (int) tableLines.get(i).getToCoord().getY();
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            Color randomColor = new Color(r, g, b);
            System.out.println(tableLines.get(i).getFromCoord().getX() + " " + tableLines.get(i).getFromCoord().getX() + " " + tableLines.get(i).getFromCoord().getY() + " " + tableLines.get(i).getToCoord().getY());
            incrementi();
            comp.addLine(x1, y1, x2, y2, randomColor);           
        	
        }
    });
    clearButton.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            comp.clearLines();
        }
    });
    testFrame.pack();
    testFrame.setVisible(true);
}
}