package com.lecture.nitika.grub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lecture.nitika.grub.Helper.RecipeItem;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeInfo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipeInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeInfo extends Fragment implements View.OnClickListener {

    private static final String TAG = "RecipeInfo";
    private OnFragmentInteractionListener mListener;
    private RecipeItem recipeItem = null;
    private ImageView imageView = null;
    private Button btn_Ingrediants = null;
    private Button btn_Directions = null;
    private TextView txtView_title = null;

    public RecipeInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param rItem Recipe Item clicked on the list view
     * @return A new instance of fragment RecipeInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeInfo newInstance(RecipeItem rItem) {
        RecipeInfo fragment = new RecipeInfo();
        Bundle args = new Bundle();
        args.putSerializable(Constants.clicked_ListItem_Key,rItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipeItem = (RecipeItem)getArguments().getSerializable(Constants.clicked_ListItem_Key);
            Log.d(TAG,recipeItem.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_recipe_info, container, false);

        imageView = (ImageView)rootView.findViewById(R.id.image_recipeInfo);
        txtView_title = (TextView)rootView.findViewById(R.id.txtView_recipeInfo_title);
        btn_Directions = (Button)rootView.findViewById(R.id.btn_Directions);
        btn_Ingrediants = (Button)rootView.findViewById(R.id.btn_Ingrediants);
        btn_Directions.setOnClickListener(this);
        btn_Ingrediants.setOnClickListener(this);
        btn_Ingrediants.performClick();


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        new ImageDownloadTask(imageView).execute(recipeItem.getContent().getImage());
        txtView_title.setText(recipeItem.getContent().getTitle());
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_Ingrediants:
                btn_Directions.setBackground(getResources().getDrawable(R.drawable.recipeinfobuttonright));
                btn_Directions.setTextColor(getResources().getColor(R.color.white));
                btn_Ingrediants.setTextColor(getResources().getColor(R.color.colorLogin));
                btn_Ingrediants.setBackground(getResources().getDrawable(R.drawable.plainbtn));
                break;
            case R.id.btn_Directions:
                btn_Ingrediants.setBackground(getResources().getDrawable(R.drawable.recipeinfobuttonleft));
                btn_Ingrediants.setTextColor(getResources().getColor(R.color.white));
                btn_Directions.setTextColor(getResources().getColor(R.color.colorLogin));
                btn_Directions.setBackground(getResources().getDrawable(R.drawable.plainbtn));
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class ImageDownloadTask extends AsyncTask<String,Void,Bitmap>{

        ImageView imgView = null;
        public ImageDownloadTask(ImageView mImageView){
            this.imgView = mImageView;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            URL imageURL = null;
            Bitmap bitmapfrmServer = null;
            try {
                imageURL = new URL(params[0]);
                bitmapfrmServer = BitmapFactory.decodeStream(imageURL.openStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch(Exception ex){
                ex.printStackTrace();
            }
            return bitmapfrmServer;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
           if(bitmap != null){
               imgView.setImageBitmap(bitmap);
           }
        }
    }
}
