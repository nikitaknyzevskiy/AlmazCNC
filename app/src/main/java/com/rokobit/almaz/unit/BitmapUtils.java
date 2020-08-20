package com.rokobit.almaz.unit;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class BitmapUtils {

    private static final int BMP_WIDTH_OF_TIMES = 4;
    private static final int BYTE_PER_PIXEL = 3;

    public static boolean save(Bitmap bitmap, File file) throws IOException {

        if (bitmap == null){
            return false;
        }

        //image size
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        //image dummy data size
        //reason : the amount of bytes per image row must be a multiple of 4 (requirements of bmp format)
        byte[] dummyBytesPerRow = null;
        boolean hasDummy = false;
        int rowWidthInBytes = BYTE_PER_PIXEL * width; //source image width * number of bytes to encode one pixel.
        if(rowWidthInBytes % BMP_WIDTH_OF_TIMES > 0){
            hasDummy=true;
            //the number of dummy bytes we need to add on each row
            dummyBytesPerRow = new byte[(BMP_WIDTH_OF_TIMES-(rowWidthInBytes%BMP_WIDTH_OF_TIMES))];
            //just fill an array with the dummy bytes we need to append at the end of each row
            for(int i = 0; i < dummyBytesPerRow.length; i++){
                dummyBytesPerRow[i] = (byte)0x00;
            }
        }

        //an array to receive the pixels from the source image
        int[] pixels = new int[width * height];

        //the number of bytes used in the file to store raw image data (excluding file headers)
        int imageSize = (rowWidthInBytes+(hasDummy?dummyBytesPerRow.length:0)) * height;
        //file headers size
        int imageDataOffset = 0x36;

        //final size of the file
        int fileSize = imageSize + imageDataOffset;

        //Android Bitmap Image Data
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        //ByteArrayOutputStream baos = new ByteArrayOutputStream(fileSize);
        ByteBuffer buffer = ByteBuffer.allocate(fileSize);

        /**
         * BITMAP FILE HEADER Write Start
         **/
        buffer.put((byte)0x42);
        buffer.put((byte)0x4D);

        //size
        buffer.put(writeInt(fileSize));

        //reserved
        buffer.put(writeShort((short)0));
        buffer.put(writeShort((short)0));

        //image data start offset
        buffer.put(writeInt(imageDataOffset));

        /** BITMAP FILE HEADER Write End */

        //*******************************************

        /** BITMAP INFO HEADER Write Start */
        //size
        buffer.put(writeInt(0x28));

        //width, height
        //if we add 3 dummy bytes per row : it means we add a pixel (and the image width is modified.
        buffer.put(writeInt(width+(hasDummy?(dummyBytesPerRow.length==3?1:0):0)));
        buffer.put(writeInt(height));

        //planes
        buffer.put(writeShort((short)1));

        //bit count
        buffer.put(writeShort((short)24));

        //bit compression
        buffer.put(writeInt(0));

        //image data size
        buffer.put(writeInt(imageSize));

        //horizontal resolution in pixels per meter
        buffer.put(writeInt(0));

        //vertical resolution in pixels per meter (unreliable)
        buffer.put(writeInt(0));

        buffer.put(writeInt(0));

        buffer.put(writeInt(0));

        /** BITMAP INFO HEADER Write End */

        int row = height;
        int col = width;
        int startPosition = (row - 1) * col;
        int endPosition = row * col;
        while( row > 0 ){
            for(int i = startPosition; i < endPosition; i++ ){
//                if (Color.alpha(pixels[i]) < 127){
//                    pixels[i] = Color.BLACK;
//                }
                buffer.put((byte)(pixels[i] & 0x000000FF));
                buffer.put((byte)((pixels[i] & 0x0000FF00) >> 8));
                buffer.put((byte)((pixels[i] & 0x00FF0000) >> 16));
            }
            if(hasDummy){
                buffer.put(dummyBytesPerRow);
            }
            row--;
            endPosition = startPosition;
            startPosition = startPosition - col;
        }

        if (file.exists()) {
            return saveToDisk(file, buffer);
        } else {
            return file.createNewFile() && saveToDisk(file, buffer);
        }
    }

    public static float calculateFinalFileSizeMB(Bitmap bitmap){
        if (bitmap == null){
            return 0f;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        byte[] dummyBytesPerRow = null;
        boolean hasDummy = false;
        int rowWidthInBytes = BYTE_PER_PIXEL * width;
        if(rowWidthInBytes % BMP_WIDTH_OF_TIMES > 0){
            hasDummy = true;
            dummyBytesPerRow = new byte[(BMP_WIDTH_OF_TIMES-(rowWidthInBytes%BMP_WIDTH_OF_TIMES))];
            for(int i = 0; i < dummyBytesPerRow.length; i++){
                dummyBytesPerRow[i] = (byte)0x00;
            }
        }
        int imageSize = (rowWidthInBytes+(hasDummy ? dummyBytesPerRow.length : 0)) * height;
        int imageDataOffset = 0x36;
        int fileSize = imageSize + imageDataOffset;
        return fileSize / 1024f / 1024f;
    }

    private static boolean saveToDisk(File file, ByteBuffer buffer){
        FileOutputStream fos = null;
        boolean result = false;
        try {
            fos = new FileOutputStream(file);
            result = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            result = false;
        }
        try {
            if (fos != null) {
                fos.write(buffer.array());
                fos.close();
                result = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    private static byte[] writeInt(int value) throws IOException {
        byte[] b = new byte[4];

        b[0] = (byte)(value & 0x000000FF);
        b[1] = (byte)((value & 0x0000FF00) >> 8);
        b[2] = (byte)((value & 0x00FF0000) >> 16);
        b[3] = (byte)((value & 0xFF000000) >> 24);

        return b;
    }

    private static byte[] writeShort(short value) throws IOException {
        byte[] b = new byte[2];

        b[0] = (byte)(value & 0x00FF);
        b[1] = (byte)((value & 0xFF00) >> 8);

        return b;
    }
}
