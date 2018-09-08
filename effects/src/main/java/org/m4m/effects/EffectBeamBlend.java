package org.m4m.effects;

import org.m4m.android.graphics.VideoEffect;
import org.m4m.domain.graphics.IEglUtil;

public class EffectBeamBlend extends VideoEffect {

    public EffectBeamBlend(int angle, IEglUtil eglUtil) {
        super(angle, eglUtil);
        setFragmentShader(getFragmentShader());
    }

    protected String getFragmentShader() {
        return "#extension GL_OES_EGL_image_external : require\n" +
                "precision mediump float;\n" +
                "varying highp vec2 vTextureCoord;\n" +
                "uniform samplerExternalOES sTexture;\n" +
                "\n" +
                "//uniform float fractionalWidthOfPixel;\n" +
                "//uniform float aspectRatio;\n" +
                "\n" +
                "uniform lowp float iGlobalTime ;\n" +
                "//uniform lowp float frameWidth ;\n" +
                "//uniform lowp float frameHeight ;\n" +
                "uniform lowp float sliderValue ;\n" +
                "uniform lowp vec2 iMouse;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "    \n" +
                "    const float sliderV = 0.5; \n" +
                "    const float iGT =  100.0; \n" +
                "    const vec2 iM =  vec2(0.0,0.0); \n" +
                "    lowp vec2 uv =  vTextureCoord.xy; ///iResolution.xy;\n" +
                "    lowp vec4 tc = texture2D(sTexture, vTextureCoord);\n" +
                "    lowp float t = iGT;\n" +
                "    \n" +
                "    lowp vec3 cl = vec3 ( 1.0, 0.5, 0.5 );// beam !\n" +
                "    \n" +
                "    //cl *= 4.*sqrt(abs( 1.0 / (sin(0.5- uv.x + sin(uv.y+t)* 0.20 ) * 2.0)));\n" +
                "    cl *= (4.1 * sliderV)  *sqrt(abs( 1.0 / (sin(0.5- uv.x + sin(uv.y+t)* 0.20 ) * 2.0)));\n" +
                "\n" +
                "    // annunaki formula from heaven !\n" +
                "    \n" +
                "    lowp vec4 fragColor;\n" +
                "\n" +
                "    \n" +
                "    //if(tc.r>0.2 )\n" +
                "    {\n" +
                "        fragColor = vec4(cl-1.8, 1.0 );\n" +
                "    }\n" +
                "    \n" +
                "    fragColor *= vec4(tc) ;\n" +
                "\n" +
                "    \n" +
                "    lowp vec3 texColor = texture2D(sTexture,uv).rgb;\n" +
                "    if (vTextureCoord.x < iM.x)\n" +
                "    {\n" +
                "        fragColor = vec4(texColor, 1.0);\n" +
                "    }\n" +
                "    \n" +
                "    gl_FragColor = fragColor;\n" +
                "    \n" +
                "    \n" +
                "}";
    }
}
