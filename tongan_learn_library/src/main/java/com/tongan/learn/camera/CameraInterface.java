package com.tongan.learn.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import static android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE;
import static android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static com.tongan.learn.camera.CameraParaUtil.CAMERA_FILE_PATH;


class CameraInterface {
    private static final String TAG = "SydCamera";
    private static CameraInterface mCameraInterface;
    private Camera mCamera = null;
    private CameraListener cameraListener = null;
    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;

    private int screenWith;
    private int screenHeight;
    public boolean safeToTakePicture = true;





    public interface CameraListener {

        void onTakePictureFail(byte[] data);

        /**
         * 拍照完成后 传递字节
         *
         * @param data
         */
        void onTakePictureByte(byte[] data);
    }


    public static synchronized CameraInterface getInstance() {
        if (mCameraInterface == null) {
            mCameraInterface = new CameraInterface();
        }
        return mCameraInterface;
    }


    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }



    public void takePicture() {
        if (mCamera != null && safeToTakePicture) {
            mCamera.takePicture(null, null, mPicture);
            safeToTakePicture = false;
        }
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
//            File pictureFile = FileUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE, CAMERA_FILE_PATH);
            if (data == null || data.length == 0) {
                onTakePictureFail(data);
                return;
            }

            try {
                if (cameraListener != null) {
                    cameraListener.onTakePictureByte(data);
                }

            } catch (Exception e) {
                onTakePictureFail(data);
            }


        }
    };

    private void onTakePictureFail(byte[] data) {
        if (cameraListener != null) {
            cameraListener.onTakePictureFail(data);
        }
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    private Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(mCameraId); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    public Camera getCamera() {
        if (mCamera == null) {
            mCamera = getCameraInstance();
        }
        return mCamera;
    }

    public void setCameraListener(CameraListener cameraListener) {
        this.cameraListener = cameraListener;
    }




    public int getmCameraId() {
        return mCameraId;
    }



}
