package com.alloymobiletech.image.service;

import com.alloymobiletech.image.integration.AzureBlobService;
import com.alloymobiletech.image.utils.MultipartFileConvertor;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;

@Service
public class ImageService {

    private final AzureBlobService azureBlobService;

    public ImageService(AzureBlobService azureBlobService) {
        this.azureBlobService = azureBlobService;
    }

    public URI upload(MultipartFile multipartFile){
        return this.azureBlobService.upload(multipartFile);
    }



    public URI simpleResizeImage(MultipartFile file, int targetWidth) throws Exception {
        String[] parts = file.getContentType().split("/");
        String contentType="application/jpg";
        byte[] bytes = file.getBytes();
        InputStream is = new ByteArrayInputStream(bytes);
        BufferedImage originalImage = ImageIO.read(is);

        BufferedImage finalImage = Scalr.resize(originalImage, targetWidth);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if(parts.length == 2){
            ImageIO.write(finalImage, parts[1], baos);
            contentType = file.getContentType();
        }else{
            ImageIO.write(finalImage, "jpg", baos);
        }

        MultipartFileConvertor fileConvertor = new MultipartFileConvertor(
                baos.toByteArray()
                , file.getOriginalFilename()
                , file.getName()
                , contentType);
        return upload(fileConvertor);
    }
}
