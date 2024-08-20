package com.example.starwarsencyclopedia;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Character> characterList = new ArrayList<>();
    private CharacterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.list_view);
        adapter = new CharacterAdapter(this, characterList);
        listView.setAdapter(adapter);

        new FetchDataTask().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Character selectedCharacter = characterList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("name", selectedCharacter.getName());
                bundle.putString("height", selectedCharacter.getHeight());
                bundle.putString("mass", selectedCharacter.getMass());
                bundle.putString("hair_color", selectedCharacter.getHairColor());

                FrameLayout frameLayout = findViewById(R.id.fragment_container);
                if (frameLayout == null) { // Phone
                    Intent intent = new Intent(MainActivity.this, EmptyActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else { // Tablet
                    DetailsFragment fragment = new DetailsFragment();
                    fragment.setArguments(bundle);

                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                }
            }
        });
    }

    private class FetchDataTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://swapi.dev/api/people/");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            } catch (Exception e) {
                Log.e("Error", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray resultsArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject characterObject = resultsArray.getJSONObject(i);
                    String name = characterObject.getString("name");
                    String height = characterObject.getString("height");
                    String mass = characterObject.getString("mass");
                    String hairColor = characterObject.getString("hair_color");

                    Character character = new Character(name, height, mass, hairColor);
                    characterList.add(character);
                }
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                Log.e("Error", e.getMessage(), e);
            }
        }
    }
}
