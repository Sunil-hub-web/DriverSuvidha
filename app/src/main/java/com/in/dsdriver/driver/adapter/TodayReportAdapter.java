package com.in.dsdriver.driver.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.in.dsdriver.R;
import com.in.dsdriver.extra.AppUrl;
import com.in.dsdriver.extra.SharedPrefManager_Driver;
import com.in.dsdriver.driver.modelclass.TodayReport_ModelClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TodayReportAdapter extends RecyclerView.Adapter<TodayReportAdapter.ViewHolder> {

    Context context;
    ArrayList<TodayReport_ModelClass> reporttoday;
    String driverId;

    public TodayReportAdapter(Context context, ArrayList<TodayReport_ModelClass> todayReport) {

        this.context = context;
        this.reporttoday = todayReport;
    }

    @NonNull
    @Override
    public TodayReportAdapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todayreportdetails,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  TodayReportAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        TodayReport_ModelClass today_Report = reporttoday.get(position);

        driverId = SharedPrefManager_Driver.getInstance(context).getUser().getDriverID();


        String reportdate = today_Report.getDate();

        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {

            Date dateFromUser = fromUser.parse(reportdate); // Parse it to the exisitng date pattern and return Date type
            String dateMyFormat = myFormat.format(dateFromUser); // format it to the date pattern you prefer
            holder.text_Date.setText(dateMyFormat);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.liner.setVisibility(View.VISIBLE);
        holder.liner1.setVisibility(View.VISIBLE);
        holder.liner2.setVisibility(View.VISIBLE);
        holder.liner3.setVisibility(View.VISIBLE);
        holder.liner4.setVisibility(View.VISIBLE);
        holder.liner5.setVisibility(View.VISIBLE);
        holder.liner6.setVisibility(View.VISIBLE);
        holder.liner7.setVisibility(View.VISIBLE);


        if(today_Report.getDuty_type().equals("Local")){

            holder.text_Locality.setText("Locality :" +" "+ today_Report.getLocality());
            holder.text_Landmark.setText("Landmark :" +" "+ today_Report.getLandmark());
            holder.text_BookingType.setText(today_Report.getDuty_type());
            holder.text_Date.setText(today_Report.getDate());
            holder.text_ReportTime.setText(today_Report.getTime());

            holder.text_NoofDays.setText(today_Report.getNoofDays() +" "+"Day");
            holder.textNoofDays.setText("Day");

            holder.text_DutyHours.setText(today_Report.getDutyHour() +" "+"Hours");
            holder.text_Shift.setText(today_Report.getShift());
            holder.text_DropLoc.setText(today_Report.getDrop_locality());

            holder.text_CarDetails.setText(today_Report.getCar_details());
            holder.text_Remarks.setText(today_Report.getRemark());

            holder.liner7.setVisibility(View.GONE);
            holder.liner3.setVisibility(View.GONE);

        }else if(today_Report.getDuty_type().equals("Outstation")){

            holder.text_Locality.setText("Locality :" +" "+ today_Report.getLocality());
            holder.text_Landmark.setText("Landmark :" +" "+ today_Report.getLandmark());
            holder.text_BookingType.setText(today_Report.getDuty_type());
            holder.text_ReportTime.setText(today_Report.getTime());

            holder.text_Date.setText(today_Report.getDate());
            holder.text_NoofDays.setText(today_Report.getNoofDays());
            holder.textNoofDays.setText("No of Days");

            holder.text_ReturnDate.setText(today_Report.getReturn_date());
            holder.text_ToCity.setText(today_Report.getTo_city());

            holder.text_CarDetails.setText(today_Report.getCar_details());
            holder.text_Remarks.setText(today_Report.getRemark());

            holder.liner2.setVisibility(View.GONE);
            holder.liner4.setVisibility(View.GONE);
            holder.liner7.setVisibility(View.GONE);

        }else if(today_Report.getDuty_type().equals("Drop")){

            holder.text_Locality.setText("Locality :" +" "+ today_Report.getLocality());
            holder.text_Landmark.setText("Landmark :" +" "+ today_Report.getLandmark());
            holder.text_BookingType.setText(today_Report.getDuty_type());
            holder.text_Date.setText(today_Report.getDate());
            holder.text_ReportTime.setText(today_Report.getTime());

            holder.text_NoofDays.setText(today_Report.getDrop_city());
            holder.textNoofDays.setText("Drop City");
            holder.text_Charges.setText(today_Report.getDrop_city());

            holder.text_CarDetails.setText(today_Report.getCar_details());
            holder.text_Remarks.setText(today_Report.getRemark());

            holder.textCharges.setVisibility(View.VISIBLE);
            holder.text_Charges.setVisibility(View.VISIBLE);

            holder.liner4.setVisibility(View.GONE);
            holder.liner2.setVisibility(View.GONE);
            holder.liner3.setVisibility(View.GONE);

           /* holder.text_DutyHours.setVisibility(View.GONE);
            holder.text_Shift.setVisibility(View.GONE);
            holder.text_DropLoc.setVisibility(View.GONE);
            holder.textDutyHours.setVisibility(View.GONE);
            holder.textShift.setVisibility(View.GONE);
            holder.textDropLoc.setVisibility(View.GONE);
            holder.text_ReturnDate.setVisibility(View.GONE);
            holder.textReturnDate.setVisibility(View.GONE);*/

        }

        holder.text_acceptBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String bookingId = today_Report.getBooking_id();

                acceptBooking(driverId,bookingId);

                reporttoday.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return reporttoday.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_Locality,text_Landmark,textDate,text_Date,textReportTime,text_ReportTime,textBookingType,text_BookingType,
                textNoofDays,text_NoofDays,textShift,text_Shift,textDutyHours,text_DutyHours,textReturnDate,text_ReturnDate,
                textToCity,text_ToCity,textDropLoc,text_DropLoc,textCarDetails,text_CarDetails,textRemarks,text_Remarks,text_acceptBooking,
                textCharges,text_Charges;

        LinearLayout liner,liner1,liner2,liner3,liner4,liner5,liner6,liner7;

        public ViewHolder(@NonNull  View itemView) {
            super(itemView);

            text_Locality = itemView.findViewById(R.id.text_Locality);
            text_Landmark = itemView.findViewById(R.id.text_Landmark);
            textDate = itemView.findViewById(R.id.textDate);
            text_Date = itemView.findViewById(R.id.text_Date);
            textReportTime = itemView.findViewById(R.id.textReportTime);
            text_ReportTime = itemView.findViewById(R.id.text_ReportTime);
            textBookingType = itemView.findViewById(R.id.textBookingType);
            text_BookingType = itemView.findViewById(R.id.text_BookingType);
            textNoofDays = itemView.findViewById(R.id.textNoofDays);
            textShift = itemView.findViewById(R.id.textShift);
            text_Shift = itemView.findViewById(R.id.text_Shift);
            textDutyHours = itemView.findViewById(R.id.textDutyHours);
            text_NoofDays = itemView.findViewById(R.id.text_NoofDays);
            text_DutyHours = itemView.findViewById(R.id.text_DutyHours);
            text_acceptBooking = itemView.findViewById(R.id.text_acceptBooking);
            textReturnDate = itemView.findViewById(R.id.textReturnDate);
            text_ReturnDate = itemView.findViewById(R.id.text_ReturnDate);
            textToCity = itemView.findViewById(R.id.textToCity);
            text_ToCity = itemView.findViewById(R.id.text_ToCity);
            textDropLoc = itemView.findViewById(R.id.textDropLoc);
            text_DropLoc = itemView.findViewById(R.id.text_DropLoc);
            textCarDetails = itemView.findViewById(R.id.textCarDetails);
            text_CarDetails = itemView.findViewById(R.id.text_CarDetails);
            textRemarks = itemView.findViewById(R.id.textRemarks);
            text_Remarks = itemView.findViewById(R.id.text_Remarks);
            textCharges = itemView.findViewById(R.id.textCharges);
            text_Charges = itemView.findViewById(R.id.text_Charges);

            liner = itemView.findViewById(R.id.liner);
            liner1 = itemView.findViewById(R.id.liner1);
            liner2 = itemView.findViewById(R.id.liner2);
            liner3 = itemView.findViewById(R.id.liner3);
            liner4 = itemView.findViewById(R.id.liner4);
            liner5 = itemView.findViewById(R.id.liner5);
            liner6 = itemView.findViewById(R.id.liner6);
            liner7 = itemView.findViewById(R.id.liner7);
        }
    }

    public void acceptBooking(String driverId,String bookingId){

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Accept Booking Please Wait....");
        progressDialog.show();

        JSONObject jsonObject = new JSONObject();

        try{

            jsonObject.put("driver_id",driverId);
            jsonObject.put("booking_id",bookingId);

        }catch(Exception e){

            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppUrl.acceptBooking, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progressDialog.dismiss();

                try {
                    String status = response.getString("status");

                    if(status.equals("success")){
                        
                        String message = response.getString("message");
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

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
                Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjectRequest);
    }
}
