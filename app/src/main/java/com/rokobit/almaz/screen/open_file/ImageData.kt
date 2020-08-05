package com.rokobit.almaz.screen.open_file

import android.graphics.Bitmap

class ImageData private constructor() {
    var initialPictureDensity = 0 // in dots per cm
    private var initialPictureDensityIsLocked // коли стає true змінити роздільну здатність вже неможна
            = false
    var mainFileImageWidth // in pixels
            = 0
    var mainFileImageHeight // in pixels
            = 0
    var initialImageWidth_mm = 0f
    var initialImageHeight_mm = 0f
    var taskId // id завдання до якого належить група файлів
            = 0
   /* var printerDeviceList: List<PrinterDevice>? = null
    var connectedPrinter: PrinterDevice? = null
    var layerImageDataList: List<LayerImageData>? = null
    private var mainPreviewMonoImages: List<MainPreviewMonoImage>? = null
    var printImageDataList: List<PrintImageData>? = null
        set(printImageDataList) {
            field = printImageDataList
            if (printImageDataList == null) System.gc()
        }
    var itemsToSaveList: List<ItemToSave>? = null
        set(itemsToSaveList) {
            field = itemsToSaveList
            if (itemsToSaveList == null) System.gc()
        }
    var mainLayer: LayerImageData? =
        null // шар створюємо один раз при відкритті файлу і зберігаємо тут для оптимізації швидкості
*/
    //        saveBitmapToDisk(mainFileImage);
    //        return readBitmapFromFile();
    var mainFileImageBitmap: Bitmap? = null
    var selectedFileNameToOpen: String? =
        null // назва файлу який користувач пробує відкрити (він може і не відкрити цей файл)
    var isSelectedFileOpened =
        false // прапор який показує що вибраний файл був відкритий
        private set
    private var filesHaveBeenSaved =
        true // прапор який показує що результат роботи був збережений
    private var projectMustBeResubmitted =
        false // прапор який показує що відбулися зміни в даних.
    private var projectResubmittingСonfirmed =
        false // ця змінна містить відповідь користувача на діалог про необхідність повторної передачі проекту
    var imageHistory // це клас в якому зберігаються малюнки по мірі їх редагування в конструкторі
            : ImageHistory? = null
    /*var contoursList // список даних контурів. 0 контур це завжди контур полотна
            : List<Contour>? = null
    */var lastPrinterCommand: String? =
        null // остання команда, яка була відправлена на принтер
    private var mNewProject: Project? = null
    private var mLastProject: Project? = null

    fun filesHaveBeenSaved(): Boolean {
        return filesHaveBeenSaved
    }

    fun setFilesHaveBeenSaved(filesHaveBeenSaved: Boolean) {
        this.filesHaveBeenSaved = filesHaveBeenSaved
    }

    fun setSelectedFileIsOpened(selectedFileWasOpened: Boolean) {
        isSelectedFileOpened = selectedFileWasOpened
    }

   /* var mainPreviewMonoImageList: List<Any>?
        get() = mainPreviewMonoImages
        set(mainPreviewMonoImages) {
            this.mainPreviewMonoImages = mainPreviewMonoImages
        }
*/
    fun initialPictureDensityIsLocked(): Boolean {
        return initialPictureDensityIsLocked
    }

    fun lockInitialPictureDensity() {
        initialPictureDensityIsLocked = true
    }

    fun setInitialPictureDensityIsLocked(initialPictureDensityIsLocked: Boolean) {
        this.initialPictureDensityIsLocked = initialPictureDensityIsLocked
    }

    var newProject: Project?
        get() = mNewProject
        set(project) {
            mNewProject = project
        }

    var lastProject: Project?
        get() = mLastProject
        set(lastProject) {
            mLastProject = lastProject
        }

    fun projectMustBeResubmitted(): Boolean {
        return projectMustBeResubmitted
    }

    fun setProjectMustBeResubmitted(projectMustBeResubmitted: Boolean) {
        this.projectMustBeResubmitted = projectMustBeResubmitted
    }

    fun projectResubmittingIsСonfirmed(): Boolean {
        return projectResubmittingСonfirmed
    }

    fun setProjectResubmittingСonfirmed(projectResubmittingСonfirmed: Boolean) {
        this.projectResubmittingСonfirmed = projectResubmittingСonfirmed
    }

    companion object {
        private var instance: ImageData? = null
        fun getInstance(): ImageData? {
            if (instance == null) {
                instance = ImageData()
            }
            return instance
        }

        fun destroy() {
            instance = null
            System.gc()
        }
    }
}
