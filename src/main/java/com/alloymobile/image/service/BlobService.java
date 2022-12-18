package com.alloymobile.image.service;

import com.alloymobile.image.integration.AzureBlobService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.List;

@Service
public class BlobService {

    private final AzureBlobService azureBlobService;

    public BlobService(AzureBlobService azureBlobService) {
        this.azureBlobService = azureBlobService;
    }

    public List<String> listAllBlob(String containerName){
        return this.azureBlobService.listBlobs(containerName);
    }

    public Resource readBlob(String containerName, String blobName){
        byte[] bytes = this.azureBlobService.readBlob(containerName,blobName);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        return new ByteArrayResource(bytes);
    }

    public URI addBlob(MultipartFile multipartFile, String containerName){
        return this.azureBlobService.upload(multipartFile,containerName);
    }

    public void deleteBlob(String containerName, String fileName){
        this.azureBlobService.deleteBlob(containerName,fileName);
    }

}
