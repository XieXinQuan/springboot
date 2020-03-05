package com.huanting.quan.controller;

import com.huanting.quan.Enum.CommonEnum;
import com.huanting.quan.annotation.EnumValue;
import com.huanting.quan.service.ActivitiService;
import org.springframework.web.bind.annotation.*;

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
 * DATE:2020/2/4
 */
@RestController
@RequestMapping("/approval")
public class ApprovalController {

    @Resource
    ActivitiService activitiService;

    @PostMapping("/vipApplication")
    public String vipApplication() throws Exception {

        activitiService.vipApplication();
        return "Success";
    }


    @PostMapping("/vipApprove")
    public String agreeVipApplication(@RequestParam("processInstanceId") String processInstanceId,
                                      @RequestParam("status") @EnumValue(CommonEnum.class) Integer status){
        activitiService.agreeVipApplication(processInstanceId, status);
        return "Success";
    }

    @GetMapping("/vipApplicationViewProcess")
    public void vipApplicationViewProcess(HttpServletResponse response) throws IOException {

//        InputStream inputStream = activitiService.vipApplicationViewProcess();
//        if (inputStream == null) {
//            throw new GlobalException(ResultEnum.CustomException.getKey(), "您还没提交过会员申请,请前往提交..");
//        }
//
//        response.setContentType("image/png");
//
//        BufferedImage image = ImageIO.read(inputStream);
//        OutputStream out = response.getOutputStream();
//        ImageIO.write(image, "png", out);
//
//        inputStream.close();
//        out.close();
    }


    @GetMapping("/test")
    public String test() throws Exception {

        activitiService.applicant();
        return "Success";
    }

    @GetMapping("/view")
    public String view(){
        activitiService.loadMyStartTask();
        return "Success";
    }

    @GetMapping("/process/{id}")
    public void process(HttpServletResponse response, @PathVariable("id") String id) throws IOException {

        try {
            InputStream is = activitiService.process(id);
            if (is == null) {
                return;
            }

            response.setContentType("image/png");


            BufferedImage image = ImageIO.read(is);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "png", out);

            is.close();
            out.close();
        } catch (Exception ex) {
            System.out.println(".................查看流程图失败!");
        }
    }
}
