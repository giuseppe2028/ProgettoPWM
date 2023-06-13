package com.example.progettopwm.SchermataHome.FragmentPagine

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.progettopwm.ClientNetwork
import com.example.progettopwm.R
import com.example.progettopwm.databinding.FragmentDatiPagamentoBinding
import com.example.progettopwm.databinding.FragmentSchermataAccountBinding
import com.example.progettopwm.idPersona
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentDatiPagamento.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentDatiPagamento : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentDatiPagamentoBinding

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDatiPagamentoBinding.inflate(inflater)

        binding.editTextCvv.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(3))
        binding.editTextmese.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(2))
        binding.editTextanno.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(2))
        binding.editTextnumero.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(16))

        //chiamo la query
        val id_p = idPersona.getId()
        recuperaDati(id_p){result, numero_carta, cvv, nome_titolare, mese_scadenza, anno_scadenza->
            if(result){
                binding.editTextnumero.hint= numero_carta.toString()
                binding.editTextNomeCognome.hint = nome_titolare.toString()
                binding.editTextCvv.hint = cvv.toString()
                binding.editTextmese.hint = mese_scadenza.toString()
                binding.editTextanno.hint = anno_scadenza.toString()
            }
            else{
                binding.editTextnumero.hint= "xxxxxxxxxxxx"
                binding.editTextNomeCognome.hint = "stiven bassler"
                binding.editTextCvv.hint = "xxx"
                binding.editTextmese.hint = "xx"
                binding.editTextanno.hint = "xx"
            }
        }
        //impongo che non si possono inserire piu di un tot di caratteri dentro il plaintext
        binding.editTextCvv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length != 3) {
                    binding.editTextCvv.setBackgroundResource(R.drawable.edittext_border_red)
                }
                else{
                    binding.editTextCvv.setBackgroundResource(R.drawable.edittext_border_green)

                }
            }
        })

        binding.editTextmese.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                if (input.length == 2) {
                    val month = input.toIntOrNull()
                    if(month != null){
                        if ( month < 1 || month > 12) {
                            binding.editTextmese.setBackgroundResource(R.drawable.edittext_border_red)
                        }
                        else{
                            binding.editTextmese.setBackgroundResource(R.drawable.edittext_border_green)
                        }}
                }

            }
        })

        binding.editTextanno.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                if (input.length == 2) {
                    val anno = input.toIntOrNull()
                    if (anno != null) {
                        if (anno < 23) {
                            binding.editTextanno.setBackgroundResource(R.drawable.edittext_border_red)
                        }
                        else{
                            binding.editTextanno.setBackgroundResource(R.drawable.edittext_border_green)

                        }
                    }
                }
            }
        })

        binding.editTextnumero.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length != 16) {
                    binding.editTextnumero.setBackgroundResource(R.drawable.edittext_border_red)
                }else{
                    binding.editTextnumero.setBackgroundResource(R.drawable.edittext_border_green)

                }
            }
        })



        binding.buttonConfermaDati.setOnClickListener {
            if (!validateOtherFields(binding.editTextnumero,binding.editTextNomeCognome,binding.editTextCvv,binding.editTextmese,binding.editTextanno)) {
                Toast.makeText(this.context, "Controllare il contenuto dei campi", Toast.LENGTH_SHORT).show()
            }
            else{
                val n_c = binding.editTextnumero.text.toString().toLong()
                val nome =binding.editTextNomeCognome.text.toString()
                val cvv = binding.editTextCvv.text.toString().toInt()
                val mese = binding.editTextmese.text.toString().toInt()
                val anno = binding.editTextanno.text.toString().toInt()
                inserisciDati(id_p, n_c, cvv, nome, mese, anno){value->
                    if(value){
                        Toast.makeText(context, "caricati", Toast.LENGTH_SHORT).show()

                    }
                    else{
                        Toast.makeText(context, "Errore durante la chiamata di rete", Toast.LENGTH_SHORT).show()

                    }
                }

            }
        }
        return binding.root
    }



    private fun inserisciDati(id: Int,numero_carta: Long, cvv: Int, nome_titolare: String, mese_scadenza:Int, anno_scadenza:Int, callback: (Boolean) -> Unit){
        val query = "UPDATE DatiPagamento JOIN Persona ON DatiPagamento.numero_carta = Persona.ref_datiPagamento SET DatiPagamento.numero_carta = $numero_carta, Persona.ref_datiPagamento=$numero_carta, DatiPagamento.cvv = $cvv, DatiPagamento.nome_titolare = '$nome_titolare', DatiPagamento.mese_scadenza = $mese_scadenza , DatiPagamento.anno_scadenza = $anno_scadenza WHERE Persona.id = $id"
        ClientNetwork.retrofit.update(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                            callback(true)
                        }else{
                            callback(false)
                        }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    callback(false)
                    Log.e("Errore", "Errore durante la chiamata di rete", t)
                    Toast.makeText(context, "Errore durante la chiamata di rete", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }





    private fun recuperaDati(id: Int, callback: (Boolean, Long?, Int?, String?, Int?, Int?) -> Unit){
        val query = "select D.numero_carta, D.cvv, D.nome_titolare, D.mese_scadenza, D.anno_scadenza from DatiPagamento D join Persona P on D.numero_carta = P.ref_datiPagamento where P.id =$id"
        ClientNetwork.retrofit.registrazione(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val resultSet = response.body()?.get("queryset") as JsonArray
                        if (resultSet.size() == 1) {
                            val numero_carta = resultSet[0].asJsonObject.get("numero_carta").asLong
                            val cvv = resultSet[0].asJsonObject.get("cvv").asInt
                            val nome_titolare = resultSet[0].asJsonObject.get("nome_titolare").asString
                            val mese_scadenza = resultSet[0].asJsonObject.get("mese_scadenza").asInt
                            val anno_scadenza = resultSet[0].asJsonObject.get("anno_scadenza").asInt
                            callback(true, numero_carta, cvv, nome_titolare, mese_scadenza, anno_scadenza)
                        }else{
                            callback(false, null, null, null, null, null)
                        }
                    }
                    else{
                        callback(false, null, null, null, null, null)
                        Log.i("errore", "non funziona")
                        Log.i("errore", response.message())
                        Log.i("errore",  response.toString())
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    callback(false, null, null, null, null, null)
                    Log.e("Errore", "Errore durante la chiamata di rete", t)
                    Toast.makeText(context, "Errore durante la chiamata di rete", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }








private fun validateOtherFields(EditText1: EditText, EditText2: EditText, EditText3: EditText, EditText4: EditText, EditText5: EditText): Boolean {
    val field1 = EditText1.text.toString().trim()
    val field2 = EditText2.text.toString().trim()
    val field3 = EditText3.text.toString().trim()
    val field4 = EditText4.text.toString().trim()
    val field5 = EditText5.text.toString().trim()
    return field1.isNotEmpty() && field2.isNotEmpty() && field3.isNotEmpty()&& field4.isNotEmpty()&& field5.isNotEmpty()
}

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentDatiPagamento.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentDatiPagamento().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}