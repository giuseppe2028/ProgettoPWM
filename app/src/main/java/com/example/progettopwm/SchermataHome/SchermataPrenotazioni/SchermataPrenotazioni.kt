package com.example.progettopwm.SchermataHome.SchermataPrenotazioni

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.pdf.PdfDocument
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.progettopwm.ClientNetwork
import com.example.progettopwm.GestioneDB
import com.example.progettopwm.SchermataHome.SchermataPrenotazioni.RecyclerView.CustomAdapterPrenotazioni
import com.example.progettopwm.SchermataHome.SchermataPrenotazioni.RecyclerView.ItemViewModelP
import com.example.progettopwm.databinding.FragmentSchermataPrenotazioniBinding
import com.example.progettopwm.idPersona
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
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
    private lateinit var binding:FragmentSchermataPrenotazioniBinding

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
        binding.listaPrenotazioni.layoutManager = LinearLayoutManager(this.context)
        //faccio la query
        setSchermata()

        // Inflate the layout for this fragment
        return binding.root

    }



    private fun setSchermata() {
        val idP = idPersona.getId()
        val query = "select distinct V.id,V.luogo,V.nome_struttura,V.recensione,V.prezzo,V.path_immagine,V.num_persone, V.giorni_pernotto, V.data from Viaggio V, Compra C, Immagini I, Persona P where C.ref_persona = P.id and P.id=$idP and C.ref_viaggio = V.id and I.ref_viaggio = V.id"
        var iesimoDato: JsonObject  // Dichiarazione della variabile fuori dal callback
        var listaPre = ArrayList<ItemViewModelP>()
//faccio la query
        GestioneDB.queryGenerica(query) { dati ->

            for (i in dati) {
                iesimoDato = i as JsonObject
                Log.i("entro", "entro")

                // Carico l'immagine in modo asincrono
                GestioneDB.getImage(i) { immagineRitorno ->
                    val immagine = immagineRitorno

                    // Aggiungo l'elemento alla lista
                     listaPre.add(
                        ItemViewModelP(
                            iesimoDato.get("id").asInt,
                            immagine,
                            iesimoDato.get("data").asString,
                            iesimoDato.get("giorni_pernotto").asInt,
                            iesimoDato.get("nome_struttura").asString,
                            iesimoDato.get("luogo").asString,
                            iesimoDato.get("recensione").asDouble,
                            iesimoDato.get("prezzo").asDouble,
                            iesimoDato.get("num_persone").asInt
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
                                    val pdfData = generateReceiptText(item.id, nome, cognome, luogo, nome_struttura, data, giorni, prezzo)
                                    val pdfFileName = "ricevuta_${item.id}.pdf"

                                    val file = generatePDF(pdfData, pdfFileName)
                                    downloadPDF(file)
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


    private fun richiediDatiFattura(idP: Int, idV: Int,){
        val query = "select P.nome, P.cognome, V.luogo, V.nome_struttura, V.data, V.giorni_pernotto, V.prezzo  from Persona P, Compra C, Viaggio V where P.id =$idP and V.id=$idV and C.ref_persona = P.id and C.ref_viaggio = V.id"
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
                            val pdfData = generateReceiptText(idV, nome, cognome, luogo, nome_struttura, data, giorni, prezzo)
                            val pdfFileName = "ricevuta_$idV.pdf"

                            val file = generatePDF(pdfData, pdfFileName)
                            downloadPDF(file)
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

    // Dimensioni della pagina del documento PDF
    private val pageWidth: Int = 595 // Valore in pixel
    private val pageHeight: Int = 842 // Valore in pixel
    private val pageNumber: Int = 1

    // Dimensione del testo
    private val textSize: Float = 12f // Valore in sp (scale-independent pixels)

    // Spaziatura tra le righe del testo
    private val lineSpacing: Float = 8f // Valore in pixel

    // Contesto dell'applicazione
    private lateinit var context: Context

    // Authority per il provider di file
    private val authority: String = "com.example.progettopwm.fileprovider"


    // Metodo per generare il file PDF con il contenuto desiderato
    private fun generatePDF(content: String, fileName: String): File? {
        // Crea un nuovo documento PDF
        val pdfDocument = PrintedPdfDocument(context, PrintAttributes.Builder().build())

        // Inizia una nuova pagina
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
        val page = pdfDocument.startPage(pageInfo)

        // Crea il canvas per disegnare il contenuto sulle pagine
        val canvas = page.canvas

        // Configura il paint per il testo
        val paint = Paint()
        paint.color = Color.BLACK
        paint.textSize = textSize

        // Disegna il contenuto sulla pagina
        val contentRect = Rect()
        page.getCanvas().getClipBounds(contentRect)
        val textPaint = TextPaint()
        textPaint.setTextSize(textSize.toFloat())
        StaticLayout.Builder.obtain(content, 0, content.length, textPaint, contentRect.width())
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(lineSpacing, 1f)
            .setIncludePad(true)
            .build()
            .draw(canvas)

        // Fine del documento
        pdfDocument.finishPage(page)

        // Salva il documento PDF su file
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
        try {
            val fileOutputStream = FileOutputStream(file)
            pdfDocument.writeTo(fileOutputStream)
            fileOutputStream.close()
            pdfDocument.close()
            return file
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    // Metodo per avviare il download del file PDF
    private fun downloadPDF(file: File?) {
        file?.let {
            val uri = context?.let { it1 -> FileProvider.getUriForFile(it1, authority, file) }
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            context?.startActivity(intent)
        }
    }

}