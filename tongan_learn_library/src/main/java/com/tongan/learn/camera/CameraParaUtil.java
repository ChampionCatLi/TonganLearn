package com.tongan.learn.camera;

import android.hardware.Camera;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 2019-5-23
 */

class CameraParaUtil {
    static int defaultPicQuality = 80;
    private CameraSizeComparator sizeComparator = new CameraSizeComparator();
    private static CameraParaUtil myCamPara = null;
    static final String CAMERA_FILE_PATH = "tonganphoto";

    static CameraParaUtil getInstance() {
        if (myCamPara == null) {
            myCamPara = new CameraParaUtil();
            return myCamPara;
        } else {
            return myCamPara;
        }
    }

    public class CameraSizeComparator implements Comparator<Camera.Size> {
        public int compare(Camera.Size lhs, Camera.Size rhs) {

            return Integer.compare(lhs.width, rhs.width);
        }
    }

    public Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        Collections.sort(sizes, sizeComparator);
        int desiredWidth;
        int desiredHeight;
        desiredWidth = h;
        desiredHeight = w;

        for (Camera.Size mSize : sizes) {
            if (mSize.width == desiredWidth && (mSize.height == desiredHeight)) {
                return mSize;
            }
        }
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;


        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);

            }
        }
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }


}
