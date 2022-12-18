package com.alloymobile.image.application.config;

import com.azure.storage.blob.BlobClientBuilder;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.Objects;

@Configuration
public class AzureStorageConfig {

    private final String connectionString;

    private final String containerName;

    public AzureStorageConfig(ImageConfiguration configuration) {
        this.connectionString = configuration.getAzureStorageConnectionString();
        this.containerName = configuration.getAzureStorageContainerName();
    }

    @Bean
    public CloudBlobClient cloudBlobClient() throws URISyntaxException, StorageException, InvalidKeyException {
        CloudStorageAccount storageAccount = CloudStorageAccount.parse(connectionString);
        return storageAccount.createCloudBlobClient();
    }

    @Bean
    public CloudBlobContainer testBlobContainer() throws URISyntaxException, StorageException, InvalidKeyException {
        return cloudBlobClient().getContainerReference("cpagarg");
    }

}