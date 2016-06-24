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


public class Details extends AppCompatActivity implements MediaPlayer.OnCompletionListener{

    MediaRecorder recorder;
    MediaPlayer player;
    File archivo;
    TextView estadoGrabacion;
    TextView reproduccion;
    private String TEXTO_GRABANDO = "Grabando audio";
    private String TEXTO_PAUSADO = "";




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


        this.estadoGrabacion = (TextView) findViewById(R.id.grabando);
        this.estadoGrabacion.setText(this.TEXTO_PAUSADO);

        this.reproduccion = (TextView) findViewById(R.id.reproducion);
        this.reproduccion.setText(this.TEXTO_PAUSADO);
    }

    public void grabarAudio() {
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
        this.estadoGrabacion.setText("Grabando audio");

    }

    public void accionarAudio(View v) {
        if(this.TEXTO_GRABANDO.equals(this.estadoGrabacion.getText())){
            this.detener();
        }
        else{
            this.grabarAudio();
        }
    }

    public void detener() {
        recorder.stop();
        recorder.release();

        this.estadoGrabacion.setText(this.TEXTO_PAUSADO);
        this.reproduccion.setText("Listo para reproducir");
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        this.estadoGrabacion.setText(this.TEXTO_PAUSADO);
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
}
