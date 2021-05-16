# Yelp app using MVVM clean architecture, Jetpack, Retrofit, Glide and Coroutines API

## App Overview      
 
- On launch, App shows a dialog explaining why it needs Location permission and trigger Permission menu.
- Declining the permission would show a "rationale" dialog requesting location permission again with a button to navigate users to the app's settings page so user can grant the Location permission.
- Granting the permission would dismiss the dialog and user can start interacting the UI.

 The app features a 2 screen navigation      
      
- Business Search Screen has a search bar to take a input text. Clicking on search button would fetch a list of nearby businesses using Yelp API. Clicking on any of the business would navigate the user to detail screen.
- Details screen showing the details of the Business with a picture, rating, review count, address, marker on google maps.
  - Call button -> Opens phone dialer app with Business phone number pre-filled.
  - Website button -> Opens business listing page on yelp.
  - Directions button -> Opens google maps navigation from current location to business location in Drive mode      
  
- App supports Dark mode. Changing the system wide Theme would update the App's theme.
      
    
### Installation

1. Get a Yelp API Key [here](https://www.yelp.com/developers/documentation/v3/get_started)
2. Get a Google Maps API key [here](https://developers.google.com/maps/documentation/android-sdk/get-api-key)
3. Clone the repo
   ```sh
   git clone https://github.com/Rohithkadaveru/YelpProto.git
   ```
4. Enter your GoogleMaps API key in `apiKey.properties`
   ```JS
   GOOGLE_MAPS_API_KEY=MAPS_KEY_HERE
   ```  
5. Enter your Yelp API key in `apiKey.properties`
   ```JS
   YELP_API_KEY="YELP_KEY_HERE"
   ```  

### Demo

1. Demo vide is located at /demo/video.mp4
2. Debug apk is located at /demo/YelpProto.apk

### Known Bugs   

1. If a Network/API error occurs, moving between screens would trigger error dialog even if it's not valid anymore. 
   Solution: use SingleLiveEvent or Flow
