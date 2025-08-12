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
            log.info("path: {}", path);
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
}
