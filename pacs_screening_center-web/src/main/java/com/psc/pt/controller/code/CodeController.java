package com.psc.pt.controller.code;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.psc.pt.constants.ConstantsInterior;

@Controller
@RequestMapping(value="/code")
/**
 * 验证码类
 * @author YQ
 *
 */
public class CodeController {
	private static final Logger LOG = Logger.getLogger(CodeController.class);
	/**
	 * 获取验证码图片
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(method=RequestMethod.GET, value="/getAuthCode.htm")
	public void getAuthCode(HttpServletRequest request, HttpServletResponse response) throws IOException{
		//设置response头信息
        //禁止缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        
        int width = 90;//定义图片的width  
        int height = 30;//定义图片的height  
        int codeCount = 4;//定义图片上显示验证码的个数  
        int xx = 15;  
        int fontHeight = 26;  
        int codeY = 26;  
        char[] codeSequence = { 'a', 'B', 'c', 'D', 'e', 'F', 'G', 'H', 'i', 'J', 'k', 'L', 'M', 'N', 'o', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'w', 'x', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        
     // 定义图像buffer  
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics gd = buffImg.getGraphics();  
        // 创建一个随机数生成器类  
        Random random = new Random();  
        // 将图像填充为白色  
        gd.setColor(Color.WHITE);  
        gd.fillRect(0, 0, width, height);
        // 创建字体，字体的大小应该根据图片的高度来定。  
        Font font = new Font("Fixedsys", Font.BOLD, fontHeight); 
        // 设置字体
        gd.setFont(font);
        // 画边框
        gd.setColor(Color.BLACK);
        gd.drawRect(0, 0, width - 1, height - 1);
        // 随机产生40条干扰线，使图象中的认证码不易被其它程序探测到。 
        gd.setColor(Color.BLACK);
        for (int i = 0; i < 40; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(5);
            int yl = random.nextInt(5);
            gd.drawLine(x, y, x + xl, y + yl);
        }
        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。  
        StringBuffer randomCode = new StringBuffer();
        int red = 0, green = 0, blue = 0;
        // 随机产生codeCount数字的验证码。  
        for (int i = 0; i < codeCount; i++) {
            // 得到随机产生的验证码数字。  
            String code = String.valueOf(codeSequence[random.nextInt(36)]);
            // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同
            red = random.nextInt(50);
            green = random.nextInt(40);
            blue = random.nextInt(255);
  
            // 用随机产生的颜色将验证码绘制到图像中。  
            gd.setColor(new Color(red, green, blue));
            gd.drawString(code, (i + 1) * xx, codeY);
  
            // 将产生的四个随机数组合在一起。  
            randomCode.append(code);
        }
     // 随机产生2条干扰线，使图象中的认证码不易被其它程序探测到。 
        gd.setColor(Color.BLACK);
        for (int i = 0; i < 2; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(90);
            int yl = random.nextInt(30);
            gd.drawLine(x, y, x + xl, y + yl);
        }
        // 将四位数字的验证码保存到Session中。  
        HttpSession session = request.getSession();
        LOG.info(ConstantsInterior.AUTHCODE + ":" + randomCode);
        session.setAttribute(ConstantsInterior.AUTHCODE, randomCode.toString());
        
     // 将图像输出到Servlet输出流中。  
        ServletOutputStream sos = response.getOutputStream();  
        ImageIO.write(buffImg, "jpeg", sos);  
        sos.close();
	}
}
