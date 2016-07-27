package thinkup.com.carruselinfantil;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

        RecyclerView listView = (RecyclerView) findViewById(R.id.mis_carruseles_recycler);
        if (this.carruseles == null || this.carruseles.size() == 0) {
            this.carruseles = new ArrayList<Carrusel>();
        }
        updateTutorial();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
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

        this.adapter = new CarruselesAdapter(this, this.carruseles);
        listView.setAdapter(adapter);

        /*
        Creando una nueva escucha para los elementos del Grid
         */
        adapter.setOnClickListener(new CarruselesAdapter.OnItemClick() {
            @Override
            public void onItemClick(Carrusel c) {
                Intent i = new Intent(MisCarruselesActivity.this, Presentacion.class);
                i.putExtra(ConstantesAplicacion.CARRUSEL, c);
                startActivityForResult(i, REQUEST_PRESENTACION);

            }

        });
    }

    private void updateTutorial() {
        View tutorial = findViewById(R.id.tutorial);
        RecyclerView listView = (RecyclerView) findViewById(R.id.mis_carruseles_recycler);
        if (this.carruseles.size() <= 0) {
            tutorial.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            tutorial.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
    }

    public int getContentView() {
        return R.layout.mis_carruseles_drawer;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CARRUSELES:
                    carruseles = (List<Carrusel>) data.getSerializableExtra(ConstantesAplicacion.CARRUSELES);
                    this.adapter.setGallery(carruseles);
                    this.adapter.notifyDataSetChanged();
                    updateTutorial();
                    break;
            }
        }
    }
}
