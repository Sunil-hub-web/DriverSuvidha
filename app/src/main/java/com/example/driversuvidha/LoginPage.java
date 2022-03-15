package com.example.driversuvidha;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.driversuvidha.extra.AppUrl;
import com.example.driversuvidha.extra.SharedPrefManager;
import com.example.driversuvidha.modelclass.Login_ModelClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginPage extends AppCompatActivity {

    Button btn_signin;
    EditText edit_UserName,edit_Password;
    String str_UserName,str_Password;
    TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_signin = findViewById(R.id.btn_signin);
        edit_UserName = findViewById(R.id.edit_UserName);
        edit_Password = findViewById(R.id.edit_Password);
        forgotPassword = findViewById(R.id.forgotPassword);

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edit_UserName.getText().toString().trim().equals("")){

                    edit_UserName.setError("Fill The Details");

                }else if(edit_Password.getText().toString().trim().equals("")){

                    edit_Password.setError("Fill the details");

                }else{

                    str_UserName = edit_UserName.getText().toString().trim();
                    str_Password = edit_Password.getText().toString().trim();

                    userLogin(str_UserName,str_Password);
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginPage.this,ForgotPassword.class);
                startActivity(intent);
            }
        });


    }

    public void userLogin(String mobileNo,String password){

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Login Please Wait....");
        progressDialog.show();

        JSONObject jsonObject = new JSONObject();

        try{

            jsonObject.put("mobile",mobileNo);
            jsonObject.put("password",password);

        }catch (Exception e){

            e.printStackTrace();

        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppUrl.userLogin, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progressDialog.dismiss();

                try {
                    String status = response.getString("status");

                    if(status.equals("true")){

                        String message = response.getString("message");

                        Toast.makeText(LoginPage.this, message, Toast.LENGTH_SHORT).show();

                        String data = response.getString("data");

                        JSONObject jsonObject_data = new JSONObject(data);

                        String user_id = jsonObject_data.getString("user_id");
                        String email = jsonObject_data.getString("email");
                        String name = jsonObject_data.getString("name");
                        String mobile = jsonObject_data.getString("mobile");
                        String status1 = jsonObject_data.getString("status");
                        String password = edit_Password.getText().toString().trim();

                        Login_ModelClass login_modelClass = new Login_ModelClass(

                                user_id,name,email,mobile,status1,password
                        );

                        SharedPrefManager.getInstance(LoginPage.this).userLogin(login_modelClass);

                        Intent intent = new Intent(LoginPage.this,HomeDeshbord.class);
                        startActivity(intent);


                    }else if(status.equals("false")){

                        String message = response.getString("message");

                        Toast.makeText(LoginPage.this, message, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                error.printStackTrace();
                Toast.makeText(LoginPage.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjectRequest);
    }

    /*public void userLogin1(String mobileNo,String password){

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Login Please Wait....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.userLogin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    if(status.equals("true")){

                        Toast.makeText(LoginPage.this, "Login Success Fully", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginPage.this,HomeDeshbord.class);
                        startActivity(intent);

                    }else if(status.equals("false")){

                        String message = jsonObject.getString("message");

                        Toast.makeText(LoginPage.this, message, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                error.printStackTrace();
                Toast.makeText(LoginPage.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("mobile",mobileNo);
                params.put("password",password);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }
*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);

        /*this.finish();
        System.exit(0);*/
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (SharedPrefManager.getInstance(LoginPage.this).isLoggedIn()){

            Intent intent = new Intent(LoginPage.this,HomeDeshbord.class);
            startActivity(intent);
        }
    }
}