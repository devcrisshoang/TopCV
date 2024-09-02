package com.example.topcv;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.topcv.adapter.ListInformationlAdapter;
import java.util.Arrays;
import java.util.List;

public class CompanyInformationsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ListInformationlAdapter listInformationlAdapter;
    private ImageButton informationBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_company_informations);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        informationBackButton = findViewById(R.id.information_back_button);
        recyclerView = findViewById(R.id.recyclerview_information);

        List<String> horizontalItems = Arrays.asList("Item A", "Item B", "Item C", "Item D", "Item E", "Item G");
        listInformationlAdapter = new ListInformationlAdapter(horizontalItems);

        recyclerView.setAdapter(listInformationlAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        informationBackButton.setOnClickListener(v -> finish()); // Handle back button click
    }

}
