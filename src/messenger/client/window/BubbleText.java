package messenger.client.window;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import javax.swing.JPanel;
/**
 * @author harsh
 */
public class BubbleText extends JPanel {
   private static final long serialVersionUID = -5389178141802153305L;
  private Color c;
  private boolean right;
  public BubbleText(Color c, boolean right){
	  this.c = c;
	  this.right = right;
  }
   @Override
   protected void paintComponent(final Graphics g) {
	   AffineTransform tx = null;
       final Graphics2D graphics2D = (Graphics2D) g;
       RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
       qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
       graphics2D.setRenderingHints(qualityHints);
       graphics2D.setPaint(c);
       int width = getWidth();
       int height = getHeight();
       if(right){
    	   tx = new AffineTransform(new double[] {-1.0,0.0,0.0,1.0});
	       tx.translate(-width, 0);
	       graphics2D.transform(tx);
       }
       GeneralPath path = new GeneralPath();
       path.moveTo(5, 10);
       path.curveTo(5, 10, 7, 5, 0, 0);
       path.curveTo(0, 0, 12, 0, 12, 5);
       path.curveTo(12, 5, 12, 0, 20, 0);
       path.lineTo(width - 10, 0);
       path.curveTo(width - 10, 0, width, 0, width, 10);
       path.lineTo(width, height - 10);
       path.curveTo(width, height - 10, width, height, width - 10, height);
       path.lineTo(15, height);
       path.curveTo(15, height, 5, height, 5, height - 10);
       path.lineTo(5, 15);
       path.closePath();
       graphics2D.fill(path);
       if(right)
    	   graphics2D.transform(tx);
   }
}
