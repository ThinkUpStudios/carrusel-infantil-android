package thinkup.com.carruselinfantil;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Presentacion extends Activity {

    //PARA EL CARRUSEL
    private ImageSwitcher imageSwitcher;

    private List<Uri> gallery;

    private int position;

    private static final Integer DURATION = 2500;

    private Timer timer = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presentacion);

        gallery = (List<Uri>) getIntent().getSerializableExtra("CARRUSEL_SELECCIONADO");
        imageSwitcher = (ImageSwitcher) findViewById(R.id.carrusel_seleccionado_presentacion);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                ImageView imageView = new ImageView(Presentacion.this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                ViewGroup.LayoutParams params = new ImageSwitcher.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                imageView.setLayoutParams(params);
                return imageView;
            }
        });


        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        imageSwitcher.setInAnimation(fadeIn);
        imageSwitcher.setOutAnimation(fadeOut);

        this.start();

    }

    public void start() {
        if(gallery!=null) {
            if (timer != null) {
                timer.cancel();
            }
            position = 0;
            startSlider();
        }

    }

    public void startSlider() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                // avoid exception:
                // "Only the original thread that created a view hierarchy can touch its views"
                runOnUiThread(new Runnable() {
                    public void run() {
                        imageSwitcher.setImageURI(gallery.get(position));
                        position++;
                        if (position == gallery.size()) {
                            position = 0;
                        }
                    }
                });
            }

        }, 0, DURATION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null) {
            startSlider();
        }

    }



}
