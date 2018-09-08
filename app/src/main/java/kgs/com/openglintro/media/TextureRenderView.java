/*
 * Copyright (C) 2015 Bilibili
 * Copyright (C) 2015 Zhang Rui <bbcallen@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kgs.com.openglintro.media;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.ISurfaceTextureHolder;
import tv.danmaku.ijk.media.player.ISurfaceTextureHost;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class TextureRenderView extends TextureView implements IRenderView {
    private static final String TAG = "TextureRenderView";
    private MeasureHelper mMeasureHelper;
    private TextureUpdateListener textureUpdateListener;

    public TextureRenderView(Context context) {
        super(context);
        initView(context);
    }

    public TextureRenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TextureRenderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TextureRenderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        mMeasureHelper = new MeasureHelper(this);
        mSurfaceCallback = new SurfaceCallback(this);
        setSurfaceTextureListener(mSurfaceCallback);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public boolean shouldWaitForResize() {
        return false;
    }

    @Override
    protected void onDetachedFromWindow() {
        mSurfaceCallback.willDetachFromWindow();
        super.onDetachedFromWindow();
        mSurfaceCallback.didDetachFromWindow();
    }

    //--------------------
    // Layout & Measure
    //--------------------
    @Override
    public void setVideoSize(int videoWidth, int videoHeight) {
        if (videoWidth > 0 && videoHeight > 0) {
            mMeasureHelper.setVideoSize(videoWidth, videoHeight);
            requestLayout();
        }
    }

    @Override
    public void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen) {
        if (videoSarNum > 0 && videoSarDen > 0) {
            mMeasureHelper.setVideoSampleAspectRatio(videoSarNum, videoSarDen);
            requestLayout();
        }
    }

    @Override
    public void setVideoRotation(int degree) {
        mMeasureHelper.setVideoRotation(degree);
        setRotation(degree);
    }

    @Override
    public void setAspectRatio(int aspectRatio) {
        mMeasureHelper.setAspectRatio(aspectRatio);

        requestLayout();
    }

    @Override
    public void applyTransformation(float videoViewWidth, float videoViewHeight, float videoWidth, float videoHeight, float cropWidth, float cropHeight) {

    }

    public void applyTransformation(float viewWidth, float viewHeight, float videoWidth, float videoHeight, float cropWidth, float cropHeight, float px, float py) {

        Log.d(TAG, "====> in (applyTransformation) video width: " + videoWidth + " video height " + videoHeight
                + " video view width: " + viewWidth + " video view height " + viewHeight);

        float videoWidthScale = videoWidth / viewWidth;
        float videoHeightScale = videoHeight / viewHeight;

        Log.d(TAG, "====> in (applyTransformation) video width scale: " + videoWidthScale + " video height scale: " + videoHeightScale);


        float cropWidthScale = viewWidth / cropWidth;
        float cropHeightScale = viewHeight / cropHeight;

        Log.d(TAG, "====> in (applyTransformation) crop x: " + px + " crop y: " + py + " crop width: " + cropWidth + " crop height: " + cropHeight +
                " crop width scale: " + cropWidthScale + " crop height scale: " + cropHeightScale);

        float cropScale = Math.max(cropWidthScale, cropHeightScale);


        float displayWidth = cropScale * videoWidth;
        float displayHeight = cropScale * videoHeight;

        Log.d("===>", "display width: " + displayWidth + " display height: " + displayHeight);

        float xOff = (viewWidth - displayWidth) / 2;
        float yOff = (viewHeight - displayHeight) / 2;

        Matrix matrix = new Matrix();
        matrix.setScale(videoWidthScale, videoHeightScale);  // resizing texture view to match video resolution ( texture view size = video view size)
        matrix.postTranslate(-px, -py);         // shifting to crop position
        matrix.postScale(cropScale, cropScale);  // scaling to match crop ratio

        setTransform(matrix);

        requestLayout();
        Log.d("===>", "textureview width: " + getWidth() + " textureview height: " + getHeight());

    }

    public void applyFill(float viewWidth, float viewHeight, float videoWidth, float videoHeight, float cropWidth, float cropHeight, float px, float py) {

        Log.d(TAG, "====> in (applyTransformation) video width: " + videoWidth + " video height " + videoHeight
                + " video view width: " + viewWidth + " video view height " + viewHeight);

        float videoWidthScale = videoWidth / viewWidth;
        float videoHeightScale = videoHeight / viewHeight;

        Log.d(TAG, "====> in (applyTransformation) video width scale: " + videoWidthScale + " video height scale: " + videoHeightScale);


        float cropWidthScale = viewWidth / cropWidth;
        float cropHeightScale = viewHeight / cropHeight;

        Log.d(TAG, "====> in (applyTransformation) crop x: " + px + " crop y: " + py + " crop width: " + cropWidth + " crop height: " + cropHeight +
                " crop width scale: " + cropWidthScale + " crop height scale: " + cropHeightScale);

        float cropScale = Math.max(cropWidthScale, cropHeightScale);


        float displayWidth = cropScale * videoWidth;
        float displayHeight = cropScale * videoHeight;

        Log.d("===>", "display width: " + displayWidth + " display height: " + displayHeight);

        float xOff = (viewWidth - displayWidth) / 2;
        float yOff = (viewHeight - displayHeight) / 2;

        Matrix matrix = new Matrix();
        matrix.setScale(videoWidthScale, videoHeightScale);  // resizing texture view to match video resolution ( texture view size = video view size)
        matrix.postTranslate(-px, -py);         // shifting to crop position
        matrix.postScale(cropScale, cropScale);  // scaling to match crop ratio

        setTransform(matrix);

        requestLayout();
        Log.d("===>", "textureview width: " + getWidth() + " textureview height: " + getHeight());

    }


    /*   @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        *//*mMeasureHelper.doMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mMeasureHelper.getMeasuredWidth(), mMeasureHelper.getMeasuredHeight());*//*


    }*/

    /*@Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        updateTextureViewSize(mMeasureHelper.getMeasuredWidth(),mMeasureHelper.getMeasuredHeight());
    }
*/
    public void updateTextureViewSize(int viewWidth, int viewHeight) {
        float scaleX = 2.0f;
        float scaleY = 2.0f;

//        if (mVideoWidth > viewWidth && mVideoHeight > viewHeight) {
//            scaleX = mVideoWidth / viewWidth;
//            scaleY = mVideoHeight / viewHeight;
//        } else if (mVideoWidth < viewWidth && mVideoHeight < viewHeight) {
//            scaleY = viewWidth / mVideoWidth;
//            scaleX = viewHeight / mVideoHeight;
//        } else if (viewWidth > mVideoWidth) {
//            scaleY = (viewWidth / mVideoWidth) / (viewHeight / mVideoHeight);
//        } else if (viewHeight > mVideoHeight) {
//            scaleX = (viewHeight / mVideoHeight) / (viewWidth / mVideoWidth);
//        }

        // Calculate pivot points, in our case crop from center
        int pivotPointX = 500;//viewWidth / 2;
        int pivotPointY = 500;//viewHeight / 2;

        Matrix matrix = new Matrix();
        matrix.setScale(1.34f, 1);
//        matrix.postTranslate(-200, 1);

        this.setTransform(matrix);
        //ViewParent viewParent = getParent();
        //this.setLayoutParams(new ViewGroup.LayoutParams(viewWidth, viewHeight));
    }

    //--------------------
    // TextureViewHolder
    //--------------------

    public IRenderView.ISurfaceHolder getSurfaceHolder() {
        return new InternalSurfaceHolder(this, mSurfaceCallback.mSurfaceTexture, mSurfaceCallback);
    }

    private static final class InternalSurfaceHolder implements IRenderView.ISurfaceHolder {
        private TextureRenderView mTextureView;
        private SurfaceTexture mSurfaceTexture;
        private ISurfaceTextureHost mSurfaceTextureHost;

        public InternalSurfaceHolder(@NonNull TextureRenderView textureView,
                                     @Nullable SurfaceTexture surfaceTexture,
                                     @NonNull ISurfaceTextureHost surfaceTextureHost) {
            mTextureView = textureView;
            mSurfaceTexture = surfaceTexture;
            mSurfaceTextureHost = surfaceTextureHost;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public void bindToMediaPlayer(IMediaPlayer mp) {
            if (mp == null)
                return;

            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) &&
                    (mp instanceof ISurfaceTextureHolder)) {
                ISurfaceTextureHolder textureHolder = (ISurfaceTextureHolder) mp;
                mTextureView.mSurfaceCallback.setOwnSurfaceTexture(false);

                SurfaceTexture surfaceTexture = textureHolder.getSurfaceTexture();
                if (surfaceTexture != null) {
                    mTextureView.setSurfaceTexture(surfaceTexture);
                } else {
                    textureHolder.setSurfaceTexture(mSurfaceTexture);
                    textureHolder.setSurfaceTextureHost(mTextureView.mSurfaceCallback);
                }
            } else {
                mp.setSurface(openSurface());
            }
        }

        @NonNull
        @Override
        public IRenderView getRenderView() {
            return mTextureView;
        }

        @Nullable
        @Override
        public SurfaceHolder getSurfaceHolder() {
            return null;
        }

        @Nullable
        @Override
        public SurfaceTexture getSurfaceTexture() {
            return mSurfaceTexture;
        }

        @Nullable
        @Override
        public Surface openSurface() {
            if (mSurfaceTexture == null)
                return null;
            return new Surface(mSurfaceTexture);
        }
    }

    //-------------------------
    // SurfaceHolder.Callback
    //-------------------------

    @Override
    public void addRenderCallback(IRenderCallback callback) {
        mSurfaceCallback.addRenderCallback(callback);
    }

    @Override
    public void removeRenderCallback(IRenderCallback callback) {
        mSurfaceCallback.removeRenderCallback(callback);
    }

    private SurfaceCallback mSurfaceCallback;

    private static final class SurfaceCallback implements SurfaceTextureListener, ISurfaceTextureHost {
        private SurfaceTexture mSurfaceTexture;
        private boolean mIsFormatChanged;
        private int mWidth;
        private int mHeight;

        private boolean mOwnSurfaceTexture = true;
        private boolean mWillDetachFromWindow = false;
        private boolean mDidDetachFromWindow = false;

        private WeakReference<TextureRenderView> mWeakRenderView;
        private Map<IRenderCallback, Object> mRenderCallbackMap = new ConcurrentHashMap<IRenderCallback, Object>();

        public SurfaceCallback(@NonNull TextureRenderView renderView) {
            mWeakRenderView = new WeakReference<TextureRenderView>(renderView);
        }

        public void setOwnSurfaceTexture(boolean ownSurfaceTexture) {
            mOwnSurfaceTexture = ownSurfaceTexture;
        }

        public void addRenderCallback(@NonNull IRenderCallback callback) {
            mRenderCallbackMap.put(callback, callback);

            ISurfaceHolder surfaceHolder = null;
            if (mSurfaceTexture != null) {
                if (surfaceHolder == null)
                    surfaceHolder = new InternalSurfaceHolder(mWeakRenderView.get(), mSurfaceTexture, this);
                callback.onSurfaceCreated(surfaceHolder, mWidth, mHeight);
            }

            if (mIsFormatChanged) {
                if (surfaceHolder == null)
                    surfaceHolder = new InternalSurfaceHolder(mWeakRenderView.get(), mSurfaceTexture, this);
                callback.onSurfaceChanged(surfaceHolder, 0, mWidth, mHeight);
            }
        }

        public void removeRenderCallback(@NonNull IRenderCallback callback) {
            mRenderCallbackMap.remove(callback);
        }

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            mSurfaceTexture = surface;
            mIsFormatChanged = false;
            mWidth = 0;
            mHeight = 0;

            ISurfaceHolder surfaceHolder = new InternalSurfaceHolder(mWeakRenderView.get(), surface, this);
            for (IRenderCallback renderCallback : mRenderCallbackMap.keySet()) {
                renderCallback.onSurfaceCreated(surfaceHolder, 0, 0);
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            mSurfaceTexture = surface;
            mIsFormatChanged = true;
            mWidth = width;
            mHeight = height;

            ISurfaceHolder surfaceHolder = new InternalSurfaceHolder(mWeakRenderView.get(), surface, this);
            for (IRenderCallback renderCallback : mRenderCallbackMap.keySet()) {
                renderCallback.onSurfaceChanged(surfaceHolder, 0, width, height);
            }
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            mSurfaceTexture = surface;
            mIsFormatChanged = false;
            mWidth = 0;
            mHeight = 0;

            ISurfaceHolder surfaceHolder = new InternalSurfaceHolder(mWeakRenderView.get(), surface, this);
            for (IRenderCallback renderCallback : mRenderCallbackMap.keySet()) {
                renderCallback.onSurfaceDestroyed(surfaceHolder);
            }

            Log.d(TAG, "onSurfaceTextureDestroyed: destroy: " + mOwnSurfaceTexture);
            return mOwnSurfaceTexture;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            if(mWeakRenderView.get().textureUpdateListener!=null){
                mWeakRenderView.get().textureUpdateListener.onTextureUpdate();
            }
        }

        //-------------------------
        // ISurfaceTextureHost
        //-------------------------

        @Override
        public void releaseSurfaceTexture(SurfaceTexture surfaceTexture) {
            if (surfaceTexture == null) {
                Log.d(TAG, "releaseSurfaceTexture: null");
            } else if (mDidDetachFromWindow) {
                if (surfaceTexture != mSurfaceTexture) {
                    Log.d(TAG, "releaseSurfaceTexture: didDetachFromWindow(): release different SurfaceTexture");
                    surfaceTexture.release();
                } else if (!mOwnSurfaceTexture) {
                    Log.d(TAG, "releaseSurfaceTexture: didDetachFromWindow(): release detached SurfaceTexture");
                    surfaceTexture.release();
                } else {
                    Log.d(TAG, "releaseSurfaceTexture: didDetachFromWindow(): already released by TextureView");
                }
            } else if (mWillDetachFromWindow) {
                if (surfaceTexture != mSurfaceTexture) {
                    Log.d(TAG, "releaseSurfaceTexture: willDetachFromWindow(): release different SurfaceTexture");
                    surfaceTexture.release();
                } else if (!mOwnSurfaceTexture) {
                    Log.d(TAG, "releaseSurfaceTexture: willDetachFromWindow(): re-attach SurfaceTexture to TextureView");
                    setOwnSurfaceTexture(true);
                } else {
                    Log.d(TAG, "releaseSurfaceTexture: willDetachFromWindow(): will released by TextureView");
                }
            } else {
                if (surfaceTexture != mSurfaceTexture) {
                    Log.d(TAG, "releaseSurfaceTexture: alive: release different SurfaceTexture");
                    surfaceTexture.release();
                } else if (!mOwnSurfaceTexture) {
                    Log.d(TAG, "releaseSurfaceTexture: alive: re-attach SurfaceTexture to TextureView");
                    setOwnSurfaceTexture(true);
                } else {
                    Log.d(TAG, "releaseSurfaceTexture: alive: will released by TextureView");
                }
            }
        }

        public void willDetachFromWindow() {
            Log.d(TAG, "willDetachFromWindow()");
            mWillDetachFromWindow = true;
        }

        public void didDetachFromWindow() {
            Log.d(TAG, "didDetachFromWindow()");
            mDidDetachFromWindow = true;
        }
    }

    //--------------------
    // Accessibility
    //--------------------

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(TextureRenderView.class.getName());
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(TextureRenderView.class.getName());
    }

    public interface TextureUpdateListener{
        void onTextureUpdate();
    }

    public void setTextureUpdateListener(TextureUpdateListener textureUpdateListener) {
        this.textureUpdateListener = textureUpdateListener;
    }
}
