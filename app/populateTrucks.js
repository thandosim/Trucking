// Import the Firebase Admin SDK
const admin = require("firebase-admin");

// Initialize Firebase
admin.initializeApp({
    credential: admin.credential.cert(require("C:\\trucking-f4c19-firebase-adminsdk-fbsvc-7ad93f0795.json")),
    projectId: "trucking-f4c19",
});

  

const db = admin.firestore();

// Function to populate the "trucks" collection
async function populateTrucksCollection() {
  try {
    const trucks = [
      {
        name: "Truck A",
        location: "Johannesburg Logistics Hub",
        geoPoint: new admin.firestore.GeoPoint(-26.2041, 28.0473), // Johannesburg, South Africa
      },
      {
        name: "Truck B",
        location: "Durban Port Terminal",
        geoPoint: new admin.firestore.GeoPoint(-29.8587, 31.0218), // Durban, South Africa
      },
      {
        name: "Truck C",
        location: "Windhoek Transport Depot",
        geoPoint: new admin.firestore.GeoPoint(-22.5609, 17.0658), // Windhoek, Namibia
      },
    ];

    // Add each truck to the "trucks" collection
    for (const truck of trucks) {
      const docRef = db.collection("trucks").doc();
      await docRef.set(truck);
      console.log(`Added truck: ${truck.name}`);
    }
    console.log("Trucks collection populated successfully!");
  } catch (error) {
    console.error("Error populating trucks collection:", error);
  }
}

// Run the script
populateTrucksCollection();
