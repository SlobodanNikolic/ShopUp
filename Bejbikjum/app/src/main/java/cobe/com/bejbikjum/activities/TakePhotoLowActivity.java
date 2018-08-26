package cobe.com.bejbikjum.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cobe.com.bejbikjum.R;
import cobe.com.bejbikjum.helpers.CameraPreview;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class TakePhotoLowActivity extends AppCompatActivity {

    private CameraPreview cameraPreview;
    private RelativeLayout overlay;
    private final int IMAGE_SIZE = 1024;
    private final int IMAGE_ORIENTATION = 90;

    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_take_photo_low);

        overlay = (RelativeLayout) findViewById(R.id.overlay);

        initializeCamera();

        cameraPreview = new CameraPreview(this, camera);

        RelativeLayout parentLayout = (RelativeLayout) findViewById(R.id.parent_layout);
        parentLayout.addView(cameraPreview);

        RelativeLayout.LayoutParams parentLayoutParams = (RelativeLayout.LayoutParams) parentLayout.getLayoutParams();
        cameraPreview.setLayoutParams(parentLayoutParams);


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

    public void initializeCamera(){

        camera = getCameraInstance();
        if(camera == null){
            Log.d("TakePhotoLowActivity", "Camera currently unavailable");
            return;
        }
        Camera.Parameters camParams = camera.getParameters();

// Find a preview size that is at least the size of our IMAGE_SIZE
        Camera.Size previewSize = camParams.getSupportedPreviewSizes().get(0);
        for (Camera.Size size : camParams.getSupportedPreviewSizes()) {
            if (size.width >= IMAGE_SIZE && size.height >= IMAGE_SIZE) {
                previewSize = size;
                break;
            }
        }
        camParams.setPreviewSize(previewSize.width, previewSize.height);

// Try to find the closest picture size to match the preview size.
        Camera.Size pictureSize = camParams.getSupportedPictureSizes().get(0);
        for (Camera.Size size : camParams.getSupportedPictureSizes()) {
            if (size.width == previewSize.width && size.height == previewSize.height) {
                pictureSize = size;
                break;
            }
        }
        camParams.setPictureSize(pictureSize.width, pictureSize.height);
    }

    public void startPreview(){
        try {
            camera.setPreviewDisplay(cameraPreview.getHolder());
            camera.startPreview();
        } catch (IOException e) {
            Log.d("TakePhotoLowActivity", "Unable to start camera preview");
            e.printStackTrace();
        }
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private Bitmap processImage(byte[] data) throws IOException {
        // Determine the width/height of the image
        int width = camera.getParameters().getPictureSize().width;
        int height = camera.getParameters().getPictureSize().height;

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

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);

            if (pictureFile == null){
                Log.d("TakePhotoLowActivity", "Error creating media file, check storage permissions: ");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d("TakePhotoLowActivity", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("TakePhotoLowActivity", "Error accessing file: " + e.getMessage());
            }
        }
    };


    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "BejbikjumTakePhotoLow");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
}
