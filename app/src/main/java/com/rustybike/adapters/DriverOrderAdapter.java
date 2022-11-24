package com.rustybike.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.rustybike.R;
import com.rustybike.models.Order;
import com.rustybike.response.OrderListRes;

import java.util.List;

public class DriverOrderAdapter extends RecyclerView.Adapter<DriverOrderAdapter.UsersViewHolder> {

    Context context;
    List<Order> order;
    OrderListRes orderListResData;

    public DriverOrderAdapter(Context context, OrderListRes orderListResData) {
        this.orderListResData = orderListResData;
        this.context = context;
        order = orderListResData.getData();
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.order_card, null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        UsersViewHolder usersViewHolder = new UsersViewHolder(view);
        return usersViewHolder;
    }

    @Override
    public void onBindViewHolder(UsersViewHolder holder, final int position) {


        String id_string = order.get(position).getOrderRefNumber();
        String status_code = order.get(position).getOrderStatus();
        holder.order_num.setText(id_string);
        holder.count_tv.setText(""+(position+1));
        if (status_code.equals("R") || status_code.equals("r")){
            holder.status.setText("Ready to pick-up");
            holder.status.setTextColor(context.getResources().getColor(R.color.ready_to_pickup));
        }
        else {
            holder.status.setText("Driver Waiting");
            holder.status.setTextColor(context.getResources().getColor(R.color.app_color));
        }

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

        TextView order_num, status,count_tv;
        ImageView company_img;
        public UsersViewHolder(View itemView) {
            super(itemView);
            order_num = (TextView) itemView.findViewById(R.id.driver_orderNO_tv);
            status = (TextView) itemView.findViewById(R.id.driver_status_tv);
            count_tv =  itemView.findViewById(R.id.count_tv);
            company_img =(ImageView)itemView.findViewById(R.id.driver_company_img);

        }

    }

}

