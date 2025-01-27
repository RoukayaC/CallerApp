package roukaya.chelly.callerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    //declaration des composantes
    Button btnexit,btnval;
    EditText edname,edpass ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //METTRE UNE INTERFACE XML SUR L"ECRAN
        //R cest le dossier recourse res
        setContentView(R.layout.activity_main);

        //recuperation des composantes
        btnexit=findViewById(R.id.btn_exit);
        btnval=findViewById(R.id.btn_valider);
        edname=findViewById(R.id.eduser_auth);
        edpass=findViewById(R.id.edpass_auth);

        //evenement
        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();  //fermer l action courante
            }
        });
        btnval.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                 String name= edname.getText().toString();
                 String pwd= edname.getText().toString();
                 if(name.equalsIgnoreCase("a") && pwd.equals("1") ){
                     //passage vers lactivite home
                     Intent i =new Intent(MainActivity.this,Home.class);
                     startActivity(i);
                     finish();
                 }else {
                     //message derreur
                     Toast.makeText(MainActivity.this, "erreur lors du saisie", Toast.LENGTH_SHORT).show();
                 };
    }
});


    }
    @Override
    protected void onStart()
    {
       super.onStart();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}