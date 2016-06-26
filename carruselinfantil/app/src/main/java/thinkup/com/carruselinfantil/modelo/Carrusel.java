package thinkup.com.carruselinfantil.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dario on 24/6/16.
 */
public class Carrusel implements Serializable{

    private static final long serialVersionUID = 1L;

    private List<ImagenConAudio> galeria;

    public Carrusel() {
        this.galeria = new ArrayList<>();
    }

    public void addImagen(ImagenConAudio imagenConAudio){
        this.galeria.add(imagenConAudio);
    }

    public List<ImagenConAudio> getGaleria() {
        return galeria;
    }

    public void setGaleria(List<ImagenConAudio> galeria) {
        this.galeria = galeria;
    }
}
