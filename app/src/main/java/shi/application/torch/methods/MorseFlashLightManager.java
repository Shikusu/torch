package shi.application.torch.methods;

import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

public class MorseFlashLightManager {

    private final CameraManager cameraManager;
    private String cameraId;
    // 1 Unit = 250ms
    private static final int UNIT_TIME = 250;

    public MorseFlashLightManager(Context context) {
        cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (Exception e) {
            Log.e("Error", "Description",e);
        }
    }

    // Pass a callback runnable into the method
    public synchronized void startSOS(Runnable onSequenceComplete) {
        if (cameraId == null) return;

        HandlerThread handlerThread = new HandlerThread("FlashlightThread");
        handlerThread.start();
        Handler backgroundHandler = new Handler(handlerThread.getLooper());

        backgroundHandler.post(() -> {
            try {
                flash(1); flash(1); flash(1);
                Thread.sleep(UNIT_TIME * 2);
                flash(3); flash(3); flash(3);
                Thread.sleep(UNIT_TIME * 2);
                flash(1); flash(1); flash(1);
                Thread.sleep(UNIT_TIME * 7);
            } catch (InterruptedException e) {
                // Thread stopped intentionally
            } finally {
                turnOffFlashlight();

                if (onSequenceComplete != null) {
                    new Handler(Looper.getMainLooper()).post(onSequenceComplete);
                }

                // Clean up the thread loop safely
                handlerThread.quitSafely();
            }
        });
    }

    private void flash(int units) throws InterruptedException {
        setFlashlight(true);
        Thread.sleep((long) UNIT_TIME * units);
        setFlashlight(false);
        Thread.sleep(UNIT_TIME);
    }

    private void setFlashlight(boolean enabled) {
        try {
            cameraManager.setTorchMode(cameraId, enabled);
        } catch (Exception e) {
            Log.e("Error", "Description",e);
        }
    }

    private void turnOffFlashlight() {
        setFlashlight(false);
    }
}