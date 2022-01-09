package cn.ml.controller;

import cn.ml.entity.FileResult;
import cn.ml.entity.Result;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FileController {
    @PostMapping("/file/uploadFile")
    public FileResult SingleFileUpload(@RequestParam("file") MultipartFile file) {
        String originFileName = file.getOriginalFilename();
        String fileName = System.currentTimeMillis() + "." + originFileName.substring(originFileName.lastIndexOf(".") + 1);
        try {
            String filePath = ResourceUtils.getURL("classpath:static").getPath();
            File targetFile = new File(filePath + "/files");
            if (!targetFile.exists()) {
                targetFile.mkdir();
            }
            file.transferTo(new File(targetFile + "/" + fileName));
            Map<String, String> data = new HashMap<>();
            data.put("filePath", "/files/" + fileName);
            return FileResult.success(data);
        } catch (IOException e) {
            e.printStackTrace();
            return FileResult.failure("上传失败");
        }
    }
}
