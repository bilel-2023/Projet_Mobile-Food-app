package com.example.projetmobile;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import org.osmdroid.views.MapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.views.overlay.Marker;

public class MapSP extends AppCompatActivity {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map_sp);

        // Handle window insets (system bars)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the map view
        mapView = findViewById(R.id.mapview);
        mapView.setTileSource(TileSourceFactory.MAPNIK);  // Use OSM tiles

        // Set the center and zoom level
        GeoPoint startPoint = new GeoPoint(48.8588443, 2.2943506);  // Paris location
        mapView.getMapCenter(startPoint);

        // Enable zoom controls and multi-touch controls
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Optionally, add a marker to the map
        Marker marker = new Marker(mapView);
        marker.setPosition(startPoint);
        marker.setTitle("Paris");
        mapView.getOverlays().add(marker);
    }
}
