package com.example.opencvandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static String TAG = "MainActivity";
    JavaCameraView javaCameraView;
    Mat mRGBA,mRGBAT;

    BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(MainActivity.this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status)
            {
                case BaseLoaderCallback.SUCCESS:
                {
                    javaCameraView.enableView();
                    break;
                }
                default:
                {
                    super.onManagerConnected(status);
                    break;
                }
            }

        }
    };

    static {
      /*  if (OpenCVLoader.initDebug())
        {
            Log.d(TAG,"Open CV is Configured or Connected Successfully");
        }
        else
        {
            Log.d(TAG,"Open CV is not working or loaded");
        }

       */
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        javaCameraView = (JavaCameraView)findViewById(R.id.my_camera_view);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(MainActivity.this);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
          mRGBA = new Mat(height,width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
       mRGBA.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRGBA = inputFrame.rgba();
        mRGBAT = mRGBA.t();
        Core.flip(mRGBA.t(),mRGBAT,1);
        Imgproc.resize(mRGBAT,mRGBAT,mRGBA.size());
        return mRGBAT;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (javaCameraView != null)
        {
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (javaCameraView != null)
        {
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug())
        {
            Log.d(TAG,"Open CV is Configured or Connected Successfully");
            baseLoaderCallback.onManagerConnected(BaseLoaderCallback.SUCCESS);
        }
        else
        {
            Log.d(TAG,"Open CV is not working or loaded");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION,this,baseLoaderCallback);
        }

    }
}
