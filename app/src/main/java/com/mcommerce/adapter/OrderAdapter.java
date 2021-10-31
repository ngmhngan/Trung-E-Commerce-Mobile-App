package com.mcommerce.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mcommerce.model.OrderModel;
import com.mcommerce.model.Product;
import com.mcommerce.nhom8.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OderViewHolder> {

    public static final int HISTORY_ITEM = 1, COMING_ITEM = 0;

    private Context context;
    private int item_layout,
                type;
    private List<OrderModel> orderList;


    public OrderAdapter(Context context, int item_layout, List<OrderModel> orderList, int type) {
        this.context = context;
        this.item_layout = item_layout;
        this.orderList = orderList;
        this.type = type;
    }

    @NonNull
    @Override
    public OderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        switch(type) {
            case HISTORY_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_history_order_item,parent,false);
                break;
            case COMING_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_coming_order_item,parent,false);
                break;
        }
        return new OderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OderViewHolder holder, int position) {

        OrderModel order = orderList.get(position);

        if (order == null){
            return;
        }

        switch (type){
            case COMING_ITEM:
                holder.txtDes_iComingOrder.setText(String.valueOf(order.getPriceOrder())+" "+String.valueOf(order.getItemOrder().size())+"sản phẩm "+ order.getPaymentOrder());
                holder.txtDate_iComingOrder.setText(order.getDateOrder());
                holder.txtAddress_iComingOrder.setText(order.getAddOrder());
                Glide.with(context).load(order.getImgOrder()).into(holder.imv_iComingOrder);

                switch(order.getStatusOrder()) {
                    case OrderModel.DAT_HANG_THANH_CONG:
                        holder.txtSatus_iComingOrder.setText("Đặt hàng thành công");
                        break;
                    case OrderModel.XAC_NHAN:
                        holder.txtSatus_iComingOrder.setText("Xác nhận");
                        break;
                    case OrderModel.CHUAN_BI:
                        holder.txtSatus_iComingOrder.setText("Chuẩn bị");
                        break;
                    case OrderModel.DONG_GOI:
                        holder.txtSatus_iComingOrder.setText("Đóng gói");
                        break;
                    case OrderModel.VAN_CHUYEN:
                        holder.txtSatus_iComingOrder.setText("Vận chuyển");
                        break;
                }
                break;

            case HISTORY_ITEM:
                holder.txtID_iHistoryOrder.setText(order.getIdOrder());
                holder.txtDes_iHistoryOrder.setText(String.valueOf(order.getPriceOrder())+" "+String.valueOf(order.getItemOrder().size())+"sản phẩm "+ order.getPaymentOrder());
                holder.txtDate_iHistoryOrder.setText(order.getDateOrder());
                holder.txtAddress_iHistoryOrder.setText(order.getAddOrder());
                Glide.with(context).load(order.getImgOrder()).into(holder.imv_iHistoryOrder);

                switch(order.getStatusOrder()) {
                    case OrderModel.THANH_CONG:
                        holder.txtStatus_iHistoryOrder.setText("Hoàn thành");
                        holder.imvStatus_iHistoryOrder.setImageResource(R.drawable.ic_check_circle_fill);
                        break;
                    case OrderModel.DA_HUY:
                        holder.txtStatus_iHistoryOrder.setText("Đã hủy");
                        holder.imvStatus_iHistoryOrder.setImageResource(R.drawable.ic_x_circle_fill);
                        break;
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OderViewHolder extends RecyclerView.ViewHolder {

        TextView    txtID_iHistoryOrder,
                    txtDate_iHistoryOrder,
                    txtAddress_iHistoryOrder,
                    txtDes_iHistoryOrder,
                    txtStatus_iHistoryOrder;
        ImageView   imv_iHistoryOrder,
                    imvStatus_iHistoryOrder;
        Button      btnDatLai_iHistoryOrder;


        TextView    txtSatus_iComingOrder,
                    txtDate_iComingOrder,
                    txtAddress_iComingOrder,
                    txtDes_iComingOrder;
        ImageView   imv_iComingOrder;
        Button      btnChiTiet_iComingOrder;


        public OderViewHolder(@NonNull View itemView) {
            super(itemView);

            switch (type){
                case COMING_ITEM:
                    txtAddress_iComingOrder = itemView.findViewById(R.id.txtAddress_iComingOrder);
                    txtDate_iComingOrder = itemView.findViewById(R.id.txtDate_iComingOrder);
                    txtDes_iComingOrder = itemView.findViewById(R.id.txtDes_iComingOrder);
                    txtSatus_iComingOrder = itemView.findViewById(R.id.txtSatus_iComingOrder);
                    imv_iComingOrder = itemView.findViewById(R.id.imv_iComingOrder);
                    btnChiTiet_iComingOrder = itemView.findViewById(R.id.btnChiTiet_iComingOrder);
                    break;
                case HISTORY_ITEM:
                    txtAddress_iHistoryOrder = itemView.findViewById(R.id.txtAddress_iHistoryOrder);
                    txtDate_iHistoryOrder = itemView.findViewById(R.id.txtDate_iHistoryOrder);
                    txtDes_iHistoryOrder = itemView.findViewById(R.id.txtDes_iHistoryOrder);
                    txtID_iHistoryOrder = itemView.findViewById(R.id.txtID_iHistoryOrder);
                    txtStatus_iHistoryOrder = itemView.findViewById(R.id.txtStatus_iHistoryOrder);

                    imv_iHistoryOrder = itemView.findViewById(R.id.imv_iHistoryOrder);
                    imvStatus_iHistoryOrder = itemView.findViewById(R.id.imvStatus_iHistoryOrder);

                    btnDatLai_iHistoryOrder = itemView.findViewById(R.id.btnDatLai_iHistoryOrder);
                    break;
            }


        }
    }
}