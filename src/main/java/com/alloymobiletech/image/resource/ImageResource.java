package com.alloymobiletech.image.resource;

import com.alloymobiletech.image.model.Image;
import com.alloymobiletech.image.service.ImageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

import static com.alloymobiletech.image.config.SecurityConstants.IMAGE_WIDTH;

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
            value = "/v1/image"
            ,consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
            ,produces = "application/json"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Image> uploadImage(@RequestPart(value = "file") MultipartFile file) {
        URI imageName = this.imageService.upload(file);
        Image image = new Image();
        image.setImageUrl(imageName.toString());
        return ResponseEntity.ok(image);
    }

    //add one image
    @PostMapping(
            value = "/v1/thumbnail"
            ,consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
            ,produces = "application/json"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Image> uploadThumbnail(@RequestPart(value = "file") MultipartFile file) throws Exception {
        URI imageName = this.imageService.simpleResizeImage(file,IMAGE_WIDTH);
        Image image = new Image();
        image.setImageUrl(imageName.toString());
        return ResponseEntity.ok(image);
    }
}
