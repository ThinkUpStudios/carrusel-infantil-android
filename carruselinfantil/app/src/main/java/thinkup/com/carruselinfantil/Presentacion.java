package thinkup.com.carruselinfantil;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

    private static final int INSERT_ID = Menu.FIRST;

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
                return new ImageView(Presentacion.this);
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



    //Metodos para que la pantalla sea fullScreen
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0,"FullScreen");

        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case INSERT_ID:
                createNote();
        }
        return true;
    }

    private void createNote() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
