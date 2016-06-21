package thinkup.com.carruselinfantil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.List;


public class Details extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        /*
        Recibiendo el identificador de la imagen
         */
        Intent i = getIntent();
        int position = i.getIntExtra("position", -1);// -1 si no se encontró la referencia
        List<Uri> gallery = (List<Uri>) getIntent().getSerializableExtra("CARRUSEL_SELECCIONADO");

        ImageAdapter adapter = new ImageAdapter(this, gallery);

        /*
        Seteando el recurso en el ImageView
         */
        ImageView originalImage = (ImageView)findViewById(R.id.originalImage);
        originalImage.setImageURI(adapter.getThumbId(position));
    }

}
