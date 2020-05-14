package com.example.siamesemnist.model;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ImageUtils {

    public static void convertBitmapToByteBuffer(Bitmap bitmap, ByteBuffer inputImage, int[] imagePixels) {
        inputImage.rewind();

        bitmap.getPixels(imagePixels, 0, bitmap.getWidth(), 0, 0,
                bitmap.getWidth(), bitmap.getHeight());

        int pixel = 0;
        for (int i = 0; i < 28; ++i) {
            for (int j = 0; j < 28; ++j) {
                final int val = imagePixels[pixel++];
                inputImage.putFloat(convertToGreyScale(val));
            }
        }
    }

    private static float convertToGreyScale(int color) {
        float r = ((color >> 16) & 0xFF);
        float g = ((color >> 8) & 0xFF);
        float b = ((color) & 0xFF);

        int grayscaleValue = (int) (0.299f * r + 0.587f * g + 0.114f * b);
        return grayscaleValue / 255.0f;
    }

    public static ByteBuffer allocateTensor(){
        ByteBuffer inputImage =
                ByteBuffer.allocateDirect(4 * 28 * 28);
        inputImage.order(ByteOrder.nativeOrder());
        return inputImage;
    }

    public static byte[] imageToByte(Bitmap scaledBitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.PNG,100, stream);
        return stream.toByteArray();
    }

}
