package com.mine.product.czmtr.ram.asset.util;

import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * <一句话功能简述>
 * 生成资产设备二维码跟条形码
 *
 * @author 何森
 * @version [V1.00, 2019年7月24日]
 * @see [相关类/方法]
 * @since V1.00
 */
public class QRcodeImageUtil {
    private static final Logger logger = LoggerFactory.getLogger(QRcodeImageUtil.class);
// 	private static final int BLACK = 0xFF000000;
//    private static final int WHITE = 0xFFFFFFFF;

    /**
     * @Description:生成二维码
     * @Param:
     * @return:
     * @Author: 何森
     * @Date: 2019年7月24日
     *//*
    public  String buildQuickMark(String assetCoding,String deviceTypes,String deviceName,String pace) throws Exception {
    	String path = this.getUrl().getProperty("savePath").replaceAll("\\s*", "");
        try {
        	String content= "资产编码："+assetCoding+"。设备类型："+deviceTypes+"。设备名称："+deviceName+"。所在位置："+pace;
            BitMatrix byteMatrix = new MultiFormatWriter().encode(new String(content.getBytes(), "ISO-8859-1"),
                    BarcodeFormat.QR_CODE, 300, 300);
            String format = "png";
            Date date = new Date();
            SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddHHmmss");
            String filename=format1.format(date);
            File file = new File(path+"\\"+filename+"."+format);
            BufferedImage image = toBufferedImage(byteMatrix);
            if (!ImageIO.write(image, format, file)) {
                throw new IOException("Could not write an image of format " + format + " to " + file);
            }
            //二维码存储路径
            String src = filename+"."+format;
            return src;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return "ok";
       
    }*/

    /**
     * @Description:生成条形码
     * @Param:
     * @return:
     * @Author: 何森
     * @Date: 2019年7月24日
     */
    public String bulidBarCode(String assetCoding) {
        String path = new String();
        String separator = System.getProperties().getProperty("file.separator");
        File file;

        if (isWindows()) {
            path = this.getUrl().getProperty("savePathWindows").replaceAll("\\s*", "");
        } else {
            path = this.getUrl().getProperty("savePathLiunx").replaceAll("\\s*", "");
            logger.info("path" + path);
        }

        File dirs = new File(path);
        if(!dirs.exists()){
            dirs.mkdirs();
        }

        String format = "code.png";
        Date date = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = format1.format(date);

        if (isWindows()) {
            file = new File(path + "\\" + filename + "." + format);
        } else {
            file = new File(path + separator + filename + "." + format);
            logger.info("file" + file);
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        OutputStream out;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
//        EAN128Bean bean = new EAN128Bean();//条形码类型
        Code128Bean bean = new Code128Bean();
        final int height = 150;
        if (isWindows()) {
            bean.setModuleWidth(UnitConv.in2mm(2.5f / height));
        } else {
            bean.setModuleWidth(UnitConv.in2mm(2.6f / height));
        }
        bean.setHeight(15.0);
        bean.doQuietZone(false);
//        bean.setChecksumMode(ChecksumMode.CP_CHECK);
        try {
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, "image/png", height, BufferedImage.TYPE_BYTE_BINARY, false, 0);
            // 生成图片
            bean.generateBarcode(canvas, assetCoding);
            // 关闭流
            canvas.finish();
            out.close();
            //条形码存储路径
            String src = new String();
            if (isWindows()) {
                src = path + "\\" + filename + "." + format;
            } else {
                src = path + separator + filename + "." + format;
                logger.info("src" + src);
            }
            return src;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
	    
    /*private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }*/

    //获取储存二维码地址配置文件
    private Properties getUrl() {
        Resource res1 = new ClassPathResource("QRcodeImage.properties");
        Properties p = new Properties();
        try {
            p.load(res1.getInputStream());
            res1.getInputStream().close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return p;
    }

    private boolean isWindows() {
        return System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1;
    }
}