package com.rustybike.adapters;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.rustybike.response.OrderListRes;
import com.rustybike.response.UpdateOrderRes;
import com.rustybike.retrofit.Api;
import com.rustybike.retrofit.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantOrderAdapter extends RecyclerView.Adapter<RestaurantOrderAdapter.UsersViewHolder> {

    Context context;
    List<Order> order;
    String token;
    int ID;
    OrderListRes orderListResData;
    ApiInterface apiInterface;

    public RestaurantOrderAdapter(Context context, OrderListRes orderListResData, String token,int ID) {
        this.orderListResData = orderListResData;
        this.context = context;
        this.token = token;
        this.ID = ID;
        order = orderListResData.getData();
        apiInterface = Api.getClient();
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.order2_card, null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        UsersViewHolder usersViewHolder = new UsersViewHolder(view);
        return usersViewHolder;
    }

    @Override
    public void onBindViewHolder(final UsersViewHolder holder, final int position) {


        final String id_string = order.get(position).getOrderRefNumber();
        String status_code = order.get(position).getOrderStatus();
        final String orderDate = order.get(position).getOrderDate();
        holder.order_num.setText(id_string);
        if (status_code.equals("R") || status_code.equals("r")){
            holder.status.setText("Ready to pick-up");
            holder.status.setTextColor(context.getResources().getColor(R.color.ready_to_pickup));
            holder.action_btn.setVisibility(View.INVISIBLE);
        }
        else{
            holder.status.setText("Driver Waiting");
            holder.action_btn.setVisibility(View.VISIBLE);

            holder.status.setTextColor(context.getResources().getColor(R.color.app_color));
        }
        holder.count_tv.setText(""+(position+1));
        int company_id = order.get(position).getOrderCompanyId();
        if (company_id == 1){
            holder.company_img.setImageResource(R.drawable.delivro_icon);
        }else if (company_id == 2){
            holder.company_img.setImageResource(R.drawable.uber_icon);
        }
        else if (company_id == 3){
            holder.company_img.setImageResource(R.drawable.justeat_icon);
        }


        final int order_id=order.get(position).getOrderId();
        final String Order_status="R";
        holder.action_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.status.getText().equals("Driver Waiting")){
                final ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Please Wait");
                progressDialog.show();
                final Call<UpdateOrderRes> resultCall = apiInterface.UpdateOrder(token,ID,order_id,Order_status,orderDate);
                resultCall.enqueue(new Callback<UpdateOrderRes>() {
                    @Override
                    public void onResponse(Call<UpdateOrderRes> call, Response<UpdateOrderRes> response) {
                        progressDialog.dismiss();
                        if (response.body().getResult().getCode() == 0){
                            holder.action_btn.setText("Delivered");
                            holder.action_btn.setBackgroundResource(R.drawable.delivered_btn_bg);
                            holder.status.setText("DriverOrdersActivity Delivered");
                            holder.status.setTextColor(context.getResources().getColor(R.color.blue_txt));
                            holder.status.setText("Ready to pick-up");
                            holder.status.setTextColor(context.getResources().getColor(R.color.ready_to_pickup));
                            holder.action_btn.setVisibility(View.INVISIBLE);

                        }
                        else {
                            Toast.makeText(context,response.body().getResult().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateOrderRes> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(context,t.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
                else {
                    Toast.makeText(context,"DriverOrdersActivity already sent",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }



    @Override
    public int getItemCount() {
        return order.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {

        TextView order_num, status,count_tv;
        Button action_btn;
        ImageView company_img;
        public UsersViewHolder(View itemView) {
            super(itemView);
            order_num = (TextView) itemView.findViewById(R.id.order_no_txt);
            status = (TextView) itemView.findViewById(R.id.status_txt);
            action_btn = itemView.findViewById(R.id.action_btn);
            count_tv = itemView.findViewById(R.id.count_tv);
            company_img =(ImageView)itemView.findViewById(R.id.company_img);

        }

    }

}
