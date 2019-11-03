import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JComponent;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import javax.swing.JFileChooser;
import java.awt.Point;

public class SwingPaint {

  JButton clearBtn, blackBtn, blueBtn, greenBtn, redBtn,EraserBtn,saveBtn,drawlnBtn,drawFreeBtn;
  DrawArea drawArea;
  ActionListener actionListener = new ActionListener() {

  public void actionPerformed(ActionEvent e) {
      if (e.getSource() == clearBtn) {
        drawArea.clear();
      } else if (e.getSource() == blackBtn) {
        drawArea.black();
      } else if (e.getSource() == blueBtn) {
        drawArea.blue();
      } else if (e.getSource() == greenBtn) {
        drawArea.green();
      } else if (e.getSource() == redBtn) {
        drawArea.red();
      } else if (e.getSource() == EraserBtn) {
        drawArea.Eraser();
      } else if (e.getSource() == saveBtn) {
        saveToFile();
      }
      if (e.getSource() == drawlnBtn) {
          drawArea.line();
      }
      if(e.getSource() == drawFreeBtn){
        drawArea.generalDrawing();
      }

    }
  };

  public static void main(String[] args) {
    new SwingPaint().show();
  }

  public void show() {
    // create main frame
    JFrame frame = new JFrame("Swing Paint");
    Container content = frame.getContentPane();
    // set layout on content pane
    content.setLayout(new BorderLayout());
    // create draw area
    drawArea = new DrawArea();

    // add to content pane
    content.add(drawArea, BorderLayout.CENTER);

    // create controls to apply colors and call clear feature
    JPanel controls = new JPanel();

    clearBtn = new JButton("Clear");
    clearBtn.addActionListener(actionListener);
    blackBtn = new JButton("Black");
    blackBtn.addActionListener(actionListener);
    blueBtn = new JButton("Blue");
    blueBtn.addActionListener(actionListener);
    greenBtn = new JButton("Green");
    greenBtn.addActionListener(actionListener);
    redBtn = new JButton("Red");
    redBtn.addActionListener(actionListener);
    EraserBtn = new JButton("Eraser");
    EraserBtn.addActionListener(actionListener);
    saveBtn = new JButton("save");
    saveBtn.addActionListener(actionListener);
    drawlnBtn= new JButton("Line");
    drawlnBtn.addActionListener(actionListener);
    drawFreeBtn= new JButton("Free Style");
    drawFreeBtn.addActionListener(actionListener);

    // add to panel
    controls.add(greenBtn);
    controls.add(blueBtn);
    controls.add(blackBtn);
    controls.add(redBtn);
    controls.add(EraserBtn);
    controls.add(clearBtn);
    controls.add(saveBtn);
    controls.add(drawlnBtn);
    controls.add(drawFreeBtn);

    // add to content pane
    content.add(controls, BorderLayout.NORTH);

    frame.setSize(1920, 768);
    // can close frame
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // show the swing paint result
    frame.setVisible(true);
  }
  public void saveToFile() {
    JFileChooser fileChooser = new JFileChooser();
    int retval = fileChooser.showSaveDialog(saveBtn);
    if (retval == JFileChooser.APPROVE_OPTION) {
      File file = fileChooser.getSelectedFile();
      if (file == null) {
        return;
      }
      if (!file.getName().toLowerCase().endsWith(".jpg")) {
        file = new File(file.getParentFile(), file.getName() + ".jpg");
      }
      }
 }
}

class DrawArea extends JComponent {

  Point pointStart;
  Point pointEnd;

  boolean isGeneralDrawing;

  // Image in which we're going to draw
  private Image image;
  // Graphics2D object ==> used to draw on
  private Graphics2D g2;
  // Mouse coordinates
  private int currentX, currentY, oldX, oldY;

  public DrawArea() {
    setDoubleBuffered(false);
    addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        // save coord x,y when mouse is pressed
        oldX = e.getX();
        oldY = e.getY();
      }
    });

    addMouseMotionListener(generalDrawing);
    isGeneralDrawing = true;
  }

  MouseMotionAdapter generalDrawing = new MouseMotionAdapter() {
    public void mouseDragged(MouseEvent e) {
      // coord x,y when drag mouse
      currentX = e.getX();
      currentY = e.getY();

      if (g2 != null) {
        // draw line if g2 context not null
        g2.drawLine(oldX, oldY, currentX, currentY);
        // refresh draw area to repaint
        repaint();
        // store current coords x,y as olds x,y
        oldX = currentX;
        oldY = currentY;
      }
    }
  };

  protected void generalDrawing(){
    isGeneralDrawing = true;
    addMouseMotionListener(generalDrawing);
    removeMouseListener(lineMouseListener);
  }

 protected void line() {
      pointStart = null;
      pointEnd   = null;

      removeMouseMotionListener(generalDrawing);
      isGeneralDrawing = false;

     addMouseListener(lineMouseListener);
}

MouseAdapter lineMouseListener = new MouseAdapter() {
               public void mousePressed(MouseEvent e) {
               pointStart = e.getPoint();
           }

               public void mouseReleased(MouseEvent e) {
                 pointEnd = e.getPoint();
               if(!isGeneralDrawing){
                 if (pointStart != null) {
                     g2.drawLine(pointStart.x, pointStart.y, pointEnd.x,pointEnd.y);
                     pointStart = null;
                     repaint();
                 }
               }
           }
       };


  protected void paintComponent(Graphics g) {
    if (image == null) {
      // image to draw null ==> we create
      image = createImage(getSize().width, getSize().height);
      g2 = (Graphics2D) image.getGraphics();
      // enable antialiasing
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      // clear draw area
      clear();
    }

    g.drawImage(image, 0, 0, null);
  }

  // now we create exposed methods
  public void clear() {
    g2.setPaint(Color.white);
    // draw white on entire draw area to clear
    g2.fillRect(0, 0, getSize().width, getSize().height);
    g2.setPaint(Color.black);
    repaint();
  }

  public void red() {
    // apply red color on g2 context
    g2.setPaint(Color.red);
  }

  public void black() {
    g2.setPaint(Color.black);
  }

  public void Eraser() {
    g2.setPaint(Color.white);
  }

  public void green() {
    g2.setPaint(Color.green);
  }

  public void blue() {
    g2.setPaint(Color.blue);
  }
}
