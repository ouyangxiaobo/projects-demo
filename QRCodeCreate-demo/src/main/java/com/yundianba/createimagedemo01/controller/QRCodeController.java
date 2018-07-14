package com.yundianba.createimagedemo01.controller;


import com.yundianba.createimagedemo01.utils.CreateParseCode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;


@RestController
public class QRCodeController {


    @RequestMapping("/image")
   public Object image(HttpServletResponse response) throws  Exception{
        CreateParseCode cpCode=new CreateParseCode();
        // 生成二维码
       BufferedImage img = cpCode.createCode("http://192.168.0.102:10010", 300, 300);

        OutputStream stream = response.getOutputStream();
       //不要缓存
       //response.setDateHeader("expires", -1);
       //告诉所有浏览器不要缓存


            response.setHeader("Cache-control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("content-type", "image/png");



        return ImageIO.write(img,"png",stream);
   }
}
