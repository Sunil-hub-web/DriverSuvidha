package com.in.dsdriver.driver.drivers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.in.dsdriver.LoginPage;
import com.in.dsdriver.R;
import com.in.dsdriver.extra.AppUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasssword extends AppCompatActivity {

    EditText edit_Password,edit_ConfirmPassword;
    String str_Password,str_ConfirmPassword,driver_id;
    Button btn_ChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_passsword);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edit_Password = findViewById(R.id.edit_Password);
        edit_ConfirmPassword = findViewById(R.id.edit_ConfirmPassword);
        btn_ChangePassword = findViewById(R.id.btn_ChangePassword);

        Intent intent = getIntent();
        driver_id = intent.getStringExtra("driver_id");

        btn_ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(edit_Password.getText())){

                    edit_Password.setError("Fill The Details");

                }else if(TextUtils.isEmpty(edit_ConfirmPassword.getText())){

                    edit_ConfirmPassword.setError("Fill The Details");

                }else if(edit_Password.getText().toString().equals(edit_ConfirmPassword.getText().toString().trim())){

                    str_Password = edit_Password.getText().toString().trim();

                    changePassword_Driver(driver_id,str_Password);

                }else{

                    edit_ConfirmPassword.setError("Password not match");
                }

            }
        });
    }

    public void changePassword_Driver(String driverId,String password){

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Change Password Please Wait....");
        progressDialog.show();

        JSONObject jsonObject = new JSONObject();

        try{

            jsonObject.put("driver_id",driverId);
            jsonObject.put("password",password);

        }catch(Exception e){

            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppUrl.resetpassword, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progressDialog.dismiss();

                try {
                    String status = response.getString("status");

                    if(status.equals("success")){

                        String message = response.getString("message");
                        Toast.makeText(ChangePasssword.this, message, Toast.LENGTH_SHORT).show();
                        String driver_id = response.getString("driver_id");

                        Intent intent = new Intent(ChangePasssword.this, LoginPage.class);
                        startActivity(intent);
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
                Toast.makeText(ChangePasssword.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjectRequest);
    }


    public void changePassword_Owner(String password,String confirm_password,String cab_owner_id){

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Change Password Please Wait....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.change_password, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    if(status.equals("true")){

                        String message = jsonObject.getString("message");
                        Toast.makeText(ChangePasssword.this, message, Toast.LENGTH_SHORT).show();
                        // String driver_id = response.getString("driver_id");

                        Intent intent = new Intent(ChangePasssword.this, LoginPage.class);
                        startActivity(intent);
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
                Toast.makeText(ChangePasssword.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("password",password);
                params.put("confirm_password",confirm_password);
                params.put("cab_owner_id",cab_owner_id);

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

}