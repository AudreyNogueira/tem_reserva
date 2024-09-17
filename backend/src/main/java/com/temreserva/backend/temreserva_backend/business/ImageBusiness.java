package com.temreserva.backend.temreserva_backend.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import com.temreserva.backend.temreserva_backend.data.entity.Image;
import com.temreserva.backend.temreserva_backend.data.repository.ImageRepository;
import com.temreserva.backend.temreserva_backend.web.model.response.ImageModel;
import com.temreserva.backend.temreserva_backend.web.utils.Enumerators;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ImageBusiness {

    private final ImageRepository imageRepository;

    public ImageBusiness(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // BUSINESS
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public Image buildImage(MultipartFile file, Long imageOwnerId, Boolean isProfilePic, Boolean isRestaurant)
            throws IOException {
        return Image.builder().imageOwnerId(imageOwnerId).isProfilePic(isProfilePic).isRestaurant(isRestaurant)
                .name(file.getOriginalFilename()).type(file.getContentType()).picByte(compressBytes(file.getBytes()))
                .build();
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

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // CREATE
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public ImageModel imageUpload(MultipartFile file, Long imageOwnerId, Boolean isProfilePic, Boolean isRestaurant)
            throws IOException {

        Image image = imageRepository.findImage(imageOwnerId, isProfilePic, isRestaurant).map(i -> {
            try {
                if (!isProfilePic)
                    return buildImage(file, imageOwnerId, isProfilePic, isRestaurant);

                i.setPicByte(compressBytes(file.getBytes()));
                return imageRepository.save(i);
            } catch (IOException e) {
                return null;
            }
        }).orElse(buildImage(file, imageOwnerId, isProfilePic, isRestaurant));

        if (image != null) {
            imageRepository.save(image);
            return ImageModel.builder().id(image.getId()).image(decompressBytes(image.getPicByte())).build();
        }

        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
        Enumerators.apiExceptionCodeEnum.DEFAULT_ERROR.getEnumValue());
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // GET
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public ImageModel getProfileImageByOwnerId(Long ownerId, Boolean isRestaurant) {
        Image img = imageRepository.findImage(ownerId, true, isRestaurant).orElse(null);

        if (img != null)
            return ImageModel.builder().image(decompressBytes(img.getPicByte())).id(img.getId()).build();

        return null;
    }

    public List<ImageModel> getRestaurantImagesByOwner(Long ownerId) {
        List<Image> images = imageRepository.findRestaurantImages(ownerId, false, true).orElse(null);

        if (images != null) {
            List<ImageModel> response = new ArrayList<ImageModel>();
            images.forEach(img -> response
                    .add(ImageModel.builder().image(decompressBytes(img.getPicByte())).id(img.getId()).build()));
            return response;
        }

        return null;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // DELETE
    // ------------------------------------------------------------------------------------------------------------------------------------------

    public void deleteImageByOwnerId(Long ownerId) {
        imageRepository.findByOwnerId(ownerId).map(imgs -> {
            imgs.forEach(i -> imageRepository.delete(i));
            return Void.TYPE;
        });
    }

    public void deleteImageById(Long id) {
        imageRepository.findById(id).map(i -> {
            imageRepository.delete(i);
            return Void.TYPE;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                Enumerators.apiExceptionCodeEnum.IMAGE_NOT_FOUND.getEnumValue()));

    }
}
