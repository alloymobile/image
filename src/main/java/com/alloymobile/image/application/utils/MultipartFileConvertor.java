package com.alloymobile.image.application.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/*
 *<p>
 * Trivial implementation of the {@link MultipartFile} interface to wrap a byte[] decoded
 * from a BASE64 encoded String
 *</p>
 */
public class MultipartFileConvertor implements MultipartFile {
    private final byte[] imgContent;
    private final String name;
    private final String thumbnail;
    private final String contentType;

    public MultipartFileConvertor(byte[] imgContent, String name, String thumbnail, String contentType) {
        this.imgContent = imgContent;
        this.name = name;
        this.thumbnail = thumbnail;
        this.contentType = contentType;
    }

    @Override
    public String getName() {
        // TODO - implementation depends on your requirements
        return thumbnail;
    }

    @Override
    public String getOriginalFilename() {
        // TODO - implementation depends on your requirements
        return name;
    }

    @Override
    public String getContentType() {
        // TODO - implementation depends on your requirements
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }

    @Override
    public long getSize() {
        return imgContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return imgContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(imgContent);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(imgContent);
    }
}