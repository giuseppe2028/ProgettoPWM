package com.example.progettopwm.Login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.progettopwm.R
import com.example.progettopwm.SchermataIniziale.FragmentLogin
import com.example.progettopwm.SchermataIniziale.PasswordDimenticataFragment
import com.example.progettopwm.SchermataIniziale.SchermataIniziale
import com.example.progettopwm.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class Login : AppCompatActivity() {
    private lateinit var account: String

    private lateinit var auth:FirebaseAuth
    private lateinit var googleSignClient:GoogleSignInClient
    private lateinit var binding:ActivityLoginBinding
    private var sentinellaCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        val manager = supportFragmentManager
        val trasaction = manager.beginTransaction()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        //indico il builder della creazione dell'account
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignClient = GoogleSignIn.getClient(this,gso)

       /* binding.accediConGoogle.setOnClickListener {
            signInGoogle()
            Log.i("Ciao","Cliccato")
        }
        binding.accediNormale.setOnClickListener {
            Log.i("prova",account)
           auth.signOut()
            Log.i("prova","Uscito")
            startActivity(Intent(this,SchermataIniziale::class.java))
        }

        */

        //metto in comunicazione il fragment con l'host, per poi cambiare fragment
        supportFragmentManager
            .setFragmentResultListener("requestKey", this) { requestKey, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported.
                val result = bundle.getBoolean("bundleKey")
            if(result && sentinellaCounter == 0){
                sentinellaCounter ++
                trasaction.replace(binding.fragmentView.id,PasswordDimenticataFragment()).addToBackStack(null).commit()

                Log.i("prova","${manager.backStackEntryCount}")
            }
            else{
                Log.i("prova","Faccio niente")
                trasaction.replace(binding.fragmentView.id,FragmentLogin()).addToBackStack(null).commit()
            }
            }
    }

    private fun signInGoogle() {
        val signIntent = googleSignClient.signInIntent
       launcher.launch(signIntent)
    }
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result->
                if(result.resultCode == Activity.RESULT_OK){
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    handleResults(task)
                }

    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful){
            val account:GoogleSignInAccount? = task.result
            if(account != null){
                updateUI(account)
            }
        }
        else{
            //mostro un toast
            Toast.makeText(this,task.exception.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        this.account = account.email.toString()
        val credential = GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                Log.i("prova","Sei nella nuova pagina")
            }
            else{
                Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
            }
        }
    }


}