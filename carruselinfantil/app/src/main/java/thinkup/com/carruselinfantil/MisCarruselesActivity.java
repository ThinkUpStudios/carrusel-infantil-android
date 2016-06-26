package thinkup.com.carruselinfantil;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import thinkup.com.carruselinfantil.modelo.Carrusel;

public class MisCarruselesActivity extends DrawerAbstractActivity {

    private static final int REQUEST_CARRUSELES = 2;

    private List<Carrusel> carruseles;


    TextView misCarruseles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.carruseles = (List<Carrusel>) getIntent().getSerializableExtra(ConstantesAplicacion.CARRUSELES);

        misCarruseles = (TextView) findViewById(R.id.textoMisCarruseles);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_nuevo_carrusel);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MisCarruselesActivity.this, NuevoCarruselActivity.class);
                i.putExtra(ConstantesAplicacion.CARRUSELES, (Serializable) carruseles);
                startActivity(i);
            }
        });

        ImageView carrusel = (ImageView) findViewById(R.id.un_carrusel);
        if(this.carruseles != null)
            carrusel.setImageURI(this.carruseles.get(0).getGaleria().get(0).getUri());
        else{
            this.carruseles = new ArrayList<Carrusel>();
        }
        carrusel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MisCarruselesActivity.this, Presentacion.class);
                i.putExtra(ConstantesAplicacion.CARRUSEL, (Serializable) carruseles.get(0).getGaleria());
                startActivity(i);
            }
        });
    }

    public int getContentView(){
        return R.layout.mis_carruseles_drawer;
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_CARRUSELES:
                    carruseles = (List<Carrusel>) data.getSerializableExtra(ConstantesAplicacion.CARRUSELES);
                    break;
            }
        }
    }
}
