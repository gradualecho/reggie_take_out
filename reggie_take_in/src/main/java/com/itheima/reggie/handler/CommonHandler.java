package com.itheima.reggie.handler;

import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonHandler {

    @Value("${Basepaths}")
    private String Baath;

    /**
     * 文件上传
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {  // 参数名必须是file且file只是临时文件须转存到指定位置，否则当前请求结束后临时文件自动删除
        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        // 截取原始文件名后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 拼装文件名
        String filename = UUID.randomUUID() +suffix;
        // 防止目录不存在情况
        File dir = new File(Baath);
        if (!dir.exists()){
            dir.mkdir();
        }
        // 转存文件到指定路径
        file.transferTo(new File(Baath+filename));

        return R.success(filename);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
     public void download(String name, HttpServletResponse response){
        try {
            // 输入流，用于读取文件
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(Baath+name));
            // 用于存储读取的文件
            byte[] bytes = new byte[1024];
            int len=0;
            // 输出流，用于将文件写出到浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            // 设置写出到浏览器的文件类型。image/jpeg--> 图片类型
            response.setContentType("image/jpeg");
            // 循环读取文件
            while((len=inputStream.read(bytes)) != -1){
                // 将读取到的文件写出
                outputStream.write(bytes,0,len);
                // 刷新输出流
                outputStream.flush();
            }
            // 关闭流
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
