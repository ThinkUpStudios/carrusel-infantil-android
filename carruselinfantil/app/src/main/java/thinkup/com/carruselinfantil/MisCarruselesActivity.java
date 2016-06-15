package thinkup.com.carruselinfantil;

import android.os.Bundle;
import android.widget.TextView;

public class MisCarruselesActivity extends DrawerAbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView misCarruseles = (TextView) findViewById(R.id.textoMisCarruseles);
        misCarruseles.setText("Estoy en Mis Carruseles");
    }

    public int getContentView(){
        return R.layout.mis_carruseles_drawer;
    }
}
