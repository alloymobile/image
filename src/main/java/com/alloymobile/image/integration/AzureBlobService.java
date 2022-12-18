package com.alloymobile.image.integration;

import com.azure.storage.blob.*;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AzureBlobService {

    private final CloudBlobClient cloudBlobClient;

    private final CloudBlobContainer cloudBlobContainer;

    public AzureBlobService(CloudBlobClient cloudBlobClient, CloudBlobContainer cloudBlobContainer) {
        this.cloudBlobClient = cloudBlobClient;
        this.cloudBlobContainer = cloudBlobContainer;
    }

    public boolean createContainer(String containerName){
        boolean containerCreated = false;
        CloudBlobContainer container = null;
        try {
            container = cloudBlobClient.getContainerReference(containerName);
        } catch (URISyntaxException | StorageException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        try {
            containerCreated = container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(), new OperationContext());
        } catch (StorageException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return containerCreated;
    }

    public List<String> listBlobs(String containerName){
        List<String> uris = new ArrayList<>();
        try {
            CloudBlobContainer container = cloudBlobClient.getContainerReference(containerName);
            for (ListBlobItem blobItem : container.listBlobs()) {
                uris.add(blobItem.getUri().toString());
            }
        } catch (URISyntaxException | StorageException e) {
            e.printStackTrace();
        }
        return uris;
    }

    public URI upload(MultipartFile multipartFile, String containerName){
        URI uri = null;
        CloudBlockBlob blob = null;
        try {
            CloudBlobContainer container = cloudBlobClient.getContainerReference(containerName);
            blob = container.getBlockBlobReference(multipartFile.getOriginalFilename());
            blob.upload(multipartFile.getInputStream(), -1);
            uri = blob.getUri();
        } catch (URISyntaxException | StorageException | IOException e) {
            e.printStackTrace();
        }
        return uri;
    }

    public void deleteBlob(String containerName, String blobName){
        try {
            CloudBlobContainer container = cloudBlobClient.getContainerReference(containerName);
            CloudBlockBlob blobToBeDeleted = container.getBlockBlobReference(blobName);
            blobToBeDeleted.deleteIfExists();
        } catch (URISyntaxException | StorageException e) {
            e.printStackTrace();
        }
    }

    public byte[] readBlob(String containerName, String blobName){
        try {
            CloudBlobContainer container = cloudBlobClient.getContainerReference(containerName);
            CloudBlockBlob blobToBeRead = container.getBlockBlobReference(blobName);
            //creating an object of output stream to recieve the file's content from azure blob.
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            blobToBeRead.download(outputStream);
            return outputStream.toByteArray();
        } catch (URISyntaxException | StorageException e) {
            e.printStackTrace();
        }
        return null;
    }
}