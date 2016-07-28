package thinkup.com.carruselinfantil;


import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import thinkup.com.carruselinfantil.adapters.CarruselAdapter;
import thinkup.com.carruselinfantil.modelo.Carrusel;
import thinkup.com.carruselinfantil.modelo.ImagenConAudio;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class NuevoCarruselActivity extends AppCompatActivity {

    private static String APP_DIRECTORY = "MyPictureApp/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "PictureApp";

    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;
    private final int REQUEST_AUDIO = 1;

    //private ImageView mSetImage;
    private FloatingActionButton mOptionButton;
    private RelativeLayout mRlView;

    private String mPath;

    private CarruselAdapter adapter;

    private List<Carrusel> carruseles;
    private boolean editando;
    private Carrusel carrusel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_carrusel);
        this.carruseles = (List<Carrusel>) getIntent().getSerializableExtra(ConstantesAplicacion.CARRUSELES);
        Integer carruselAModificar = (Integer) getIntent().getSerializableExtra(ConstantesAplicacion.CARRUSEL_A_MODIFICAR);
        TextInputEditText nombre = (TextInputEditText)findViewById(R.id.nombre_carrusel);
        if(carruselAModificar == null){
            this.carrusel = new Carrusel();
            this.carrusel.setNombre(getString(R.string.default_nombre));
            editando = false;
        }
        else{
            this.carrusel = this.carruseles.get(carruselAModificar);
            editando = true;

        }
        nombre.setText(carrusel.getNombre());
        updateTutorial();

        GridView gridview = (GridView) findViewById(R.id.grid_carrusel);

        this.adapter = new CarruselAdapter(this, this.carrusel);
        this.adapter.setListener(new CarruselAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Integer position) {
                Intent i = new Intent(NuevoCarruselActivity.this,Details.class);
                i.putExtra("position",position);//Posición del elemento
                i.putExtra(ConstantesAplicacion.CARRUSEL, carrusel);
                startActivityForResult(i, REQUEST_AUDIO);
            }
        });
        gridview.setAdapter(adapter);


        //mSetImage = (ImageView) findViewById(R.id.set_picture);
        mOptionButton = (FloatingActionButton) findViewById(R.id.fab_add);

        if(mayRequestStoragePermission())
            mOptionButton.setEnabled(true);
        else
            mOptionButton.setEnabled(false);


        mOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptions();
            }
        });

        /*
        Seteando el adaptador al GridView
         */

    }


    private void updateTutorial() {
        View tutorial = findViewById(R.id.tutorial);
        GridView gridview = (GridView) findViewById(R.id.grid_carrusel);

        if (this.carrusel.getGaleria().size() <= 0) {
            tutorial.setVisibility(View.VISIBLE);
            gridview.setVisibility(View.GONE);
        } else {
            tutorial.setVisibility(View.GONE);
            gridview.setVisibility(View.VISIBLE);
        }
    }


    private boolean mayRequestStoragePermission() {

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;

        if((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))){
            Snackbar.make(mRlView, "Los permisos son necesarios para poder usar la aplicación",
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
                }
            });
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        }

        return false;
    }

    private void showOptions() {
        final CharSequence[] option = {"Tomar foto", "Elegir de galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(NuevoCarruselActivity.this);
        builder.setTitle("Elige una opción");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(option[which] == "Tomar foto"){
                    openCamera();
                }else if(option[which] == "Elegir de galeria"){
                    Intent chooseIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    chooseIntent.setType("image/*");
                    startActivityForResult(Intent.createChooser(chooseIntent, "Selecciona app de imagen"), SELECT_PICTURE);
                }else {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    private void openCamera() {
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated){
            Long timestamp = System.currentTimeMillis() / 1000;
            String imageName = timestamp.toString() + ".jpg";

            mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY
                    + File.separator + imageName;

            File newFile = new File(mPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
            startActivityForResult(intent, PHOTO_CODE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path", mPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mPath = savedInstanceState.getString("file_path");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case PHOTO_CODE:

                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    ImagenConAudio iaudio = new ImagenConAudio();
                    iaudio.setImage(mPath);
                    this.carrusel.addImagen(iaudio);
                    this.adapter.notifyDataSetChanged();
                    break;
                case SELECT_PICTURE:
                    ClipData clipData = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        clipData = data.getClipData();
                        String dataUri = data.getDataString();
                        for (int i = 0; i < clipData.getItemCount(); i++)
                        {
                            ImagenConAudio ia = new ImagenConAudio();
                            Uri selectedImageUri = clipData.getItemAt(i).getUri();
                            String p = getRealPathFromURI(selectedImageUri);
                            ia.setImage(p);

                            this.carrusel.addImagen(ia);
                        }
                    }
                    this.adapter.notifyDataSetChanged();
                    break;
                case REQUEST_AUDIO:
                    //TODO: Actualizar la lista.
                    carrusel = (Carrusel) data.getSerializableExtra(ConstantesAplicacion.CARRUSEL);
                    this.adapter.notifyDataSetChanged();

                    break;
            }
            updateTutorial();
        }
    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_PERMISSIONS){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(NuevoCarruselActivity.this, "Permisos aceptados", Toast.LENGTH_SHORT).show();
                mOptionButton.setEnabled(true);
            }
        }else{
            showExplanation();
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NuevoCarruselActivity.this);
        builder.setTitle("Permisos denegados");
        builder.setMessage("Para usar las funciones de la app necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.show();
    }


    @Override
    public void onBackPressed()
    {
        Intent i = new Intent();
        TextInputEditText nombre = (TextInputEditText)findViewById(R.id.nombre_carrusel);
        if(!existeCarrusel(nombre.getText().toString())){
            carrusel.setNombre(nombre.getText().toString());
            if(!editando) this.carruseles.add(carrusel);
            i.putExtra(ConstantesAplicacion.CARRUSELES, (Serializable) carruseles);
            setResult(RESULT_OK, i);
            finish();
        }


    }

    private boolean existeCarrusel(String nombre) {
        return false;
    }

}
