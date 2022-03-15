package com.alloymobile.image.resource;

import com.alloymobile.image.application.config.SecurityConstants;
import com.alloymobile.image.service.ImageService;
import com.alloymobile.image.model.Image;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@Tag(name = "Test APIs", description = "Test APIs for demo purpose")
public class ImageResource {

    private final ImageService imageService;

    public ImageResource(ImageService imageService) {
        this.imageService = imageService;
    }

    //add one image
//    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(
            value = "/v1/{containerName}/image"
            ,consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
            ,produces = "application/json"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Image> uploadImage(@RequestPart(value = "file") MultipartFile file,@PathVariable(name = "containerName") String containerName) {
        URI imageName = this.imageService.upload(file,containerName);
        Image image = new Image();
        image.setImageUrl(imageName.toString());
        return ResponseEntity.ok(image);
    }

    //add one image
    @PostMapping(
            value = "/v1/{containerName}/thumbnail"
            ,consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
            ,produces = "application/json"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Image> uploadThumbnail(@RequestPart(value = "file") MultipartFile file, @PathVariable(name = "containerName") String containerName) throws Exception {
        URI imageName = this.imageService.simpleResizeImage(file, SecurityConstants.IMAGE_WIDTH,containerName);
        Image image = new Image();
        image.setImageUrl(imageName.toString());
        return ResponseEntity.ok(image);
    }
}
