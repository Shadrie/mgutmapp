package com.mgutm.sosinann.mgutmapp;


        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.ImageButton;
        import android.widget.FrameLayout;
        import android.widget.Toast;

public class CategoriesActivity extends AppCompatActivity {

    // Переменные, соответсвующие элементам интерфейса определенного типа
    FrameLayout imgLayout;
    ImageButton aboutButton;
    ImageButton entrantButton;
    ImageButton studentButton;
    ImageButton doctorButton;
    ImageButton olimpButton;
    ImageButton eleButton;
    ImageButton secondButton;
    ImageButton scienceButton;
    ImageButton graduateButton;
    ImageButton employeeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Задать отображаемый макет
        setContentView(R.layout.categories);

        // Определить элементы интерфейса из макетов, соответствующие заданным переменным
        imgLayout = (FrameLayout) findViewById(R.id.imgLayout);
        aboutButton = (ImageButton) findViewById(R.id.aboutButton);
        entrantButton = (ImageButton) findViewById(R.id.entrantButton);
        studentButton = (ImageButton) findViewById(R.id.studentButton);
        doctorButton = (ImageButton) findViewById(R.id.doctorButton);
        olimpButton = (ImageButton) findViewById(R.id.olimpButton);
        eleButton = (ImageButton) findViewById(R.id.eleButton);
        secondButton = (ImageButton) findViewById(R.id.secondButton);
        scienceButton = (ImageButton) findViewById(R.id.scienceButton);
        graduateButton = (ImageButton) findViewById(R.id.graduateButton);
        employeeButton = (ImageButton) findViewById(R.id.employeeButton);

        // При нажатии на свободную область, производится возврат к главной странице
        View.OnClickListener GoHome = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CategoriesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        };
        imgLayout.setOnClickListener(GoHome);


        // В зависимости от вызываемой функции, совершается переход в определенный раздел
        // по заданному url-адресу
        View.OnClickListener AboutClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesActivity.this, ViewActivity.class);
                intent.putExtra("url", "http://mgutm.ru/about/");
                startActivity(intent);
            }
        };

        View.OnClickListener EntrantClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesActivity.this, ViewActivity.class);
                intent.putExtra("url", "http://mgutm.ru/entrant_2012/");
                startActivity(intent);
            }
        };

        View.OnClickListener StudentClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesActivity.this, ViewActivity.class);
                intent.putExtra("url", "http://mgutm.ru/students-and-masters/");
                startActivity(intent);
            }
        };

        View.OnClickListener DoctorClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesActivity.this, ViewActivity.class);
                intent.putExtra("url", "http://mgutm.ru/graduates-and-doctors/");
                startActivity(intent);
            }
        };

        View.OnClickListener OlimpClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesActivity.this, ViewActivity.class);
                intent.putExtra("url", "http://mgutm.ru/olimpiadi/");
                startActivity(intent);
            }
        };

        View.OnClickListener SecondClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesActivity.this, ViewActivity.class);
                intent.putExtra("url", "http://mgutm.ru/second-education/");
                startActivity(intent);
            }
        };

        View.OnClickListener ScienceClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesActivity.this, ViewActivity.class);
                intent.putExtra("url", "http://mgutm.ru/science/");
                startActivity(intent);
            }
        };

        View.OnClickListener GraduateClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesActivity.this, ViewActivity.class);
                intent.putExtra("url", "http://mgutm.ru/graduate/");
                startActivity(intent);
            }
        };

        View.OnClickListener EmployeeClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesActivity.this, ViewActivity.class);
                intent.putExtra("url", "http://mgutm.ru/employee/");
                startActivity(intent);
            }
        };

        // При наличии подключения, разработанные функции задаются определенным кнопкам,
        // иначе выводится сообщение об ошибке подключения
        if (!DetectConnection.checkInternetConnection(this)) {
            Toast.makeText(getApplicationContext(), "Подключение отсутствует!", Toast.LENGTH_SHORT).show();
        }
        else {
            aboutButton.setOnClickListener(AboutClick);
            entrantButton.setOnClickListener(EntrantClick);
            studentButton.setOnClickListener(StudentClick);
            doctorButton.setOnClickListener(DoctorClick);
            olimpButton.setOnClickListener(OlimpClick);
            secondButton.setOnClickListener(SecondClick);
            scienceButton.setOnClickListener(ScienceClick);
            graduateButton.setOnClickListener(GraduateClick);
            employeeButton.setOnClickListener(EmployeeClick);
        }
    }
}
