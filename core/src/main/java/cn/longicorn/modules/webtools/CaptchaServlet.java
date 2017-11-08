package cn.longicorn.modules.webtools;

import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.RandomStringUtils;
import cn.longicorn.modules.spring.security.Constants;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * 生成Web上的验证码图片
 */
public class CaptchaServlet extends HttpServlet {

	private static final long serialVersionUID = 4309163003030298772L;

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 * @param request servlet request
	 * @param response servlet response
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		response.setContentType("image/jpeg");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		ServletOutputStream responseOutputStream = response.getOutputStream();
		ImageIO.write(createRandImage(request), "JPEG", responseOutputStream);

		responseOutputStream.flush();
		responseOutputStream.close();
	}

	private BufferedImage createRandImage(HttpServletRequest request) {
		int width = 64, height = 26;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics graphic = image.getGraphics();
		Random random = new Random();

		graphic.setColor(getRandColor(200, 250));
		graphic.fillRect(0, 0, width, height);
		graphic.setFont(new Font("Times New Roman", Font.PLAIN, 18));

		//画边框
		//graphic.setColor(new Color());
		//graphic.drawRect(0,0,width-1,height-1);

		// 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
		graphic.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			graphic.drawLine(x, y, x + xl, y + yl);
		}

		// 取随机产生的认证码(4位数字)
		String sRand = "";
		for (int i = 0; i < 4; i++) {
			String rand = RandomStringUtils.random(1, false, true);
			sRand += rand;
			// 将认证码显示到图象中
			graphic.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));//调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
			graphic.drawString(rand, 13 * i + 6, 20);
		}
		// 将认证码存入SESSION
		request.getSession().setAttribute(Constants.CAPTCHA_KEY, sRand);
		// 图象生效
		graphic.dispose();
		return image;
	}

	private Color getRandColor(int fc, int bc) {//给定范围获得随机颜色
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	/** 
	 * Handles the HTTP <code>GET</code> method.
	 * @param request servlet request
	 * @param response servlet response
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/** 
	 * Handles the HTTP <code>POST</code> method.
	 * @param request servlet request
	 * @param response servlet response
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		processRequest(request, response);
	}

}