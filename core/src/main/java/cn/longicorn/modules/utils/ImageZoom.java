package cn.longicorn.modules.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * 生成缩略图
 * 使用规则：ImageZoom.convert("/home/user1/1.jpg", "/home/user1/1_1.jpg", 404);
 * 
 * @author zhuchanglin
 */
public class ImageZoom {

	private static final Logger logger = LoggerFactory.getLogger(ImageZoom.class);

	/**
	 * 缩略图
	 * @param orgiImageFileName		原始图
	 * @param targetImageFileName	目标图
	 */
	public static void convert(final String orgiImageFileName, final String targetImageFileName, int width) {
		try {
			File fi = new File(orgiImageFileName); 		//大图文件
			File fo = new File(targetImageFileName); 	//将要转换出的小图文件

			AffineTransform transform = new AffineTransform();
			BufferedImage bis = ImageIO.read(fi);

			int w = bis.getWidth();
			int h = bis.getHeight();

			int nh = (width * h) / w;

			double sx = (double) width / w;
			double sy = (double) nh / h;

			transform.setToScale(sx, sy);

			AffineTransformOp ato = new AffineTransformOp(transform, null);
			BufferedImage bid = new BufferedImage(width, nh, BufferedImage.TYPE_3BYTE_BGR);
			ato.filter(bis, bid);
			ImageIO.write(bid, "jpeg", fo);
		} catch (Exception e) {
			logger.error("转换成缩略图时出现错误", e);
		}
	}
	
}