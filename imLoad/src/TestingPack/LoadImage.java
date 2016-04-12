package TestingPack;
/**
 * @author Aidos Sarsembayev 
 * This class reads the image as BufferedImage
 * you can get the height, width and RGB values of the image 
 * */

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class LoadImage{
	public BufferedImage image;
	public int width;
	public int height;
	
	public LoadImage(String imString){
		String im = imString;
		loadIm(im);
	}
	
	/** loadIm method reads the image and reads its width and height
	 * in order to use it, you need to add the parameter String */
	public BufferedImage loadIm(String im){
	      try {
	          //File input = new File(im);
	          image = ImageIO.read(new File(im));
	          width = image.getWidth();
	          height = image.getHeight();
	          

	       } catch (Exception e) {
	    	   e.printStackTrace();
	       }
	      return image;
	}
	
	/** getColorss method uses BufferedImage as an input and returns RGB values of the image
	 * to use this method you need to call: im.getColorss(im.image); 
	 * don't forget to write the LoadImage variable (im) first, then 'dot', then image*/
	@SuppressWarnings("unused")
	public void getColorss(BufferedImage imag) {
		//THIS IS A METHOD BODY TODO Auto-generated method stub
		int count = 0;
        for(int i=0; i<height; i++){
        
           for(int j=0; j<width; j++){
           
              count++;
              Color c = new Color(image.getRGB(j, i));
              System.out.println("S.No: " + count + " Red: " + c.getRed() +"  Green: " + c.getGreen() + " Blue: " + c.getBlue());
           }
        }
	}
	
	public int getPixel(BufferedImage imag, int x, int y){
		int p = imag.getRGB(x, y);
		return p;
	}
	
	public void grayScale(BufferedImage imag){
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				int p = imag.getRGB(x, y);
				
				int a = (p>>24)&0xff;
				int r = (p>>16)&0xff;
				int g = (p>>8)&0xff;
				int b = p&0xff;
				//calculate average
				int avg = (r+g+b)/3;
				
				//replace RGB value with avg
				p = (a << 24) | (a << 16) | (a << 8) | avg;
				
				imag.setRGB(x, y, p);
			}
		}
	}
	
	public void writeImage(BufferedImage imag){
		try {
			File f = new File("img/out.png");
			ImageIO.write(imag, "png", f);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
	}
	

}
