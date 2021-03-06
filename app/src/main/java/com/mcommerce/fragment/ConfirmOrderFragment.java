package com.mcommerce.fragment;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mcommerce.adapter.CartAdapter;
import com.mcommerce.model.Order;
import com.mcommerce.model.Product;
import com.mcommerce.model.User;
import com.mcommerce.nhom8.MainActivity;
import com.mcommerce.nhom8.R;
import com.mcommerce.util.Key;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ConfirmOrderFragment extends Fragment {
    public static final String ADDRESS = "address";
    public static final String PHONE = "phone";
    public static final String NAME = "name";
    View view;

    Button btnPayment;
    TextView txtName,
            txtTamTinh,
            txtPoint,
            txtDiscount,
            txtDiscountTitle,
            txtTotal,
            txtAddress,
            txtPhone,
            txtChangeInfo;
    ImageButton btnBack;
    RecyclerView rcv;
    Switch swUsePoint;
    RadioButton radCash;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference ref;
    DataSnapshot userSnapshot, productSnapshot;

    User mUser = new User();
    Product product = new Product();

    String phone, name, address, editAddress, editName, editPhone;
    HashMap<String, HashMap<String,?>> cartList = new HashMap<>();
    CartAdapter adapter;
    long sum, point, total, discount, ship;
    boolean isClear = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_confirm_order, container, false);
        linkView();
        loadData();
        addEvent();
        Bundle receiveBundle = getArguments();
        if (receiveBundle!= null){
            editAddress = receiveBundle.getString(ADDRESS);
            editName = receiveBundle.getString(NAME);
            editPhone = receiveBundle.getString(PHONE);
        }
        return view;
    }

    private void linkView() {
        btnPayment = view.findViewById(R.id.btnPayment);
        txtName = view.findViewById(R.id.txtName);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtPhone = view.findViewById(R.id.txtPhone);
        txtChangeInfo = view.findViewById(R.id.txtChangeInfo);

        rcv = view.findViewById(R.id.rcv);
        rcv.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        swUsePoint = view.findViewById(R.id.swUsePoint);
        txtTamTinh = view.findViewById(R.id.txtTamTinh);
        txtPoint = view.findViewById(R.id.txtPoint);
        txtDiscount = view.findViewById(R.id.txtDiscount);
        txtDiscountTitle = view.findViewById(R.id.txtDiscountTitle);
        txtTotal  = view.findViewById(R.id.txtTotal);
        radCash = view.findViewById(R.id.radCash);
        btnPayment = view.findViewById(R.id.btnPayment);
        btnBack=view.findViewById(R.id.btnBack);
    }

    private void loadData() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        if (user!=null) {
            ref = firebaseDatabase.getReference();
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userSnapshot = snapshot.child(Key.USER).child(user.getUid());
                    mUser = userSnapshot.getValue(User.class);
                    cartList = (HashMap<String, HashMap<String, ?>>) mUser.getUserCart();

                    String idFirstItem = cartList.keySet().toArray()[0].toString();
                    String idProduct = String.valueOf(cartList.get(idFirstItem).get("id"));
                    if (cartList != null){
                        productSnapshot = snapshot.child(Key.PRODUCT).child(idProduct).child(Product.Image);
                        product.setProductImg(productSnapshot.getValue().toString());
                        loadUI();
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(),error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void loadUI() {
        //region th??ng tin ng?????i d??ng

        if (editName!=null && !editName.equals("")){
            name = editName;
        } else {
            name = user.getDisplayName();
        }

        if (editPhone != null && !editPhone.equals("")){
            phone = editPhone;
        } else {
            phone= user.getPhoneNumber();
        }

        if (editAddress != null && !editAddress.equals("")) {
            address = editAddress;
        } else {
            if (mUser.getUserAddress() != null){
                address = mUser.getUserAddress();
            }
        }

        txtName.setText(name);
        txtPhone.setText(phone);
        txtAddress.setText(address != null ? address: "");

        //endregion

        //region th??ng tin gi??? h??ng
        cartList = (HashMap<String, HashMap<String, ?>>) mUser.getUserCart();
        adapter = new CartAdapter(getContext(),cartList,R.layout.item_product_orderdetail_layout);
        rcv.setAdapter(adapter);
        //endregion

        //region th??ng tin thanh to??n
        sum=0L;
        for (HashMap<String, ?> i: cartList.values()) {
            if (i.get("quantity") != null && i.get("price") != null){
                sum = sum + (long) i.get("quantity") * (long) i.get("price");
            }
        }
        txtTamTinh.setText(sum+" ??");
        ship = 15000L;
        total = sum+ship;
        txtTotal.setText(total+" ??");

        if (mUser.getUserPoint()==0L){
            txtDiscount.setVisibility(View.GONE);
            txtDiscountTitle.setVisibility(View.GONE);
            swUsePoint.setVisibility(View.GONE);
        }
        //endregion
    }

    private void addEvent() {
        swUsePoint.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                discount = (sum+ship- mUser.getUserPoint()*10) < 0 ? (sum+ship) :  mUser.getUserPoint()*10;
                point = discount <  mUser.getUserPoint()*10 ? discount/10 :  mUser.getUserPoint();
                total = sum+ship-discount;

                txtPoint.setVisibility(View.VISIBLE);
                txtDiscount.setVisibility(View.VISIBLE);
                txtDiscount.setText(discount+" ??");
                txtDiscountTitle.setVisibility(View.VISIBLE);

                txtPoint.setText("??p d???ng "+point+" ??i???m t??ch l??y");

                txtTotal.setText(total+" ??");
            } else {
                total = sum+ship;
                txtPoint.setVisibility(View.GONE);
                txtDiscount.setVisibility(View.GONE);
                txtDiscountTitle.setVisibility(View.GONE);
                txtTotal.setText(total+" ??");
            }
        });

        btnPayment.setOnClickListener(v -> {
            if (txtAddress.getText().toString().equals("")){
                txtAddress.setError("Vui l??ng nh???p ?????a ch???");
            } else {
                if (radCash.isChecked()){
                    createrOrder();
                } else {
                    Dialog commingsoon = new Dialog(getContext());
                    commingsoon.setContentView(R.layout.diaglog_comming_soon);
                    commingsoon.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Button btnOk=commingsoon.findViewById(R.id.btnOK);

                    btnOk.setOnClickListener(l -> {
                        commingsoon.dismiss();
                        radCash.setChecked(true);
                    });

                    commingsoon.show();
                }
            }
        });
        txtAddress.setOnClickListener(editInfomation);
        txtChangeInfo.setOnClickListener(editInfomation);

        btnBack.setOnClickListener(view -> getActivity().finish());

    }

    View.OnClickListener editInfomation = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            Bundle bundle = new Bundle();
            if (txtAddress.getText().toString().equals("Vui l??ng ch???n ?????a ch???")) {
                bundle.putString(ADDRESS, null);

            } else bundle.putString(ADDRESS, txtAddress.getText().toString());
            bundle.putString(PHONE, phone);
            bundle.putString(NAME, name);
            ChangeCustomerFragment changeCustomerFragment = new ChangeCustomerFragment();
            changeCustomerFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.layoutContainer, changeCustomerFragment).addToBackStack(ChangeCustomerFragment.TAG).commit();
        }
    };


    private void createrOrder() {
        Order order = new Order();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Timestamp ts = new Timestamp(date.getTime());
        order.setDateLongOrder(ts.getTime());
        order.setDateOrder(simpleDateFormat.format(ts));
        if (txtDiscountTitle.getVisibility()==View.GONE) order.setDiscountOrder(0);
         else order.setDiscountOrder((int) discount);
        order.setIdOrder(user.getUid()+ts.getTime());
        order.setItemOrder(cartList);
        order.setPaymentOrder(Order.CASH);
        order.setPriceOrder(sum);
        order.setStatusOrder(Order.DAT_HANG_THANH_CONG);
        order.setUidOrder(user.getUid());
        order.setImgOrder(product.getProductImg());
        order.setShippingFeeOrder(ship);
        order.setCustomerName(name);
        order.setCustomerPhone(phone);
        order.setAddOrder(address);
        order.setTotalOrder(total);
        if (order.getDiscountOrder()==0) order.setRewardOrder(sum/100);
         else order.setRewardOrder(0);
        ref = firebaseDatabase.getReference().child(Key.ORDER).child(order.getIdOrder());
        ref.setValue(order).addOnSuccessListener(task -> {
            isClear= true;
            ref = firebaseDatabase.getReference().child(Key.USER).child(user.getUid()).child(User.Cart);
            ref.removeValue();
            order.setUidOrder(null);
            ref = firebaseDatabase.getReference().child(Key.USER).child(user.getUid()).child(User.Order).child(order.getIdOrder());
            ref.setValue(order).addOnCompleteListener(task1 -> {
                if (task1.isSuccessful()){
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra(MainActivity.SELECTED_FRAGMENT,MainActivity.ORDER_FRAGMENT);
                    startActivity(intent);
                    Toast.makeText(getActivity(), "?????t h??ng th??nh c??ng", Toast.LENGTH_SHORT).show();
                    getActivity().finishAffinity();
                }
                else {
                    Toast.makeText(getContext(), task1.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
