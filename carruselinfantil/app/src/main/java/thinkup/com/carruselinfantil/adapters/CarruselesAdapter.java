package thinkup.com.carruselinfantil.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import thinkup.com.carruselinfantil.R;
import thinkup.com.carruselinfantil.modelo.Carrusel;


public class CarruselesAdapter extends RecyclerView.Adapter<CarruselesAdapter.ViewHolder> {
    // Contexto de la aplicación
    private Context mContext;

    public void setOnClickListener(CarruselesAdapter.OnItemClick onClickListener) {
        this.listener = onClickListener;
    }

    public interface OnItemClick {

        void onItemClick(Carrusel c);
    }

    public interface OnEditItem {
        void onEdit(Integer c);
    }

    private OnItemClick listener;
    private OnEditItem editListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView mBackground;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.info_text);
            mBackground = (ImageView) v.findViewById(R.id.item_image);

        }

        public void bind(final Carrusel item, final OnItemClick listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }

        public void bindEditor(final Integer position, final OnEditItem editListener) {
            itemView.findViewById(R.id.edit_carrusel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editListener.onEdit(position);
                }
            });
        }
    }


    List<Carrusel> gallery;

    @Override
    public CarruselesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.carrusel_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    public CarruselesAdapter(Context c, List<Carrusel> galeria) {
        mContext = c;
        this.gallery = galeria;
    }

    public void setGallery(List<Carrusel> gallery) {
        this.gallery = gallery;
    }

    public OnEditItem getEditListener() {
        return editListener;
    }

    public void setEditListener(OnEditItem editListener) {
        this.editListener = editListener;
    }

    public int getCount() {
        if (this.gallery != null)
            return this.gallery.size();
        return 0;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Carrusel c = gallery.get(position);

        holder.mTextView.setText(c.getNombre());
        holder.bind(gallery.get(position), listener);
        holder.bindEditor(position, editListener);
        Picasso.with(mContext).load(new File(c.getGaleria().get(0).getUri().getPath())).into(holder.mBackground);


    }

    @Override
    public int getItemCount() {
        return this.gallery.size();
    }


}