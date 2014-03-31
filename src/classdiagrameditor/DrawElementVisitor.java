package classdiagrameditor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.util.Arrays;

public class DrawElementVisitor implements ElementVisitor {
    private final Graphics2D graphics_;
    private final DiagramManager diagram_;

    private static final Color ELEMENT_FOREGROUND_COLOR = Color.BLACK;
    private static final Color ELEMENT_BACKGROUND_COLOR = Color.WHITE;
    private static final Color SELECTED_COLOR          = new Color(0xFF3333FF, true);
    private static final Color SELECTED_COLOR_BG       = new Color(0xBBCCCCFF, true);
    private static final Color SELECTED_COLOR_BG_SOLID = new Color(0xFFCCCCFF, true);

    private static final int BOX_PADDING = 5;
    private static final int ANCHOR_POINT_SIZE = 10;

    private static final Color CLASS_HEADER_COLOR      = new Color(0xFF8800);
    private static final Color CLASS_HEADER_TEXT_COLOR = new Color(0xFFFFFF);
    private static final Color CLASS_TEXT_COLOR        = new Color(0x000000);
    private static final Color CLASS_OUTLINE_COLOR     = new Color(0xFF8800);
    private static final Color CLASS_BACKGROUND_COLOR  = new Color(0xFFDDCC);

    private static final Color PKG_HEADER_COLOR      = CLASS_HEADER_COLOR;
    private static final Color PKG_HEADER_TEXT_COLOR = CLASS_HEADER_TEXT_COLOR;
    private static final Color PKG_OUTLINE_COLOR     = CLASS_OUTLINE_COLOR;

    // Relationship endpoints
    private static final AffineTransform ENDPT_SCALE =AffineTransform.getScaleInstance(10.0, 10.0);
    private static final Polygon RELATION_ENDPTS[] = new Polygon[] {
        // Simple arrowhead
        Util.buildPolygon(ENDPT_SCALE, new double[] {-1, 1, 0, 0, -1, -1, -0.001, 0}),
        // Diamond
        Util.buildPolygon(ENDPT_SCALE, new double[] {-2, 0, -1, 0.8, 0, 0, -1, -0.8}),
        // Triangle
        Util.buildPolygon(ENDPT_SCALE, new double[] {-1, 1, 0, 0, -1, -1}),
    };

    public DrawElementVisitor(DiagramManager diagram, Graphics2D graphics2d) {
        graphics_ = graphics2d;
        diagram_ = diagram;
    }

    // Draw an outlined box filled with one string per line
    private int drawStringBox(Color fg, Color bg, Color text, Rectangle box, Iterable<String> strings) {
        // Draw box
        graphics_.setColor(bg);
        graphics_.fill(box);
        graphics_.setColor(fg);
        graphics_.draw(box);
        graphics_.setColor(text);

        // Narrow clipping area to just the box
        Shape lastClip = graphics_.getClip();
        graphics_.setClip(box);

        // Draw the text in the box
        int strHeight = graphics_.getFontMetrics().getHeight();
        int y = box.y + BOX_PADDING;
        for (String s : strings) {
            y += strHeight;
            graphics_.drawString(s, box.x + BOX_PADDING, y);
        }

        graphics_.setClip(lastClip);
        return y - box.y + BOX_PADDING; // Return actual drawn box height
    }

    private void drawPoints(double points[][]) {
        for (int i = 0; i < points[0].length; i++) {
            int x = (int)points[0][i] - ANCHOR_POINT_SIZE / 2;
            int y = (int)points[1][i] - ANCHOR_POINT_SIZE / 2;

            graphics_.setColor(ELEMENT_BACKGROUND_COLOR);
            graphics_.fillRect(x, y, ANCHOR_POINT_SIZE, ANCHOR_POINT_SIZE);
            graphics_.setColor(ELEMENT_FOREGROUND_COLOR);
            graphics_.drawRect(x, y, ANCHOR_POINT_SIZE, ANCHOR_POINT_SIZE);
        }
    }

    @Override
    public void visit(ClassElement e) {
        // Draw header
        Rectangle drawArea = new Rectangle(e.getArea());
        drawArea.y += drawStringBox(CLASS_OUTLINE_COLOR, CLASS_HEADER_COLOR, CLASS_HEADER_TEXT_COLOR,
                drawArea, Arrays.asList(e.getName()));
        drawArea = drawArea.intersection(e.getArea());
        
        // Draw properties and operations
        if(e.getProperties().size() > 0 || e.getOperations().size() > 0) {
            drawArea.y += drawStringBox(CLASS_OUTLINE_COLOR, CLASS_BACKGROUND_COLOR, CLASS_TEXT_COLOR,
                    drawArea, e.getProperties());
            drawArea = drawArea.intersection(e.getArea());

            drawArea.y += drawStringBox(CLASS_OUTLINE_COLOR, CLASS_BACKGROUND_COLOR, CLASS_TEXT_COLOR,
                    drawArea, e.getOperations());
        }

        if(diagram_.isSelected(e)) {
            Rectangle area = e.getArea();

            graphics_.setColor(SELECTED_COLOR_BG);
            graphics_.fill(area);
            
            graphics_.setColor(SELECTED_COLOR);
            graphics_.draw(area);
            graphics_.fillRect(area.x + area.width - 10, area.y + area.height - 10, 10, 10);
        }
    }
    
    @Override
    public boolean visit(ClassElement e, String name) {
        throw new UnsupportedOperationException("DrawElementVisitor ClassElement not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(AggregationRelationship e) {
        // If dragging, draw boxes for anchor points
        BoxElement src = e.getSource();
        BoxElement dest = e.getDest();

        if (e.isDraggingSrc() && src != null) drawPoints(src.getAnchorPoints());
        if (e.isDraggingDest() && dest != null) drawPoints(dest.getAnchorPoints());

        // Save off original transform matrix
        AffineTransform origTx = graphics_.getTransform();
        AffineTransform tx     = new AffineTransform(origTx);

        Point srcPt = e.getSrcPoint();
        Point destPt = e.getDestPoint();

        double x1 = srcPt.getX();
        double y1 = srcPt.getY();
        double x2 = destPt.getX();
        double y2 = destPt.getY();
        double midX = (x2 - x1) / 2.0 + x1;
        double midY = (y2 - y1) / 2.0 + y1;

        double angle = Math.atan2(y2 - y1, x2 - x1);
        double len   = Math.hypot(x2 - x1, y2 - y1);
        double width = 10.0;


        Color fgColor = diagram_.isSelected(e) ? SELECTED_COLOR : Color.BLACK;
        Color bgColor = diagram_.isSelected(e) ? SELECTED_COLOR_BG_SOLID : Color.WHITE;

        // Draw line
        graphics_.setColor(fgColor);
        graphics_.drawLine((int)x1, (int)y1, (int)x2, (int)y2);

        // Draw src aggregation diamond
        tx.setTransform(origTx);
        tx.rotate(angle, x1, y1);
        tx.translate(x1, y1);
        tx.scale(-1, 1); // Point the other way
        graphics_.setTransform(tx);
        graphics_.setColor(Color.black);
        graphics_.setColor(bgColor);
        graphics_.fill(RELATION_ENDPTS[1]);
        graphics_.setColor(fgColor);
        graphics_.draw(RELATION_ENDPTS[1]);

        // Draw label
        tx.setTransform(origTx);
        tx.rotate(angle, midX, midY);
        tx.translate(midX, midY);
        if(x1 > x2) tx.scale(-1, -1);
        
        int labelOffset = -graphics_.getFontMetrics().stringWidth(e.getLabel()) / 2;
        graphics_.setTransform(tx);
        graphics_.setColor(fgColor);
        graphics_.drawString(e.getLabel(), labelOffset, -2);

        // Restore original transform
        graphics_.setTransform(origTx);

        // Draw connector points
        if (diagram_.isSelected(e)) {
            int size = ANCHOR_POINT_SIZE;
            graphics_.fillOval(srcPt.x - size / 2, srcPt.y - size / 2, size, size);
            graphics_.fillOval(destPt.x - size / 2, destPt.y - size / 2, size, size);
        }
    }

    @Override
    public boolean visit(AggregationRelationship e, String name) {
        throw new UnsupportedOperationException("DrawElementVisitor AggregationRelationship not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void visit(AssociationRelationship e) {
        // If dragging, draw boxes for anchor points
        BoxElement src = e.getSource();
        BoxElement dest = e.getDest();

        if (e.isDraggingSrc() && src != null) drawPoints(src.getAnchorPoints());
        if (e.isDraggingDest() && dest != null) drawPoints(dest.getAnchorPoints());

        // Save off original transform matrix
        AffineTransform origTx = graphics_.getTransform();
        AffineTransform tx     = new AffineTransform(origTx);

        Point srcPt = e.getSrcPoint();
        Point destPt = e.getDestPoint();

        double x1 = srcPt.getX();
        double y1 = srcPt.getY();
        double x2 = destPt.getX();
        double y2 = destPt.getY();
        double midX = (x2 - x1) / 2.0 + x1;
        double midY = (y2 - y1) / 2.0 + y1;

        double angle = Math.atan2(y2 - y1, x2 - x1);
        double len   = Math.hypot(x2 - x1, y2 - y1);
        double width = 10.0;


        Color fgColor = diagram_.isSelected(e) ? SELECTED_COLOR : Color.BLACK;
        Color bgColor = diagram_.isSelected(e) ? SELECTED_COLOR_BG_SOLID : Color.WHITE;

        // Draw line
        graphics_.setColor(fgColor);
        graphics_.drawLine((int)x1, (int)y1, (int)x2, (int)y2);

        // Draw label
        tx.setTransform(origTx);
        tx.rotate(angle, midX, midY);
        tx.translate(midX, midY);
        if(x1 > x2) tx.scale(-1, -1);
        
        int labelOffset = -graphics_.getFontMetrics().stringWidth(e.getLabel()) / 2;
        graphics_.setTransform(tx);
        graphics_.setColor(fgColor);
        graphics_.drawString(e.getLabel(), labelOffset, -2);

        // Restore original transform
        graphics_.setTransform(origTx);

        // Draw connector points
        if (diagram_.isSelected(e)) {
            int size = ANCHOR_POINT_SIZE;
            graphics_.fillOval(srcPt.x - size / 2, srcPt.y - size / 2, size, size);
            graphics_.fillOval(destPt.x - size / 2, destPt.y - size / 2, size, size);
        }
    }

    @Override
    public boolean visit(AssociationRelationship e, String name) {
        throw new UnsupportedOperationException("DrawElementVisitor AssociationRelationship not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void visit(GeneralizationRelationship e) {
        // If dragging, draw boxes for anchor points
        BoxElement src = e.getSource();
        BoxElement dest = e.getDest();

        if (e.isDraggingSrc() && src != null) drawPoints(src.getAnchorPoints());
        if (e.isDraggingDest() && dest != null) drawPoints(dest.getAnchorPoints());

        // Save off original transform matrix
        AffineTransform origTx = graphics_.getTransform();
        AffineTransform tx     = new AffineTransform(origTx);

        Point srcPt = e.getSrcPoint();
        Point destPt = e.getDestPoint();

        double x1 = srcPt.getX();
        double y1 = srcPt.getY();
        double x2 = destPt.getX();
        double y2 = destPt.getY();
        double midX = (x2 - x1) / 2.0 + x1;
        double midY = (y2 - y1) / 2.0 + y1;

        double angle = Math.atan2(y2 - y1, x2 - x1);
        double len   = Math.hypot(x2 - x1, y2 - y1);
        double width = 10.0;


        Color fgColor = diagram_.isSelected(e) ? SELECTED_COLOR : Color.BLACK;
        Color bgColor = diagram_.isSelected(e) ? SELECTED_COLOR_BG_SOLID : Color.WHITE;

        // Draw line
        graphics_.setColor(fgColor);
        graphics_.drawLine((int)x1, (int)y1, (int)x2, (int)y2);

        // Draw dest arrowhead
        tx.setTransform(origTx);
        tx.rotate(angle, x1, y1);
        tx.translate(x1, y1);
        tx.scale(-1, 1); // Point the other way
        graphics_.setTransform(tx);
        graphics_.setColor(bgColor);
        graphics_.fill(RELATION_ENDPTS[2]);
        graphics_.setColor(fgColor);
        graphics_.draw(RELATION_ENDPTS[2]);

        // Draw label
        tx.setTransform(origTx);
        tx.rotate(angle, midX, midY);
        tx.translate(midX, midY);
        if(x1 > x2) tx.scale(-1, -1);
        
        int labelOffset = -graphics_.getFontMetrics().stringWidth(e.getLabel()) / 2;
        graphics_.setTransform(tx);
        graphics_.setColor(fgColor);
        graphics_.drawString(e.getLabel(), labelOffset, -2);

        // Restore original transform
        graphics_.setTransform(origTx);

        // Draw connector points
        if (diagram_.isSelected(e)) {
            int size = ANCHOR_POINT_SIZE;
            graphics_.fillOval(srcPt.x - size / 2, srcPt.y - size / 2, size, size);
            graphics_.fillOval(destPt.x - size / 2, destPt.y - size / 2, size, size);
        }
    }

    @Override
    public boolean visit(GeneralizationRelationship e, String name) {
        throw new UnsupportedOperationException("DrawElementVisitor GeneralizationRelationship not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void visit(CompositionRelationship e) {
        // If dragging, draw boxes for anchor points
        BoxElement src = e.getSource();
        BoxElement dest = e.getDest();

        if (e.isDraggingSrc() && src != null) drawPoints(src.getAnchorPoints());
        if (e.isDraggingDest() && dest != null) drawPoints(dest.getAnchorPoints());

        // Save off original transform matrix
        AffineTransform origTx = graphics_.getTransform();
        AffineTransform tx     = new AffineTransform(origTx);

        Point srcPt = e.getSrcPoint();
        Point destPt = e.getDestPoint();

        double x1 = srcPt.getX();
        double y1 = srcPt.getY();
        double x2 = destPt.getX();
        double y2 = destPt.getY();
        double midX = (x2 - x1) / 2.0 + x1;
        double midY = (y2 - y1) / 2.0 + y1;

        double angle = Math.atan2(y2 - y1, x2 - x1);
        double len   = Math.hypot(x2 - x1, y2 - y1);
        double width = 10.0;


        Color fgColor = diagram_.isSelected(e) ? SELECTED_COLOR : Color.BLACK;
        Color bgColor = diagram_.isSelected(e) ? SELECTED_COLOR_BG_SOLID : Color.WHITE;

        // Draw line 
        graphics_.setColor(fgColor);
        graphics_.drawLine((int)x1, (int)y1, (int)x2, (int)y2);

        // Draw src aggregation diamond
        tx.setTransform(origTx);
        tx.rotate(angle, x1, y1);
        tx.translate(x1, y1);
        tx.scale(-1, 1); // Point the other way
        graphics_.setTransform(tx);
        graphics_.setColor(Color.black);
        graphics_.fill(RELATION_ENDPTS[1]);
        graphics_.setColor(fgColor);
        graphics_.draw(RELATION_ENDPTS[1]);

        // Draw label
        tx.setTransform(origTx);
        tx.rotate(angle, midX, midY);
        tx.translate(midX, midY);
        if(x1 > x2) tx.scale(-1, -1);
        
        int labelOffset = -graphics_.getFontMetrics().stringWidth(e.getLabel()) / 2;
        graphics_.setTransform(tx);
        graphics_.setColor(fgColor);
        graphics_.drawString(e.getLabel(), labelOffset, -2);

        // Restore original transform
        graphics_.setTransform(origTx);

        // Draw connector points
        if (diagram_.isSelected(e)) {
            int size = ANCHOR_POINT_SIZE;
            graphics_.fillOval(srcPt.x - size / 2, srcPt.y - size / 2, size, size);
            graphics_.fillOval(destPt.x - size / 2, destPt.y - size / 2, size, size);
        }
    }

    @Override
    public boolean visit(CompositionRelationship e, String name) {
        throw new UnsupportedOperationException("DrawElementVisitor CompositionRelationship not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void visit(RealizationRelationship e) {
        // If dragging, draw boxes for anchor points
        BoxElement src = e.getSource();
        BoxElement dest = e.getDest();

        if (e.isDraggingSrc() && src != null) drawPoints(src.getAnchorPoints());
        if (e.isDraggingDest() && dest != null) drawPoints(dest.getAnchorPoints());

        // Save off original transform matrix
        AffineTransform origTx = graphics_.getTransform();
        AffineTransform tx     = new AffineTransform(origTx);

        Point srcPt = e.getSrcPoint();
        Point destPt = e.getDestPoint();

        double x1 = srcPt.getX();
        double y1 = srcPt.getY();
        double x2 = destPt.getX();
        double y2 = destPt.getY();
        double midX = (x2 - x1) / 2.0 + x1;
        double midY = (y2 - y1) / 2.0 + y1;

        double angle = Math.atan2(y2 - y1, x2 - x1);
        double len   = Math.hypot(x2 - x1, y2 - y1);
        double width = 10.0;


        Color fgColor = diagram_.isSelected(e) ? SELECTED_COLOR : Color.BLACK;
        Color bgColor = diagram_.isSelected(e) ? SELECTED_COLOR_BG_SOLID : Color.WHITE;

        // Draw line
        graphics_.setColor(fgColor);
        Stroke dotted = new BasicStroke(1, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL, 0, new float[] {10}, 0);
        graphics_.setStroke(dotted);
        graphics_.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
        Stroke soild = new BasicStroke(2.0f);
        graphics_.setStroke(soild);

        // Draw dest arrowhead
        tx.setTransform(origTx);
        tx.rotate(angle, x2, y2);
        tx.translate(x2, y2);
        graphics_.setTransform(tx);
        graphics_.setColor(bgColor);
        graphics_.fill(RELATION_ENDPTS[2]);
        graphics_.setColor(fgColor);
        graphics_.draw(RELATION_ENDPTS[2]);

        // Draw label
        tx.setTransform(origTx);
        tx.rotate(angle, midX, midY);
        tx.translate(midX, midY);
        if(x1 > x2) tx.scale(-1, -1);
        
        int labelOffset = -graphics_.getFontMetrics().stringWidth(e.getLabel()) / 2;
        graphics_.setTransform(tx);
        graphics_.setColor(fgColor);
        graphics_.drawString(e.getLabel(), labelOffset, -2);

        // Restore original transform
        graphics_.setTransform(origTx);

        // Draw connector points
        if (diagram_.isSelected(e)) {
            int size = ANCHOR_POINT_SIZE;
            graphics_.fillOval(srcPt.x - size / 2, srcPt.y - size / 2, size, size);
            graphics_.fillOval(destPt.x - size / 2, destPt.y - size / 2, size, size);
        }
    }

    @Override
    public boolean visit(RealizationRelationship e, String name) {
        throw new UnsupportedOperationException("DrawElementVisitor RealizationRelationship not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void visit(DependencyRelationship e) {
        // If dragging, draw boxes for anchor points
        BoxElement src = e.getSource();
        BoxElement dest = e.getDest();

        if (e.isDraggingSrc() && src != null) drawPoints(src.getAnchorPoints());
        if (e.isDraggingDest() && dest != null) drawPoints(dest.getAnchorPoints());

        // Save off original transform matrix
        AffineTransform origTx = graphics_.getTransform();
        AffineTransform tx     = new AffineTransform(origTx);

        Point srcPt = e.getSrcPoint();
        Point destPt = e.getDestPoint();

        double x1 = srcPt.getX();
        double y1 = srcPt.getY();
        double x2 = destPt.getX();
        double y2 = destPt.getY();
        double midX = (x2 - x1) / 2.0 + x1;
        double midY = (y2 - y1) / 2.0 + y1;

        double angle = Math.atan2(y2 - y1, x2 - x1);
        double len   = Math.hypot(x2 - x1, y2 - y1);
        double width = 10.0;


        Color fgColor = diagram_.isSelected(e) ? SELECTED_COLOR : Color.BLACK;
        Color bgColor = diagram_.isSelected(e) ? SELECTED_COLOR_BG_SOLID : Color.WHITE;

        // Draw line
        graphics_.setColor(fgColor);
        Stroke dotted = new BasicStroke(1, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL, 0, new float[] {10}, 0);
        graphics_.setStroke(dotted);
        graphics_.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
        Stroke soild = new BasicStroke(2.0f);
        graphics_.setStroke(soild);

        // Draw dest arrowhead - TODO: change to arrowhead 
        tx.setTransform(origTx);
        tx.rotate(angle, x2, y2);
        tx.translate(x2, y2);
        graphics_.setTransform(tx);
        graphics_.setColor(bgColor);
        graphics_.fill(RELATION_ENDPTS[2]);
        graphics_.setColor(fgColor);
        graphics_.draw(RELATION_ENDPTS[2]);

        // Draw label
        tx.setTransform(origTx);
        tx.rotate(angle, midX, midY);
        tx.translate(midX, midY);
        if(x1 > x2) tx.scale(-1, -1);
        
        int labelOffset = -graphics_.getFontMetrics().stringWidth(e.getLabel()) / 2;
        graphics_.setTransform(tx);
        graphics_.setColor(fgColor);
        graphics_.drawString(e.getLabel(), labelOffset, -2);

        // Restore original transform
        graphics_.setTransform(origTx);

        // Draw connector points
        if (diagram_.isSelected(e)) {
            int size = ANCHOR_POINT_SIZE;
            graphics_.fillOval(srcPt.x - size / 2, srcPt.y - size / 2, size, size);
            graphics_.fillOval(destPt.x - size / 2, destPt.y - size / 2, size, size);
        }
    }

    @Override
    public boolean visit(DependencyRelationship e, String name) {
        throw new UnsupportedOperationException("DrawElementVisitor DependencyRelationship not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void visit(CommentElement e) {
        throw new UnsupportedOperationException("DrawElementVisitor CommentElement not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public boolean visit(CommentElement e, String name) {
        throw new UnsupportedOperationException("DrawElementVisitor CommentElement not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(PackageElement e) {
        // Draw header
        Rectangle nameArea = new Rectangle(e.getArea());
        nameArea.width = nameArea.width * 3/4;
        nameArea.height = graphics_.getFontMetrics().getHeight() + BOX_PADDING * 2;
        drawStringBox(PKG_OUTLINE_COLOR, PKG_HEADER_COLOR, PKG_HEADER_TEXT_COLOR,
                nameArea, Arrays.asList(e.getName()));

        Rectangle outerArea = new Rectangle(e.getArea());
        outerArea.y += nameArea.height;
        outerArea.height -= nameArea.height;
        graphics_.setColor(PKG_OUTLINE_COLOR);
        graphics_.draw(outerArea);
        
        if(diagram_.isSelected(e)) {
            Rectangle area = e.getArea();

            graphics_.setColor(SELECTED_COLOR_BG);
            graphics_.fill(area);
            
            graphics_.setColor(SELECTED_COLOR);
            graphics_.draw(area);
            graphics_.fillRect(area.x + area.width - 10, area.y + area.height - 10, 10, 10);
        }
    }
    
    @Override
    public boolean visit(PackageElement e, String name) {
        throw new UnsupportedOperationException("DrawElementVisitor PackageElement not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}