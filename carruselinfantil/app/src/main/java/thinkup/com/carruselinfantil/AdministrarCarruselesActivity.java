package thinkup.com.carruselinfantil;

import android.os.Bundle;
import android.widget.TextView;

public class AdministrarCarruselesActivity extends DrawerAbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        TextView misCarruseles = (TextView) findViewById(R.id.textoMisCarruseles);
        misCarruseles.setText("Estoy en administrar");
    }

    public int getContentView(){
        return R.layout.administrar_carruseles_drawer;
    }
}
