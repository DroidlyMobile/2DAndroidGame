package misterdneh.ca.myandroidgame;

import android.app.Presentation;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TopScreen extends Presentation {

    public GameView gameView;
    public RelativeLayout gameviewtop;

    public TopScreen(Context outerContext, Display display) {
        super(outerContext, display);
        //gameView = new GameView(outerContext,getDisplay().getWidth(),getDisplay().getHeight());
        /*gameView.screenWidth = display.getWidth();
        gameView.screenHeight = display.getHeight();*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout_top);
        gameviewtop = findViewById(R.id.gamelayouttop);
        gameviewtop.addView(gameView);
        fullscreen();
    }
    public void fullscreen() {
        View decorView = this.getWindow().getDecorView();
        View View = null;
        int uiOptions = android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | Window.FEATURE_NO_TITLE | android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        decorView.setSystemUiVisibility(uiOptions);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
    }
}