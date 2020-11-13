package com.tongan.learn.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.SortedSet;


/**
 * 2019-5-23
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Context context;

    /**
     * 预览尺寸集合
     */
    private final SizeMap mPreviewSizes = new SizeMap();

    /**
     * 图片尺寸集合
     */
    private final SizeMap mPictureSizes = new SizeMap();

    private AspectRatio mAspectRatio;
    /**
     * 屏幕旋转显示角度
     */
    private int mDisplayOrientation;

    public CameraPreview(Context context) {
        super(context);
        this.context = context;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mHolder.setFormat(PixelFormat.TRANSPARENT);


    }

    public void surfaceCreated(SurfaceHolder holder) {
//
        initCamera(holder);
        startPreview(holder);
    }

    private void initCamera(SurfaceHolder holder) {
        try {
            Camera mCamera = CameraInterface.getInstance().getCamera();
            //设置设备高宽比
            mAspectRatio = getDeviceAspectRatio((Activity) context);

            Camera.Parameters parameters = mCamera.getParameters();
            //获取所有支持的预览尺寸
            mPreviewSizes.clear();
            for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
                int width = Math.min(size.width, size.height);
                int heigth = Math.max(size.width, size.height);
                mPreviewSizes.add(new Size(width, heigth));
            }
            //获取所有支持的图片尺寸
            mPictureSizes.clear();
            for (Camera.Size size : parameters.getSupportedPictureSizes()) {
                int width = Math.min(size.width, size.height);
                int heigth = Math.max(size.width, size.height);
                mPictureSizes.add(new Size(width, heigth));
            }
            Size previewSize = chooseOptimalSize(mPreviewSizes.sizes(mAspectRatio));
            Size pictureSize = mPictureSizes.sizes(mAspectRatio).last();
            //设置相机参数
            parameters.setPreviewSize(Math.max(previewSize.getWidth(), previewSize.getHeight()), Math.min(previewSize.getWidth(), previewSize.getHeight()));
            parameters.setPictureSize(Math.max(pictureSize.getWidth(), pictureSize.getHeight()), Math.min(pictureSize.getWidth(), pictureSize.getHeight()));
            parameters.setPictureFormat(ImageFormat.JPEG);
//            parameters.setRotation(getDisplayOrientation());
            mCamera.setParameters(parameters);
            //设置预览方向
            mCamera.setDisplayOrientation(90);
            //把这个预览效果展示在SurfaceView上面
            mCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void surfaceDestroyed(SurfaceHolder holder) {

        CameraInterface.getInstance().releaseCamera();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (mHolder.getSurface() == null) {
            return;
        }

        try {
            if (CameraInterface.getInstance().getCamera() != null)
                CameraInterface.getInstance().getCamera().stopPreview();
        } catch (Exception e) {
        }
        startPreview(holder);
    }

    private void startPreview(SurfaceHolder holder) {

        try {
            if (CameraInterface.getInstance().getCamera() != null) {
                CameraInterface.getInstance().getCamera().setPreviewDisplay(holder);
                CameraInterface.getInstance().getCamera().startPreview();
                CameraInterface.getInstance().getCamera().cancelAutoFocus();
                CameraInterface.getInstance().safeToTakePicture = true;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 注释：选择合适的预览尺寸
     * 时间：2019/3/4 0004 11:25
     * 作者：郭翰林
     *
     * @param sizes
     * @return
     */
    @SuppressWarnings("SuspiciousNameCombination")
    private Size chooseOptimalSize(SortedSet<Size> sizes) {
        int desiredWidth;
        int desiredHeight;
        final int surfaceWidth = getWidth();
        final int surfaceHeight = getHeight();

        if (isLandscape(mDisplayOrientation)) {
            desiredWidth = surfaceHeight;
            desiredHeight = surfaceWidth;
        } else {
            desiredWidth = surfaceWidth;
            desiredHeight = surfaceHeight;
        }
        Size result = new Size(desiredWidth, desiredHeight);
        if (sizes != null && !sizes.isEmpty()) {
            for (Size size : sizes) {
                if (desiredWidth <= size.getWidth() && desiredHeight <= size.getHeight()) {
                    return size;
                }
                result = size;
            }
        }
        return result;
    }

    private boolean isLandscape(int orientationDegrees) {
        return (orientationDegrees == Surface.ROTATION_90 ||
                orientationDegrees == Surface.ROTATION_270);
    }


    /**
     * 注释：获取设备屏宽比
     */
    private AspectRatio getDeviceAspectRatio(Activity activity) {
        int width = activity.getWindow().getDecorView().getWidth();
        int height = activity.getWindow().getDecorView().getHeight();
        return AspectRatio.of(Math.min(width, height), Math.max(width, height));
    }

    /**
     * 注释：获取摄像头应该显示的方向
     * 时间：2020/8/7 0007 15:10
     *
     * @return
     */
    private int getDisplayOrientation() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
        int orientation;
        int degrees = 0;
        if (mDisplayOrientation == Surface.ROTATION_0) {
            degrees = 0;
        } else if (mDisplayOrientation == Surface.ROTATION_90) {
            degrees = 90;
        } else if (mDisplayOrientation == Surface.ROTATION_180) {
            degrees = 180;
        } else if (mDisplayOrientation == Surface.ROTATION_270) {
            degrees = 270;
        }
        orientation = (degrees + 45) / 90 * 90;
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation - orientation + 360) % 360;
        } else {
            // back-facing
            result = (info.orientation + orientation) % 360;
        }
        return result;
    }

}
