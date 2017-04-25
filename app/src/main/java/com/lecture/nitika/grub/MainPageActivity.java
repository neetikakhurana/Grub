package com.lecture.nitika.grub;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainPageActivity extends AppCompatActivity {

    private Context mcontext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        mcontext = this;
        //init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    /***
     * Trying to initialize all the client
     */
    private void init() {
        RecipeClient rClient = RecipeClient.getInstance(mcontext);
        //rClient.SearchRecipe();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_search,menu);
        MenuItem item=menu.findItem(R.id.menu_search);
        SearchView searchView=(SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                        return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
