package com.example.progettopwm.Recensioni.GestioneSchermataViaggio

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.progettopwm.Gestione.ClientNetwork
import com.example.progettopwm.Gestione.GestioneDB
import com.example.progettopwm.R
import com.example.progettopwm.Recensioni.RecyclerView.recensioneFragment
import com.example.progettopwm.databinding.FragmentContenitoreSchermataViaggioBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



class ContenitoreSchermataViaggio : Fragment() {
    var stato:Boolean = false
    //setto il permesso:
    val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()){

            isGaranted:Boolean->
        if(isGaranted){
            Log.i("TAG", "Permission enabled")
        }else{
            Log.i("TAG", "Permission enabled")
        }

    }

    private lateinit var binding:FragmentContenitoreSchermataViaggioBinding
    var numeroTelefono:String = ""
    // TODO: Rename and change types of parameters
    private var idPersona:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if( arguments?.getInt("valore")!=null){
            idPersona = arguments?.getInt("valore")!!
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var stato:Boolean = false
        binding = FragmentContenitoreSchermataViaggioBinding.inflate(layoutInflater)
        //Aggiungere id viaggio
        setAzienda(1)
        clickAzienda()
        Log.i("id","$idPersona 123")

        setLike(id)
        setSchermata(idPersona)
        // Inflate the layout for this fragment
        return binding.root

    }


        private fun setAzienda(id:Int) {
            val query = "select distinct nome_azienda, num_telefono, email from Azienda A, Viaggio V where A.ref_viaggio = V.id and V.id = $id"
            GestioneDB.richiestaInformazioni(query){
                    dati ->
                binding.nomePersona.text = dati.get("nome_azienda").asString
                numeroTelefono = dati.get("num_telefono").asString
                //TODO(Aggiungere l'immagine del profilo)
            }
        }
    private fun clickAzienda() {
        binding.imagePhoneButton.setOnClickListener {
            //chiedo il permesso
            val permesso = ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.CALL_PHONE)
            Log.i("permesso", "sono dentro")
            if(permesso != PackageManager.PERMISSION_GRANTED){
                Log.i("permesso","PermessoAbilitato")
                val intent = Intent(Intent.ACTION_DIAL)
                intent.setData(Uri.parse("tel:$numeroTelefono"));
                startActivity(intent)
            }
            else{
                requestPermission.launch(android.Manifest.permission.CALL_PHONE)
            }
        }
        binding.imageEmailButton.setOnClickListener {
            //chiedo il permesso
            val permesso = ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.CALL_PHONE)
            Log.i("permesso", "sono dentro")
            if(permesso != PackageManager.PERMISSION_GRANTED){
                Log.i("permesso","PermessoAbilitato")
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
                //startActivity(intent)
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        }
    }



    fun setSchermata(id:Int){
        Log.i("Ciao","$id")
        val query = "select * from Viaggio, Immagini where  ref_viaggio = Viaggio.id and Viaggio.id = $id and immagine_default = 1"
        ClientNetwork.retrofit.registrazione(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if(response.isSuccessful){
                        val risposta = response.body()?.get("queryset") as JsonArray
                        if(risposta.size() != 0){
                            Log.i("ciao123wdq","${(risposta.get(0) as JsonObject).get("nome_struttura").asString}")
                            binding.titleViaggio.text = (risposta.get(0) as JsonObject).get("nome_struttura").asString
                            binding.numeroPersone.text = (risposta.get(0) as JsonObject).get("num_persone").asString
                            binding.testoLuogo.text = (risposta.get(0) as JsonObject).get("luogo").asString
                            binding.ratingBar3.rating = (risposta.get(0) as JsonObject).get("recensione").asFloat
                            binding.testoDescrizione.text = (risposta.get(0) as JsonObject).get("descrizione").asString
                            //binding.prezzoViaggio.text = (risposta.get(0) as JsonObject).get("prezzo").asString.plus("$")
                            val image = risposta.get(0) as JsonObject
                            //mi prendo l'immagine:
                            getImage(image)
                            //binding.sfondo.background

                        }
                        else{
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("prova","prova123")
                }

            }
        )
    }
    private fun getImage(jsonObject: JsonObject){
        val string = jsonObject.get("ref_immagine").asString
        Log.i("ciao90", "$string")
        Log.i("ciaoProva","$string")
        ClientNetwork.retrofit.getImage(string).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if(response.isSuccessful){
                        var immagine: Bitmap? = null
                        if (response.body()!=null) {
                            immagine = BitmapFactory.decodeStream(response.body()?.byteStream())
                            //binding.sfondo.setImageBitmap(immagine)
                        }
                    }
                    else{
                        Log.i("ciao","notsuccessiful")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.i("ciao","fail")
                }

            }
        )


    }
    private fun clickLike(idPersona:Int,id:Int) {
        binding.like.setOnClickListener {
            if(stato){
                //disattivo il bottone:
                Toast.makeText(context,R.string.Disattivato, Toast.LENGTH_SHORT).show()
               // binding.like.setImageDrawable(getDrawable(R.drawable.heartbutton))
                stato = false
                //request http

                aggiornaDati(idPersona,id,false)
            }else if(!stato) {
                Toast.makeText(context,R.string.Attivato, Toast.LENGTH_SHORT).show()
                //binding.like.setImageDrawable(getDrawable(R.drawable.heart_3))
                stato = true
                aggiornaDati(idPersona,id,true)
            }
        }
    }

    private fun setLike(id:Int){
        val query = "select * from Preferiti where ref_viaggio = $id"
        GestioneDB.queryGenerica(query){
                data->
            if(data.size()!=0){
                //vuol dire che l'elemento è nei preferiti
                //binding.like.setImageDrawable(getDrawable(R.drawable.heart_3))
                stato = true
            }
            else if(data.size() ==0){
                //binding.like.setImageDrawable(getDrawable(R.drawable.heartbutton))
                stato = false
            }
        }
    }
    fun aggiornaDati(idPersona:Int,id:Int,attivazione:Boolean) {
        //aggiungo o rimuovo
        if(attivazione){
            Log.i("debug","debug: Persona: $idPersona, Viaggio:$id")
            val query = "insert into Preferiti(ref_persona,ref_viaggio) values($idPersona,$id)"
            GestioneDB.inserisciElemento(query)
        }else if(!attivazione){
            val query = "delete from Preferiti where ref_viaggio = $id"
            GestioneDB.eliminaElemento(query)
        }
    }
    companion object {
        fun newInstance(dato:Int): ContenitoreSchermataViaggio {
            val fragment = ContenitoreSchermataViaggio()
            val argomenti = Bundle()
            argomenti.putInt("valore", dato)
            fragment.arguments = argomenti
            return fragment
        }
    }

}