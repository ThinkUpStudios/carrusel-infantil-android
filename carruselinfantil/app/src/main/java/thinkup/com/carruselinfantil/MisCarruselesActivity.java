package thinkup.com.carruselinfantil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.io.Serializable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MisCarruselesActivity extends DrawerAbstractActivity {

    //PARA EL CARRUSEL
    private ImageSwitcher imageSwitcher;

    private List<Uri> gallery;

    private int position;

    private static final Integer DURATION = 2500;

    private Timer timer = null;

    TextView misCarruseles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gallery = (List<Uri>) getIntent().getSerializableExtra("CARRUSEL_SELECCIONADO");

        misCarruseles = (TextView) findViewById(R.id.textoMisCarruseles);


        //PARA EL CARRUSEL
        imageSwitcher = (ImageSwitcher) findViewById(R.id.carrusel_seleccionado);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                return new ImageView(MisCarruselesActivity.this);
            }
        });


        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        imageSwitcher.setInAnimation(fadeIn);
        imageSwitcher.setOutAnimation(fadeOut);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_nuevo_carrusel);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MisCarruselesActivity.this, NuevoCarruselActivity.class);
                startActivity(i);
            }
        });

        ImageView carrusel = (ImageView) findViewById(R.id.un_carrusel);
        if(gallery != null)
            carrusel.setImageURI(gallery.get(0));
        carrusel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MisCarruselesActivity.this, PresentacionActivity.class);
                i.putExtra("CARRUSEL_SELECCIONADO", (Serializable) gallery);
                startActivity(i);
            }
        });
    }

    public int getContentView(){
        return R.layout.mis_carruseles_drawer;
    }


    /**
     * starts or restarts the slider
     *
     * @param button
     */
    public void start(View button) {
        if(gallery!=null) {
            if (timer != null) {
                timer.cancel();
            }
            position = 0;
            startSlider();
        }
        else {
            misCarruseles.setText("Aún no ha seleccionado un carrusel.");
        }
    }

    public void stop(View button) {
        if (timer != null) {
            timer.cancel();
            timer = null;
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

    // Stops the slider when the Activity is going into the background
    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null) {
            startSlider();
        }

    }
}
