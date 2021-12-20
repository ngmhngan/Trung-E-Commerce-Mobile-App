package com.mcommerce.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mcommerce.nhom8.auth.LoginActivity;
import com.mcommerce.nhom8.R;
import com.mcommerce.nhom8.UserInfoActivity;

public class UserFragment extends Fragment {

    private View view;
    private LinearLayout llSupport_fmuser,
            llSetting_fmuser,
            llChinhSach_fmuser,
            llPoint_fmuser,
            llUserInfo_fmuser;
    private TextView txtUserName_fmuser,
            txtUserPoint_fmuser;

    private ImageView imv_fmuser;
    private Button btnLogout_fmuser;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_user, container, false);
        linkview();
        loadUserInfo();
        addEvent();
        return view;
    }

    private void linkview() {
        llSupport_fmuser = view.findViewById(R.id.llSupport_fmuser);
        llSetting_fmuser = view.findViewById(R.id.llSetting_fmuser);
        llChinhSach_fmuser = view.findViewById(R.id.llChinhSach_fmuser);
        llPoint_fmuser = view.findViewById(R.id.llPoint_fmuser);
        llUserInfo_fmuser = view.findViewById(R.id.llUserInfo_fmuser);

        txtUserName_fmuser = view.findViewById(R.id.txtUserName_fmuser);
        txtUserPoint_fmuser = view.findViewById(R.id.txtUserPoint_fmuser);

        imv_fmuser = view.findViewById(R.id.imv_fmuser);

        btnLogout_fmuser = view.findViewById(R.id.btnLogOut_fmuser);
    }

    private void loadUserInfo() {

        if (user == null) {
            btnLogout_fmuser.setText("Đăng nhập");
            return;
        }

        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri uri = user.getPhotoUrl();

        txtUserPoint_fmuser.setText("900 điểm");
        txtUserName_fmuser.setText(name);
        Glide.with(this).load(uri).error(R.drawable.default_ava).into(imv_fmuser);
    }

    private void addEvent() {

        llUserInfo_fmuser.setOnClickListener(goToContentActivity);
        llPoint_fmuser.setOnClickListener(pointsHistory);
//        llSetting_fmuser.setOnClickListener(settingActivity);

        if (user == null) {
            btnLogout_fmuser.setOnClickListener(signin);
        } else {
            btnLogout_fmuser.setOnClickListener(logout);
        }
    }

    View.OnClickListener logout = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
    };

    View.OnClickListener signin = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
    };

    View.OnClickListener pointsHistory = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            if (v.getId() == R.id.llPoint_fmuser){
//                startActivity(new Intent(getActivity(), PointsHistoryFragment.class));
//            }
//        }
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.lv_fragmentHistoryOrder, getParentFragment());
            transaction.commit();
        }
    };

//    View.OnClickListener settingActivity = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            startActivity(new Intent(getActivity(), SettingActivity.class));
//            getActivity().finish();
//        }
//    };



        View.OnClickListener goToContentActivity = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.llUserInfo_fmuser) {
                    startActivity(new Intent(getActivity(), UserInfoActivity.class));
                }
            }
        };
}
