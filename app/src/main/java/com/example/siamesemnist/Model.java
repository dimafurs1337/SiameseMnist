package com.example.siamesemnist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

class Model {

    static MappedByteBuffer loadModelFile(Context context) throws IOException {
        String MODEL_ASSETS_PATH = "23_new_model.tflite";
        AssetFileDescriptor assetFileDescriptor = context.getAssets().openFd(MODEL_ASSETS_PATH) ;
        FileInputStream fileInputStream = new FileInputStream( assetFileDescriptor.getFileDescriptor() ) ;
        FileChannel fileChannel = fileInputStream.getChannel() ;
        long startoffset = assetFileDescriptor.getStartOffset() ;
        long declaredLength = assetFileDescriptor.getDeclaredLength() ;
        return fileChannel.map( FileChannel.MapMode.READ_ONLY , startoffset , declaredLength ) ;
    }

    static float predict(Interpreter interpreter, Context context, int selectedNumber, ByteBuffer inputImage1, ByteBuffer inputImage2, Bitmap scaledBitmap){
        scaledBitmap = Bitmap.createScaledBitmap( scaledBitmap , 28 , 28 , false );
        int[] imagePixels1 = new int[28 * 28];
        int[] imagePixels2 = new int[28 * 28];
        ImageUtils.convertBitmapToByteBuffer(scaledBitmap, inputImage2, imagePixels2);
        float result = 1;
        Bitmap sample =  BitmapFactory.decodeResource(context.getResources(),R.drawable.image00);

        for (int i=0; i<5;i++){
            String sampleName = "image"+String.valueOf(selectedNumber)+String.valueOf(i)+".png";
            int resId = context.getResources().
                    getIdentifier(sampleName.split("\\.")[0], "drawable", context.getApplicationInfo().packageName);
            sample = BitmapFactory.decodeResource(context.getResources(), resId);
            sample = Bitmap.createScaledBitmap( sample , 28 , 28 , false ) ;
            ImageUtils.convertBitmapToByteBuffer(sample, inputImage1, imagePixels1);
            Object[] inputs = { inputImage1 , inputImage2};
            @SuppressLint("UseSparseArrays") Map<Integer, Object> outputs = new HashMap<>();
            outputs.put(0, new float[1][1] );
            interpreter.runForMultipleInputsOutputs( inputs , outputs ) ;
            float[][] similarity = ( float[][] )outputs.get( 0 ) ;
            if (similarity[0][0]< result)
                result = similarity[0][0];
        }
        return result;
    }
}
