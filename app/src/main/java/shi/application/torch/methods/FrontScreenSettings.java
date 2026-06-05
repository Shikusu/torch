package shi.application.torch.methods;

import android.app.Activity;
import android.graphics.Color;
import android.view.WindowManager;

public class FrontScreenSettings {
    public static void enableScreenFlash(Activity activity) {

        // Set screen brightness to maximum
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.screenBrightness = 1.0f;
        activity.getWindow().setAttributes(params);

        // Make screen white
        activity.getWindow().getDecorView().setBackgroundColor(Color.WHITE);
    }
    public static void disableScreenFlash(Activity activity) {

        // Restore system brightness
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.screenBrightness =
                WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        activity.getWindow().setAttributes(params);

        // Restore background color
        activity.getWindow().getDecorView().setBackgroundColor(Color.BLACK);
    }
}
