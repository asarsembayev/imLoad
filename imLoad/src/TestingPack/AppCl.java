package TestingPack;

import java.awt.image.BufferedImage;


public class AppCl {
		static String imString = "img/123.png";
		BufferedImage im;
		public static void main(String args[]){
			LoadImage im = new LoadImage(imString);
			im.getColorss(im.image);
			im.grayScale(im.image);
			im.writeImage(im.image);
			//image.getImage(imString);
			//System.out.println(image.width);
		}
	
	
/*	private LoadImage loadIm(String imString2){
		image = new LoadImage(imString);
		return image;
	}
*/
}
