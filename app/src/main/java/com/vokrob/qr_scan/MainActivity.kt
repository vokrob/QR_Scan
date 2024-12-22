package com.vokrob.qr_scan

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.vokrob.qr_scan.data.MainDb
import com.vokrob.qr_scan.data.Product
import com.vokrob.qr_scan.ui.theme.QR_ScanTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var mainDb: MainDb
    var counter = 0

    private val scanLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents == null) {

        } else {
            Toast.makeText(this, "Scan data: ${result.contents}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val productStateList = mainDb.dao.getAllProducts().collectAsState(initial = emptyList())
            val coroutineScope = rememberCoroutineScope()

            QR_ScanTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(top = 50.dp)
                            .fillMaxWidth()
                            .fillMaxHeight(0.9f)
                    ) {
                        items(productStateList.value) { product ->
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = product.name,
                                textAlign = TextAlign.Center
                            )
                            Spacer(
                                modifier = Modifier.height(10.dp)
                            )
                        }
                    }
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                mainDb.dao.insertProduct(
                                    Product(
                                        null,
                                        "Product ${counter++}",
                                        "aegatduygatdufgatdfigatdu"
                                    )
                                )
                            }
                        }
                    ) {
                        Text(
                            text = "Create data"
                        )
                    }
                }
            }
        }
    }

    private fun scan() {
        val options = ScanOptions()

        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("Scan a barcode")
        options.setCameraId(0)
        options.setBeepEnabled(false)
        options.setBarcodeImageEnabled(true)
        scanLauncher.launch(options)
    }
}

















