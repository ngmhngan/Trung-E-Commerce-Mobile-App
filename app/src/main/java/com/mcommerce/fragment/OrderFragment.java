package com.mcommerce.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mcommerce.nhom8.R;

public class OrderFragment extends Fragment {

    private View view;
    private Button btnComingOrder, btnHistoryOder;
    private ImageButton btnBack, btnCart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order,container,false);

        loadFragment(new ComingOrderFragment());
        linkview();
        initData();
        addEvent();
        return view;
    }

    private void addEvent() {
        btnHistoryOder.setOnClickListener(myClick);
        btnComingOrder.setOnClickListener(myClick);
    }

    private void initData() {
    }

    private void linkview() {
        btnBack = view.findViewById(R.id.btnBack_orderactivity);
        btnCart = view.findViewById(R.id.btnCart_orderactivity);
        btnComingOrder = view.findViewById(R.id.btnComingOrder_orderactivity);
        btnHistoryOder = view.findViewById(R.id.btnHistoryOrder_orderactivity);

        btnComingOrder.setEnabled(false);
        btnHistoryOder.setEnabled(true);
        btnComingOrder.setBackgroundResource(R.drawable.button_underline);
    }

    View.OnClickListener myClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if ( view.getId()==R.id.btnHistoryOrder_orderactivity){
                loadFragment(new HistoryOrderFragment());
                btnHistoryOder.setEnabled(false);
                btnComingOrder.setEnabled(true);
                btnHistoryOder.setBackgroundResource(R.drawable.button_underline);
                btnComingOrder.setBackgroundResource(R.color.white);

            } else if (view.getId()==R.id.btnComingOrder_orderactivity){

                loadFragment(new ComingOrderFragment());
                btnComingOrder.setEnabled(false);
                btnHistoryOder.setEnabled(true);
                btnComingOrder.setBackgroundResource(R.drawable.button_underline);
                btnHistoryOder.setBackgroundResource(R.color.white);
            }
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.containerOrderLists_orderactivity, fragment);
        transaction.commit();
    }
}
