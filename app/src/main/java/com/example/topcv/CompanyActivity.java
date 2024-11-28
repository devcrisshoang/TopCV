package com.example.topcv;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.topcv.api.ApiCompanyService;
import com.example.topcv.model.Company;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CompanyActivity extends AppCompatActivity {

    private ImageView image;

    private TextView name;
    private TextView content;
    private TextView hotline;

    private ImageButton back_button;

    private int id_Company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_company);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setWidget();

        setClick();
    }

    private void setClick(){
        back_button.setOnClickListener(view -> finish());
    }

    private void getCompanyByID(int ID) {
        ApiCompanyService.ApiCompanyService.getCompanyByID(ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer <Company>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Company company) {
                        name.setText(company.getName());
                        content.setText(company.getField());
                        hotline.setText(company.getHotline());
                        image.setImageURI(Uri.parse(company.getImage()));
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Toast.makeText(CompanyActivity.this, "Lỗi khi gọi API công ty: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(CompanyActivity.this, "Lấy dữ liệu công ty thành công", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setWidget(){
        image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        content = findViewById(R.id.content);
        hotline = findViewById(R.id.hotline);
        back_button = findViewById(R.id.back_button);
        id_Company = getIntent().getIntExtra("company_id",0);

        getCompanyByID(id_Company);
    }
}