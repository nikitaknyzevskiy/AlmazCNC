package com.rokobit.almaz.screen

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.rokobit.almaz.R
import kotlin.math.min

enum class MotionType { MINUS_X, PLUS_Y, PLUS_X, MINUS_Y }

class Gamepad(context: Context, attrs: AttributeSet) : View(context, attrs) {

    interface OnButtonTouchListener {
        fun onActionDown(motionType: MotionType)
        fun onActionUp(motionType: MotionType)
    }

    private lateinit var onButtonTouchListener: OnButtonTouchListener

    private val path: Path = Path()

    private val buttonDefaultPaint: Paint = Paint()
    private val verticalGradientPaint: Paint = Paint()
    private val horizontalGradientPaint: Paint = Paint()
    private val borderPaint: Paint = Paint()
    private val arrowPaint: Paint = Paint()

    private val leftRoundness: RectF = RectF()
    private val topRoundness: RectF = RectF()
    private val rightRoundness: RectF = RectF()
    private val bottomRoundness: RectF = RectF()

    private var color = ContextCompat.getColor(context, android.R.color.holo_blue_light)
    private var gradientColor = Color.BLACK
    private var borderColor = Color.BLACK
    private var arrowColor = Color.WHITE
    private var borderStrokeWidth = 5f
    private var buttonSize = 30
    private var arrowSize = 8
    private var arrowStrokeWidth = 20f
    private var roundness = 30

    private var isTouchLeft = false
    private var isTouchTop = false
    private var isTouchRight = false
    private var isTouchBottom = false

    private var buttonStart = 0f
    private var buttonEnd = 0f

    private var arcSize = 0f

    private var screenSize = 0

    private var leftArrowX = 0f
    private var leftArrowY = 0f
    private var topArrowX = 0f
    private var topArrowY = 0f
    private var rightArrowX = 0f
    private var rightArrowY = 0f
    private var bottomArrowX = 0f
    private var bottomArrowY = 0f

    init {
        setupAttributes(attrs)
    }

    fun setOnButtonTouchListener(onButtonTouchListener: OnButtonTouchListener) {
        this.onButtonTouchListener = onButtonTouchListener
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        screenSize = min(width, height)

        arcSize = screenSize * roundness / 100.toFloat()

        buttonStart = (screenSize / 2 - screenSize * buttonSize / 200).toFloat()
        buttonEnd = (screenSize / 2 + screenSize * buttonSize / 200).toFloat()

        arrowSize = screenSize * arrowSize / 100

        topRoundness.set(buttonStart, 0f, buttonEnd, arcSize)
        bottomRoundness.set(buttonStart, screenSize - arcSize, buttonEnd, screenSize.toFloat())

        rightRoundness.set(screenSize - arcSize, buttonStart, screenSize.toFloat(), buttonEnd)
        leftRoundness.set(0f, buttonStart, arcSize, buttonEnd)

        leftArrowX = arcSize / 1.5.toFloat()
        leftArrowY = screenSize / 2.toFloat()
        topArrowX = screenSize / 2.toFloat()
        topArrowY = arcSize / 1.5.toFloat()
        rightArrowX = screenSize - arcSize / 1.5.toFloat()
        rightArrowY = screenSize / 2.toFloat()
        bottomArrowX =  screenSize / 2.toFloat()
        bottomArrowY = screenSize - arcSize / 1.5.toFloat()

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        setPaintProperties()

        drawVerticalButton(
            buttonStart,
            buttonStart,
            buttonEnd,
            arcSize,
            topRoundness,
            180f,
            canvas,
            if (isTouchTop) verticalGradientPaint else buttonDefaultPaint
        )

        drawVerticalButton(
            buttonEnd,
            buttonEnd,
            buttonStart,
            screenSize - arcSize,
            bottomRoundness,
            0f,
            canvas,
            if (isTouchBottom) verticalGradientPaint else buttonDefaultPaint
        )

        drawVerticalBorder(canvas)

        drawHorizontalButton(
            buttonEnd,
            buttonStart,
            screenSize - arcSize,
            buttonEnd,
            rightRoundness,
            270f,
            canvas,
            if (isTouchRight) horizontalGradientPaint else buttonDefaultPaint
        )

        drawHorizontalButton(
            buttonStart,
            buttonEnd,
            arcSize,
            buttonStart,
            leftRoundness,
            90f,
            canvas,
            if (isTouchLeft) horizontalGradientPaint else buttonDefaultPaint
        )

        fillCentralSquare(canvas)
        drawHorizontalBorder(canvas)
        drawArrows(canvas)

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchY = event.y.toDouble()
        val touchX = event.x.toDouble()

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (touchX in buttonStart..buttonEnd) {
                    if (touchY > buttonEnd) {
                        isTouchBottom = true
                        onButtonTouchListener.onActionDown(MotionType.MINUS_Y)
                    }
                    if (touchY < buttonStart) {
                        isTouchTop = true
                        onButtonTouchListener.onActionDown(MotionType.PLUS_Y)
                    }
                }
                if (touchY in buttonStart..buttonEnd) {
                    if (touchX > buttonEnd) {
                        isTouchRight = true
                        onButtonTouchListener.onActionDown(MotionType.PLUS_X)
                    }
                    if (touchX < buttonStart) {
                        isTouchLeft = true
                        onButtonTouchListener.onActionDown(MotionType.MINUS_X)
                    }
                }
                this.invalidate()
                return true
            }
            MotionEvent.ACTION_UP -> {
                if (touchX in buttonStart..buttonEnd) {
                    if (touchY > buttonEnd) onButtonTouchListener.onActionUp(MotionType.MINUS_Y)
                    if (touchY < buttonStart) onButtonTouchListener.onActionUp(MotionType.PLUS_Y)
                }
                if (touchY in buttonStart..buttonEnd) {
                    if (touchX > buttonEnd) onButtonTouchListener.onActionUp(MotionType.PLUS_X)
                    if (touchX < buttonStart) onButtonTouchListener.onActionUp(MotionType.MINUS_X)
                }
                isTouchTop = false
                isTouchBottom = false
                isTouchLeft = false
                isTouchRight = false
                this.invalidate()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun setupAttributes(attrs: AttributeSet) {
        val attrsList = context.theme.obtainStyledAttributes(attrs, R.styleable.Gamepad, 0, 0)
        color = attrsList.getColor(R.styleable.Gamepad_color, color)
        gradientColor = attrsList.getColor(R.styleable.Gamepad_gradientColor, gradientColor)
        borderColor = attrsList.getColor(R.styleable.Gamepad_borderColor, borderColor)
        arrowColor = attrsList.getColor(R.styleable.Gamepad_arrowColor, arrowColor)
        borderStrokeWidth = attrsList.getFloat(R.styleable.Gamepad_borderStrokeWidth, borderStrokeWidth)
        buttonSize = attrsList.getInt(R.styleable.Gamepad_buttonWidth, buttonSize)
        arrowSize = attrsList.getInt(R.styleable.Gamepad_arrowSize, arrowSize)
        arrowStrokeWidth =
            attrsList.getFloat(R.styleable.Gamepad_arrowStrokeWidth, arrowStrokeWidth)
        roundness = attrsList.getInt(R.styleable.Gamepad_roundness, roundness)
        attrsList.recycle()
    }

    private fun setPaintProperties() {
        setDefaultPaint()
        setTouchPaints()
        setBorderPaint()
        setArrowPaint()
    }

    private fun setCommonPaintProperties(paint: Paint) {
        paint.isDither = true
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        paint.isAntiAlias = true
    }

    private fun setDefaultPaint() {
        val defaultButtonColor = color
        setCommonPaintProperties(buttonDefaultPaint)
        buttonDefaultPaint.style = Paint.Style.FILL
        buttonDefaultPaint.color = defaultButtonColor
    }

    private fun setTouchPaints() {
        val verticalButtonGradient = LinearGradient(
            0f, 0f, 0f, (height / 2).toFloat(),
            intArrayOf(gradientColor, color, color),
            floatArrayOf(0f, 0.85.toFloat(), 1f), Shader.TileMode.MIRROR
        )
        val horizontalButtonGradient = LinearGradient(
            0f, 0f, (width / 2).toFloat(), 0f,
            intArrayOf(gradientColor, color, color),
            floatArrayOf(0f, 0.85.toFloat(), 1f), Shader.TileMode.MIRROR
        )

        setCommonPaintProperties(verticalGradientPaint)
        verticalGradientPaint.style = Paint.Style.FILL
        verticalGradientPaint.shader = verticalButtonGradient

        setCommonPaintProperties(horizontalGradientPaint)
        horizontalGradientPaint.style = Paint.Style.FILL
        horizontalGradientPaint.shader = horizontalButtonGradient
    }

    private fun setBorderPaint() {
        setCommonPaintProperties(borderPaint)
        borderPaint.color = borderColor
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = borderStrokeWidth
    }

    private fun setArrowPaint() {
        setCommonPaintProperties(arrowPaint)
        arrowPaint.style = Paint.Style.STROKE
        arrowPaint.color = arrowColor
        arrowPaint.strokeWidth = arrowStrokeWidth
    }

    private fun drawVerticalButton(
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        arc: RectF,
        angle: Float,
        canvas: Canvas,
        paint: Paint
    ) {
        path.reset()
        path.moveTo(x1, y1)
        path.lineTo(x1, y2)
        path.arcTo(arc, angle, 180f, true)
        path.lineTo(x2, y1)
        path.lineTo(x1, y1)
        canvas.drawPath(path, paint)
    }

    private fun drawHorizontalButton(
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        semiRect: RectF,
        angle: Float,
        canvas: Canvas,
        paint: Paint
    ) {
        path.reset()
        path.moveTo(x1, y1)
        path.lineTo(x2, y1)
        path.arcTo(semiRect, angle, 180f, true)
        path.lineTo(x1, y2)
        path.lineTo(x1, y1)
        canvas.drawPath(path, paint)
    }

    private fun drawVerticalBorder(
        canvas: Canvas
    ) {
        path.reset()
        path.moveTo(buttonStart, arcSize / 2)
        path.arcTo(topRoundness, 180f, 180f, true)
        path.lineTo(buttonEnd, screenSize - arcSize / 2)
        path.arcTo(bottomRoundness, 0f, 180f, true)
        path.lineTo(buttonStart, arcSize / 2)
        canvas.drawPath(path, borderPaint)
    }

    private fun drawHorizontalBorder(canvas: Canvas) {
        path.reset()
        path.moveTo(screenSize - arcSize / 2, buttonStart)
        path.arcTo(rightRoundness, 270f, 180f, true)
        path.lineTo(arcSize / 2, buttonEnd)
        path.arcTo(leftRoundness, 90f, 180f, true)
        path.lineTo(screenSize - arcSize / 2, buttonStart)
        canvas.drawPath(path, borderPaint)
    }

    private fun fillCentralSquare(canvas: Canvas) {
        path.reset()
        path.moveTo(buttonStart, buttonStart)
        path.lineTo(buttonEnd, buttonStart)
        path.lineTo(buttonEnd, buttonEnd)
        path.lineTo(buttonStart, buttonEnd)
        path.lineTo(buttonStart, buttonStart)
        canvas.drawPath(path, buttonDefaultPaint)
    }

    private fun drawArrows(canvas: Canvas) {
        drawLeftArrow(canvas)
        drawTopArrow(canvas)
        drawRightArrow(canvas)
        drawBottomArrow(canvas)
    }

    private fun drawLeftArrow(canvas: Canvas) {
        path.reset()
        path.moveTo(leftArrowX + arrowSize, leftArrowY)
        path.lineTo(leftArrowX - arrowSize / 4, leftArrowY)
        path.close()
        path.moveTo(leftArrowX, leftArrowY - arrowSize / 2)
        path.lineTo(leftArrowX - arrowSize / 2, leftArrowY)
        path.lineTo(leftArrowX, leftArrowY + arrowSize / 2)
        canvas.drawPath(path, arrowPaint)
    }

    private fun drawTopArrow(canvas: Canvas) {
        path.reset()
        path.moveTo(topArrowX, topArrowY + arrowSize)
        path.lineTo(topArrowX, topArrowY - arrowSize / 2)
        path.close()
        path.moveTo(topArrowX - arrowSize / 2, topArrowY)
        path.lineTo(topArrowX, topArrowY - arrowSize / 2)
        path.lineTo(topArrowX + arrowSize / 2, topArrowY)
        canvas.drawPath(path, arrowPaint)
    }

    private fun drawRightArrow(canvas: Canvas) {
        path.reset()
        path.moveTo(rightArrowX - arrowSize, rightArrowY)
        path.lineTo(rightArrowX + arrowSize / 4, rightArrowY)
        path.close()
        path.moveTo(rightArrowX, rightArrowY - arrowSize / 2)
        path.lineTo(rightArrowX + arrowSize / 2, rightArrowY)
        path.lineTo(rightArrowX, rightArrowY + arrowSize / 2)
        canvas.drawPath(path, arrowPaint)
    }

    private fun drawBottomArrow(canvas: Canvas) {
        path.reset()
        path.moveTo(bottomArrowX, bottomArrowY - arrowSize)
        path.lineTo(bottomArrowX, bottomArrowY + arrowSize / 2)
        path.close()
        path.moveTo(bottomArrowX - arrowSize / 2, bottomArrowY)
        path.lineTo(bottomArrowX, bottomArrowY + arrowSize / 2)
        path.lineTo(bottomArrowX + arrowSize / 2, bottomArrowY)
        canvas.drawPath(path, arrowPaint)
    }

}