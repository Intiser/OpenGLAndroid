package org.m4m.effects;

import org.m4m.android.graphics.VideoEffect;
import org.m4m.domain.graphics.IEglUtil;

public class EffectRegular extends VideoEffect{

    public EffectRegular(int angle, IEglUtil eglUtil) {
        super(angle, eglUtil);
        setFragmentShader(getFragmentShader());
    }

    protected String getFragmentShader() {
        return "#extension GL_OES_EGL_image_external : require\n" +
            "precision mediump float;\n" +
            "varying vec2 vTextureCoord;\n" +
            "uniform samplerExternalOES sTexture;\n" +
            "void main() {\n" +
            "  vec4 color = texture2D(sTexture, vTextureCoord);\n" +
            "  //float colorR = (1.0 - color.r) / 1.0;\n" +
            "  //float colorG = (1.0 - color.g) / 1.0;\n" +
            "  //float colorB = (1.0 - color.b) / 1.0;\n" +
            "  //gl_FragColor = vec4(colorR, colorG, colorB, color.a);\n" +
            "  gl_FragColor = color;\n" +
            "}\n";
    }

}
