package cobe.com.bejbikjum.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.*;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import butterknife.OnClick;
import cobe.com.bejbikjum.R;
import cobe.com.bejbikjum.controlers.AppControler;

public class TakePhotoActivity extends AppCompatActivity {

    private TextureView cameraPreview;
    private RelativeLayout overlay;
    private static final int IMAGE_SIZE = 1024;
    private static final int IMAGE_ORIENTATION = 90;
    private static final int CAMERA_REQUEST_CODE = 1888;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static final int CAMERA_PERMISSION_CODE = 2;

    private CameraManager cameraManager;
    private int cameraFacing;
    private TextureView.SurfaceTextureListener surfaceTextureListener;
    private Size previewSize;
    private String cameraId;
    private HandlerThread backgroundThread;
    private Handler backgroundHandler;
    private CameraDevice.StateCallback stateCallback;
    private CameraDevice camera;
    private CaptureRequest.Builder captureRequestBuilder;
    private CaptureRequest captureRequest;
    private CameraCaptureSession cameraCaptureSession;
    private File galleryFolder;
    private Button takePhotoButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Optional: Hide the status bar at the top of the window
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set the content view and get references to our views
        setContentView(R.layout.activity_take_photo);

        requestWritePermission();


        cameraPreview = (TextureView) findViewById(R.id.camera_preview);
        overlay = (RelativeLayout) findViewById(R.id.overlay);

//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);

//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);




        takePhotoButton = (Button) findViewById(R.id.take_photo_button);
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("photo", "Photo taken");
                lock();

                //Koristimo za upis na disk
                FileOutputStream outputPhoto = null;
                ByteArrayOutputStream stream = null;
                Bitmap cropped = null;
                try {
                    //Koristimo za upis na disk
                    Log.d("TakePhotoActivity", "Gallery folder exists: " +
                            AppControler.getInstance().getGalleryFolder().exists() +
                            ", " + AppControler.getInstance().getGalleryFolder().isDirectory());
                    outputPhoto = new FileOutputStream(createImageFile(AppControler.getInstance().getGalleryFolder()));
                    Bitmap bitmap = cameraPreview.getBitmap();
                    stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();


                    //Koristimo za upis na disk
                    //bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputPhoto);

                    cropped = cropImage(byteArray);
                    cropped.compress(Bitmap.CompressFormat.PNG, 100, outputPhoto);

                    bitmap.recycle();

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    unlock();
                    //Koristimo za upis na disk
                        if (outputPhoto != null) {
                            try {
                                outputPhoto.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                }
            }
        });


    }

    public void oldCameraOnCreate(){

    }

    public void newCameraOnCreate(){
        if(Build.VERSION.SDK_INT >= 21) {
            cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            cameraFacing = CameraCharacteristics.LENS_FACING_BACK;

            surfaceTextureListener = new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
                    setUpCamera();
                    openCamera();
                }

                @Override
                public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {

                }

                @Override
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                    return false;
                }

                @Override
                public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

                }
            };


            stateCallback = new CameraDevice.StateCallback() {
                @Override
                public void onOpened(CameraDevice camera) {
                    TakePhotoActivity.this.camera = camera;
                    createPreviewSession();
                }

                @Override
                public void onDisconnected(CameraDevice camera) {
                    if(Build.VERSION.SDK_INT >= 21) {
                        camera.close();
                    }
                    TakePhotoActivity.this.camera = null;
                }

                @Override
                public void onError(CameraDevice camera, int error) {
                    if(Build.VERSION.SDK_INT >= 21) {
                        camera.close();
                    }
                    TakePhotoActivity.this.camera = null;
                }
            };
        }
    }

    public void requestWritePermission(){
        if (ContextCompat.checkSelfPermission(TakePhotoActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(TakePhotoActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.d("TakePhotoActivity", "Should show request permission window - WRITE_EXTERNAL");
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(TakePhotoActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        if (ContextCompat.checkSelfPermission(TakePhotoActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(TakePhotoActivity.this,
                    Manifest.permission.CAMERA)) {
                Log.d("TakePhotoActivity", "Should show request permission window - Camera");
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(TakePhotoActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION_CODE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                Log.d("TakePhotoActivity", "onRequestPermissionsResult - WRITE");
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d("TakePhotoActivity", "Write permission granted, creating image gallery.");
                    createImageGallery();

                } else {
                    Log.d("TakePhotoActivity", "Write permission denied");

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;

            }

            case CAMERA_PERMISSION_CODE: {
                Log.d("TakePhotoActivity", "onRequestPermissionsResult - CAMERA");

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d("TakePhotoActivity", "Camera permission granted, creating image gallery.");

                    createImageGallery();
                    newCameraOnCreate();

                } else {
                    Log.d("TakePhotoActivity", "Camera permission denied");

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // Get the preview size
        int previewWidth = cameraPreview.getMeasuredWidth(),
                previewHeight = cameraPreview.getMeasuredHeight();

        // Set the height of the overlay so that it makes the preview a square
        RelativeLayout.LayoutParams overlayParams = (RelativeLayout.LayoutParams) overlay.getLayoutParams();
        overlayParams.height = previewHeight - previewWidth;
        overlay.setLayoutParams(overlayParams);
    }

    private void setUpCamera() {

        if(Build.VERSION.SDK_INT >= 21) {

            try {
                for (String cameraId : cameraManager.getCameraIdList()) {
                    CameraCharacteristics cameraCharacteristics =
                            cameraManager.getCameraCharacteristics(cameraId);
                    if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) ==
                            cameraFacing) {
                        StreamConfigurationMap streamConfigurationMap = cameraCharacteristics.get(
                                CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                        //Preview size may be wrong and streched on Nexus and some others, so
                        //we use the other method, and comment this one
                        //previewSize = streamConfigurationMap.getOutputSizes(SurfaceTexture.class)[0];

                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        int height = displayMetrics.heightPixels;
                        int width = displayMetrics.widthPixels;

                        previewSize = chooseOptimalSize(streamConfigurationMap.getOutputSizes(SurfaceTexture.class),
                                width, height);
                        this.cameraId = cameraId;
                    }
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }

        }
    }

    private Size chooseOptimalSize(Size[] outputSizes, int width, int height) {
        if(Build.VERSION.SDK_INT >= 21) {
            double preferredRatio = height / (double) width;
            Size currentOptimalSize = outputSizes[0];
            double currentOptimalRatio = currentOptimalSize.getWidth() / (double) currentOptimalSize.getHeight();
            for (Size currentSize : outputSizes) {
                double currentRatio = currentSize.getWidth() / (double) currentSize.getHeight();
                if (Math.abs(preferredRatio - currentRatio) <
                        Math.abs(preferredRatio - currentOptimalRatio)) {
                    currentOptimalSize = currentSize;
                    currentOptimalRatio = currentRatio;
                }
            }

            return currentOptimalSize;
        }
        else return null;

    }

    private void openCamera() {
        if(Build.VERSION.SDK_INT >= 21) {
            try {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    cameraManager.openCamera(cameraId, stateCallback, backgroundHandler);
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void openBackgroundThread() {
        backgroundThread = new HandlerThread("camera_background_thread");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }


    private Bitmap cropImage(byte[] data) throws IOException {

        int width = 0;
        int height = 0;

        // Determine the width/height of the image
        if(Build.VERSION.SDK_INT >= 21) {
            width = previewSize.getWidth();
            height = previewSize.getHeight();
        }

        // Load the bitmap from the byte array
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);

        // Rotate and crop the image into a square
        int croppedWidth = (width > height) ? height : width;
        int croppedHeight = (width > height) ? height : width;

        Matrix matrix = new Matrix();
        matrix.postRotate(IMAGE_ORIENTATION);
        Bitmap cropped = Bitmap.createBitmap(bitmap, 0, 0, croppedWidth, croppedHeight, matrix, true);
        bitmap.recycle();

        // Scale down to the output size
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(cropped, IMAGE_SIZE, IMAGE_SIZE, true);
        cropped.recycle();

        return scaledBitmap;
    }


    @Override
    protected void onResume() {
        super.onResume();
        openBackgroundThread();
        if (cameraPreview.isAvailable()) {
            setUpCamera();
            openCamera();
        } else {
            cameraPreview.setSurfaceTextureListener(surfaceTextureListener);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        closeCamera();
        closeBackgroundThread();
    }

    private void closeCamera() {
        if(Build.VERSION.SDK_INT >= 21) {
            if (cameraCaptureSession != null) {
                cameraCaptureSession.close();
                cameraCaptureSession = null;
            }

            if (camera != null) {
                camera.close();
                camera = null;
            }
        }
    }

    private void closeBackgroundThread() {
        if(Build.VERSION.SDK_INT >= 21) {
            if (backgroundHandler != null) {
                backgroundThread.quitSafely();
                backgroundThread = null;
                backgroundHandler = null;
            }
        }
    }


    private void createPreviewSession() {
        if(Build.VERSION.SDK_INT >= 21) {
            try {
                SurfaceTexture surfaceTexture = cameraPreview.getSurfaceTexture();
                surfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
                Surface previewSurface = new Surface(surfaceTexture);
                captureRequestBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                captureRequestBuilder.addTarget(previewSurface);

                camera.createCaptureSession(Collections.singletonList(previewSurface),
                        new CameraCaptureSession.StateCallback() {

                            @Override
                            public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                                if (camera == null) {
                                    return;
                                }
                                if(Build.VERSION.SDK_INT >= 21) {
                                    try {
                                        captureRequest = captureRequestBuilder.build();
                                        TakePhotoActivity.this.cameraCaptureSession = cameraCaptureSession;
                                        TakePhotoActivity.this.cameraCaptureSession.setRepeatingRequest(captureRequest,
                                                null, backgroundHandler);
                                    } catch (CameraAccessException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {

                            }
                        }, backgroundHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void createImageGallery() {
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        galleryFolder = new File(storageDirectory, getResources().getString(R.string.app_name));

        if (!galleryFolder.exists()) {

            galleryFolder.mkdirs();
        }

        AppControler.getInstance().setGalleryFolder(galleryFolder);

        Log.d("photo", "File path: " + AppControler.getInstance().getGalleryFolder().getAbsolutePath());
    }


    private File createImageFile(File galleryFolder) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "image_" + timeStamp + "_";
        return File.createTempFile(imageFileName, ".jpg", galleryFolder);
    }



    private void lock() {
        if(Build.VERSION.SDK_INT >= 21) {
            try {
                cameraCaptureSession.capture(captureRequestBuilder.build(),
                        null, backgroundHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void unlock() {
        if(Build.VERSION.SDK_INT >= 21) {
            try {
                cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(),
                        null, backgroundHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }
}



