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
    private val geoPoint1 = GeoPoint(-26.305, 31.136) // Static test location 1
    private val geoPoint2 = GeoPoint(-26.310, 31.140) // Static test location 2
    private val geoPoint3 = GeoPoint(-26.315, 31.145) // Static test location 3
    private val truckList = listOf(
        Truck("Truck 1", "Location A", geoPoint1),
        Truck("Truck 2", "Location B", geoPoint2),
        Truck("Truck 3", "Location C", geoPoint3)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_find_trucks)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.trucks_list)
        truckAdapter = TruckAdapter(truckList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = truckAdapter

        // Initialize MapView
        mapView = findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // Loop through the truck list and add a marker for each truck
        for (truck in truckList) {
            addTruckMarker(googleMap, truck)
        }

        // Center the camera on the first truck
        val firstLocation = LatLng(geoPoint1.latitude, geoPoint1.longitude)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 10f))
    }

    // Function to add a marker to the map for each truck
    private fun addTruckMarker(googleMap: GoogleMap, truck: Truck) {
        val latLng = LatLng(truck.geoPoint.latitude, truck.geoPoint.longitude)
        googleMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(truck.name)
                .snippet(truck.locationName)
        )
    }

    // MapView Lifecycle Methods
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
