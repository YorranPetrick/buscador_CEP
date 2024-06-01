package com.yorran.viacep

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.yorran.viacep.api.ApiRetroFit
import com.yorran.viacep.api.Endereco
import com.yorran.viacep.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var mainBiding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBiding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(mainBiding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Configuração do RetroFit
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://viacep.com.br/")
            .build()
            .create(ApiRetroFit::class.java)


        mainBiding.searchCep.setOnClickListener{
           val cep = mainBiding.textInputCep.text.toString()
            if (cep.isNotEmpty()){

                retrofit.setEndereco(cep).enqueue(object : Callback<Endereco>{
                    //Função para quando a requisição é um sucesso
                    override fun onResponse(call: Call<Endereco>, response: Response<Endereco>) {
                        // Veificando se a requisição deu certo
                        if(response.code() == 200){
                            val ddd = response.body()?.ddd.toString()
                            val uf = response.body()?.uf.toString()
                            val logradouro = response.body()?.logradouro.toString()
                            val complemento = response.body()?.complemento.toString()

                            verificacaoUF(uf)
                            setFormularios(ddd, uf, logradouro, complemento)

                        }
                    }
                    //Função para quando a requisição dá erro
                    override fun onFailure(call: Call<Endereco>, t: Throwable) {
                        TODO("Not yet implemented")
                    }

                })

            }
            else{
                Toast.makeText(this, "CEP can´t be empty", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun setFormularios(ddd: String, uf: String, logradouro: String, complemento : String){
        mainBiding.txtDdd.setText("DDD\n\n$ddd")
        mainBiding.txtLocalidade.setText(uf)
        mainBiding.txtLogradouro.setText("Logradouro\n$logradouro")
        mainBiding.txtComplemento.setText("Complemento\n$complemento")
    }


    fun verificacaoUF(uf: String){
        if (uf.isNotEmpty()){
            val resourceId = resources.getIdentifier(uf.lowercase(), "drawable", packageName)
            val image = BitmapFactory.decodeResource(resources, resourceId)
            val circularImage = RoundedBitmapDrawableFactory.create(resources, image)
            mainBiding.uf.setImageDrawable(circularImage)
        }else{
            mainBiding.uf.setImageResource(R.drawable.ic_launcher_foreground)
        }
    }
}