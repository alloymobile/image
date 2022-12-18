package com.alloymobile.image.application.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ImageConfiguration {
    @Value("${azure.subscriptionId}")
    private String azureSubscriptionId;
    @Value("${azure.storage.connectionString}")
    private String azureStorageConnectionString;
    @Value("${azure.storage.container.name}")
    private String azureStorageContainerName;
    @Value("${image.secret}")
    private String imageSecret;
}

