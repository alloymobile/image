package com.alloymobile.image.integration;

import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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
        } catch (URISyntaxException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } catch (StorageException e) {
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

    public List listBlobs(String containerName){
        List uris = new ArrayList<>();
        try {
            CloudBlobContainer container = cloudBlobClient.getContainerReference(containerName);
            for (ListBlobItem blobItem : container.listBlobs()) {
                uris.add(blobItem.getUri());
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (StorageException e) {
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
            blob.getProperties().setContentType("image/jpg");
            blob.upload(multipartFile.getInputStream(), -1);
            uri = blob.getUri();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (StorageException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }

    public void deleteBlob(String containerName, String blobName){
        try {
            CloudBlobContainer container = cloudBlobClient.getContainerReference(containerName);
            CloudBlockBlob blobToBeDeleted = container.getBlockBlobReference(blobName);
            blobToBeDeleted.deleteIfExists();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (StorageException e) {
            e.printStackTrace();
        }
    }
}