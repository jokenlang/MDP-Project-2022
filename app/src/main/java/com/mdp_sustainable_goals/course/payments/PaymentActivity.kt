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
import com.mdp_sustainable_goals.course.LoginActivity
import com.mdp_sustainable_goals.course.R
import com.mdp_sustainable_goals.course.local_storage.AppDatabase
import com.mdp_sustainable_goals.course.local_storage.entity.CertificateEntity
import com.mdp_sustainable_goals.course.showCustomToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class PaymentActivity : AppCompatActivity() {
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

        imageView3.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                generatePDF()
            }
        }
        btnPay.setOnClickListener {
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
}
