package com.example.progettopwm.SchermataHome.SchermataPrenotazioni

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.print.PrintAttributes
import android.print.pdf.PrintedPdfDocument
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.progettopwm.Gestione.ClientNetwork
import com.example.progettopwm.Gestione.idPersona
import com.example.progettopwm.GestioneDB
import com.example.progettopwm.R
import com.example.progettopwm.SchermataHome.SchermataPreferiti.RecyclerView.ItemViewModel
import com.example.progettopwm.SchermataHome.SchermataPrenotazioni.RecyclerView.CustomAdapterPrenotazioni
import com.example.progettopwm.SchermataHome.SchermataPrenotazioni.RecyclerView.ItemViewModelP
import com.example.progettopwm.databinding.FragmentPrenotazioniBinding
import com.example.progettopwm.databinding.FragmentSchermataPrenotazioniBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SchermataPrenotazioni.newInstance] factory method to
 * create an instance of this fragment.
 */
class SchermataPrenotazioni : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentSchermataPrenotazioniBinding
    private var statoCaricamento:Boolean = false
    private val STORAGE_CODE =1001
    private var pdfData = ""
    private var pdfFileName = ""
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
    ): View {
        binding = FragmentSchermataPrenotazioniBinding.inflate(inflater)
        binding.listaPrenotazioni.layoutManager = LinearLayoutManager(requireContext())
        //faccio la query
        setSchermata()

        // Inflate the layout for this fragment
        return binding.root

    }



    private fun setSchermata() {
        val idP = idPersona.getId()
        val query = "select distinct V.id,V.luogo,V.nome_struttura,V.prezzo,I.ref_immagine,V.num_persone, V.giorni_pernotto, V.data from Viaggio V, Compra C, Immagini I, Persona P where C.ref_persona = P.id and P.id=$idP and C.ref_viaggio = V.id and I.ref_viaggio = V.id"
        var iesimoDato: JsonObject  // Dichiarazione della variabile fuori dal callback
        var listaPre = ArrayList<ItemViewModelP>()
//faccio la query
        GestioneDB.getArray(query){
                array ->
            for(i in array){
                val element = i as JsonObject
                Log.i("Prova12345", "${element.get("nome_struttura").asString}")
                GestioneDB.getImage(element){
                        immagine ->
                    listaPre.add(
                        ItemViewModelP(
                            element.get("id").asInt,
                            immagine,
                            element.get("data").asString,
                            element.get("giorni_pernotto").asInt,
                            element.get("nome_struttura").asString,
                            element.get("luogo").asString,
                            element.get("prezzo").asDouble,
                            element.get("num_persone").asInt
                        )
                    )
                    // Aggiorno l'adapter dopo l'aggiunta di un nuovo elemento
                    binding.listaPrenotazioni.adapter?.notifyDataSetChanged()
                }
            }

        }

// Imposto l'adapter fuori dal callback

       val adapter = CustomAdapterPrenotazioni(listaPre)
        binding.listaPrenotazioni.adapter = adapter
        adapter.setOnClickListener(object:
            CustomAdapterPrenotazioni.OnClickListener {
            override fun onScaricaClick(position: Int, item: ItemViewModelP) {
               val query = "select P.nome, P.cognome, V.luogo, V.nome_struttura, V.data, V.giorni_pernotto, V.prezzo  from Persona P, Compra C, Viaggio V where P.id =$idP and V.id=${item.id} and C.ref_persona = P.id and C.ref_viaggio = V.id"
                ClientNetwork.retrofit.registrazione(query).enqueue(
                    object : Callback<JsonObject> {
                        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                            if (response.isSuccessful) {
                                val resultSet = response.body()?.get("queryset") as JsonArray
                                if (resultSet.size() == 1) {
                                    val nome= resultSet[0].asJsonObject.get("nome").asString
                                    val cognome= resultSet[0].asJsonObject.get("cognome").asString
                                    val luogo= resultSet[0].asJsonObject.get("luogo").asString
                                    val nome_struttura= resultSet[0].asJsonObject.get("nome_struttura").asString
                                    val data= resultSet[0].asJsonObject.get("data").asString
                                    val giorni= resultSet[0].asJsonObject.get("giorni_pernotto").asInt
                                    val prezzo= resultSet[0].asJsonObject.get("prezzo").asDouble
                                    pdfData = generateReceiptText(item.id, nome, cognome, luogo, nome_struttura, data, giorni, prezzo)
                                    pdfFileName = "ricevuta_${item.id}.pdf"
                                    pdf(pdfData, pdfFileName)
                                }else{
                                }
                            }
                            else{
                                Log.i("errore", "non funziona")
                                Log.i("errore", response.message())
                                Log.i("errore",  response.toString())
                            }
                        }

                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                            Log.e("Errore", "Errore durante la chiamata di rete", t)
                            Toast.makeText(context, "Errore durante la chiamata di rete", Toast.LENGTH_SHORT).show()
                        }
                    }
                )

            }
            override fun Onclick(position: Int, item: ItemViewModelP) {
                val query = "delete from Compra where ref_viaggio = ${item.id}"
                GestioneDB.eliminaElemento(query)
                //lo aggiorno pure localemnte per evitare una query di troppo:
                listaPre.removeAt(position)
                adapter.notifyDataSetChanged()
            }



        }
        )

    }



    private fun pdf(file: String, fileName: String){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            if(this.context?.let { checkSelfPermission(it, Manifest.permission.WRITE_EXTERNAL_STORAGE) } == PermissionChecker.PERMISSION_DENIED){
                val permission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permission, STORAGE_CODE)
            }else{
                savePDF(file, fileName)
            }
        }else{
            savePDF(file, fileName)
        }
    }
    private fun savePDF(file: String, fileName: String){
        val mDoc = Document()
        val mFileName = fileName
            //SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        val mFilePath = Environment.getExternalStorageDirectory().toString() + "/" + mFileName + ".pdf"
        try{
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
            mDoc.open()
            val data = file
            /*val drawableId = R.drawable.photo_2023_05_31_21_01_17_removebg_preview
            val drawable: Drawable? = context?.getDrawable(drawableId)
            val img = ImageDataFactory.create(R.drawable.photo_2023_05_31_21_01_17_removebg_preview, null)*/
            mDoc.addAuthor("Gabriele")
            mDoc.add(Paragraph(data))
           // mDoc.add()
            mDoc.close()
            Toast.makeText(this.context, "$mFileName.pdf\n is created in \n $mFilePath", Toast.LENGTH_SHORT).show()

        }catch(e:Exception){
            Toast.makeText(this.context, "" + e.toString(), Toast.LENGTH_SHORT).show()
        }

    }
    private fun generateReceiptText(id: Int, nomeCliente: String,cognomeCliente: String, luogo: String,nomeStruttura: String,data: String,giorni: Int, importo: Double): String {
        val random = Random.Default
        val numeroRicevuta =id
        val numeroTransazione = random.nextInt(1000000000, 9999999999.toInt())

        val receiptText = """
        Numero ricevuta: $numeroRicevuta
        
        
        Cliente: $nomeCliente $cognomeCliente
        Descrizione: Ricevuta "misvago SRL" 
        Luogo: $luogo, $nomeStruttura
        Data: $data, $giorni giorni
        Importo: $importo EUR
        
        Metodo di pagamento: Wallet
        Numero transazione: $numeroTransazione
        
        Grazie per il pagamento!
        """.trimIndent()

        return receiptText
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            STORAGE_CODE -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    savePDF(pdfData, pdfFileName)
                }else{
                    Toast.makeText(this.context, "permissione denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }





    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SchermataPrenotazioni.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SchermataPrenotazioni().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }



}