package shi.application.torch;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    FrameLayout on_off_button;

    Drawable on_bg, off_bg;



     Boolean isFlashOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        String cameraId;
        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }

        on_off_button = findViewById(R.id.masterDial);
        on_bg= ContextCompat.getDrawable(this,R.drawable.bg_dial_on);
        off_bg=ContextCompat.getDrawable(this,R.drawable.bg_dial_off);
        String finalCameraId = cameraId;
        on_off_button.setOnClickListener(v->{

            try {
                isFlashOn = !isFlashOn;

                cameraManager.setTorchMode(finalCameraId, isFlashOn);

                on_off_button.setBackground((isFlashOn?on_bg:off_bg));

            } catch (Exception e) {
                Log.d("ERRORING", "error");
            }
        });

    }
}