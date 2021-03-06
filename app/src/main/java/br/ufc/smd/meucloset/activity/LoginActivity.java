// https://medium.com/crud-firebase-android-studio/crud-firebase-android-studio-359805616078
// https://www.journaldev.com/23219/android-capture-image-camera-gallery-using-fileprovider
// https://developer.android.com/training/camera/photobasics?hl=fr#java
// https://developer.android.com/training/secure-file-sharing/setup-sharing#DefineProvider
// https://medium.com/firebase-tips-tricks/how-to-map-an-array-of-objects-from-cloud-firestore-to-a-list-of-objects-122e579eae10
// https://stackoverflow.com/questions/49659549/how-to-show-a-firestore-collection-in-an-android-listview-using-an-adapter
// https://github.com/bikashthapa01/basic-camera-app-android/blob/master/app/src/main/java/net/smallacademy/cameraandgallery/MainActivity.java
// https://www.geeksforgeeks.org/how-to-create-dynamic-listview-in-android-using-firebase-firestore/

package br.ufc.smd.meucloset.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import br.ufc.smd.meucloset.R;
import br.ufc.smd.meucloset.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    FirebaseFirestore db;

    private EditText edUser;
    private EditText edPass;
    private Button btnLogin;
    private Button btnNovoUsuario;

    Usuario u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = FirebaseFirestore.getInstance();

        edUser  = findViewById(R.id.edtLogin);
        edPass  = findViewById(R.id.edtSenha);
        btnLogin = findViewById(R.id.btnLogin);
        btnNovoUsuario  = findViewById(R.id.btnNovoUsuario);

        //Novo Usu??rio
        btnNovoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, NovoUsuarioActivity.class);
                startActivity(intent);
            }
        });

        //Fazer login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String login, senha;
                login = edUser.getText().toString();
                senha = edPass.getText().toString();

                db.collection("usuarios")
                        .whereEqualTo("usuario", login)
                        .whereEqualTo("senha", senha)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().isEmpty()) {
                                        Toast.makeText(LoginActivity.this, "Login ou senha errados...", Toast.LENGTH_LONG).show();
                                        Log.d("TAG", "Login ou senha errados...");
                                    } else {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if (document.exists()) {
                                                u = new Usuario();
                                                u.setUsuario(document.getData().get("usuario").toString());
                                                u.setNome(document.getData().get("nome").toString());
                                                u.setSenha(document.getData().get("senha").toString());
                                                u.setEmail(document.getData().get("email").toString());
                                                u.setTelefone(document.getData().get("telefone").toString());
                                                Log.d("TAG", document.getId() + " => " + document.getData());

                                                if (u.getUsuario() != null) {
                                                    Intent intent = new Intent(LoginActivity.this, ListaProdutosActivity.class);
                                                    intent.putExtra("usuario", u);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });
    }
}