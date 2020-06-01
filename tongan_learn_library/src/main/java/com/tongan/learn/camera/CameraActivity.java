package com.tongan.learn.camera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tongan.learn.R;
import com.tongan.learn.TaConstant;
import com.tongan.learn.network.CallBackString;
import com.tongan.learn.network.HttpUtil;
import com.tongan.learn.util.BitmapUtils;

import org.json.JSONObject;

import java.io.File;

import static com.tongan.learn.TaConstant.cameraTipsColor;
import static com.tongan.learn.TaConstant.themColor;
import static com.tongan.learn.util.BitmapUtils.CUT_START_Y;


public class CameraActivity extends Activity implements CameraInterface.CameraListener {
    private static String STATUS_OK = "ok";
    private static final String STATS_KEY = "status";
    private static final String UP_LOAD_URL = "https://mb.anjia365.com/m/home/my/facerecog/submit";
    //    private static final String MENG_UP_LOAD_URL = "http://192.168.3.93/m/home/my/facerecog/submit";
//    private static final String WANG_UP_LOAD_URL = "http://192.168.3.205/m/home/my/facerecog/submit";
    private AlertDialog dialog;
    private final int PERMISSION_REQUEST_CODE_CAMERA = 0x02;
    private final int PERMISSION_REQUEST_CODE_STORAGE = 0x03;
    private FrameLayout preview;
    private CameraPreview mSurfaceView;
    private OrientationEventListener mOrientationListener;
    private int cameraOrientation = 0;
    private int picQuality;

    private int pictureSize;
    private LinearLayout tongAnTakePhoto;
    private LinearLayout tongAnCameraReplay;
    private LinearLayout tongAnCameraCommit;
    private LinearLayout tongAnLearnLayoutResult;
    private RelativeLayout cameraArea;
    private int screenWith;
    private int screenHeight;
    private TextView tongAnCameraTipsTitle;
    private ProgressBar progressCircular;
    private File file;
    private String type;
    private String clazzId;
    private LinearLayout progressLayout;
    private ImageView cameraFrameImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//拍照过程屏幕一直处于高亮
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tong_an_activity_camera);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }

    private void init() {
        getIntentData();
        getScreenData();
        initView();
        initEvent();
        checkPermission();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        type = intent.getStringExtra(TaConstant.TYPE);
        clazzId = intent.getStringExtra(TaConstant.CAZZ_ID);
    }

    private void getScreenData() {

        Point point = DisplayUtils.getScreenMetrics(this);
        picQuality = CameraParaUtil.defaultPicQuality;
        screenWith = point.x;
        screenHeight = point.y;
        pictureSize = 200;

    }

    private void initView() {
        progressLayout = findViewById(R.id.progress_layout);
        preview = findViewById(R.id.camera_preview);
        tongAnTakePhoto = findViewById(R.id.tong_an_take_photo);
        tongAnCameraReplay = findViewById(R.id.tong_an_camera_replay);
        tongAnCameraCommit = findViewById(R.id.tong_an_camera_commit);
        tongAnLearnLayoutResult = findViewById(R.id.tong_an_learn_layout_result);
        cameraArea = findViewById(R.id.camera_area);
        cameraFrameImg = findViewById(R.id.camera_frame_img);
        progressCircular = findViewById(R.id.progress_circular);
        tongAnCameraTipsTitle = findViewById(R.id.tong_an_camera_tips_title);
        initThemColor();
        initViewSize();
        initTips();

    }

    private void initThemColor() {
        progressCircular.getIndeterminateDrawable().setColorFilter(themColor, PorterDuff.Mode.MULTIPLY);
        setShapBackColor((GradientDrawable) tongAnTakePhoto.getBackground());
        setShapBackColor((GradientDrawable) tongAnCameraCommit.getBackground());
    }

    private void setShapBackColor(GradientDrawable drawable) {
        drawable.mutate();
        drawable.setColor(themColor);
    }

    private void initTips() {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(this.getString(R.string.tong_an_learn_camera_tips_title));


        spannableStringBuilder.setSpan(new ForegroundColorSpan(cameraTipsColor), 1, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tongAnCameraTipsTitle.setText(spannableStringBuilder);

    }

    /**
     * init  view and  cut photo size
     */
    private void initViewSize() {
        ViewGroup.LayoutParams cameraFrameImgParam = cameraFrameImg.getLayoutParams();
        ViewGroup.LayoutParams layoutParams = cameraArea.getLayoutParams();
        if (600 <= screenWith && screenWith < 1080) {
            layoutParams.height = screenHeight / 2;
            layoutParams.width = screenWith;
        } else if (screenWith < 600) {
            layoutParams.height = 350;
            layoutParams.width = screenWith;
            cameraFrameImgParam.width = BitmapUtils.dp2px(CameraActivity.this, 180);
            cameraFrameImgParam.height = BitmapUtils.dp2px(CameraActivity.this, 180);
            cameraFrameImg.setLayoutParams(cameraFrameImgParam);
        } else {
            layoutParams.height = screenWith;
            layoutParams.width = screenWith;
        }

        cameraArea.setLayoutParams(layoutParams);
        BitmapUtils.CUT_WITH = screenWith;
        BitmapUtils.CUT_HEIGHT = screenWith;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initCameraView() {
        CameraInterface.getInstance().setScreenWith(screenWith);
        CameraInterface.getInstance().setScreenHeight(screenHeight);
        CameraInterface.getInstance().setCameraListener(this);
        mSurfaceView = new CameraPreview(this);
        preview.addView(mSurfaceView);
        cameraArea.post(new Runnable() {
            @Override
            public void run() {
                int[] point = new int[2];
                cameraArea.getLocationInWindow(point);
                CUT_START_Y = point[1];
            }
        });
    }

    boolean isTakingPhoto = false;

    private void initEvent() {
        //拍照
        tongAnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTakingPhoto) {
                    isTakingPhoto = true;
                    CameraInterface.getInstance().takePicture();
                }
            }
        });
        tongAnCameraReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartCamera();
            }
        });
        tongAnCameraCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (file.exists() && !TextUtils.isEmpty(type) && !TextUtils.isEmpty(clazzId)) {
                    progressLayout.setVisibility(View.VISIBLE);
                    String url = UP_LOAD_URL + "/" + clazzId + "/" + type;
                    HttpUtil.uploadFile(url, file, file.getName(), HttpUtil.FILE_TYPE_IMAGE, new CallBackString() {
                        @Override
                        protected void onFailure(int code, String errorMessage) {
                            showDialog();
                            progressLayout.setVisibility(View.GONE);
                        }

                        @Override
                        protected void onResponse(String obj) {
                            progressLayout.setVisibility(View.GONE);
                            try {
                                JSONObject jsonObject = new JSONObject(obj);
                                String statusKey = jsonObject.optString(STATS_KEY);
                                if (STATUS_OK.equals(statusKey)) {
                                    setDataAndFinish(true);
                                } else {
                                    showDialog();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    });
                }
            }
        });
        //
        mOrientationListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                cameraOrientation = orientation;
            }
        };
    }

    private void restartCamera() {
        CameraInterface.getInstance().getCamera().startPreview();
        tongAnTakePhoto.setVisibility(View.VISIBLE);
        tongAnLearnLayoutResult.setVisibility(View.GONE);
    }


    private void setDataAndFinish(boolean isSuccess) {

        setResult(TaConstant.STUDY_ACTIVITY_CODE, new Intent().putExtra(TaConstant.FACE_IS_SUCCESS, isSuccess));
        this.finish();

    }


    private void checkPermission() {
        XPermissionUtils.requestPermissions(this, PERMISSION_REQUEST_CODE_STORAGE, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                new XPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        if (CameraInterface.getInstance().getCamera() == null) {
                            DialogUtil.showPermissionDeniedDialog(CameraActivity.this, "相机");
                        } else {
                            initCameraView();
                        }
                    }

                    @Override
                    public void onPermissionDenied(final String[] deniedPermissions, boolean alwaysDenied) {
                        if (alwaysDenied) {
                            DialogUtil.showPermissionDeniedDialog(CameraActivity.this, "文件存储");
                        } else {
                            DialogUtil.showPermissionRemindDiaog(CameraActivity.this, "文件存储", deniedPermissions, PERMISSION_REQUEST_CODE_CAMERA);
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mOrientationListener.enable();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mOrientationListener != null) {

            mOrientationListener.disable();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onTakePictureSuccess(File pictureFile) {
        isTakingPhoto = false;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapUtils.rotateBitmap(BitmapFactory.decodeFile(pictureFile.getPath(), options), CameraInterface.getInstance().getmCameraId(), cameraOrientation);
        if (pictureSize > 0) {
            bitmap = BitmapUtils.bitmapCompress(bitmap, 200);
        }
        BitmapUtils.saveBitmapToSd(bitmap, pictureFile.getPath(), picQuality);
        file = pictureFile;
        //更新本地相册
        Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(pictureFile));
        sendBroadcast(localIntent);
        CameraInterface.getInstance().getCamera().stopPreview();
        tongAnLearnLayoutResult.setVisibility(View.VISIBLE);
        tongAnTakePhoto.setVisibility(View.GONE);
    }

    @Override
    public void onTakePictureFail(byte[] data) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        XPermissionUtils.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void showDialog() {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(this).setMessage(R.string.tong_an_learn_camera_dialog_tips).setPositiveButton(R.string.tong_an_learn_camera_dialog_positive_button
                    , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            restartCamera();
                        }
                    }).setNegativeButton(R.string.tong_an_learn_camera_dialog_negative_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setDataAndFinish(false);
                }
            }).create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    CameraActivity.this.dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(CameraActivity.this, R.color.tong_an_learn_gray));
                    CameraActivity.this.dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(themColor);
                }
            });
        }
        dialog.show();

    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }


}
