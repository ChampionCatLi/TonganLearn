package com.tongan.learn.camera;

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


    private MediaRecorder mMediaRecorder;
    private boolean isRecording = false;
    private boolean isScaleView = true;
    private VideoRecordListener videoRecordListener = null;
    private int screenWith;
    private int screenHeight;
    public boolean safeToTakePicture = true;

    public void setScreenData(int screenWith, int screenHeight) {
        this.screenWith = screenWith;
        this.screenHeight = screenHeight;

    }

    public interface CameraListener {

        void onTakePictureFail(byte[] data);

        /**
         * 拍照完成后 传递字节
         *
         * @param data
         */
        void onTakePictureByte(byte[] data);
    }

    public interface VideoRecordListener {
        void onStartRecorder();

        void onStopRecorder();
    }

    public static synchronized CameraInterface getInstance() {
        if (mCameraInterface == null) {
            mCameraInterface = new CameraInterface();
        }
        return mCameraInterface;
    }

    void initCamera(float previewRate) {
        if (mCamera == null) {
            mCamera = getCameraInstance();
        }
        if (mCamera == null)
            return;

        Camera.Parameters params = mCamera.getParameters();
        List<String> focusModes = params.getSupportedFocusModes();
        params.setPictureFormat(PixelFormat.JPEG);//设置拍照后存储的图片格式
        if (focusModes.contains(FOCUS_MODE_CONTINUOUS_PICTURE)) {
            params.setFocusMode(FOCUS_MODE_CONTINUOUS_PICTURE);

        } else if (focusModes.contains(FOCUS_MODE_CONTINUOUS_VIDEO)) {
            params.setFocusMode(FOCUS_MODE_CONTINUOUS_VIDEO);

        }
        Camera.Size previewSize = CameraParaUtil.getInstance().getOptimalPreviewSize(params.getSupportedPreviewSizes(), screenWith, screenHeight);
        Camera.Size pictureSize = CameraParaUtil.getInstance().getOptimalPreviewSize(params.getSupportedPictureSizes(), screenWith, screenHeight);
        params.setPreviewSize(previewSize.width, previewSize.height);
        params.setPictureSize(pictureSize.width, pictureSize.height);
        mCamera.setParameters(params);
        mCamera.setDisplayOrientation(90);//预览旋转90度
    }


    public void focusOnTouch(int x, int y, FrameLayout preview) {
        Rect rect = new Rect(x - 100, y - 100, x + 100, y + 100);
        int left = rect.left * 2000 / preview.getWidth() - 1000;
        int top = rect.top * 2000 / preview.getHeight() - 1000;
        int right = rect.right * 2000 / preview.getWidth() - 1000;
        int bottom = rect.bottom * 2000 / preview.getHeight() - 1000;
        // 如果超出了(-1000,1000)到(1000, 1000)的范围，则会导致相机崩溃
        left = left < -1000 ? -1000 : left;
        top = top < -1000 ? -1000 : top;
        right = right > 1000 ? 1000 : right;
        bottom = bottom > 1000 ? 1000 : bottom;
        focusOnRect(new Rect(left, top, right, bottom));
    }

    private void focusOnRect(Rect rect) {
        if (getCamera() != null) {
            Camera.Parameters parameters = getCamera().getParameters(); // 先获取当前相机的参数配置对象
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO); // 设置聚焦模式
            Log.i(TAG, "parameters.getMaxNumFocusAreas() : " + parameters.getMaxNumFocusAreas());
            if (parameters.getMaxNumFocusAreas() > 0) {
                List<Camera.Area> focusAreas = new ArrayList<Camera.Area>();
                focusAreas.add(new Camera.Area(rect, 1000));
                parameters.setFocusAreas(focusAreas);
            }

            getCamera().cancelAutoFocus(); // 先要取消掉进程中所有的聚焦功能
            getCamera().setParameters(parameters);
        }
    }

    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
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

    public void setVideoRecordListener(VideoRecordListener videoRecordListener) {
        this.videoRecordListener = videoRecordListener;
    }

    public void setScreenWith(int screenWith) {
        this.screenWith = screenWith;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public void setScaleView(boolean scaleView) {
        isScaleView = scaleView;
    }

    public int getmCameraId() {
        return mCameraId;
    }


}
