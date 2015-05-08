package listfolders.includes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.Line2D;

import javax.swing.border.Border;

public class TopDashedBorder implements Border {

  @Override
  public void paintBorder(Component c, Graphics g, int x, int y, int width, int height){
    drawBorder(c,g);
  }
  
  @Override
  public Insets getBorderInsets(Component c){
    return new Insets(0,0,0,0);
  }
  
  @Override
  public boolean isBorderOpaque(){
    return true;
  }
  
  private void drawBorder(Component c, Graphics g){
    int x = c.getWidth();
    float dash[] = {1.0f};
    Graphics2D g2;
    BasicStroke stroke;

    g2 = (Graphics2D)g;
    stroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dash, 0.0f);
    g2.setStroke(stroke);
    g2.setColor((Color) new Color(120, 120, 120));

    g2.draw(new Line2D.Double(0, 0, x, 0));
  }

}
