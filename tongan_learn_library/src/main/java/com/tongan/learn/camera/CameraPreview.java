package com.tongan.learn.camera;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 * 2019-5-23
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Context context;

    public CameraPreview(Context context) {
        super(context);
        this.context = context;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mHolder.setFormat(PixelFormat.TRANSPARENT);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        CameraInterface.getInstance().initCamera(DisplayUtils.getScreenRate(context));
        startPreview(holder);
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

}
