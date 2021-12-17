package com.mcommerce.nhom8.recipe;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.mcommerce.model.Recipe;
import com.mcommerce.nhom8.R;
import com.mcommerce.util.Constant;

import java.util.HashMap;
import java.util.List;

public class EachRecipeActivity extends AppCompatActivity {

    Button btnAddToCart_Recipe;
    ImageView imvDropDownMaterial, imvRecipe;
    TextView txtPreparedMaterials_Recipe,txtRecipe_Info_recipe, txtRecipeName_recipe, txtShortRecipe, txtDescription;
    LinearLayout llMaterialBuying;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    Recipe recipe = new Recipe();
    HashMap<String,HashMap<String,?>> recipeIngredient;
    Chip chip;
    ChipGroup chipGroup;
    ChipDrawable chipDrawable;
    List<Integer> filter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_recipe);
        linkViews();
        getData();
        initUI();
        addEvents();
    }


    private void linkViews() {
        chipGroup=findViewById(R.id.chip_group);

        llMaterialBuying=findViewById(R.id.llMaterialBuying);
        llMaterialBuying.setVisibility(View.GONE);
        imvDropDownMaterial=findViewById(R.id.imvDropDownMaterial);
        txtPreparedMaterials_Recipe=findViewById(R.id.txtPreparedMaterials_Recipe);
        txtRecipe_Info_recipe=findViewById(R.id.txtRecipe_Info_recipe);
        txtShortRecipe = findViewById(R.id.txtShortRecipe);
        txtDescription = findViewById(R.id.txtDescription);
        txtRecipeName_recipe = findViewById(R.id.txtRecipeName_recipe);
        btnAddToCart_Recipe = findViewById(R.id.btnAddToCart_Recipe);

        imvRecipe = findViewById(R.id.imvRecipe);
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Constant.RECIPE_BUNDLE);
        recipe = bundle.getParcelable(Constant.SECLECTED_RECIPE);
        filter = bundle.getIntegerArrayList(Constant.FILTER_OPTION);
        recipeIngredient= (HashMap<String, HashMap<String, ?>>) bundle.getSerializable(Constant.ITEMS_INGREDIENT);

    }
    private void addEvents() {
        imvDropDownMaterial.setOnClickListener(clickSetVisibility);
        txtPreparedMaterials_Recipe.setOnClickListener(clickSetVisibility);
        btnAddToCart_Recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int productID: chipGroup.getCheckedChipIds()) {
                    ref.child("User").child(user.getUid()).child("userCart").child("id"+productID).child("quantity").setValue(ServerValue.increment(1));
                    ref.child("User").child(user.getUid()).child("userCart").child("id"+productID).child("name").setValue(recipeIngredient.get("id"+productID).get("name"));
                    ref.child("User").child(user.getUid()).child("userCart").child("id"+productID).child("id").setValue(productID);
                    ref.child("User").child(user.getUid()).child("userCart").child("id"+productID).child("price").setValue(recipeIngredient.get("id"+productID).get("price"));
                    Toast.makeText(EachRecipeActivity.this, "Thêm hàng thành công",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

     View.OnClickListener clickSetVisibility=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(llMaterialBuying.getVisibility()==view.VISIBLE){
                llMaterialBuying.setVisibility(View.GONE);
                imvDropDownMaterial.setImageResource(R.drawable.ic_arrow_down_24);
            }else {
                llMaterialBuying.setVisibility(View.VISIBLE);
                imvDropDownMaterial.setImageResource(R.drawable.ic__arrow_up_24);
            }
        }
    };

    private void initUI() {
        txtRecipeName_recipe.setText("Công thức làm "+recipe.getRecipeName());
        Glide.with(EachRecipeActivity.this).load(recipe.getRecipeImage()).into(imvRecipe);
        for (HashMap<String,?>  ingredient: recipeIngredient.values()) {
            chip = new Chip(this);
            chip.setText(ingredient.get("name").toString());
            chip.setId(  ( (Long)ingredient.get("id") ).intValue() );

            if (filter!= null && filter.contains(chip.getId())){
                chipDrawable= ChipDrawable.createFromAttributes(this,
                        null,
                        0,
                        R.style.available_chip);
                chip.setTextColor(ContextCompat.getColorStateList(this, R.color.available_chip_text_selector));
            } else {
                chipDrawable= ChipDrawable.createFromAttributes(this,
                        null,
                        0,
                        R.style.unavailable_chip);
                chip.setTextColor(ContextCompat.getColorStateList(this, R.color.unavailable_chip_text_selector));
            }
            chip.setCheckedIconTint(ContextCompat.getColorStateList(this,R.color.white));

            chip.setChipDrawable(chipDrawable);
            chip.setEnsureMinTouchTargetSize(false);
            chipGroup.addView(chip);
        }
        String  khauphan = "Khẩu phần: ",
                khauphan_content = recipe.getRecipeRation() + " người",
                thoigian = " - Thời gian: ",
                thoigian_content = recipe.getRecipeTime()+ " phút",
                mucdo = " - Mức độ: ",
                mucdo_content = recipe.getRecipeLevel();
        SpannableStringBuilder info = new SpannableStringBuilder(khauphan+ khauphan_content +
                thoigian + thoigian_content +
                mucdo + mucdo_content );
        int[] lengths = new int[]{khauphan.length(), khauphan_content.length(), thoigian.length(), thoigian_content.length(), mucdo.length(), mucdo_content.length()};
        int s =0, e=0;
        for (int i=0; i<lengths.length; i++) {
            e = s + lengths[i];
            if (i % 2 == 0) {
                info.setSpan(new TextAppearanceSpan(this, R.style.sfpro_medium_style), s, e, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                info.setSpan(new TextAppearanceSpan(this, R.style.sfpro_bold_style), s, e, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            s = e;
        }
        txtRecipe_Info_recipe.setText(info);
        txtShortRecipe.setText(recipe.getRecipeShortDescription());

        String[] steps = recipe.getRecipeDescription().split("#");
        String step, step_content;
        SpannableStringBuilder instruction = new SpannableStringBuilder();
        String content= "";
        s =0; int e2 = 0;
        for (int i =0; i<steps.length; i=i+2){
            step = steps[i]; step_content = steps[i+1]+"\n";
            e = s+ step.length();
            e2 = e + step_content.length();
            content = step+step_content;
            instruction.append(content);
            instruction.setSpan(new TextAppearanceSpan(this, R.style.step_title), s, e, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            instruction.setSpan(new TextAppearanceSpan(this, R.style.step_content), e, e2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            s = e2;
        }
        txtDescription.setText(instruction);
    }

}