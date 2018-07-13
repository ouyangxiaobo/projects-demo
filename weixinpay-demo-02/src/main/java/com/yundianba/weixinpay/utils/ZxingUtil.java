package com.yundianba.weixinpay.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.pdf417.decoder.ec.ErrorCorrection;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import sun.font.CreatedFontTracker;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class ZxingUtil {

    public static  Boolean encode(String contents,String format,int width,int height,String saveImgFilePath){

        Boolean bool=false;
        BufferedImage image=createImage(contents,width,height);
        if(image!=null){
              bool=writeToFile(image,format,saveImgFilePath);
        }
        return bool;
    }

    public static  void encode(String contents,int width,int height){
        createImage(contents,width,height);

    }
    public static  BufferedImage createImage(String contents,int width,int height){
        BufferedImage bufImg=null;
        Map<EncodeHintType,Object>  hints=new HashMap<EncodeHintType,Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.MARGIN,10);
        hints.put(EncodeHintType.CHARACTER_SET,"utf-8");
        try {
            BitMatrix bitMatrix=new MultiFormatWriter().encode(contents,BarcodeFormat.QR_CODE,10,11);
            MatrixToImageConfig config=new MatrixToImageConfig(0xFF00001,0xFFF0001);
            bufImg=MatrixToImageWriter.toBufferedImage(bitMatrix,config);

        }catch (Exception e){
            e.printStackTrace();
        }
       return bufImg;
    }


    @SuppressWarnings("finally")
    public static  Boolean writeToFile(BufferedImage bufImg,String format,String saveImgFilePath){
        Boolean bool=false;
        try{
            bool=ImageIO.write(bufImg,format,new File(saveImgFilePath));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return bool;
        }

    }
}
