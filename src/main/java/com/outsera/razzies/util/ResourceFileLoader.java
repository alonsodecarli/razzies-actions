package com.outsera.razzies.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceFileLoader {

    private final String filePath;

    public ResourceFileLoader(String filePath) {
        this.filePath = filePath;
    }

    public InputStream getFile() {
        try {
            Resource resource = new ClassPathResource(filePath);

            if (resource.exists()) {
                // Caso o arquivo esteja no classpath
                if (resource.isFile()) {
                    Path path = Paths.get(resource.getFile().getAbsolutePath());
                    return Files.newInputStream(path);
                } else {
                    return resource.getInputStream();
                }
            } else {
                // Caso o arquivo esteja em outro local no sistema de arquivos
                Path path = Paths.get(filePath);
                if (Files.exists(path)) {
                    return Files.newInputStream(path);
                } else {
                    throw new IllegalStateException("Arquivo não encontrado no classpath ou no sistema de arquivos: " + filePath);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível carregar o arquivo: " + filePath, e);
        }
    }
}
