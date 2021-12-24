package com.mcommerce.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.mcommerce.model.Order;
import com.mcommerce.model.User;
import com.mcommerce.nhom8.order.CartActivity;
import com.mcommerce.nhom8.order.OrderDetailActivity;
import com.mcommerce.nhom8.R;
import com.mcommerce.interfaces.RecyclerViewItemClickListener;
import com.mcommerce.util.Constant;
import com.mcommerce.util.Key;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OderViewHolder> {

    public static final int HISTORY_ITEM = 1, COMING_ITEM = 0, POINT = 2;

    private Context context;
    private int item_layout,
                type;
    long amount;
    private String s;
    private List<Order> orderList;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public OrderAdapter(Context context, int item_layout, List<Order> orderList, int type) {
        this.context = context;
        this.item_layout = item_layout;
        this.orderList = orderList;
        this.type = type;
    }

    @NonNull
    @Override
    public OderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(item_layout,parent,false);
        return new OderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OderViewHolder holder, int position) {
        if (orderList == null){
            return;
        } else {
            Order order = orderList.get(position);
            switch (type){
                case COMING_ITEM:
                    amount =0;

                    for (String i :  order.getItemOrder().keySet()) {
                        s = String.valueOf(order.getItemOrder().get(i).get("quantity"));
                        amount += Long.parseLong(s);
                    }
                    holder.txtPrice_iComingOrder.setText(""+order.getTotalOrder() +"đ");

                    holder.txtAmount_iComingOrder.setText("   |   " + amount +" sản phẩm");

                    holder.txtPayment_iComingOrder.setText("   |   " + order.getPaymentOrder());

                    holder.txtDate_iComingOrder.setText(order.getDateOrder());


                    holder.txtAddress_iComingOrder.setText(order.getAddOrder());
                    Glide.with(context).load(order.getImgOrder()).into(holder.imv_iComingOrder);

                    switch(order.getStatusOrder()) {
                        case Order.DAT_HANG_THANH_CONG:
                            holder.txtSatus_iComingOrder.setText("Đặt hàng thành công");
                            break;
                        case Order.XAC_NHAN:
                            holder.txtSatus_iComingOrder.setText("Xác nhận");
                            break;
                        case Order.CHUAN_BI:
                            holder.txtSatus_iComingOrder.setText("Chuẩn bị");
                            break;
                        case Order.DONG_GOI:
                            holder.txtSatus_iComingOrder.setText("Đóng gói");
                            break;
                        case Order.VAN_CHUYEN:
                            holder.txtSatus_iComingOrder.setText("Vận chuyển");
                            break;
                    }
                    holder.btnCancel.setOnClickListener(v ->{

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Bạn muốn hủy đơn ?");
                        builder.setNegativeButton("Hong muốn", (dialog, l) -> {
                            dialog.dismiss();
                        });
                        builder.setPositiveButton("Có", (dialog, l) -> {
                            DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference(Key.ORDER+"/"+order.getIdOrder()+"/"+Order.Status);
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(Key.USER+"/"+
                                    user.getUid() +"/"+
                                    User.Order+ "/"+
                                    order.getIdOrder()+"/"+
                                    Order.Status);
                            orderRef.setValue(Order.DA_HUY);
                            userRef.setValue(Order.DA_HUY);
                        });
                        builder.create().show();

                    });
                    break;

                case HISTORY_ITEM:
                    amount =0L;

                    for (String i :  order.getItemOrder().keySet()) {
                        s = String.valueOf(order.getItemOrder().get(i).get("quantity"));
                        amount += Long.parseLong(s);
                    }

                    holder.txtPrice_iHistoryOrder.setText(""+order.getTotalOrder() +"đ");
                    holder.txtAmount_iHistoryOrder.setText("   |   " + amount +" sản phẩm");
                    holder.txtPayment_iHistoryOrder.setText("   |   " + order.getPaymentOrder());
                    holder.txtID_iHistoryOrder.setText(order.getIdOrder());
                    holder.txtDate_iHistoryOrder.setText(order.getDateOrder());
                    holder.txtAddress_iHistoryOrder.setText(order.getAddOrder());
                    Glide.with(context).load(order.getImgOrder()).into(holder.imv_iHistoryOrder);

                    switch(order.getStatusOrder()) {
                        case Order.THANH_CONG:
                            holder.txtStatus_iHistoryOrder.setText("Hoàn thành");
                            holder.imvStatus_iHistoryOrder.setImageResource(R.drawable.ic_check_circle_fill);
                            break;
                        case Order.DA_HUY:
                            holder.txtStatus_iHistoryOrder.setText("Đã hủy");
                            holder.imvStatus_iHistoryOrder.setImageResource(R.drawable.ic_x_circle_fill);
                            break;
                    }
                    holder.btnDatLai_iHistoryOrder.setOnClickListener(v -> {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        for (HashMap<String,?> item : order.getItemOrder().values()){
                            ref.child(Key.USER).child(user.getUid()).child(User.Cart).child("id"+item.get("id")).child("quantity").setValue(ServerValue.increment((long) item.get("quantity")));
                            ref.child(Key.USER).child(user.getUid()).child(User.Cart).child("id"+item.get("id")).child("name").setValue(item.get("name"));
                            ref.child(Key.USER).child(user.getUid()).child(User.Cart).child("id"+item.get("id")).child("id").setValue(item.get("id"));
                            ref.child(Key.USER).child(user.getUid()).child(User.Cart).child("id"+item.get("id")).child("price").setValue(item.get("price"));
                        }
                        Intent intent = new Intent(context, CartActivity.class);
                        context.startActivity(intent);
                    });
                    break;
                case POINT:
                    holder.txtID_iPointsHistory.setText(order.getIdOrder());
                    holder.txtDate_iPointsHistory.setText(order.getDateOrder());
                    holder.txtPoints_iPointsHistory.setText(order.getRewardOrder()+"");
                    break;
            }

            holder.setItemClickListener(new RecyclerViewItemClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Intent intent= new Intent(context, OrderDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Constant.SELECTED_ORDER,order);
                    bundle.putSerializable(Constant.ITEMS_ORDER, (Serializable) order.getItemOrder());
                    intent.putExtra(Constant.ORDER_BUNDLE,bundle);
                    context.startActivity(intent);
                }
            });
        }



    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OderViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        TextView    txtID_iHistoryOrder,
                    txtDate_iHistoryOrder,
                    txtAddress_iHistoryOrder,
                    txtStatus_iHistoryOrder,
                    txtPrice_iHistoryOrder,
                    txtAmount_iHistoryOrder,
                    txtPayment_iHistoryOrder;
        ImageView   imv_iHistoryOrder,
                    imvStatus_iHistoryOrder;
        Button      btnDatLai_iHistoryOrder;
        Button      btnCancel;


        TextView    txtSatus_iComingOrder,
                    txtDate_iComingOrder,
                    txtAddress_iComingOrder,
                    txtPrice_iComingOrder,
                    txtAmount_iComingOrder,
                    txtPayment_iComingOrder,
                    txtDate_iPointsHistory,
                    txtID_iPointsHistory,
                    txtPoints_iPointsHistory;
        ImageView   imv_iComingOrder;

        private RecyclerViewItemClickListener itemClickListener;


        public OderViewHolder(@NonNull View itemView) {
            super(itemView);

            switch (type){
                case COMING_ITEM:
                    txtAddress_iComingOrder = itemView.findViewById(R.id.txtAddress_iComingOrder);
                    txtDate_iComingOrder = itemView.findViewById(R.id.txtDate_iComingOrder);
                    txtPrice_iComingOrder = itemView.findViewById(R.id.txtPrice_iComingOrder);
                    txtSatus_iComingOrder = itemView.findViewById(R.id.txtSatus_iComingOrder);
                    txtAmount_iComingOrder = itemView.findViewById(R.id.txtAmount_iComingOrder);
                    txtPayment_iComingOrder = itemView.findViewById(R.id.txtPayment_iComingOrder);
                    btnCancel = itemView.findViewById(R.id.btnCancel);

                    imv_iComingOrder = itemView.findViewById(R.id.imv_iComingOrder);
                    itemView.setOnClickListener(this);
                    break;
                case HISTORY_ITEM:
                    txtAddress_iHistoryOrder = itemView.findViewById(R.id.txtAddress_iHistoryOrder);
                    txtDate_iHistoryOrder = itemView.findViewById(R.id.txtDate_iHistoryOrder);
                    txtPrice_iHistoryOrder = itemView.findViewById(R.id.txtPrice_iHistoryOrder);
                    txtAmount_iHistoryOrder = itemView.findViewById(R.id.txtAmount_iHistoryOrder);
                    txtPayment_iHistoryOrder = itemView.findViewById(R.id.txtPayment_iHistoryOrder);

                    txtID_iHistoryOrder = itemView.findViewById(R.id.txtID_iHistoryOrder);
                    txtStatus_iHistoryOrder = itemView.findViewById(R.id.txtStatus_iHistoryOrder);

                    imv_iHistoryOrder = itemView.findViewById(R.id.imv_iHistoryOrder);
                    imvStatus_iHistoryOrder = itemView.findViewById(R.id.imvStatus_iHistoryOrder);

                    btnDatLai_iHistoryOrder = itemView.findViewById(R.id.btnDatLai_iHistoryOrder);
                    itemView.setOnClickListener(this);
                    break;
                case POINT:
                    txtDate_iPointsHistory = itemView.findViewById(R.id.txtDate_iPointsHistory);
                    txtID_iPointsHistory = itemView.findViewById(R.id.txtID_iPointsHistory);
                    txtPoints_iPointsHistory = itemView.findViewById(R.id.txtPoints_iPointsHistory);
                    itemView.setOnClickListener(this);
                    break;
            }
        }
        public void setItemClickListener(RecyclerViewItemClickListener itemClickListener)
        {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition());
        }
    }
}