package thinkup.com.carruselinfantil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdministrarCarruselesActivity extends DrawerAbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button misCarruseles = (Button) findViewById(R.id.nuevo_carrusel);
        misCarruseles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdministrarCarruselesActivity.this, NuevoCarruselActivity.class);
                startActivity(i);
            }
        });
    }

    public int getContentView(){
        return R.layout.administrar_carruseles_drawer;
    }
}
