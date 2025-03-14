package com.example.trucking

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.GeoPoint

class FindTrucks : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var recyclerView: RecyclerView
    private lateinit var truckAdapter: TruckAdapter

    // Static GeoPoints for testing truck locations
    private val geoPoint1 = GeoPoint(-26.305, 31.136) // Static test location 1
    private val geoPoint2 = GeoPoint(-26.310, 31.140) // Static test location 2
    private val geoPoint3 = GeoPoint(-26.315, 31.145) // Static test location 3

    // List of trucks with test data for name, location, and GeoPoint
    private val truckList = listOf(
        Truck("Truck 1", "Location A", geoPoint1),
        Truck("Truck 2", "Location B", geoPoint2),
        Truck("Truck 3", "Location C", geoPoint3)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enables edge-to-edge UI styling
        setContentView(R.layout.activity_find_trucks)

        // Initialize the RecyclerView for displaying the list of trucks
        recyclerView = findViewById(R.id.trucks_list)
        truckAdapter = TruckAdapter(truckList) // Pass the truck list to the adapter
        recyclerView.layoutManager = LinearLayoutManager(this) // Set layout manager
        recyclerView.adapter = truckAdapter // Bind adapter to RecyclerView

        // Initialize MapView for displaying the map
        mapView = findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState) // Restore the map's state
        mapView.getMapAsync(this) // Load the map asynchronously
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // Loop through the truck list and add a marker for each truck
        for (truck in truckList) {
            addTruckMarker(googleMap, truck)
        }

        // Center the camera on the first truck's location
        val firstLocation = LatLng(geoPoint1.latitude, geoPoint1.longitude)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 10f))
    }

    /**
     * Adds a marker on the map for the specified truck.
     *
     * @param googleMap The GoogleMap instance where the marker will be added.
     * @param truck The truck object containing the name, location name, and coordinates.
     */
    private fun addTruckMarker(googleMap: GoogleMap, truck: Truck) {
        val latLng = LatLng(truck.geoPoint.latitude, truck.geoPoint.longitude) // Convert GeoPoint to LatLng
        googleMap.addMarker(
            MarkerOptions()
                .position(latLng) // Set marker position
                .title(truck.name) // Set marker title (truck name)
                .snippet(truck.locationName) // Set marker snippet (truck location name)
        )
    }

    // MapView Lifecycle Methods to handle proper map rendering and cleanup

    override fun onStart() {
        super.onStart()
        mapView.onStart() // Start the MapView
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume() // Resume the MapView
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause() // Pause the MapView
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop() // Stop the MapView
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy() // Destroy the MapView resources
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory() // Handle low memory situations
    }
}
