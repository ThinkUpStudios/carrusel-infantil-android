package thinkup.com.carruselinfantil;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import thinkup.com.carruselinfantil.adapters.CarruselAdapter;
import thinkup.com.carruselinfantil.adapters.CarruselesAdapter;
import thinkup.com.carruselinfantil.modelo.Carrusel;

public class MisCarruselesActivity extends DrawerAbstractActivity {

    private static final int REQUEST_CARRUSELES = 2;
    private static final int REQUEST_PRESENTACION = 3;

    private List<Carrusel> carruseles;
    private CarruselesAdapter adapter;

    TextView misCarruseles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.carruseles = (List<Carrusel>) getIntent().getSerializableExtra(ConstantesAplicacion.CARRUSELES);
        if(this.carruseles == null){
            this.carruseles = new ArrayList<Carrusel>();
        }
        misCarruseles = (TextView) findViewById(R.id.textoMisCarruseles);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_nuevo_carrusel);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MisCarruselesActivity.this, NuevoCarruselActivity.class);
                i.putExtra(ConstantesAplicacion.CARRUSELES, (Serializable) carruseles);
                startActivityForResult(i, REQUEST_CARRUSELES);
            }
        });

        /*
        Seteando el adaptador al GridView
         */
        GridView gridview = (GridView) findViewById(R.id.gridview_mis_carruseles_imagenes);
        this.adapter = new CarruselesAdapter(this, this.carruseles);
        gridview.setAdapter(adapter);

        /*
        Creando una nueva escucha para los elementos del Grid
         */
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                /*
                Iniciar una nueva actividad al presionar la foto
                 */
                Carrusel carrusel = carruseles.get(position);
                Intent i = new Intent(MisCarruselesActivity.this,Presentacion.class);
                i.putExtra("position",position);//Posición del elemento
                i.putExtra(ConstantesAplicacion.CARRUSEL, carrusel);
                startActivityForResult(i, REQUEST_PRESENTACION);

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
                    this.adapter.setGallery(carruseles);
                    this.adapter.notifyDataSetChanged();
                    break;
            }
        }
    }
}
