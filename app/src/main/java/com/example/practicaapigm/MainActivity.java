package com.example.practicaapigm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//Combobox para seleccionar y generar el tipo de mapa
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0: //Normal
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case 1: //Satelite
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case 2: //Terreno
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                    case 3: //Hibrido
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                    default:
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                }
            }

            @Override
            public void onNothingSelected (AdapterView < ? > adapterView){
                //
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double miLat = -0.996385;
        double miLong = -79.473150;
        LatLng latLng = new LatLng(miLat, miLong);//Mi posicion
        mMap.addMarker(new MarkerOptions().position(latLng).title("Esta es mi posición").snippet("¡Hola!, saludos al que lea esto."));//Marcar mi posicion

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);//Mostrar a nivel de ciudad, con valor de 10
        mMap.moveCamera(cameraUpdate);
        mMap.animateCamera(cameraUpdate, 5000, null);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(19).bearing(45).tilt(70).build();//Rotar camara a 70 grados
        CameraUpdate cameraUpdate1 = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.animateCamera(cameraUpdate1);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {//Ver Informacion la hacer click en una posicion y dejar un marcador
            public void onMapClick(LatLng point) {
                Projection proj = mMap.getProjection();
                Point coord = proj.toScreenLocation(point);
                Toast.makeText(
                MainActivity.this,
                        "He hecho click aqui:\n" +
                                "Lat: " + point.latitude + "\n" +
                                "Lng: " + point.longitude + "\n" +
                                "X: " + coord.x + " - Y: " + coord.y,
                        Toast.LENGTH_SHORT).show();
                mMap.addMarker(new MarkerOptions().position(new LatLng(point.latitude,point.longitude)).title("Nuevo Marcador").snippet("Sera que se quita?"));
            }
        });

        PolylineOptions lineas = new PolylineOptions()//Encerrar en cuadro rojo a Ecuador
                .add(new LatLng(2.51, -81.26))
                .add(new LatLng(2.51, -73.54))
                .add(new LatLng(-6.09, -73.54))
                .add(new LatLng(-6.09, -81.26))
                .add(new LatLng(2.51, -81.26));
        lineas.width(8);
        lineas.color(Color.RED);
        mMap.addPolyline(lineas);

        LatLng punto = new LatLng(-0.9968459947818972, -79.46766894310713);//Agregar otro marcador
        mMap.addMarker(new MarkerOptions().position(punto).title("Mi ex-colegio").snippet("Que buenos recuerdos me trae"));


    }

    public void onZoom (View view) {//metodo para alejar o acercar en el mapa
        if (view.getId() == R.id.zoomin) {
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
        if (view.getId() == R.id.zoomout) {
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }

}
//Referencias de ayuda:
//https://www.youtube.com/watch?v=_NR6RK98kSg
//https://www.youtube.com/watch?v=1wmuJMd3KnI
//https://www.youtube.com/watch?v=B4OCSRBFjkM