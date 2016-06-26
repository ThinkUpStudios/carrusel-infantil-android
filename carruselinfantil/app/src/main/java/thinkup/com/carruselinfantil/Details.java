package thinkup.com.carruselinfantil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.widget.TextView;

import java.util.List;

import thinkup.com.carruselinfantil.modelo.Carrusel;
import thinkup.com.carruselinfantil.modelo.ImagenConAudio;


public class Details extends AppCompatActivity implements MediaPlayer.OnCompletionListener{

    MediaRecorder recorder;
    MediaPlayer player;
    File archivo;
    TextView reproduccion;
    TextView audioEspañol;
    Carrusel carrusel;
    int position;
    private boolean grabando = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        /*
        Recibiendo el identificador de la imagen
         */
        Intent i = getIntent();
        position = i.getIntExtra("position", -1);// -1 si no se encontró la referencia
        carrusel = (Carrusel) getIntent().getSerializableExtra(ConstantesAplicacion.CARRUSEL);
        ImageAdapter adapter = new ImageAdapter(this, carrusel);

        /*
        Seteando el recurso en el ImageView
         */
        ImageView originalImage = (ImageView)findViewById(R.id.originalImage);
        originalImage.setImageURI(adapter.getThumbId(position));



        this.reproduccion = (TextView) findViewById(R.id.reproducion);
        this.reproduccion.setText("");

        this.audioEspañol = (TextView) findViewById(R.id.audio_español);
        this.audioEspañol.setText("Grabar audio en Español");
    }

    public void grabarAudio() {
        this.grabando = true;
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        File path = new File(Environment.getExternalStorageDirectory()
                .getPath());
        try {
            archivo = File.createTempFile("temporal", ".3gp", path);
        } catch (IOException e) {
        }
        recorder.setOutputFile(archivo.getAbsolutePath());
        try {
            recorder.prepare();
        } catch (IOException e) {
        }
        recorder.start();
        this.audioEspañol.setText("Grabando audio Español");
    }

    public void accionarAudio(View v) {
        if(this.grabando){
            this.detener();
        }
        else{
            this.grabarAudio();
        }
    }

    public void detener() {
        recorder.stop();
        recorder.release();
        ImagenConAudio imagenConAudio = this.carrusel.getGaleria().get(position);
        imagenConAudio.addAudio(archivo.getAbsolutePath());
        this.reproduccion.setText("Listo para reproducir");
        this.audioEspañol.setText("Grabar audio en Español");
        this.grabando = false;
    }


    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    public void reproducir(View v) {
        player = new MediaPlayer();
        player.setOnCompletionListener(this);
        try {
            player.setDataSource(archivo.getAbsolutePath());
        } catch (IOException e) {
        }
        try {
            player.prepare();
        } catch (IOException e) {
        }
        player.start();
    }

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent();
        i.putExtra(ConstantesAplicacion.CARRUSEL, carrusel);
        setResult(RESULT_OK, i);
        finish();

    }
}
