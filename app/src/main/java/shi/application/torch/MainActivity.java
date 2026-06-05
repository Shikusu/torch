package shi.application.torch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;

import shi.application.torch.methods.FrontScreenSettings;
import shi.application.torch.methods.MorseFlashLightManager;

public class MainActivity extends AppCompatActivity {


    FrameLayout on_off_button,sos_button;

    Drawable on_bg, off_bg;

    TextView btnBack, btnFront;

    View thumb;

    Boolean isFlash = true;

    Boolean isFlashOn = false;
    Boolean isScreenBright = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        on_off_button = findViewById(R.id.masterDial);
        sos_button = findViewById(R.id.sosButton);
        thumb = findViewById(R.id.selectorHighlight);
        btnBack = findViewById(R.id.btnBack);
        btnFront = findViewById(R.id.btnFront);

        MorseFlashLightManager morseManager = new MorseFlashLightManager(this);

        on_bg = ContextCompat.getDrawable(this, R.drawable.bg_dial_on);
        off_bg = ContextCompat.getDrawable(this, R.drawable.bg_dial_off);
        int colorOn = Color.parseColor("#00E5FF");
        int colorOff = Color.parseColor("#80FFFFFF");
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        String cameraId;
        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }
        String finalCameraId = cameraId;

        btnFront.setOnClickListener(v->{
            float place=thumb.getWidth();
            thumb.setX(place);
            TextViewCompat.setCompoundDrawableTintList(btnBack, ColorStateList.valueOf(colorOff));
            TextViewCompat.setCompoundDrawableTintList(btnFront, ColorStateList.valueOf(colorOn));

        });

        btnBack.setOnClickListener(v->{
            float place=0f;
            thumb.setX(place);
            TextViewCompat.setCompoundDrawableTintList(btnBack, ColorStateList.valueOf(colorOn));

            TextViewCompat.setCompoundDrawableTintList(btnFront, ColorStateList.valueOf(colorOff));

        });

        thumb.setOnTouchListener(new View.OnTouchListener() {

            float dX;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                float minX = 0f;
                float maxX = view.getWidth();

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        if (!isFlashOn && !isScreenBright) {
                            dX = view.getX() - event.getRawX();
                            return true;
                        }
                        return true;

                    case MotionEvent.ACTION_MOVE:

                        if (!isFlashOn && !isScreenBright) {
                            float newX = event.getRawX() + dX;
                            float clampedX = Math.max(minX, Math.min(newX, maxX));
                            view.setX(clampedX);
                            return true;
                        }
                        return true;

                    case MotionEvent.ACTION_UP:

                        if (!isFlashOn && !isScreenBright) {
                            float midpoint = (minX + maxX) / 2f;
                            float targetX = (view.getX() < midpoint) ? minX : maxX;
                            isFlash = (targetX == minX);
                            TextViewCompat.setCompoundDrawableTintList(btnBack, ColorStateList.valueOf((targetX == minX) ? colorOn : colorOff));

                            TextViewCompat.setCompoundDrawableTintList(btnFront, ColorStateList.valueOf((targetX == minX) ? colorOff : colorOn));

                            view.animate()
                                    .x(targetX)
                                    .setDuration(200)
                                    .start();
                            return true;
                        }
                        return true;
                }

                return false;
            }
        });

        on_off_button.setOnClickListener(v -> {
            try {
                if (isFlash) {
                    isFlashOn = !isFlashOn;

                    cameraManager.setTorchMode(finalCameraId, isFlashOn);

                    on_off_button.setBackground((isFlashOn ? on_bg : off_bg));
                }else{
                    isScreenBright =!isScreenBright;
                    if(isScreenBright){
                        FrontScreenSettings.enableScreenFlash(MainActivity.this);
                        on_off_button.setBackground(on_bg);
                    }else{
                        FrontScreenSettings.disableScreenFlash(MainActivity.this);
                        on_off_button.setBackground(off_bg);
                    }

                }


            } catch (Exception e) {
                Log.d("ERRORING", "error in on_off_button");
            }
        });

        sos_button.setOnClickListener(v->{
            if(isFlash){
                if(isFlashOn){
                    Toast.makeText(this, "Eteignez d'abord le Flash", Toast.LENGTH_SHORT).show();
                }else{
                    morseManager.startSOS();
                }
            }else{
                Toast.makeText(this, "Seulement possible via Flash", Toast.LENGTH_SHORT).show();
            }
        });

    }
}