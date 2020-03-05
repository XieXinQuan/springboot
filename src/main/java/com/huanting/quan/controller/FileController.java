package com.huanting.quan.controller;

import com.huanting.quan.Enum.ResultEnum;
import com.huanting.quan.exception.GlobalException;
import com.huanting.quan.service.ActivitiService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/2/12
 */
@Controller
@RequestMapping("/image")
public class FileController {

    @Resource
    ActivitiService activitiService;

    @GetMapping("/vipApplicationViewProcess")
    public void vipApplicationViewProcess(String processInstanceId,
            HttpServletResponse response) throws IOException {
        InputStream inputStream = activitiService.vipApplicationViewProcess(processInstanceId);
        if (inputStream == null) {
            throw new GlobalException(ResultEnum.CustomException.getKey(), "您还没提交过会员申请,请前往提交..");
        }

        response.setContentType("image/png");

        BufferedImage image = ImageIO.read(inputStream);
        OutputStream out = response.getOutputStream();
        ImageIO.write(image, "png", out);

        inputStream.close();
        out.close();
    }
}
