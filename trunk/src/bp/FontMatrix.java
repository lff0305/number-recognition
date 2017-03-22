package bp;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.util.*;
import java.awt.image.*;

public class FontMatrix {
  public void test(int n){
	int size = 16;
    BufferedImage image = new BufferedImage(size, size,BufferedImage.TYPE_INT_RGB);
    Graphics2D g = image.createGraphics();
    g.setFont(new Font("黑体", Font.PLAIN, size));
    g.drawString(String.valueOf(n), 0, image.getHeight()-2);
  //  System.out.println(image.getWidth() + " : " + image.getHeight());
    int[] p = image.getRGB(0,0,image.getWidth(),image.getHeight(),new int[image.getWidth()*image.getHeight()],0,image.getWidth());
    for(int i=0;i<image.getHeight();i++) {
		boolean f = false;
    	for(int j=0;j<image.getWidth();j++) {
    		if (p[i*image.getWidth()+j] == -1) {
    			f = true;
    			break;
    		}
    	}
    	for(int j=0;j<image.getWidth();j++) {
    		System.out.print(p[i*image.getWidth()+j]==-1?"1":"0"+(j==image.getWidth()-1?"":""));
    	}
    }
	System.out.println("\"},");
  }
  public static void main(String args[]){
	  for (int i=0; i<10; i++) {
		  System.out.print("{\"" + i + "\",\"");
		  new FontMatrix().test(i);
	  }
  }
}