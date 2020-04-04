package org.kaviya.data_sharing

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.kaviya.data_sharing.MainActivity
import java.io.FileNotFoundException

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val select_photo = 1

    companion object {
        private var selectImage: Button? = null
        private var shareImage: Button? = null
        private var shareText: Button? = null
        private var imageView: ImageView? = null
        private var textToShare: EditText? = null
        // Uri for image path
        private var imageUri: Uri? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        setListeners()
    }

    //region Initialize views
    private fun init() {
        selectImage = findViewById<View>(R.id.select_image) as Button
        shareImage = findViewById<View>(R.id.share_image) as Button
        shareText = findViewById<View>(R.id.btnTextShare) as Button
        imageView = findViewById<View>(R.id.share_imageview) as ImageView
        textToShare = findViewById<View>(R.id.text_share) as EditText
    }

    //endregion


    //region Implement click listeners
    private fun setListeners() {
        selectImage!!.setOnClickListener(this)
        shareImage!!.setOnClickListener(this)
        shareText!!.setOnClickListener(this)
    }

    //endregion


    //region onclick method button
    override fun onClick(view: View) {
        when (view.id) {
            R.id.select_image -> {
                // Intent to gallery
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                // start intent
                startActivityForResult(intent, select_photo)
            }
            // share image
            R.id.share_image ->
                shareImage(imageUri)
            // Share text
            R.id.btnTextShare -> {
                val getText = textToShare!!.text.toString()
                if (getText != "" && getText.length != 0) shareText(getText)
                else Toast.makeText(this, "Please enter something to share.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //endregion


    //region onActivityResult callback
    // Get intent data
    override fun onActivityResult(requestcode: Int, resultcode: Int, imagereturnintent: Intent?) {
        super.onActivityResult(requestcode, resultcode, imagereturnintent)
        when (requestcode) {
            select_photo -> if (resultcode == Activity.RESULT_OK) {
                try {
                    imageUri = imagereturnintent!!.data
                    // call
                    val bitmap = Utils.decodeUri(this, imageUri, 200)
//                    // Set image over
                    if (bitmap != null) {
                        imageView!!.setImageBitmap(bitmap)
                        // bitmap Visible button
                        shareImage!!.visibility = View.VISIBLE
                        // if bitmap is not null
                    } else {
                        shareImage!!.visibility = View.GONE
                        Toast.makeText(this, "Error while decoding image.", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    Toast.makeText(this, "File not found.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //endregion


    //region Share text
    private fun shareText(text: String) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, text)
        startActivity(Intent.createChooser(sharingIntent, "Share Text Using"))
    } //endregion

    //region Share image
    private fun shareImage(imagePath: Uri?) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
        sharingIntent.type = "image/*"
        sharingIntent.putExtra(Intent.EXTRA_STREAM, imagePath)
        startActivity(Intent.createChooser(sharingIntent, "Share Image Using"))
    }

    //endregion




}