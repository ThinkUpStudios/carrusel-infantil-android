package thinkup.com.carruselinfantil.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import thinkup.com.carruselinfantil.R;
import thinkup.com.carruselinfantil.modelo.Carrusel;
import thinkup.com.carruselinfantil.modelo.ImagenConAudio;


public class CarruselAdapter extends BaseAdapter {
    // Contexto de la aplicación
    private Context mContext;
    Carrusel gallery;
    private OnItemClickListener listener;
    static Integer VIEW_HOLDER = 1909;


    public CarruselAdapter(Context c, Carrusel galeria) {
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
        View r;
        CarruselAdapter.ViewHolder vh;
        if (convertView == null) {
            /*
            Crear un nuevo Image View de 90x90
            y con recorte alrededor del centro
             */
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.image_display, parent, false);
            vh = new CarruselAdapter.ViewHolder(rowView, listener);
            rowView.setTag(vh);
            vh.bind(position);
            convertView = rowView;
        } else {
            vh =(CarruselAdapter.ViewHolder) convertView.getTag();
        }

        vh.content.setImageURI(this.getThumbId(position));
        if(this.gallery.getGaleria().get(position).getAudios().size() <=0){
            vh.recorded.setImageResource(android.R.drawable.presence_audio_busy);
        }else{
            vh.recorded.setImageResource(android.R.drawable.presence_audio_online);
        }
        return convertView;
    }

    public OnItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface  OnItemClickListener{
        void onItemClick(Integer position);
    }
    public static class ViewHolder{
        // each data item is just a string in this case
        public ImageView content;
        public ImageView recorded;
        public View parent;
        public OnItemClickListener listener;
        public ViewHolder(View v, OnItemClickListener listener) {
            parent = v;
            this.listener = listener;
            content = (ImageView) v.findViewById(R.id.image_content);
            recorded = (ImageView) v.findViewById(R.id.recorded);

        }

        public OnItemClickListener getListener() {
            return listener;
        }

        public void setListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        public void bind(final Integer position) {

            parent.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });
        }

    }
}