package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.storage.facade;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

@Component
@Slf4j
public class LocalStorageDrive implements StorageDrive{


    @Override
    public void uploadFile(String fileName, byte[] content, String path, String contentType) {
        try{
            // Crear el directorio si no existe
            log.info("path upload: {}", path);
            File directory = new File(path);
            if (!directory.exists() && !directory.mkdirs()) {
                throw new RuntimeException("No se pudo crear el directorio local: " + path);
            }
            // Ruta completa del archivo
            //File archivo = Paths.get("/pdfs", fileName).toFile();
            try(FileOutputStream  fos=new FileOutputStream(Paths.get(path,fileName).toFile())){
                fos.write(content);
            }
        }
       catch(IOException e){
            throw new RuntimeException("Error uploading file to local storage",e);
        }
    }

    @Override
    public void deleteFile(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                if (file.delete()) {
                    log.info("File delete correctle: {}", path);
                } else {
                    throw new IOException("It can't delete file: " + path);
                }
            } else {
                log.warn("File no exists: {}", path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error deleting file from local storage", e);
        }
    }
}
