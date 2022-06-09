package com.example.krunalshah.info6130_lab2

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button

/**
 * TODO: document your custom view class.
 */
class AnimationView : View {

    private lateinit var sButton: Button
    private lateinit var aV: AnimationView

    private var _exampleString: String? = null // TODO: use a default from R.string...
    private var _exampleColor: Int = Color.RED // TODO: use a default from R.color...
    private var _exampleDimension: Float = 0f // TODO: use a default from R.dimen...

    private lateinit var textPaint: TextPaint
    private var textWidth: Float = 0f
    private var textHeight: Float = 0f

    private val ANIMATION_DURATION = 10000
    private val ANIMATION_DELAY: Long = 1000
    private val COLOR_ADJUSTER = 8
    private var mX = 0f
    private var mY = 0f
    private var mRadius = 0f
    private val mPaint = Paint()
    private val mAnimatorSet = AnimatorSet()

    /**
     * The text to draw
     */
    var exampleString: String?
        get() = _exampleString
        set(value) {
            _exampleString = value
            invalidateTextPaintAndMeasurements()
        }

    /**
     * The font color
     */
    var exampleColor: Int
        get() = _exampleColor
        set(value) {
            _exampleColor = value
            invalidateTextPaintAndMeasurements()
        }

    /**
     * In the example view, this dimension is the font size.
     */
    var exampleDimension: Float
        get() = _exampleDimension
        set(value) {
            _exampleDimension = value
            invalidateTextPaintAndMeasurements()
        }

    /**
     * In the example view, this drawable is drawn above the text.
     */
    var exampleDrawable: Drawable? = null

    constructor(context: Context) : super(context) {
//        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
//        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
//        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.AnimationView, defStyle, 0
        )

        _exampleString = a.getString(
            R.styleable.AnimationView_exampleString
        )
        _exampleColor = a.getColor(
            R.styleable.AnimationView_exampleColor,
            exampleColor
        )
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        _exampleDimension = a.getDimension(
            R.styleable.AnimationView_exampleDimension,
            exampleDimension
        )

        if (a.hasValue(R.styleable.AnimationView_exampleDrawable)) {
            exampleDrawable = a.getDrawable(
                R.styleable.AnimationView_exampleDrawable
            )
            exampleDrawable?.callback = this
        }

        a.recycle()

        // Set up a default TextPaint object
        textPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.LEFT
        }

        // Update TextPaint and text measurements from attributes
//        invalidateTextPaintAndMeasurements()

        aV = findViewById(R.id.aV)

        sButton = findViewById(R.id.stopButton)
        sButton.setOnClickListener {
            aV.clearAnimation()
        }
    }

    private fun invalidateTextPaintAndMeasurements() {
        textPaint.let {
            it.textSize = exampleDimension
            it.color = exampleColor
//            textWidth = it.measureText(exampleString)
            textHeight = it.fontMetrics.bottom
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(mX, mY, mRadius, mPaint)
//        canvas.drawOval(mRadius, mRadius, mRadius, mRadius, mPaint)

        /*
        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom

        exampleString?.let {
            // Draw the text.
            canvas.drawText(
                it,
                paddingLeft + (contentWidth - textWidth) / 2,
                paddingTop + (contentHeight + textHeight) / 2,
                textPaint
            )
        }

        // Draw the example drawable on top of the text.
        exampleDrawable?.let {
            it.setBounds(
                paddingLeft, paddingTop,
                paddingLeft + contentWidth, paddingTop + contentHeight
            )
            it.draw(canvas)
        }
        */
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val growAnimator = ObjectAnimator.ofFloat(this, "radius", 0f, width.toFloat())
        growAnimator.duration = ANIMATION_DURATION.toLong()
        growAnimator.interpolator = LinearInterpolator()

        val shrinkAnimator = ObjectAnimator.ofFloat(this, "radius", width.toFloat())
        shrinkAnimator.duration = ANIMATION_DURATION.toLong()
        shrinkAnimator.startDelay = ANIMATION_DELAY

        val repeatanimator = ObjectAnimator.ofFloat(this, "radius", 0f, width.toFloat())
        repeatanimator.duration = ANIMATION_DURATION.toLong()
        repeatanimator.startDelay = ANIMATION_DELAY
        repeatanimator.repeatCount = 1
        repeatanimator.repeatMode = ValueAnimator.REVERSE
        //combine teh two animations into a sequence
        mAnimatorSet.play(growAnimator).before(shrinkAnimator)
        mAnimatorSet.play(repeatanimator).after(shrinkAnimator)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        //return super.onTouchEvent(event: MotionEvent): Boolean {
        if (event.actionMasked == MotionEvent.ACTION_DOWN){
            mX = event.x
            mY = event.y

            if (mAnimatorSet.isRunning){
                mAnimatorSet.cancel()
            }
            //start the animcation sequence
            mAnimatorSet.start()
        }
        return super.onTouchEvent(event)
    }

    fun setRadius(radius:Float){
        mRadius = radius
        mPaint.color = Color.GREEN + radius.toInt()/COLOR_ADJUSTER
        invalidate()
    }
}