package thinkup.com.carruselinfantil.modelo;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dario on 24/6/16.
 */
public class ImagenConAudio implements Serializable {

    private static final long serialVersionUID = 1L;
    private String image;
    private List<String> audios;

    public ImagenConAudio() {
        this.audios = new ArrayList<String>();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getAudios() {
        return audios;
    }

    public void setAudios(List<String> audios) {
        this.audios = audios;
    }

    public void addAudio(String audio){
        this.audios.add(audio);
    }

    public Uri getUri(){
        return Uri.parse(this.getImage());
    }
}
