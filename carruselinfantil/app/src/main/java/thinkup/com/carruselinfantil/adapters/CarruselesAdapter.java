package thinkup.com.carruselinfantil.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

import thinkup.com.carruselinfantil.modelo.Carrusel;


public class CarruselesAdapter extends BaseAdapter {
    // Contexto de la aplicación
    private Context mContext;
    List<Carrusel> gallery;


    public CarruselesAdapter(Context c, List<Carrusel> galeria) {
        mContext = c;
        this.gallery = galeria;
    }

    public void setGallery(List<Carrusel> gallery) {
        this.gallery = gallery;
    }

    public int getCount() {
        if(this.gallery != null)
            return this.gallery.size();
        return 0;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public Uri getThumbId(int position){
        if(this.gallery != null)
            return this.gallery.get(position).getGaleria().get(0).getUri();
        return null;
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