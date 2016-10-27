package koster.tychokoster_pset678;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// Retrieves all the art that was found with the query used by the user.

class RetrieveArt extends AsyncTask<String, String, String> {
    /* Retrieves the movie info from the API and stores it into a String. */
    protected String doInBackground(String... urls) {
        String title = urls[0];

        try {
            URL url = new URL("https://www.rijksmuseum.nl/api/nl/collection?key=KTtoPFMp&format=json&q=" + title);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }
}
