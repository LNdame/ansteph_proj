package ansteph.com.beecab.view.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import ansteph.com.beecab.R;
import ansteph.com.beecab.app.Config;
import ansteph.com.beecab.app.GlobalRetainer;
import ansteph.com.beecab.helper.SessionManager;
import ansteph.com.beecab.service.RequestHandler;
import ansteph.com.beecab.view.registration.LostPassword;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String TAG = ViewProfileFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int PICK_IMAGE_REQUEST = 1;
    private int RESULT_OK = -1;
    CircleImageView profilePic;
    private Bitmap bitmap;

    private Uri filePath;

    private String mFilePath;
    private String mFileEncodedPath;


    GlobalRetainer mGlobalRetainer;
    SessionManager sessionManager;
    TextView   txtUsername, txtCellphone;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ViewProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewProfileFragment newInstance(String param1, String param2) {
        ViewProfileFragment fragment = new ViewProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mGlobalRetainer = (GlobalRetainer) getActivity().getApplicationContext();
        sessionManager = new SessionManager(getActivity().getApplicationContext());
        View rootView= inflater.inflate(R.layout.fragment_view_profile, container, false);

        profilePic = (CircleImageView) rootView.findViewById(R.id.profilepic);

        txtUsername = (TextView) rootView.findViewById(R.id.username);
        txtCellphone = (TextView) rootView.findViewById(R.id.cellnumber);

        ImageButton imgEdit = (ImageButton) rootView.findViewById(R.id.imgEdit);

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EditUsernameFragment();
                FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                        .addToBackStack(EditUsernameFragment.TAG);
                fragmentTransaction.replace(R.id.container_body, fragment,EditUsernameFragment.TAG);
                fragmentTransaction.commit();
            }
        });


        ImageButton imgpwdEdit = (ImageButton) rootView.findViewById(R.id.imgpwdEdit);
        imgpwdEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UpdatePassword.class));
            }

        });

        FloatingActionButton fab = (FloatingActionButton)  rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFileChooser();
            }
        });

        Button btnup = (Button) rootView.findViewById(R.id.btnupload);
        btnup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
                sessionManager.setProfileUsername(txtUsername.getText().toString());
            }
        });





        loadImageFromStorage(sessionManager.getProfilePath(), rootView);

        //Requesting storage permission
        requestStoragePermission();

        return rootView;
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




    private  void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData() != null) {

            filePath = data.getData();
            String path;
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);

                profilePic.setImageBitmap(bitmap);

                path = saveToInternalStorage(bitmap);
                sessionManager.setProfilePath(path);

                Log.e("path", path);
                mFilePath = path;

            }catch (IOException ie)
            {
                ie.printStackTrace();
            }
        }

    }

    /************************************************save picture in the internal storage***********************************************/
    private String saveToInternalStorage(Bitmap bitmapImage) throws IOException {
        ContextWrapper cw  = new ContextWrapper(getActivity());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        //create imageDir
        File mypath = new File(directory, "beecabprofile.jpg");

        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);

        }catch (IOException e)
        {
            e.printStackTrace();
        }finally {
            fos.close();
        }

        return directory.getAbsolutePath();


    }

    ///get the image where you left it

    private void loadImageFromStorage(String path, View rootView)
    {
        if(path!=null){
            try {
                File f=new File(path, "beecabprofile.jpg");
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                // ImageView img=(ImageView)rootView.findViewById(R.id.imgPicker);
                profilePic.setImageBitmap(b);
                bitmap = b;
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }

            txtUsername.setText(sessionManager.getProfileUsername());
            txtCellphone.setText(mGlobalRetainer.get_grClient().getMobile());

        }else {
            getProfileData();
        }


    }

    /**************************************************Retrieve profile from server*************************************************/
    private void updateUI(JSONArray profile)  {
        txtCellphone.setText(mGlobalRetainer.get_grClient().getMobile());

        String imageurl =null;
        try {
            JSONObject user = profile.getJSONObject(0);
            txtUsername.setText(user.getString(Config.KEY_USERNAME));

            //save the usernamein the prefmanager
            sessionManager.setProfileUsername(user.getString(Config.KEY_USERNAME));

            imageurl = user.getString(Config.TAG_IMAGE_URL);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(imageurl!=null)
        {
            //try to load with picasso
            // profilePic.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            Picasso.with(getActivity())
                    .load(imageurl)
                    .resize(200,200)
                    .centerInside().into(profilePic);

            try {
                //extract the bitmap ( working) maybe u should run it async
                URL url = new URL(imageurl);
                Bitmap downloadedpic = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                //Save the downloaded image in the device app data and save the path in the pref manag
                sessionManager.setProfilePath(saveToInternalStorage(downloadedpic));
                Toast.makeText(getActivity(), "Image saved",Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void getProfileData()
    {
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Please wait ","Fetching profile...",false,false);
        String url = String.format(Config.RETRIEVE_USER_PROFILE_URL,mGlobalRetainer.get_grClient().getId());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                JSONObject jsonResponse = null;


                try {

                    jsonResponse = new JSONObject(response);
                    boolean error = jsonResponse.getBoolean(Config.ERROR_RESPONSE);
                    String serverMsg = jsonResponse.getString(Config.MSG_RESPONSE);
                    if(!error)
                    {
                        //get the user detail from the server
                        JSONArray profile = jsonResponse.getJSONArray("profile");
                        //  JSONObject user = profile.getJSONObject(0); //getString(Config.KEY_ID)
                        // String carModel, String carNumberPlate, String currentCity, String profileRating

                        updateUI(profile);


                    }else {
                        Toast.makeText(getActivity(), serverMsg,Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(getActivity(),"Oops! Profile unreachable! Please check your internet connection", Toast.LENGTH_LONG).show();
            }
        }){

        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //Adding our request to the queue
        requestQueue.add(stringRequest);

    }


    /************************************************Load profile to server***********************************************/

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }



    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(getActivity(),"Saving the changes...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.UPLOAD_URL_EN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(getActivity(), "Saved" , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting username Name
                String name = txtUsername.getText().toString().trim();

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(Config.KEY_TC_ID, mGlobalRetainer._grClient.getId());
                params.put(Config.KEY_IMAGE, image);
                params.put(Config.KEY_USERNAME, name);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String text);
    }


    @Override
    public void onStart() {
        super.onStart();
        String username =( (EditProfile)getActivity()).getUsername();
        if(username!=null && !username.isEmpty())
        {
            txtUsername.setText(username);
        }
    }

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    //Requesting permission
    private void requestStoragePermission()
    {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
        {return;}

        if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }
    //This method will be called when the user will tap on allow or deny


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        //checking the request code
        if(requestCode == STORAGE_PERMISSION_CODE)
        {
            //if permission is granted
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }//Displaying a toast
            Toast.makeText(getActivity(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
        } else {
            //Displaying another toast if permission is not granted
            Toast.makeText(getActivity(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
        }

    }



}
