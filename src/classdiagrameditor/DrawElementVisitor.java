package classdiagrameditor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DrawElementVisitor implements ElementVisitor {
    private final Graphics2D graphics_;
    private final DiagramManager diagram_;

    // Colors for diagram elements
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
    
    // Fonts for diagram elements
    private static final Map<TextAttribute, Object> UNDERLINE_ATTR = new HashMap<TextAttribute, Object>() {{
        put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
    }};
    private static final Font FONT_CLASS          = new Font("Monospaced", Font.BOLD, 15);
    private static final Font FONT_CLASS_ABSTRACT = new Font("Monospaced", Font.BOLD | Font.ITALIC, 15);
    private static final Font FONT_NORM        = new Font("Monospaced", Font.PLAIN, 12);
    private static final Font FONT_UL          = FONT_NORM.deriveFont(UNDERLINE_ATTR);

    // Relationship endpoints
    private static final AffineTransform ENDPT_SCALE =AffineTransform.getScaleInstance(10.0, 10.0);

    private static final Polygon OPEN_ARROW =
        Util.buildPolygon(ENDPT_SCALE, new double[] {-1, 1, 0, 0, -1, -1, -0.001, 0});
    private static final Polygon DIAMOND =
        Util.buildPolygon(ENDPT_SCALE, new double[] {-2, 0, -1, 0.8, 0, 0, -1, -0.8});
    private static final Polygon TRIANGLE =
        Util.buildPolygon(ENDPT_SCALE, new double[] {-1, 1, 0, 0, -1, -1});

    // Relationship line styles
    private static final Stroke SOLID_STROKE =
        new BasicStroke(1.0f);
    private static final Stroke DOTTED_STROKE =
        new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {3}, 0);

    public DrawElementVisitor(DiagramManager diagram, Graphics2D graphics2d) {
        graphics_ = graphics2d;
        diagram_ = diagram;
    }

    // Draw an outlined box filled with one string per line
    private void drawStringBox(Color fg, Color bg, Color text, Rectangle box, Iterable<TextLayout> strings) {
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
        int y = box.y + BOX_PADDING;
        for (TextLayout tl : strings) {
            y += (int)(tl.getAscent() + tl.getDescent() + tl.getLeading());
            tl.draw(graphics_, box.x + BOX_PADDING, y);
        }

        graphics_.setClip(lastClip);
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

    public Rectangle[] layoutBoxes(Rectangle bounds, List<TextLayout> ... boxText) {
        int boxWidth = bounds.width;
        int boxHeight[] = new int[boxText.length];

        // Figure out width and height for text within each box
        for (int i = 0; i < boxText.length; i++) {
            boxHeight[i] = boxText[i].size() > 0 ? 2 * BOX_PADDING : 0;
            for (TextLayout tl: boxText[i]) {
                Rectangle textSize = tl.getBounds().getBounds();
                int width = textSize.width + BOX_PADDING * 2; // Account for padding
                int height = (int)(tl.getAscent() + tl.getDescent() + tl.getLeading());

                if (width > boxWidth) boxWidth = width;
                boxHeight[i] += height;
            }
        }

        // Build Rectangles representing each box
        Rectangle boxes[] = new Rectangle[boxText.length];
        int y = bounds.y;
        for (int i = 0; i < boxText.length; i++) {
            boxes[i] = new Rectangle(bounds.x, y, boxWidth, boxHeight[i]);
            y += boxes[i].height;
        }

        // Adjust overall bounds if boxes are wider
        if (boxWidth > bounds.width) bounds.width = boxWidth;

        // If overall bounds are taller, add the extra height to the last box
        int minHeight = y - bounds.y;
        if (bounds.height > minHeight) {
            boxes[boxes.length - 1].height += bounds.height - minHeight;
        } else {
            bounds.height = minHeight;
        }

        return boxes;
    }

    @Override
    public void visit(ClassElement e) {
        // Build TextLayout objects for each group of strings
        Font classFont = e.getIsAbstract() ? FONT_CLASS_ABSTRACT : FONT_CLASS;
        List<TextLayout> titleText = Arrays.asList(
            new TextLayout(e.getName(), classFont,graphics_.getFontRenderContext()));
        List<TextLayout> attributesText = new LinkedList<TextLayout>();
        List<TextLayout> operationsText = new LinkedList<TextLayout>();

        for (ClassElement.Attribute attr : e.getAttributes()) {
            Font f = attr.scope == ClassElement.ScopeType.Classifier ? FONT_UL : FONT_NORM;
            attributesText.add(new TextLayout(attr.toString(), f, graphics_.getFontRenderContext()));
        }

        for (ClassElement.Operation oper : e.getOperations()) {
            Font f = oper.scope == ClassElement.ScopeType.Classifier ? FONT_UL : FONT_NORM;
            operationsText.add(new TextLayout(oper.toString(), f, graphics_.getFontRenderContext()));
        }

        Rectangle bounds = new Rectangle(e.getArea());
        Rectangle boxes[] = layoutBoxes(bounds, titleText, attributesText, operationsText);

        // Adjust class area if it is too small to contain the text
        e.setMinSize(bounds.getSize());

        // Draw boxes
        drawStringBox(CLASS_OUTLINE_COLOR, CLASS_HEADER_COLOR, CLASS_HEADER_TEXT_COLOR,
                boxes[0], titleText);
        drawStringBox(CLASS_OUTLINE_COLOR, CLASS_BACKGROUND_COLOR, CLASS_TEXT_COLOR,
                boxes[1], attributesText);
        drawStringBox(CLASS_OUTLINE_COLOR, CLASS_BACKGROUND_COLOR, CLASS_TEXT_COLOR,
                boxes[2], operationsText);

        // Draw selection overlay and resize handle
        if(diagram_.isSelected(e)) {
            Rectangle area = e.getArea();

            graphics_.setColor(SELECTED_COLOR_BG);
            graphics_.fill(area);
            
            graphics_.setColor(SELECTED_COLOR);
            graphics_.draw(area);
            graphics_.fillRect(area.x + area.width - 10, area.y + area.height - 10, 10, 10);
        }
    }

    private void DrawRelationship(RelationshipElement e, Polygon left,
            Polygon right, boolean isFilled, Stroke stroke) {

        // If dragging, draw boxes for anchor points
        Element src = e.getSource();
        Element dest = e.getDest();

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

        // Draw line - TODO: Select dashed or solid
        graphics_.setColor(fgColor);
        graphics_.setStroke(stroke);
        graphics_.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
        graphics_.setStroke(SOLID_STROKE);

        // Draw src arrowhead
        if (left != null) {
            tx.setTransform(origTx);
            tx.rotate(angle, x1, y1);
            tx.translate(x1, y1);
            tx.scale(-1, 1); // Point the other way
            graphics_.setTransform(tx);
            graphics_.setColor(isFilled ? fgColor : bgColor);
            graphics_.fill(left);
            graphics_.setColor(fgColor);
            graphics_.draw(left);
        }

        // Draw dest arrowhead
        if (right != null) {
            tx.setTransform(origTx);
            tx.rotate(angle, x2, y2);
            tx.translate(x2, y2);
            graphics_.setTransform(tx);
            graphics_.setColor(isFilled ? fgColor : bgColor);
            graphics_.fill(right);
            graphics_.setColor(fgColor);
            graphics_.draw(right);
        }

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
    public void visit(DependencyRelationship e) {
        DrawRelationship(e, null, OPEN_ARROW, false, DOTTED_STROKE);
    }
    
    @Override
    public void visit(AggregationRelationship e) {
        DrawRelationship(e, DIAMOND, null, false, SOLID_STROKE);
    }
    
    @Override
    public void visit(AssociationRelationship e) {
        DrawRelationship(e, null, null, false, SOLID_STROKE);
    }
    
    @Override
    public void visit(CompositionRelationship e) {
        DrawRelationship(e, DIAMOND, null, true, SOLID_STROKE);
    }
    
    @Override
    public void visit(InheritanceRelationship e) {
        DrawRelationship(e, TRIANGLE, null, false, SOLID_STROKE);
    }
}