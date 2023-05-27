package com.example.progettopwm.Login

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.progettopwm.R
import com.example.progettopwm.SchermataIniziale.FragmentLogin
import com.example.progettopwm.SchermataIniziale.PasswordDimenticataFragment
import com.example.progettopwm.SchermataIniziale.SchermataInizialeFragment
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
        val transaction = manager.beginTransaction()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        if(savedInstanceState == null){
            Log.i("sonoDentro","Dentro")
            transaction.add(binding.fragmentView.id, SchermataInizialeFragment())
        }
        setContentView(binding.root)


        accediCongoogle()
        //metto in comunicazione il fragment con l'host, per poi cambiare fragment
       passDimenticata()
    }

    private fun accediCongoogle() {
        supportFragmentManager
            .setFragmentResultListener("requestGoogle", this) { requestKey, bundle ->
                //devo inserire i fragment qua dentro per far ricominciare il contatore
                val manager = supportFragmentManager
                val trasaction = manager.beginTransaction()
                // We use a String here, but any type that can be put in a Bundle is supported.
                val result = bundle.getBoolean("RispostaGoogle")

                if (result){
                    googleAuth()
                    signInGoogle()

                }
            }
    }

    private fun passDimenticata() {
        supportFragmentManager
            .setFragmentResultListener("requestKey", this) { requestKey, bundle ->
                //devo inserire i fragment qua dentro per far ricominciare il contatore
                val manager = supportFragmentManager
                val trasaction = manager.beginTransaction()
                // We use a String here, but any type that can be put in a Bundle is supported.
                val result = bundle.getBoolean("bundleKey")
                if(result && sentinellaCounter == 0){
                    sentinellaCounter ++
                    trasaction.setCustomAnimations(R.anim.enter_fragment_up_to_down,R.anim.exit_fragment_right_to_left,R.anim.enter_fragment_down_to_up,R.anim.exit_fragment_left_to_right)
                        .replace(binding.fragmentView.id,PasswordDimenticataFragment()).addToBackStack(null).commit()

                    Log.i("prova","${manager.backStackEntryCount}")
                }
                else{
                    Log.i("prova","Faccio niente")
                    trasaction.setCustomAnimations(R.anim.enter_fragment_up_to_down,R.anim.exit_fragment_right_to_left,R.anim.enter_fragment_down_to_up,R.anim.exit_fragment_left_to_right)
                        .replace(binding.fragmentView.id,PasswordDimenticataFragment()).addToBackStack(null).commit()
                }
            }
        fragmentListenerSignIn()
    }

    private fun googleAuth() {
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
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            // Ci sono fragment nello stack dei backstack, esegui il pop del fragment precedente
            supportFragmentManager.popBackStackImmediate()
        } else {
            // Nessun fragment nello stack dei backstack, esegui l'azione di default
            super.onBackPressed()
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
    private fun fragmentListenerSignIn() {
        supportFragmentManager
            .setFragmentResultListener("SignIn", this) { requestKey, bundle ->
                //devo inserire i fragment qua dentro per far ricominciare il contatore
                val manager = supportFragmentManager
                val trasaction = manager.beginTransaction()
                // We use a String here, but any type that can be put in a Bundle is supported.
                val result = bundle.getBoolean("SignInRisposta")
                if(result){

                    trasaction.setCustomAnimations(R.anim.enter_fragment_up_to_down,R.anim.exit_fragment_right_to_left,R.anim.enter_fragment_down_to_up,R.anim.exit_fragment_left_to_right)
                        .replace(binding.fragmentView.id,FragmentLogin()).addToBackStack(null).commit()

                    Log.i("prova","${manager.backStackEntryCount}")
                }
                else{
                    Log.i("prova","Faccio niente")
                }
            }
    }


}