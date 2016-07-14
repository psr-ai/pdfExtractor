package TableBorders;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdfviewer.PageDrawer;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.TextPosition;

import dataTypes.Coordinates;
import dataTypes.TableLine;
/**
 * A simple class that attempts to print out path drawing operations.
 * 
 * @author mkl
 */
public class PrintPaths extends PageDrawer
{
	private ArrayList<TableLine> tableLines = new ArrayList<TableLine>();
	
    public ArrayList<TableLine> getTableLines() {
		return tableLines;
	}

	//
    // constructor
    //
    public PrintPaths() throws IOException
    {
        super();
    }

    //
    // method overrides for mere path observation
    //
    // ignore text
    @Override
    protected void processTextPosition(TextPosition text) { }

    // ignore bitmaps
    @Override
    public void drawImage(Image awtImage, AffineTransform at) { }

    // ignore shadings
    @Override
    public void shFill(COSName shadingName) throws IOException { }

    @Override
    public void processStream(PDPage aPage, PDResources resources, COSStream cosStream) throws IOException
    {
        PDRectangle cropBox = aPage.findCropBox();
        this.pageSize = cropBox.createDimension();               
        super.processStream(aPage, resources, cosStream);
    }

    @Override
    public void fillPath(int windingRule) throws IOException
    {
        printPath();
        //System.out.printf("Fill; windingrule: %s\n\n", windingRule);
        getLinePath().reset();
    }

    @Override
    public void strokePath() throws IOException
    {
        printPath();
        //System.out.printf("Stroke; unscaled width: %s\n\n", getGraphicsState().getLineWidth());
        getLinePath().reset();
    }

    void printPath()
    {
    	TableLine tableLine;
    	float lineWidth = 0;
        GeneralPath path = getLinePath();
        PathIterator pathIterator = path.getPathIterator(null);
        
        double x = 0, y = 0;
        double coords[] = new double[6];
        while (!pathIterator.isDone()) {
            switch (pathIterator.currentSegment(coords)) {
            case PathIterator.SEG_MOVETO:            	
                //System.out.printf("Move to (%s %s)\n", coords[0], coords[1]);
                x = coords[0];
                y = coords[1];
                break;
            case PathIterator.SEG_LINETO:
                double width = getEffectiveWidth(coords[0] - x, coords[1] - y);                
                //System.out.printf("Line to (%s %s), scaled width %s\n", coords[0], coords[1], width);
                tableLine = new TableLine();
                Coordinates start = new Coordinates();
                Coordinates end = new Coordinates();                
                start.setX((float)x);
                start.setY((float)y);                
                x = coords[0];
                y = coords[1];
                end.setX((float) x);
                end.setY((float) y);
                lineWidth = (float) width;
                tableLine.setScaledWidth(lineWidth);
                tableLine.setFromCoord(start);
                tableLine.setToCoord(end);
                tableLines.add(tableLine);
                break;
            case PathIterator.SEG_QUADTO:
                //System.out.printf("Quad along (%s %s) and (%s %s)\n", coords[0], coords[1], coords[2], coords[3]);
                x = coords[2];
                y = coords[3];
                break;
            case PathIterator.SEG_CUBICTO:
                //System.out.printf("Cubic along (%s %s), (%s %s), and (%s %s)\n", coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
                x = coords[4];
                y = coords[5];
                break;
            case PathIterator.SEG_CLOSE:
                //System.out.println("Close path");
            }
            pathIterator.next();
        }
        
    }
    
    double getEffectiveWidth(double dirX, double dirY)
    {
        if (dirX == 0 && dirY == 0)
            return 0;
        Matrix ctm = getGraphicsState().getCurrentTransformationMatrix();
        double widthX = dirY;
        double widthY = -dirX;
        double widthXTransformed = widthX * ctm.getValue(0, 0) + widthY * ctm.getValue(1, 0);
        double widthYTransformed = widthX * ctm.getValue(0, 1) + widthY * ctm.getValue(1, 1);
        double factor = Math.sqrt((widthXTransformed*widthXTransformed + widthYTransformed*widthYTransformed) / (widthX*widthX + widthY*widthY));
        return getGraphicsState().getLineWidth() * factor;
    }
}