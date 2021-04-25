package com.temreserva.backend.temreserva_backend.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import com.temreserva.backend.temreserva_backend.data.entity.Image;
import com.temreserva.backend.temreserva_backend.data.repository.ImageRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageBusiness {

    private final ImageRepository imageRepository;

    public ImageBusiness(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public byte[] getImageByRestaurantId(Long id) {
        Image img = imageRepository.findById(id).orElse(null);
        if(img != null)
            return decompressBytes(img.getPicByte());

        return null;
    }

    public HttpStatus restaurantImageUpload(MultipartFile file, Long restaurantId) throws IOException {
        Image image = Image.builder().name(file.getOriginalFilename()).type(file.getContentType())
                .picByte(compressBytes(file.getBytes())).build();
        imageRepository.save(image);
        return HttpStatus.OK;
    }

    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    public static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[10000000];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
        } catch (DataFormatException e) {
        }
        return outputStream.toByteArray();
    }
}
