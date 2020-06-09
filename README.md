# Fun Places
An Android app that suggests nearby parks, play grounds, museums, and more. Powered by Google's Map API.

## Fetching nearby places
```Java
String location = "location="+Util.getCurrentLat()+","+Util.getCurrentLon();
        JsonObjectRequest jsObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject result) {
                try{
                    ContentResolver resolver = context.getContentResolver();
                    ContentValues[] contentValues = getPlaces(result.toString(), placeType);
                    Log.d(TAG, "contentvalues # = "+contentValues.length);
                    resolver.bulkInsert(PlaceContract.PlaceEntry.CONTENT_URI, contentValues);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
```
## License
This work is released under the MIT License. 
