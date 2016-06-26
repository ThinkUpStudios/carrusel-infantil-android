package thinkup.com.carruselinfantil;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

import thinkup.com.carruselinfantil.modelo.Carrusel;
import thinkup.com.carruselinfantil.modelo.ImagenConAudio;


public class ImageAdapter extends BaseAdapter {
    // Contexto de la aplicación
    private Context mContext;
    Carrusel gallery;


    public ImageAdapter(Context c, Carrusel galeria) {
        mContext = c;
        this.gallery = galeria;
    }

    public int getCount() {
        return this.gallery.getGaleria().size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public Uri getThumbId(int position){
        return this.gallery.getGaleria().get(position).getUri();
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        //ImageView a retornar
        ImageView imageView;

        if (convertView == null) {
            /*
            Crear un nuevo Image View de 90x90
            y con recorte alrededor del centro
             */
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(90,90));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            imageView = (ImageView) convertView;
        }

        //Setear la imagen desde el recurso drawable
        imageView.setImageURI(this.getThumbId(position));
        return imageView;
    }


}