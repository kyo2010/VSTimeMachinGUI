/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.MainlPannels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.FileFilter;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author kyo
 */
public class ImageImplement extends JPanel {

  public String imgFileName = null;
  public Image img = null;
  public int imgWidth = 0;
  public int imgHeight = 0;
  public boolean isChaged = false;

  public ImageImplement() {
    super();
    add(new JLabel("PHOTO"));
  }

  public void setImage(String imgFileName) {
    this.imgFileName = imgFileName;
    isChaged = true;
    int PREFERRED_WIDTH = getWidth();
    int PREFERRED_HEIGHT = getHeight();
    img = null;
    try {
      //img = ImageIO.read(new File(this.imgFileName));
      if (imgFileName != null && !imgFileName.equals("")) {
        ImageIcon icon = new ImageIcon(imgFileName);
        if (icon.getIconWidth() > PREFERRED_WIDTH) {
          icon = new ImageIcon(icon.getImage().getScaledInstance(
                  PREFERRED_WIDTH, -1, Image.SCALE_DEFAULT));
          if (icon.getIconHeight() > PREFERRED_HEIGHT) {
            icon = new ImageIcon(icon.getImage().getScaledInstance(
                    -1, PREFERRED_HEIGHT, Image.SCALE_DEFAULT));
          }
        }
        imgWidth = icon.getIconWidth();
        imgHeight = icon.getIconHeight();
        img = icon.getImage();
      }
    } catch (Exception e) {
    }
    if (img != null) {
      //Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
      //setPreferredSize(size);
      //setMinimumSize(size);
      //setMaximumSize(size);
      //setSize(size);
    }
    updateUI();
  }

  public void paintComponent(Graphics g) {
    if (img != null) {
      g.setColor(Color.WHITE);
      g.fillRect(0, 0, getWidth(), getHeight());
      g.drawImage(img, (getWidth()-imgWidth)/2, (getHeight()-imgHeight)/2, null);
    } else {
      super.paintComponent(g);
    }
  }
  
  public javax.swing.filechooser.FileFilter picFilter = new javax.swing.filechooser.FileFilter(){
    @Override
    public boolean accept(File f){
        return f.getName().toLowerCase().endsWith(".jpg")||
               f.getName().toLowerCase().endsWith(".png")||
               f.getName().toLowerCase().endsWith(".gif")||
                f.isDirectory();
    }
    
    @Override
    public String getDescription(){
        return "Images files (*.jpg,*.png,*.gif)";
    }
  };

}
