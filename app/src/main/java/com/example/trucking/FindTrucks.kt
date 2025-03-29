package com.example.trucking

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

class FindTrucks : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var recyclerView: RecyclerView
    private lateinit var truckAdapter: TruckAdapter
    private val truckList = mutableListOf<Truck>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enables edge-to-edge UI styling
        setContentView(R.layout.activity_find_trucks)

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.trucks_list)
        truckAdapter = TruckAdapter(this, truckList, { truck ->
            // Handle item click
            val latLng = LatLng(truck.geoPoint.latitude, truck.geoPoint.longitude)
            mapView.getMapAsync { googleMap ->
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
            }
        }, { deletedTruck ->
            // Handle truck deletion
            mapView.getMapAsync { googleMap ->
                googleMap.clear() // Clear all markers
                addMarkersToMap(googleMap, truckList.filter { it.name != deletedTruck.name }) // Re-add remaining markers
            }
        })


        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = truckAdapter

        // Confirm RecyclerView space
        recyclerView.post {
            Log.d("FindTrucks", "RecyclerView dimensions: ${recyclerView.width}x${recyclerView.height}")
            if (recyclerView.width == 0 || recyclerView.height == 0) {
                Log.e("FindTrucks", "RecyclerView has no allocated space. Check layout constraints.")
            }
        }

        // Initialize the MapView
        mapView = findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        // Test with dummy data
        testWithDummyData()

        // Fetch truck data dynamically from Firestore
        fetchTrucksFromFirestore()
    }

    private fun fetchTrucksFromFirestore() {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("trucks")
            .get()
            .addOnSuccessListener { snapshot ->
                val trucks = snapshot.documents.mapNotNull { doc ->
                    val name = doc.getString("name")
                    val location = doc.getString("location")
                    val geoPoint = doc.getGeoPoint("geoPoint")
                    if (name != null && location != null && geoPoint != null) {
                        Truck(name, location, geoPoint)
                    } else null
                }

                // Log for data confirmation
                Log.d("FindTrucks", "Fetched ${trucks.size} trucks from Firestore")

                // Update RecyclerView and MapView
                truckList.clear()
                truckList.addAll(trucks)
                truckAdapter.updateTrucks(trucks) // Update RecyclerView
                mapView.getMapAsync { googleMap ->
                    googleMap.clear()
                    addMarkersToMap(googleMap, trucks) // Add markers for trucks
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load trucks: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.setOnMarkerClickListener { marker ->
            Toast.makeText(this, "Clicked on ${marker.title}: ${marker.snippet}", Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun addMarkersToMap(googleMap: GoogleMap, trucks: List<Truck>) {
        for (truck in trucks) {
            val latLng = LatLng(truck.geoPoint.latitude, truck.geoPoint.longitude)
            googleMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(truck.name)
                    .snippet(truck.locationName)
            )
        }
    }

    // Test with dummy data
    private fun testWithDummyData() {
        val dummyTrucks = listOf(
            Truck("Truck A", "Johannesburg Logistics Hub", GeoPoint(-26.2041, 28.0473)),
            Truck("Truck B", "Durban Port Terminal", GeoPoint(-29.8587, 31.0218)),
            Truck("Truck C", "Windhoek Transport Depot", GeoPoint(-22.5609, 17.0658))
        )
        Log.d("FindTrucks", "Testing RecyclerView with dummy data")
        truckAdapter.updateTrucks(dummyTrucks) // Populate RecyclerView with dummy data
    }

    // MapView lifecycle methods
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
