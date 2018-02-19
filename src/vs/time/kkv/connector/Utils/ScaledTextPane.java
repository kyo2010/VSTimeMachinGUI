/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.Utils;

/**
 *
 * @author kyo
 */
import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;
import javax.swing.text.html.BlockView;
import javax.swing.text.html.CSS;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import vs.time.kkv.connector.Utils.ScaledView.LargeHTMLEditorKit;

public class ScaledTextPane extends JEditorPane {

  JComboBox zoomCombo = new JComboBox(new String[]{"50%", "75%",
    "100%", "200%",
    "250%", "500%", "1000%"});

  public static void main(String[] args) {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    ScaledTextPane scaledTextPane = new ScaledTextPane();
    //scaledTextPane.setEditorKit(new ScaledEditorKit());
    //scaledTextPane.setEditorKit(new HTMLEditorKit());
    scaledTextPane.setEditorKit( new LargeHTMLEditorKit() );
    scaledTextPane.setContentType("text/html");
    scaledTextPane.setText("Test");
    scaledTextPane.getDocument().putProperty("i18n", Boolean.TRUE);
    scaledTextPane.getDocument().putProperty("ZOOM_FACTOR",
            new Double(5));
    JScrollPane scroll = new JScrollPane(scaledTextPane);
    frame.getContentPane().add(scroll);
    frame.getContentPane().add(scaledTextPane.zoomCombo,
            BorderLayout.NORTH);

    frame.setSize(200, 200);
    frame.show();
  }

  public ScaledTextPane() {
    super();
    //setEditorKit(new HTMLEditorKit());
    setEditorKit( new LargeHTMLEditorKit() );
    zoomCombo.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String s = (String) zoomCombo.getSelectedItem();
        s = s.substring(0, s.length() - 1);
        double scale = new Double(s).doubleValue() / 100;
        ScaledTextPane.this.getDocument().putProperty("ZOOM_FACTOR",
                new Double(scale));
        try {
          ScaledTextPane.this.getDocument().insertString(0, "",
                  null);    //refresh
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    });
    zoomCombo.setSelectedItem("250%");
  }

  public void repaint(int x, int y, int width, int height) {
    super.repaint(0, 0, getWidth(), getHeight());
  }

}

class ScaledEditorKit extends StyledEditorKit {

  public ViewFactory getViewFactory() {
    return new StyledViewFactory();
  }

  class StyledViewFactory implements ViewFactory {

    public View create(Element elem) {
      String kind = elem.getName();
      if (kind != null) {
        if (kind.equals(AbstractDocument.ContentElementName)) {
          return new LabelView(elem);
        } else if (kind.equals(AbstractDocument.ParagraphElementName)) {
          return new ParagraphView(elem);
        } else if (kind.equals(AbstractDocument.SectionElementName)) {
          return new ScaledView(elem, View.Y_AXIS);
        } else if (kind.equals(StyleConstants.ComponentElementName)) {
          return new ComponentView(elem);
        } else if (kind.equals(StyleConstants.IconElementName)) {
          return new IconView(elem);
        }
      }

      // default to text display
      return new LabelView(elem);
    }

  }
}

//-----------------------------------------------------------------
class ScaledView extends BoxView {

  public ScaledView(Element elem, int axis) {
    super(elem, axis);
  }

  public double getZoomFactor() {
    Double scale = (Double) getDocument().getProperty("ZOOM_FACTOR");
    if (scale != null) {
      return scale.doubleValue();
    }

    return 1;
  }

  public void paint(Graphics g, Shape allocation) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
            RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    double zoomFactor = getZoomFactor();
    AffineTransform old = g2d.getTransform();
    g2d.scale(zoomFactor, zoomFactor);
    super.paint(g2d, allocation);
    g2d.setTransform(old);
  }

  public float getMinimumSpan(int axis) {
    float f = super.getMinimumSpan(axis);
    f *= getZoomFactor();
    return f;
  }

  public float getMaximumSpan(int axis) {
    float f = super.getMaximumSpan(axis);
    f *= getZoomFactor();
    return f;
  }

  public float getPreferredSpan(int axis) {
    float f = super.getPreferredSpan(axis);
    f *= getZoomFactor();
    return f;
  }

  protected void layout(int width, int height) {
    super.layout(new Double(width / getZoomFactor()).intValue(),
            new Double(height
                    * getZoomFactor()).intValue());
  }

  public Shape modelToView(int pos, Shape a, Position.Bias b)
          throws BadLocationException {
    double zoomFactor = getZoomFactor();
    Rectangle alloc;
    alloc = a.getBounds();
    Shape s = super.modelToView(pos, alloc, b);
    alloc = s.getBounds();
    alloc.x *= zoomFactor;
    alloc.y *= zoomFactor;
    alloc.width *= zoomFactor;
    alloc.height *= zoomFactor;

    return alloc;
  }

  public int viewToModel(float x, float y, Shape a,
          Position.Bias[] bias) {
    double zoomFactor = getZoomFactor();
    Rectangle alloc = a.getBounds();
    x /= zoomFactor;
    y /= zoomFactor;
    alloc.x /= zoomFactor;
    alloc.y /= zoomFactor;
    alloc.width /= zoomFactor;
    alloc.height /= zoomFactor;

    return super.viewToModel(x, y, alloc, bias);
  }

  public static class LargeHTMLEditorKit extends HTMLEditorKit {

    ViewFactory factory = new MyViewFactory();

    @Override
    public ViewFactory getViewFactory() {
      return factory;
    }

    class MyViewFactory extends HTMLFactory {

      @Override
      public View create(Element elem) {
        AttributeSet attrs = elem.getAttributes();
        Object elementName = attrs.getAttribute(AbstractDocument.ElementNameAttribute);
        Object o = (elementName != null) ? null : attrs.getAttribute(StyleConstants.NameAttribute);
        if (o instanceof HTML.Tag) {
          HTML.Tag kind = (HTML.Tag) o;
          if (kind == HTML.Tag.HTML) {
            return new HTMLBlockView(elem);
          } else if (kind == HTML.Tag.IMPLIED) {
            String ws = (String) elem.getAttributes().getAttribute(CSS.Attribute.WHITE_SPACE);
            if ((ws != null) && ws.equals("pre")) {
              return super.create(elem);
            }
            return new HTMLParagraphView(elem);
          } else if ((kind == HTML.Tag.P)
                  || (kind == HTML.Tag.H1)
                  || (kind == HTML.Tag.H2)
                  || (kind == HTML.Tag.H3)
                  || (kind == HTML.Tag.H4)
                  || (kind == HTML.Tag.H5)
                  || (kind == HTML.Tag.H6)
                  || (kind == HTML.Tag.DT)) {
            // paragraph
            return new HTMLParagraphView(elem);
          }
        }
        return super.create(elem);
      }

    }

    private class HTMLBlockView extends BlockView {

      public HTMLBlockView(Element elem) {
        super(elem, View.Y_AXIS);
      }

      @Override
      protected void layout(int width, int height) {
        if (width < Integer.MAX_VALUE) {
          super.layout(new Double(width / getZoomFactor()).intValue(),
                  new Double(height
                          * getZoomFactor()).intValue());
        }
      }

      public double getZoomFactor() {
        Double scale = (Double) getDocument().getProperty("ZOOM_FACTOR");
        if (scale != null) {
          return scale.doubleValue();
        }

        return 1;
      }

      @Override
      public void paint(Graphics g, Shape allocation) {
        Graphics2D g2d = (Graphics2D) g;
        double zoomFactor = getZoomFactor();
        AffineTransform old = g2d.getTransform();
        g2d.scale(zoomFactor, zoomFactor);
        super.paint(g2d, allocation);
        g2d.setTransform(old);
      }

      @Override
      public float getMinimumSpan(int axis) {
        float f = super.getMinimumSpan(axis);
        f *= getZoomFactor();
        return f;
      }

      @Override
      public float getMaximumSpan(int axis) {
        float f = super.getMaximumSpan(axis);
        f *= getZoomFactor();
        return f;
      }

      @Override
      public float getPreferredSpan(int axis) {
        float f = super.getPreferredSpan(axis);
        f *= getZoomFactor();
        return f;
      }

      @Override
      public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException {
        double zoomFactor = getZoomFactor();
        Rectangle alloc;
        alloc = a.getBounds();
        Shape s = super.modelToView(pos, alloc, b);
        alloc = s.getBounds();
        alloc.x *= zoomFactor;
        alloc.y *= zoomFactor;
        alloc.width *= zoomFactor;
        alloc.height *= zoomFactor;

        return alloc;
      }

      @Override
      public int viewToModel(float x, float y, Shape a,
              Position.Bias[] bias) {
        double zoomFactor = getZoomFactor();
        Rectangle alloc = a.getBounds();
        x /= zoomFactor;
        y /= zoomFactor;
        alloc.x /= zoomFactor;
        alloc.y /= zoomFactor;
        alloc.width /= zoomFactor;
        alloc.height /= zoomFactor;

        return super.viewToModel(x, y, alloc, bias);
      }

    }
  }

  public static class HTMLParagraphView extends ParagraphView {

    public static int MAX_VIEW_SIZE = 100;

    public HTMLParagraphView(Element elem) {
      super(elem);
      strategy = new HTMLParagraphView.HTMLFlowStrategy();
    }

    public static class HTMLFlowStrategy extends FlowStrategy {

      protected View createView(FlowView fv, int startOffset, int spanLeft, int rowIndex) {
        View res = super.createView(fv, startOffset, spanLeft, rowIndex);
        if (res.getEndOffset() - res.getStartOffset() > MAX_VIEW_SIZE) {
          res = res.createFragment(startOffset, startOffset + MAX_VIEW_SIZE);
        }
        return res;
      }

    }
    public int getResizeWeight(int axis) {
      return 0;
    }
  }
}
