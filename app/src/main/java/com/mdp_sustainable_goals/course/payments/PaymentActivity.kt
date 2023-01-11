package com.mdp_sustainable_goals.course.payments

import android.Manifest
import android.content.ContentValues
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mdp_sustainable_goals.course.LoginActivity
import com.mdp_sustainable_goals.course.R
import com.mdp_sustainable_goals.course.local_storage.AppDatabase
import com.mdp_sustainable_goals.course.local_storage.entity.CertificateEntity
import com.mdp_sustainable_goals.course.showCustomToast
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.UIKitCustomSetting
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.BillingAddress
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.corekit.models.ShippingAddress
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

class PaymentActivity : AppCompatActivity(), TransactionFinishedCallback {
    private lateinit var imageView3: ImageView
    private lateinit var btnPay: Button

    lateinit var bmp: Bitmap
    lateinit var scaledbmp: Bitmap

    var pageWidth = 842
    var pageHeight = 595
    var PERMISSION_CODE = 101

    var userFullName = ""
    var username = ""
    var className = ""
    var certID = -1
    var classId = -1

    private lateinit var db: AppDatabase
    private val coroutine = CoroutineScope(Dispatchers.IO)
    lateinit var tempCert: CertificateEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val sharedFile = packageName
        val shared: SharedPreferences = getSharedPreferences(sharedFile, MODE_PRIVATE)
        userFullName = shared.getString(LoginActivity.user_name, "-")!!
        username = shared.getString(LoginActivity.user_username, "-")!!
        className = intent.getStringExtra("className")!!
        certID = intent.getIntExtra("certID", -1)
        classId = intent.getIntExtra("classId", -1)

        imageView3 = findViewById(R.id.imageView3)
        btnPay = findViewById(R.id.btnPay)

        db = AppDatabase.build(this)
        coroutine.launch {
            tempCert = db.certificateDao().getByClassId(classId, username)!!
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), 101)
        }

        SdkUIFlowBuilder.init()
            .setClientKey("SB-Mid-client-F9HkZnDqOlLDYxLd")
            .setContext(this)
            .setTransactionFinishedCallback(this)
            .setUIkitCustomSetting(uiKitCustomSetting())
            .setMerchantBaseUrl("https://samgun-official.my.id/payment_response.php/")
            .enableLog(true)
            .setColorTheme(
                CustomColorTheme(
                    "#FFE51255",
                    "#B61548",
                    "#FFE51255"
                )
            )
            .setLanguage("en")
            .buildSDK()

        imageView3.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                generatePDF()
            }
        }
        btnPay.setOnClickListener {
            val transactionRequest = TransactionRequest("ORDER_" + System.currentTimeMillis().toString(), (1000000).toDouble())
            val itemDetails1 = ItemDetails("Certificate-${tempCert.certificate_number}", (1000000).toDouble(), 1, "Completing Class ${tempCert.module_nama}")
            val itemDetailsList: ArrayList<ItemDetails> = ArrayList()
            itemDetailsList.add(itemDetails1)
            uiKitDetails(transactionRequest)
            transactionRequest.itemDetails = itemDetailsList
            MidtransSDK.getInstance().transactionRequest = transactionRequest
            MidtransSDK.getInstance().startPaymentUiFlow(this)
        }

        bmp = BitmapFactory.decodeResource(resources, R.drawable.certificate_template)
        scaledbmp = Bitmap.createScaledBitmap(bmp, pageWidth, pageHeight, false)
        if (!checkPermissions()) {
            requestPermission()
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun generatePDF() {
        val pdfDocument: PdfDocument = PdfDocument()

        val paint: Paint = Paint()
        val title: Paint = Paint()

        val myPageInfo: PdfDocument.PageInfo? =
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        val myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)

        val canvas: Canvas = myPage.canvas
        val canvas2: Canvas = Canvas(scaledbmp)

        canvas.drawBitmap(scaledbmp, 0F, 0F, paint)
        canvas2.drawBitmap(scaledbmp, 0F, 0F, paint)

        title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        title.textSize = 15F
        title.color = ContextCompat.getColor(this, R.color.purple_200)

        title.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        title.color = ContextCompat.getColor(this, R.color.black)
        title.textSize = 24F

        title.textAlign = Paint.Align.CENTER
        canvas.drawText(tempCert.user_nama, 422F, 248F, title)
        canvas2.drawText(tempCert.user_nama, 422F, 248F, title)
        canvas.drawText(className, 422F, 366F, title)
        canvas2.drawText(className, 422F, 366F, title)
        canvas.drawText(tempCert.issued_date, 222F, 464F, title)
        canvas2.drawText(tempCert.issued_date, 222F, 464F, title)
        canvas.drawText(tempCert.certificate_number, 422F, 464F, title)
        canvas2.drawText(tempCert.certificate_number, 422F, 464F, title)

        try {
            val fileName = "Certificate.jpg"
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/")
                values.put(MediaStore.MediaColumns.IS_PENDING, 1)
            } else {
                val directory =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                val file = File(directory, fileName)
                values.put(MediaStore.MediaColumns.DATA, file.absolutePath)
            }
            val uri: Uri? =
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            contentResolver.openOutputStream(uri!!).use { output ->
                // val bm: Bitmap = textureView.getBitmap()
                scaledbmp.compress(Bitmap.CompressFormat.JPEG, 100, output)
            }
        } catch (e: java.lang.Exception) {
            Log.d("onBtnSavePng", e.toString()) // java.io.IOException: Operation not permitted
        }
        pdfDocument.finishPage(myPage)

        val file: File = File(Environment.getExternalStorageDirectory(), "Certificate.pdf")
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast(applicationContext).showCustomToast(
                "PDF file generated!",
                this,
                "success"
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Toast(applicationContext).showCustomToast(
                "Fail to generate PDF file!",
                this,
                "error"
            )
        }
        pdfDocument.close()
    }

    private fun checkPermissions(): Boolean {
        val writeStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val readStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return writeStoragePermission == PackageManager.PERMISSION_GRANTED
                && readStoragePermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    Toast(applicationContext).showCustomToast(
                        "Permission Granted",
                        this,
                        "success"
                    )
                } else {
                    Toast(applicationContext).showCustomToast(
                        "Permission Denied",
                        this,
                        "error"
                    )
                }
            }
        }
    }

    override fun onTransactionFinished(result: TransactionResult) {
        if (result.response != null) {
            fetchedData(result.response.transactionId, "SB-Mid-client-F9HkZnDqOlLDYxLd")
            when (result.status) {
                TransactionResult.STATUS_SUCCESS -> {
                    Toast.makeText(
                        this,
                        "Transaction Finished. ID: " + result.response.transactionId,
                        Toast.LENGTH_LONG
                    ).show()
                    println("Transaction Finished. ID: " + result.response.transactionId)
                }
                TransactionResult.STATUS_PENDING -> {
                    Toast.makeText(
                        this,
                        "Transaction Pending. ID: " + result.response.transactionId,
                        Toast.LENGTH_LONG
                    ).show()
                    println("Transaction Pending. ID: " + result.response.transactionId)
                }
                TransactionResult.STATUS_FAILED -> {
                    Toast.makeText(
                        this,
                        "Transaction Failed. ID: " + result.response.transactionId.toString() + ". Message: " + result.response.statusMessage,
                        Toast.LENGTH_LONG
                    ).show()
                    println("Transaction Failed. ID: " + result.response.transactionId.toString() + ". Message: " + result.response.statusMessage)
                }
            }
            result.response.validationMessages
        } else if (result.isTransactionCanceled) {
            Toast.makeText(this, "Transaction Canceled", Toast.LENGTH_LONG).show()
            println("Transaction Canceled")
        } else {
            if (result.status.equals(TransactionResult.STATUS_INVALID, true)) {
                Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show()
                println("Transaction Invalid")
            } else {
                Toast.makeText(this, "Transaction Finished with Failure.", Toast.LENGTH_LONG).show()
                println("Transaction Finished with Failure.")
            }
        }
    }

    private fun fetchedData(transaction_id: String, client_id: String) {
        // https://api.sandbox.midtrans.com/v2/[ORDER_ID]/status
        var response: JSONObject? = null
        val strReq = object: StringRequest(
            Method.POST, "https://samgun-official.my.id/payment_handler.php/status",
            Response.Listener {
                response = JSONObject(it)
                println("==================================================")
                println(response)
                println(response!!.getString("transaction_id"))
                println(response!!.getString("order_id"))
                println(response!!.getString("gross_amount"))
                println(response!!.getString("transaction_status"))
                println("==================================================")
//                val list: JSONArray = response.getJSONArray("datagroup")
//                groupList.clear()
//                for(i in 0 until list.length()) {
//                    val obj = list.getJSONObject(i)
//                    val id = obj.getString("id")
//                    val group_name = obj.getString("group_name")
//                    val agency = obj.getString("agency")
//                    val gender = obj.getString("gender")
//                    val group_desc = obj.getString("group_desc")
//                    val image = obj.getString("image")
//                    val boyGroup = GroupEntity(id, group_name, agency, gender, group_desc, image)
//                    groupList.add(boyGroup)
//                }
//                groupAdapter.notifyDataSetChanged()
            },
            Response.ErrorListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["transaction_id"] = transaction_id
                params["client_id"] = client_id
                return params
            }
        }
        val queue: RequestQueue = Volley.newRequestQueue(this)
        queue.add(strReq)
    }

    fun uiKitDetails(transactionRequest: TransactionRequest) {
        val customerDetails = CustomerDetails()
        customerDetails.setCustomerIdentifier("samgun-official")
        customerDetails.setPhone("089612494740")
        customerDetails.setFirstName("Samuel")
        customerDetails.setLastName("Gunawan")
        customerDetails.setEmail("gunawansamuel80+midtrans@gmail.com")
        transactionRequest.setCustomerDetails(customerDetails)
    }

    private fun uiKitCustomSetting(): UIKitCustomSetting {
        val uIKitCustomSetting = UIKitCustomSetting()
        uIKitCustomSetting.isSkipCustomerDetailsPages = true
        uIKitCustomSetting.isShowPaymentStatus = true
        return uIKitCustomSetting
    }
}
