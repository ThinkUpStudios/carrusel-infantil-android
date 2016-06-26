package thinkup.com.carruselinfantil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import java.util.Timer;
import java.util.TimerTask;

import thinkup.com.carruselinfantil.modelo.Carrusel;

public class Presentacion extends Activity {

    //PARA EL CARRUSEL
    private ImageSwitcher imageSwitcher;

    private int position;

    private static final Integer DURATION = 2500;

    private Timer timer = null;

    private Carrusel carrusel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presentacion);

        carrusel = (Carrusel) getIntent().getSerializableExtra(ConstantesAplicacion.CARRUSEL);
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
        if(this.carrusel.getGaleria()!=null) {
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
                        imageSwitcher.setImageURI(carrusel.getGaleria().get(position).getUri());
                        position++;
                        if (position == carrusel.getGaleria().size()) {
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
