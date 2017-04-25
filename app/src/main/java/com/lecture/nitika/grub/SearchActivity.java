package com.lecture.nitika.grub;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.lecture.nitika.grub.Helper.RecipeBriefContent;
import com.lecture.nitika.grub.Helper.RecipeItem;
import com.lecture.nitika.grub.Helper.RecipeResponse;
import com.lecture.nitika.grub.controller.QueryChecker;
import com.mashape.p.spoonacularrecipefoodnutritionv1.http.client.HttpContext;
import com.mashape.p.spoonacularrecipefoodnutritionv1.models.FindByIngredientsModel;
import com.mashape.p.spoonacularrecipefoodnutritionv1.http.client.APICallBack;

import java.util.List;

import static android.widget.Toast.*;


public class SearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnFragmentInteractionListener,
        UploadFragment.OnFragmentInteractionListener,
        RViewedFragement.OnFragmentInteractionListener,
        FavRecipeFragement.OnFragmentInteractionListener,
        FeatureRecipeFragment.OnFragmentInteractionListener,
        RecipeResultFragment.OnListFragmentInteractionListener,
        RecipeInfo.OnFragmentInteractionListener{

    private static final String TAG = "SearchActivity" ;
    private DrawerLayout drawer = null;
    private Toolbar toolbar = null;
    private ActionBarDrawerToggle toggle = null;
    private NavigationView navigationView = null;
    private Context mContext;
    private RecipeClient rClient = null;
    private RecipeResponse rResponse = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mContext = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.flContent, new HomeFragment())
                    .commit();
        }

        rClient = RecipeClient.getInstance(mContext);
        rResponse = RecipeResponse.getInstance();
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_search,menu);
        MenuItem item=menu.findItem(R.id.menu_search);

        android.widget.SearchView searchView=(android.widget.SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG,"searched Query "+query);

                if(QueryChecker.isQueryProper(query)){
                    GetDatafromServer(query);
                }else{

                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void startRecipeResultFragment() {

        if(rResponse.isSuccess()){

            //deleting all previous entries in the list
            RecipeBriefContent.deleteAllItems();

            //loading the data to the model
            for(FindByIngredientsModel item : rResponse.getResultList()){
                RecipeItem rItem = new RecipeItem(item);
                RecipeBriefContent.addItem(rItem);
            }

            //starting the fragment
            Fragment fragment = new RecipeResultFragment();
            try{

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

            }catch (Exception ex){
                Log.d(TAG,"Exception in Navigation menu "+ex.getLocalizedMessage());
            }
        }else{
            makeText(mContext,rResponse.getErrorStr(), LENGTH_LONG).show();
        }
    }

    private void GetDatafromServer(String query) {

        rResponse.setDefaults();

        //getting the data from the API
        if(rClient!= null){
            rClient.controller.findByIngredientsAsync(
                    query,
                    null,
                    Constants.numberRecipeResults,
                    Constants.ranking,
                    null,
                    new APICallBack<List<FindByIngredientsModel>>(){

                        @Override
                        public void onSuccess(HttpContext context, List<FindByIngredientsModel> response) {
                            Log.d(TAG,response.toString());
                            rResponse.setSuccess(true);
                            rResponse.setResultList(response);
                            startRecipeResultFragment();
                        }

                        @Override
                        public void onFailure(HttpContext context, Throwable error) {
                            Log.d(TAG,error.getLocalizedMessage());
                            rResponse.setErrorStr(error.getLocalizedMessage());
                            startRecipeResultFragment();
                        }
                    });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Fragment fragment = null;

        switch(id){
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_location:

                break;
            case R.id.nav_logout:

                break;
            case R.id.nav_upload:
                fragment = new UploadFragment();
                break;
            case R.id.nav_userPreferences:

                break;
            default:
                fragment = new HomeFragment();
                break;
        }

        try{

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        }catch (Exception ex){
            Log.d(TAG,"Exception in Navigation menu "+ex.getLocalizedMessage());
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast toast = makeText(this, "Wheeee!", LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onListFragmentInteraction(RecipeItem item) {

        Log.d(TAG,"List item clicked");
//        Fragment fragment = new RecipeInfo();
        Fragment fragment = RecipeInfo.newInstance(item);
        try{

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flContent, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }catch (Exception ex){
            Log.d(TAG,"Exception in on list view click "+ex.getLocalizedMessage());
        }
    }
}
