package org.kaviya.data_sharing

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.FileNotFoundException

object Utils {

        /* Method that deocde uri into bitmap. This method is necessary to deocde
         large size images to load over imageview*/
        @Throws(FileNotFoundException::class)
        fun decodeUri(context: Context, uri: Uri?, requiredSize: Int): Bitmap {
            val opt = BitmapFactory.Options()
            opt.inJustDecodeBounds = true
            BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri), null, opt)
            var width_tmp = opt.outWidth
            var height_tmp = opt.outHeight
            var scale = 1
            while (true) {
                if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                    break
                width_tmp /= 2
                height_tmp /= 2
                scale *= 2
            }

            val opt2 = BitmapFactory.Options()
            opt2.inSampleSize = scale
            return BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri), null, opt2)
        }
    }


