package org.m4m.effects;

import org.m4m.android.graphics.VideoEffect;
import org.m4m.domain.graphics.IEglUtil;

public class Effect1 extends VideoEffect {

    public Effect1(int angle, IEglUtil eglUtil) {
        super(angle, eglUtil);
        setFragmentShader(getFragmentShader());
    }

    protected String getFragmentShader() {
//        return "#extension GL_OES_EGL_image_external : require\n" +
//            "precision mediump float;\n" +
//            "varying vec2 vTextureCoord;\n" +
//            "uniform samplerExternalOES sTexture;\n" +
//            "void main() {\n" +
//            "  vec4 color = texture2D(sTexture, vTextureCoord);\n" +
//            "  float colorR = (1.0 - color.r) / 1.0;\n" +
//            "  float colorG = (1.0 - color.g) / 1.0;\n" +
//            "  float colorB = (1.0 - color.b) / 1.0;\n" +
//            "  gl_FragColor = vec4(colorR, colorG, colorB, color.a);\n" +
//            "}\n";

//        return "#extension GL_OES_EGL_image_external : require\n" +
//                "precision mediump float;\n" +
//                "varying highp vec2 vTextureCoord;\n" +
//                "uniform samplerExternalOES sTexture;\n" +
//                "\n" +
//                "uniform lowp float iGlobalTime ;\n" +
//                "\n" +
//                "uniform lowp float frameWidth ;\n" +
//                "uniform lowp float frameHeight ;\n" +
//                "\n" +
//                "\n" +
//                "const lowp float waveSpeed = 30.0;     //4.0\n" +
//                "\n" +
//                "uniform lowp float sliderValue ;\n" +
//                "uniform lowp float sliderValue2 ;\n" +
//                "\n" +
//                "\n" +
//                "void main()\n" +
//                "{\n" +
//                "    const float sliderV = 0.5;  \n" +
//                "    lowp vec2 U = vTextureCoord;\n" +
//                "    lowp vec2 iResolution = vec2(1.0,1.0);\n" +
//                "//    lowp vec2 uv = vTextureCoord.xy / iResolution.xy;\n" +
//                "\n" +
//                "//    lowp vec2 uv1 = uv;\n" +
//                "//    lowp vec2 uv2 = uv;\n" +
//                "\n" +
//                "     const float iGT = 100.0;\n" +
//                "    lowp float tempTime = mod(iGT,6.0) ;\n" +
//                "\n" +
//                "    // pixel position normalised to [-1, 1]\n" +
//                "    lowp vec2 cPos = -1.0 + 2.0 * vTextureCoord.xy / iResolution.xy;\n" +
//                "\n" +
//                "    // distance of current pixel from center\n" +
//                "    lowp float cLength = length(cPos);\n" +
//                "\n" +
//                "//    lowp vec2 uv = vTextureCoord.xy/iResolution.xy+(cPos/cLength)*cos(cLength*12.0-iGlobalTime*4.0) * 0.03;\n" +
//                "    lowp vec2 uv = vTextureCoord.xy/iResolution.xy+(cPos/cLength)*cos(cLength*12.0 - tempTime *(waveSpeed*sliderV)) * 0.03;\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "    lowp vec3 col = texture2D(sTexture,uv).xyz;\n" +
//                "\n" +
//                "    gl_FragColor = vec4(col,1.0);\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "//    gl_FragColor =  texture2D(sTexture, vTextureCoord);\n" +
//                "\n" +
//                "\n" +
//                "    /*\n" +
//                "//    lowp float y  = mod(floor(uv1.y * iResolution.y / 2.0), 2.0);\n" +
//                "//    lowp float y1 = mod(floor(0.2-uv2.y * iResolution.y / 2.0), 2.0);\n" +
//                "\n" +
//                "    lowp float y  = mod(floor(uv1.y * frameHeight / 2.0), 2.0);\n" +
//                "    lowp float y1 = mod(floor(0.2-uv2.y * frameHeight / 2.0), 2.0);\n" +
//                "\n" +
//                "//    lowp float x1 =abs(sin((uv1.y-uv1.x + iGlobalTime))) * 0.04;\n" +
//                "    lowp float x1 =abs(sin((uv1.y-uv1.x + iGlobalTime))) * 0.04;\n" +
//                "\n" +
//                "\n" +
//                "    if (y1 == 0.0 )\n" +
//                "    {\n" +
//                "//        gl_FragColor =  texture2D(sTexture, 0.80*(uv2 + vec2(-x1+0.10, 0.)));\n" +
//                "        gl_FragColor =  texture2D(sTexture, 0.80*(uv2 + vec2(-x1+0.10, 0.)));\n" +
//                "    }\n" +
//                "    else //if(y==0.0)\n" +
//                "    {\n" +
//                "//        gl_FragColor =  texture2D(sTexture, 0.80*(uv1 + vec2(x1+0.10, 0.)));\n" +
//                "        gl_FragColor =  texture2D(sTexture, 0.80*(uv1 + vec2(x1+0.10, 0.)));\n" +
//                "    }\n" +
//                "\n" +
//                " */\n" +
//                "\n" +
//                "\n" +
//                "}\n" +
//                "\n" +
//                "\n";

        return  "#extension GL_OES_EGL_image_external : require\n" +
                "precision mediump float;\n" +
                "uniform samplerExternalOES sTexture;\n" +
                "varying vec2 vTextureCoord;\n" +
                "const lowp float waveSpeed = 30.0;\n" +
                "void main()\n" +
                "{\n" +
                "    const float sliderV = 0.5;\n" +
                "    lowp vec2 U = vTextureCoord;\n" +
                "    lowp vec2 iResolution = vec2(1.0,1.0);\n" +
                "     const float iGT = 100.0;\n" +
                "    lowp float tempTime = mod(iGT,6.0) ;\n" +
                "    lowp vec2 cPos = -1.0 + 2.0 * vTextureCoord.xy / iResolution.xy;\n" +
                "    lowp float cLength = length(cPos);\n" +
                "    lowp vec2 uv = vTextureCoord.xy/iResolution.xy+(cPos/cLength)*cos(cLength*12.0 - tempTime *(waveSpeed*sliderV)) * 0.03;\n" +
                "    lowp vec3 col = texture2D(sTexture,uv).xyz;\n" +
                "    gl_FragColor = vec4(col,1.0);\n" +
                "}\n";

    }
}
