package com.alloymobile.image.resource;

import com.alloymobile.image.application.config.SecurityConstants;
import com.alloymobile.image.model.Image;
import com.alloymobile.image.service.BlobService;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@Tag(name = "Azure blob api", description = "The api performs crud operation for files to azure")
public class BlobResource {

    private final BlobService blobService;

    public BlobResource(BlobService blobService) {
        this.blobService = blobService;
    }

    @GetMapping(value = "/v1/{containerName}", produces = "application/json")
    public ResponseEntity<List<String>> listBlob(@PathVariable(name = "containerName") String containerName){
        List<String> files = this.blobService.listAllBlob(containerName);
        return ResponseEntity.ok(files);
    }

    @GetMapping(value = "/v1/{containerName}/blob/{blobName}", produces = "application/octet-stream")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Resource> readBlob(@PathVariable(name = "containerName") String containerName, @PathVariable(value = "blobName") String blobName){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + blobName + "\"");
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
                .headers(headers)
                .body(this.blobService.readBlob(containerName,blobName));
    }

    @DeleteMapping(value="/v1/{containerName}/blob/{blobName}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Void> deleteBlob(@PathVariable(name = "containerName") String containerName, @PathVariable(value = "blobName") String blobName){
        this.blobService.deleteBlob(containerName,blobName);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
