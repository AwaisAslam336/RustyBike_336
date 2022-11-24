package com.rustybike.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.rustybike.R;
import com.rustybike.models.Order;
import com.rustybike.models.Result;
import com.rustybike.response.OrderListRes;
import com.rustybike.response.UpdateOrderRes;
import com.rustybike.retrofit.Api;
import com.rustybike.retrofit.ApiInterface;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantHistoryAdapter extends RecyclerView.Adapter<RestaurantHistoryAdapter.UsersViewHolder> {

    Context context;
    List<Order> order;
    OrderListRes orderListResData;
    ApiInterface apiInterface;
    String token;
    int ID;
    public RestaurantHistoryAdapter(Context context, OrderListRes orderListResData, String token,int ID) {
        this.orderListResData = orderListResData;
        this.context = context;
        order = orderListResData.getData();
        this.token = token;
        this.ID = ID;
        apiInterface = Api.getClient();
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.history_card, null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        UsersViewHolder usersViewHolder = new UsersViewHolder(view);
        return usersViewHolder;
    }

    @Override
    public void onBindViewHolder(final UsersViewHolder holder, final int position) {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        final String formattedDate = df.format(c.getTime());
        final String id_string = order.get(position).getOrderRefNumber();
        final String orderStatus = order.get(position).getOrderStatus();
        holder.count_tv.setText(""+(position+1));
        holder.order_num.setText(id_string);

        final int order_id=order.get(position).getOrderId();
        holder.action_btn_moveBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Please Wait");
                progressDialog.show();
                final Call<UpdateOrderRes> resultCall = apiInterface.UpdateOrder(token,ID,order_id,orderStatus,formattedDate);
                resultCall.enqueue(new Callback<UpdateOrderRes>() {
                    @Override
                    public void onResponse(Call<UpdateOrderRes> call, Response<UpdateOrderRes> response) {
                        progressDialog.dismiss();
                        if (response.body().getResult().getCode()==0){
                            Toast.makeText(context,"Order Moved Back",Toast.LENGTH_SHORT).show();
                            ((callback) context).setvalue();
                        }
                        else {
                            Toast.makeText(context,response.body().getResult().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateOrderRes> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(context,"Network Error",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        holder.action_btn_delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogB =new AlertDialog.Builder(context);
                alertDialogB.setTitle("Delete Confirmation");
                alertDialogB.setIcon(R.drawable.ic_priority_high_black_24dp);
                alertDialogB.setMessage("Alert: Are you sure, you want to delete order permanently!");
                alertDialogB.setCancelable(false);
                alertDialogB.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Call<Result> resultCall = apiInterface.DeletOrder(token,ID,order_id);
                        resultCall.enqueue(new Callback<Result>() {
                            @Override
                            public void onResponse(Call<Result> call, Response<Result> response) {

                                if (response.body().getCode() == 0){
                                    Toast.makeText(context,"DriverOrdersActivity deleted",Toast.LENGTH_SHORT).show();
                                    ((callback) context).setvalue();
                                }
                                else {
                                    Toast.makeText(context,response.body().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Result> call, Throwable t) {
                                Toast.makeText(context,"Network Error",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                alertDialogB.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog1 = alertDialogB.create();
                alertDialog1.show();
            }
        });

        int company_id = order.get(position).getOrderCompanyId();
        if (company_id == 1){
            holder.company_img.setImageResource(R.drawable.delivro_icon);
        }else if (company_id == 2){
            holder.company_img.setImageResource(R.drawable.uber_icon);
        }
        else if (company_id == 3){
            holder.company_img.setImageResource(R.drawable.justeat_icon);
        }

    }



    @Override
    public int getItemCount() {
        return order.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {

        TextView order_num,count_tv;
        Button action_btn_moveBack,action_btn_delet;
        ImageView company_img;
        public UsersViewHolder(View itemView) {
            super(itemView);
            order_num = (TextView) itemView.findViewById(R.id.history_card_txt);
            action_btn_moveBack = itemView.findViewById(R.id.history_card_moveback_btn);
            action_btn_delet = itemView.findViewById(R.id.history_card_delet_btn);
            count_tv = itemView.findViewById(R.id.count_tv);
            company_img =(ImageView)itemView.findViewById(R.id.history_card_img);

        }

    }
    public interface callback{
        public void setvalue();
    }
}

