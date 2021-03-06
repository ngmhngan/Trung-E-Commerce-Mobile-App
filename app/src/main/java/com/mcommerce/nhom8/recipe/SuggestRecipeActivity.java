package com.mcommerce.nhom8.recipe;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mcommerce.adapter.SearchIngredientResultAdapter;
import com.mcommerce.model.Ingredient;
import com.mcommerce.model.Product;
import com.mcommerce.nhom8.R;
import com.mcommerce.util.Constant;
import com.mcommerce.util.Key;
import java.util.ArrayList;
import java.util.List;

public class SuggestRecipeActivity extends AppCompatActivity {

    ChipGroup   chipBots,
                chipBos,
                chipKhacs,
                chipSuaKems,
            resultChipCGroup;
    ImageView imvDropDownBot_SuggestRecipe,
            imvDropDownSuaKem_SuggestRecipe,
            imvDropDownBo_SuggestRecipe,
            imvDropDownKhac_SuggestRecipe;
    Button btnSearch;
    ImageButton btnBack_allproducts;
    SearchView searchView;

    ListView lvResult;
    SearchIngredientResultAdapter adapter;

    List<Ingredient> bots, suakems, bos, khacs, results, total;

    List<Integer> filter = new ArrayList<>();

    LinearLayout llBot_RecipeMaterial,
            llSua_RecipeMaterial,
            llBo_RecipeMaterial,
            llKhac_RecipeMaterial,
            llFilter;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    Query query = ref.child(Key.PRODUCT).orderByChild(Product.Type).equalTo(Product.NGUYEN_lIEU);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_recipe);
        linkViews();
        initData();
        addEvents();
    }

    private void linkViews() {
        chipBots = findViewById(R.id.chipBots);
        chipBos = findViewById(R.id.chipBos);
        chipKhacs = findViewById(R.id.chipKhacs);
        chipSuaKems = findViewById(R.id.chipSuaKems);
        resultChipCGroup = findViewById(R.id.resultChipCGroup);

        llBot_RecipeMaterial = findViewById(R.id.llBot_RecipeMaterial);
        llSua_RecipeMaterial = findViewById(R.id.llSuaKem_RecipeMaterial);
        llBo_RecipeMaterial = findViewById(R.id.llBo_RecipeMaterial);
        llKhac_RecipeMaterial = findViewById(R.id.llKhac_RecipeMaterial);

        imvDropDownBot_SuggestRecipe = findViewById(R.id.imvDropDownBot_SuggestRecipe);
        imvDropDownSuaKem_SuggestRecipe = findViewById(R.id.imvDropDownSuaKem_SuggestRecipe);
        imvDropDownBo_SuggestRecipe = findViewById(R.id.imvDropDownBo_SuggestRecipe);
        imvDropDownKhac_SuggestRecipe = findViewById(R.id.imvDropDownKhac_SuggestRecipe);
        searchView = findViewById(R.id.searchView);
        llFilter = findViewById(R.id.llFilter);

        btnSearch = findViewById(R.id.btnSearch);
        lvResult = findViewById(R.id.lvResult);

        btnBack_allproducts = findViewById(R.id.btnBack_allproducts);
    }

    private void addEvents() {
        imvDropDownBot_SuggestRecipe.setOnClickListener(view -> {
            if (llBot_RecipeMaterial.getVisibility() == View.VISIBLE) {
                llBot_RecipeMaterial.setVisibility(View.GONE);
                imvDropDownBot_SuggestRecipe.setImageResource(R.drawable.ic_arrow_down_24);
            } else {
                llBot_RecipeMaterial.setVisibility(View.VISIBLE);
                imvDropDownBot_SuggestRecipe.setImageResource(R.drawable.ic__arrow_up_24);
            }
        });

        imvDropDownSuaKem_SuggestRecipe.setOnClickListener(view -> {
            if (llSua_RecipeMaterial.getVisibility() == View.VISIBLE) {
                llSua_RecipeMaterial.setVisibility(View.GONE);
                imvDropDownSuaKem_SuggestRecipe.setImageResource(R.drawable.ic_arrow_down_24);
            } else {
                llSua_RecipeMaterial.setVisibility(View.VISIBLE);
                imvDropDownSuaKem_SuggestRecipe.setImageResource(R.drawable.ic__arrow_up_24);
            }
        });

        imvDropDownBo_SuggestRecipe.setOnClickListener(view -> {
            if (llBo_RecipeMaterial.getVisibility() == View.VISIBLE) {
                llBo_RecipeMaterial.setVisibility(View.GONE);
                imvDropDownBo_SuggestRecipe.setImageResource(R.drawable.ic_arrow_down_24);
            } else {
                llBo_RecipeMaterial.setVisibility(View.VISIBLE);
                imvDropDownBo_SuggestRecipe.setImageResource(R.drawable.ic__arrow_up_24);
            }
        });

        imvDropDownKhac_SuggestRecipe.setOnClickListener(view -> {
            if (llKhac_RecipeMaterial.getVisibility() == View.VISIBLE) {
                llKhac_RecipeMaterial.setVisibility(View.GONE);
                imvDropDownKhac_SuggestRecipe.setImageResource(R.drawable.ic_arrow_down_24);
            } else {
                llKhac_RecipeMaterial.setVisibility(View.VISIBLE);
                imvDropDownKhac_SuggestRecipe.setImageResource(R.drawable.ic__arrow_up_24);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                results = new ArrayList<>();
                llFilter.setVisibility(View.GONE);
                resultChipCGroup.setVisibility(View.VISIBLE);
                for (Ingredient ingredient: total){
                    if (ingredient.getShortname().toLowerCase().contains(newText.toLowerCase()))
                        results.add(ingredient);
                }
                adapter = new SearchIngredientResultAdapter(results,SuggestRecipeActivity.this);
                lvResult.setAdapter(adapter);
                return true;
            }
        });

        lvResult.setOnItemClickListener((parent, view, position, id) -> {
            if ( !filter.contains((int) results.get(position).getId())){

                filter.add((int) results.get(position).getId());
                inputCHip(resultChipCGroup, results.get(position), null);

            }
            searchView.clearFocus();
            searchView.setQuery("", false);
            results.clear();
            adapter.notifyDataSetChanged();
            lvResult.setAdapter(adapter);
        });

        btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(SuggestRecipeActivity.this, ListRecipeActivity.class);
            intent.putIntegerArrayListExtra(Constant.FILTER_OPTION, (ArrayList<Integer>) filter);
            startActivity(intent);
        });
        btnBack_allproducts.setOnClickListener(v ->finish());
    }

    private void initData() {
        bots = new ArrayList<>();
        bos = new ArrayList<>();
        suakems = new ArrayList<>();
        khacs = new ArrayList<>();
        total = new ArrayList<>();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Ingredient ingredient;
                String label, name, shortname;
                long id;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    label = dataSnapshot.child(Product.Label).getValue().toString();
                    id = (long) dataSnapshot.child(Product.ID).getValue();
                    name = dataSnapshot.child(Product.Name).getValue().toString();
                    shortname = dataSnapshot.child(Product.ShortName).getValue().toString();
                    ingredient = new Ingredient(id, name, label, shortname);

                    if (label.equals(Ingredient.LABEL_BOT)) bots.add(ingredient);
                    if (label.equals(Ingredient.LABEL_SUAKEM)) suakems.add(ingredient);
                    if (label.equals(Ingredient.LABEL_BO)) bos.add(ingredient);
                    if (label.equals(Ingredient.LABEL_KHAC)) khacs.add(ingredient);
                    total.add(ingredient);
                }
                initAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initAdapter() {
        initAdapterfor(chipBots,bots);
        initAdapterfor(chipSuaKems,suakems);
        initAdapterfor(chipBos,bos);
        initAdapterfor(chipKhacs,khacs);
    }

    private void initAdapterfor(ChipGroup chipGroup,List<Ingredient> materials) {
        Chip chip;
        ChipDrawable chipDrawable;
        for (Ingredient  ingredient: materials) {
            chip = new Chip(this);
            chip.setId((int) ingredient.getId());
            chip.setText(ingredient.getShortname());
            chipDrawable = ChipDrawable.createFromAttributes(this,
                    null,
                    0,
                    R.style.unavailable_chip);
            chip.setCheckedIconTint(ContextCompat.getColorStateList(this,R.color.white));
            chip.setTextColor(ContextCompat.getColorStateList(this, R.color.unavailable_chip_text_selector));
            chip.setChipDrawable(chipDrawable);
            chip.setEnsureMinTouchTargetSize(false);
            Chip finalchip = chip;
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked){
                    filter.add((int) ingredient.getId());
                    System.out.println("????y n?? "+filter.toString());
                    inputCHip(resultChipCGroup, ingredient, finalchip);
                }
                else {
                    filter.remove((Integer) (int) ingredient.getId());
                    System.out.println("????y n?? "+ filter.toString());
                }
            });

            chipGroup.addView(chip);
        }
    }

    private void inputCHip (ChipGroup chipGroup, Ingredient ingredient, @Nullable Chip chipFilter) {
        Chip chipEntry = new Chip(this);
        ChipDrawable chipDrawable;
        chipEntry.setId((int) ingredient.getId());
        chipEntry.setText(ingredient.getShortname());
        chipEntry.setCheckable(false);
        chipDrawable = ChipDrawable.createFromAttributes(this,
                null,
                0,
                R.style.input_chip);
        chipEntry.setCloseIconTint(ContextCompat.getColorStateList(this,R.color.white));
        chipEntry.setTextColor(ContextCompat.getColorStateList(this, R.color.available_chip_text_selector));
        chipEntry.setChipDrawable(chipDrawable);
        chipEntry.setEnsureMinTouchTargetSize(false);

        if (chipFilter!=null)
            chipFilter.setOnCheckedChangeListener(((buttonView, isChecked) -> {
                if (!isChecked) {
                    chipGroup.removeView(chipEntry);
                    filter.remove((Integer) (int) ingredient.getId());
                    System.out.println("????y n?? "+filter.toString());
                } else {
                    filter.add((int) ingredient.getId());
                    System.out.println("????y n?? "+filter.toString());
                    chipGroup.addView(chipEntry);
                }

            }));
        chipGroup.addView(chipEntry);

        chipEntry.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter.remove((Integer) (int) ingredient.getId());
                chipGroup.removeView(v);

            }
        });
    }

}