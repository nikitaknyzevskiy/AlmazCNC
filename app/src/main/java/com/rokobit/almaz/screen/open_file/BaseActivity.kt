package com.rokobit.almaz.screen.open_file

import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.graphics.*
import android.os.*
import android.util.Log
import android.view.WindowManager
import android.view.WindowManager.BadTokenException
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rokobit.almaz.MainActivity
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

open class BaseActivity : AppCompatActivity(), ComponentCallbacks2 {
    private var sImageData: ImageData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivityInstance =
            WeakReference(this)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        Log.d(
            "conf_1",
            "base activity on create " + baseActivityInstance?.javaClass?.simpleName
        )
        if (mTimer == null) {
            mTimer = createTimer()
            toastIsShown = false
        }
        sImageData = ImageData.getInstance()
    }

    protected fun showToast(resourceId: Int, length: Int) {
        val mHandler = Handler(Looper.getMainLooper())
        mHandler.post {
            if (baseActivityInstance != null) {
                Toast.makeText(
                    baseActivityInstance!!.get(),
                    resourceId,
                    length
                ).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activityInstance = null
        System.gc()
        Log.d("conf_1", " onDestroy() ")
    }

    companion object {
        private val baseActivityHandler =
            Handler(Looper.getMainLooper())
        var appMemoryErrorFlag = false
        var baseActivityInstance: WeakReference<BaseActivity>? = null
        private var toastIsShown = false
        private var mTimer: CountDownTimer? = null
        var activityInstance: WeakReference<Activity>? = null
        val instance: BaseActivity?
            get() = if (baseActivityInstance != null) baseActivityInstance!!.get() else null

        fun createBitmap(width: Int, height: Int, config: Bitmap.Config?): Bitmap? {
            var bitmap: Bitmap? = null
            try {
                bitmap = Bitmap.createBitmap(width, height, config!!)
            } catch (e: OutOfMemoryError) {
                showMemoryErrorDialog(instance)
                appMemoryErrorFlag = true
            }
            return bitmap
        }

        fun showMemoryErrorDialog(baseActivity: BaseActivity?) {
            baseActivityHandler.post(Runnable {
                if (baseActivity == null) {
                    return@Runnable
                }
            })
        }

        private fun createTimer(): CountDownTimer {
            return object : CountDownTimer(120000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    toastIsShown = true
                    Log.d("timer_tick", "timer_tick")
                }

                override fun onFinish() {
                    toastIsShown = false
                }
            }
        }

        fun finishAllActivitiesAndShowSplashFragment() {
            if (appMemoryErrorFlag) {
                if (activityInstance == null) return
                when {
                    activityInstance!!.get() is MainActivity -> {
                        val sImageData = ImageData.getInstance()
                        if (sImageData != null) {
                         //   sImageData.layerImageDataList = null
                            sImageData.mainFileImageBitmap = null
                            sImageData.selectedFileNameToOpen = null
                            sImageData.initialPictureDensity = 0
                            sImageData.setInitialPictureDensityIsLocked(false)
                     /*       sImageData.printImageDataList = null
                            sImageData.mainPreviewMonoImageList = null
                            sImageData.contoursList = null
                            sImageData.itemsToSaveList = null*/
                        }
                    }
                }
            }
        }
    }
}
