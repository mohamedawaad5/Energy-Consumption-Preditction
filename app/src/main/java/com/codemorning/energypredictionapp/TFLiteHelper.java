package com.codemorning.energypredictionapp;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.util.Log;

import org.tensorflow.lite.Interpreter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

public class TFLiteHelper {
    private Interpreter interpreter;

    public TFLiteHelper(String modelPath, AssetManager assetManager) throws IOException {
//        interpreter = new Interpreter(loadModelFile(modelPath, assetManager));
        Interpreter.Options options = new Interpreter.Options();
        options.addDelegate(new org.tensorflow.lite.flex.FlexDelegate());
        interpreter = new Interpreter(loadModelFile(modelPath, assetManager), options);
    }

    private MappedByteBuffer loadModelFile(String modelPath, AssetManager assetManager) throws IOException {
        AssetFileDescriptor assetFileDescriptor = assetManager.openFd(modelPath);
        FileInputStream fileInputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());

        FileChannel fileChannel = fileInputStream.getChannel();
        long startOffset = assetManager.openFd(modelPath).getStartOffset();
        long declaredLength = assetManager.openFd(modelPath).getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public float[] predict(float[] input) {
        try {
            float[][][] inputArray = new float[1][10][1];
            for (int i = 0; i < 10; i++) {
                inputArray[0][i][0] = input[i];
            }
            float[][] output = new float[1][1];

            for (int i = 0; i < interpreter.getInputTensorCount(); i++) {
                int[] shape = interpreter.getInputTensor(i).shape();
                Log.d("Awaad", "Input " + i + ": " + Arrays.toString(shape));
            }
            for (int i = 0; i < interpreter.getOutputTensorCount(); i++) {
                int[] shape = interpreter.getOutputTensor(i).shape();
                Log.d("Awaad", "Output " + i + ": " + Arrays.toString(shape));
            }



            interpreter.run(inputArray, output);

            Log.d("Awaad", Arrays.toString(input));
            Log.d("Awaad", Arrays.toString(output[0]));

            return output[0];
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}